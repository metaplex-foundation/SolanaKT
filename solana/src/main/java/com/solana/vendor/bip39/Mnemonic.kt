package com.solana.vendor.bip39

import com.solana.vendor.bip39.Mnemonics.MnemonicCode
import java.io.Closeable
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.*

import kotlin.experimental.or

class Mnemonic(
    val phrase: List<String>,
    val passphrase: String = ""
) {
    constructor(wordCount: WordCount, passphrase: String = ""): this(
        MnemonicCode(wordCount).words.map { it.joinToString(separator = "") { word -> "$word" } },
        passphrase
    )

    @Throws(MnemonicError::class)
    fun validate() {
        val joinedPhrase = phrase.joinToString(separator = " ") { word -> word }
        return MnemonicCode(joinedPhrase).validate()
    }

    @Throws(MnemonicError::class)
    fun validateChecksum(): ByteArray{
        val joinedPhrase = phrase.joinToString(separator = " ") { word -> word }
        return MnemonicCode(joinedPhrase).validateChecksum()
    }

    @Throws(MnemonicError::class)
    fun toEntropy(): ByteArray{
        val joinedPhrase = phrase.joinToString { word -> "$word " }
        return MnemonicCode(joinedPhrase).toEntropy()
    }
}

enum class WordCount(val count: Int) {

    COUNT_12(12), COUNT_15(15), COUNT_18(18), COUNT_21(21), COUNT_24(24);

    val bitLength = count / 3 * 32

    companion object {
        fun valueOf(count: Int): WordCount? {
            values().forEach {
                if (it.count == count) return it
            }
            return null
        }
    }
}

private object Mnemonics {

    val secureRandom = SecureRandom()
    var cachedList = WordList()

    fun getCachedWords(languageCode: String): List<String> {
        if (cachedList.languageCode != languageCode) {
            cachedList = WordList(languageCode)
        }
        return cachedList.words
    }

