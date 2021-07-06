package com.solana.vendor.borshj


import com.solana.vendor.borshj.BorshBuffer.Companion.wrap
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.ParameterizedType
import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.reflect.KClass

interface BorshInput {
    fun <T> read(borsh: Borsh, klass: Class<*>): T {
        val rule = borsh.getRules().firstOrNull { it.clazz == klass }

        if (rule != null) {
            return rule.read(this) as T
        } else if (klass == Byte::class.java || klass == Byte::class.javaPrimitiveType || klass == Byte::class.javaObjectType) {
            return java.lang.Byte.valueOf(readU8()) as T
        } else if (klass == Short::class.java || klass == Short::class.javaPrimitiveType || klass == Short::class.javaObjectType) {
            return readU16() as T
        } else if (klass == Int::class.java || klass == Int::class.javaPrimitiveType || klass == Int::class.javaObjectType) {
            return Integer.valueOf(readU32()) as T
        } else if (klass == Long::class.java || klass == Long::class.javaPrimitiveType || klass == Long::class.javaObjectType) {
            return java.lang.Long.valueOf(readU64()) as T
        } else if (klass == BigInteger::class.java) {
            return readU128() as T
        } else if (klass == Float::class.java || klass == Float::class.javaPrimitiveType || klass == Float::class.javaObjectType) {
            return java.lang.Float.valueOf(readF32()) as T
        } else if (klass == Double::class.java || klass == Double::class.javaPrimitiveType || klass == Double::class.javaObjectType) {
            return java.lang.Double.valueOf(readF64()) as T
        } else if (klass == String::class.java) {
            return readString() as T
        } else if (klass == Boolean::class.java) {
            return java.lang.Boolean.valueOf(readBoolean()) as T
        } else if (klass == Optional::class.java) {
            return this.readOptional<Any>() as T
        } else if (borsh.isSerializable(klass)) {
            return readPOJO<Any>(borsh, klass) as T
        }
        throw IllegalArgumentException()
    }

    fun <T> readPOJO(borsh: Borsh, klass: Class<*>): T {
        return try {
            val constructor = klass.kotlin.constructors.first()
            val kotlinParameters = constructor.parameters

            if (klass.declaredFields.firstOrNull { !it.isAnnotationPresent(FieldOrder::class.java) } != null) {
                throw java.lang.IllegalArgumentException("Deserializing borsh data requires all fields to be annotated with @FieldOrder")
            }

            val declaredFields = klass.declaredFields
                .filter { kotlinParameters.map { kf -> kf.name }.contains(it.name) }
                .sortedBy { field -> field.getAnnotation(FieldOrder::class.java).order }

            val map: MutableMap<String, Any?> = mutableMapOf()
            for (field in declaredFields) {
                val kfield = kotlinParameters.first { it.name == field.name }
                field.isAccessible = true

                if (kfield.isOptional) {
                    val fieldType = field.genericType as? ParameterizedType ?: throw AssertionError(
                        "unsupported Optional type"
                    )

                    val optionalArgs = fieldType.actualTypeArguments
                    assert(optionalArgs.size == 1)
                    val optionalClass = optionalArgs[0] as Class<*>
                    map[field.name] = this.readOptional<Any>(borsh, optionalClass)
                } else {
                    map[field.name] = this.read(borsh, field.type)
                }
            }

            val args = constructor
                .parameters
                .map { it to map.get(it.name) }
                .toMap()

            constructor.callBy(args) as T
        } catch (error: NoSuchMethodException) {
            throw RuntimeException(error)
        } catch (error: InstantiationException) {
            throw RuntimeException(error)
        } catch (error: IllegalAccessException) {
            throw RuntimeException(error)
        } catch (error: InvocationTargetException) {
            throw RuntimeException(error)
        }
    }

    fun <T : Any> mapToObject(map: Map<String, Any>, clazz: KClass<T>): T {
        //Get default constructor
        val constructor = clazz.constructors.first()

        //Map constructor parameters to map values
        val args = constructor
            .parameters
            .map { it to map.get(it.name) }
            .toMap()

        //return object from constructor call
        return constructor.callBy(args)
    }

    fun readU8(): Byte {
        return this.read()
    }

    fun readU16(): Short {
        return wrap(this.read(2)).readU16()
    }

    fun readU32(): Int {
        return wrap(this.read(4)).readU32()
    }

    fun readU64(): Long {
        return wrap(this.read(8)).readU64()
    }

    fun readU128(): BigInteger {
        val bytes = ByteArray(16)
        this.read(bytes)
        for (i in 0..7) {
            val a = bytes[i]
            val b = bytes[15 - i]
            bytes[i] = b
            bytes[15 - i] = a
        }
        return BigInteger(bytes)
    }

    fun readF32(): Float {
        return wrap(this.read(4)).readF32()
    }

    fun readF64(): Double {
        return wrap(this.read(8)).readF64()
    }

    fun readString(): String {
        val length = readU32()
        val bytes = ByteArray(length)
        this.read(bytes)
        return String(bytes, StandardCharsets.UTF_8)
    }

    fun readFixedArray(length: Int): ByteArray {
        require(length >= 0)
        val bytes = ByteArray(length)
        this.read(bytes)
        return bytes
    }

    fun <T> readArray(borsh: Borsh, klass: Class<*>): Array<T> {
        val length = readU32()
        val elements = java.lang.reflect.Array.newInstance(klass, length) as Array<T>
        for (i in 0 until length) {
            elements[i] = this.read(borsh, klass)
        }
        return elements
    }

    fun readBoolean(): Boolean {
        return readU8().toInt() != 0
    }

    fun <T> readOptional(): Optional<T> {
        val isPresent = readU8().toInt() != 0
        if (!isPresent) {
            return Optional.empty<Any>() as Optional<T>
        }
        throw AssertionError("Optional type has been erased and cannot be reconstructed")
    }

    fun <T> readOptional(borsh: Borsh, klass: Class<*>): Optional<T>? {
        val isPresent = readU8().toInt() != 0
        return if (isPresent) Optional.of(this.read(borsh, klass)) else Optional.empty()
    }

    fun read(): Byte
    fun read(length: Int): ByteArray? {
        if (length < 0) {
            throw IndexOutOfBoundsException()
        }
        val result = ByteArray(length)
        this.read(result)
        return result
    }

    fun read(result: ByteArray) {
        this.read(result, 0, result.size)
    }

    fun read(result: ByteArray, offset: Int, length: Int)
}