    class MnemonicCode(val chars: CharArray, val languageCode: String = Locale.ENGLISH.language) :
        Closeable, Iterable<String> {

        constructor(
            phrase: String,
            languageCode: String = Locale.ENGLISH.language
        ) : this(phrase.toCharArray(), languageCode)

        constructor(
            entropy: ByteArray,
            languageCode: String = Locale.ENGLISH.language
        ) : this(computeSentence(entropy), languageCode)

        constructor(
            wordCount: WordCount,
            languageCode: String = Locale.ENGLISH.language
        ) : this(computeSentence(wordCount.toEntropy()), languageCode)

        override fun close() = clear()

        val wordCount get() = chars.count { it == ' ' }.let { if (it == 0) it else it + 1 }

        val words: List<CharArray>
            get() = ArrayList<CharArray>(wordCount).apply {
                var cursor = 0
                chars.forEachIndexed { i, c ->
                    if (c == ' ' || i == chars.lastIndex) {
                        add(chars.copyOfRange(cursor, if (chars[i].isWhitespace()) i else i + 1))
                        cursor = i + 1
                    }
                }
            }

        fun clear() = chars.fill(0.toChar())

        fun isEmpty() = chars.isEmpty()

        override fun iterator(): Iterator<String> = object : Iterator<String> {
            var cursor: Int = 0
            override fun hasNext() = cursor < chars.size - 1

            override fun next(): String {
                val nextSpaceIndex = nextSpaceIndex()
                val word = String(chars, cursor, nextSpaceIndex - cursor)
                cursor = nextSpaceIndex + 1
                return word
            }

            private fun nextSpaceIndex(): Int {
                var i = cursor
                while (i < chars.size - 1) {
                    if (chars[i].isWhitespace()) return i else i++
                }
                return chars.size
            }
        }

        fun validate() {
            // verify: word count is supported
            wordCount.let { wordCount ->
                if (WordCount.values().none { it.count == wordCount }) {
                    throw MnemonicError.WordCountException(wordCount)
                }
            }

            // verify: all words are on the list
            var sublist = getCachedWords(languageCode)
            var nextLetter = 0
            chars.forEachIndexed { i, c ->
                // filter down, by character, ensuring that there are always matching words.
                // per BIP39, we could stop checking each word after 4 chars but we check them all,
                // for completeness
                if (c == ' ') {
                    sublist = getCachedWords(languageCode)
                    nextLetter = 0
                } else {
                    sublist = sublist.filter { it.length > nextLetter && it[nextLetter] == c }
                    if (sublist.isEmpty()) throw MnemonicError.InvalidWordAtIndexException(i)
                    nextLetter++
                }
            }

            // verify: checksum
            validateChecksum()
        }

        fun validateChecksum() = toEntropy()

        @Suppress("ThrowsCount", "NestedBlockDepth")
        fun toEntropy(): ByteArray {
            wordCount.let { if (it <= 0 || it % 3 > 0) throw MnemonicError.WordCountException(
                wordCount
            )
            }

            // Look up all the words in the list and construct the
            // concatenation of the original entropy and the checksum.
            //
            val totalLengthBits = wordCount * 11
            val checksumLengthBits = totalLengthBits / 33
            val entropy = ByteArray((totalLengthBits - checksumLengthBits) / 8)
            val checksumBits = mutableListOf<Boolean>()

            val words = getCachedWords(languageCode)
            var bitsProcessed = 0
            var nextByte = 0.toByte()
            this.forEach {
                words.binarySearch(it).let { phraseIndex ->
                    // fail if the word was not found on the list
                    if (phraseIndex < 0) throw MnemonicError.InvalidWordException(it)
                    // for each of the 11 bits of the phraseIndex
                    (10 downTo 0).forEach { i ->
                        // isolate the next bit (starting from the big end)
                        val bit = phraseIndex and (1 shl i) != 0
                        // if the bit is set, then update the corresponding bit in the nextByte
                        if (bit) nextByte = nextByte or (1 shl 7 - (bitsProcessed).rem(8)).toByte()
                        val entropyIndex = ((++bitsProcessed) - 1) / 8
                        // if we're at a byte boundary (excluding the extra checksum bits)
                        if (bitsProcessed.rem(8) == 0 && entropyIndex < entropy.size) {
                            // then set the byte and prepare to process the next byte
                            entropy[entropyIndex] = nextByte
                            nextByte = 0.toByte()
                            // if we're now processing checksum bits, then track them for later
                        } else if (entropyIndex >= entropy.size) {
                            checksumBits.add(bit)
                        }
                    }
                }
            }

            // Check each required checksum bit, against the first byte of the sha256 of entropy
            entropy.toSha256()[0].toBits().let { hashFirstByteBits ->
                repeat(checksumLengthBits) { i ->
                    // failure means that each word was valid BUT they were in the wrong order
                    if (hashFirstByteBits[i] != checksumBits[i]) throw MnemonicError.ChecksumException
                }
            }

            return entropy
        }

        companion object {

            private fun computeSentence(
                entropy: ByteArray,
                languageCode: String = Locale.ENGLISH.language
            ): CharArray {
                // initialize state
                var index = 0
                var bitsProcessed = 0
                val words = getCachedWords(languageCode)

                // inner function that updates the index and copies a word after every 11 bits
                // Note: the excess bits of the checksum are intentionally ignored, per BIP-39
                fun processBit(bit: Boolean, chars: ArrayList<Char>) {
                    // update the index
                    index = index shl 1
                    if (bit) index = index or 1
                    // if we're at a word boundary
                    if ((++bitsProcessed).rem(11) == 0) {
                        // copy over the word and restart the index
                        words[index].forEach { chars.add(it) }
                        chars.add(' ')
                        index = 0
                    }
                }

                // Compute the first byte of the checksum by SHA256(entropy)
                val checksum = entropy.toSha256()[0]
                return (entropy + checksum).toBits().let { bits ->
                    // initial size of max char count, to minimize array copies (size * 3/32 * 8)
                    ArrayList<Char>(entropy.size * 3 / 4).also { chars ->
                        bits.forEach { processBit(it, chars) }
                        // trim final space to avoid the need to track the number of words completed
                        chars.removeAt(chars.lastIndex)
                    }.let { result ->
                        // returning the result as a charArray creates a copy so clear the original
                        // so that it doesn't sit in memory until garbage collection
                        result.toCharArray().also { result.clear() }
                    }
                }
            }
        }
    }
}

sealed class MnemonicError(override val message: String?) : Exception(message) {
    object ChecksumException : MnemonicError("Error: The checksum failed. Verify that none of the words have been transposed.")
    data class WordCountException(val count: Int) : MnemonicError("Error: $count is an invalid word count.")
    data class InvalidWordAtIndexException(val index: Int) : MnemonicError("Error: invalid word encountered at index $index.")
    data class InvalidWordException(val word: String) : MnemonicError("Error: <$word> was not found in the word list.")
}

fun WordCount.toEntropy(): ByteArray = ByteArray(bitLength / 8).apply {
    Mnemonics.secureRandom.nextBytes(this)
}

private fun ByteArray?.toSha256() = MessageDigest.getInstance("SHA-256").digest(this)

private fun ByteArray.toBits(): List<Boolean> = flatMap { it.toBits() }

private fun Byte.toBits(): List<Boolean> = (7 downTo 0).map { (toInt() and (1 shl it)) != 0 }