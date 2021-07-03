package com.solana.vendor.borshj;

import androidx.annotation.NonNull;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public interface BorshInput {
  default public <T> T read(final @NonNull Class klass) {
    if (klass == Byte.class || klass == byte.class) {
      return (T)Byte.valueOf(this.readU8());
    }
    else if (klass == Short.class || klass == short.class) {
      return (T)Short.valueOf(this.readU16());
    }
    else if (klass == Integer.class || klass == int.class) {
      return (T)Integer.valueOf(this.readU32());
    }
    else if (klass == Long.class || klass == long.class) {
      return (T)Long.valueOf(this.readU64());
    }
    else if (klass == BigInteger.class) {
      return (T)this.readU128();
    }
    else if (klass == Float.class || klass == float.class) {
      return (T)Float.valueOf(this.readF32());
    }
    else if (klass == Double.class || klass == double.class) {
      return (T)Double.valueOf(this.readF64());
    }
    else if (klass == String.class) {
      return (T)this.readString();
    }
    else if (klass == Boolean.class) {
      return (T)Boolean.valueOf(this.readBoolean());
    }
    else if (klass == Optional.class) {
      return (T)this.readOptional();
    }
    else if (Borsh.isSerializable(klass)) {
      return (T)this.readPOJO(klass);
    }
    throw new IllegalArgumentException();
  }

  default public <T> T readPOJO(final @NonNull Class klass) {
    try {
      final Object object = klass.getConstructor().newInstance();
      for (final Field field : klass.getDeclaredFields()) {
        field.setAccessible(true);
        final Class fieldClass = field.getType();
        if (fieldClass == Optional.class) {
          final Type fieldType = field.getGenericType();
          if (!(fieldType instanceof ParameterizedType)) {
            throw new AssertionError("unsupported Optional type");
          }
          final Type[] optionalArgs = ((ParameterizedType)fieldType).getActualTypeArguments();
          assert(optionalArgs.length == 1);
          final Class optionalClass = (Class)optionalArgs[0];
          field.set(object, this.readOptional(optionalClass));
        }
        else {
          field.set(object, this.read(field.getType()));
        }
      }
      return (T)object;
    }
    catch (NoSuchMethodException error) {
      throw new RuntimeException(error);
    }
    catch (InstantiationException error) {
      throw new RuntimeException(error);
    }
    catch (IllegalAccessException error) {
      throw new RuntimeException(error);
    }
    catch (InvocationTargetException error) {
      throw new RuntimeException(error);
    }
  }

  default public byte readU8() {
    return this.read();
  }

  default public short readU16() {
    return BorshBuffer.wrap(this.read(2)).readU16();
  }

  default public int readU32() {
    return BorshBuffer.wrap(this.read(4)).readU32();
  }

  default public long readU64() {
    return BorshBuffer.wrap(this.read(8)).readU64();
  }

  default public @NonNull BigInteger readU128() {
    final byte[] bytes = new byte[16];
    this.read(bytes);
    for (int i = 0; i < 8; i++) {
      final byte a = bytes[i];
      final byte b = bytes[15 - i];
      bytes[i] = b;
      bytes[15 - i] = a;
    }
    return new BigInteger(bytes);
  }

  default public float readF32() {
    return BorshBuffer.wrap(this.read(4)).readF32();
  }

  default public double readF64() {
    return BorshBuffer.wrap(this.read(8)).readF64();
  }

  default public @NonNull String readString() {
    final int length = this.readU32();
    final byte[] bytes = new byte[length];
    this.read(bytes);
    return new String(bytes, StandardCharsets.UTF_8);
  }

  default public @NonNull byte[] readFixedArray(final int length) {
    if (length < 0) {
      throw new IllegalArgumentException();
    }
    final byte[] bytes = new byte[length];
    this.read(bytes);
    return bytes;
  }

  default public @NonNull <T> T[] readArray(final @NonNull Class klass) {
    final int length = this.readU32();
    final T[] elements = (T[])Array.newInstance(klass, length);
    for (int i = 0; i < length; i++) {
      elements[i] = this.read(klass);
    }
    return elements;
  }

  default public boolean readBoolean() {
    return (this.readU8() != 0);
  }

  default public <T> Optional<T> readOptional() {
    final boolean isPresent = (this.readU8() != 0);
    if (!isPresent) {
      return (Optional<T>)Optional.empty();
    }
    throw new AssertionError("Optional type has been erased and cannot be reconstructed");
  }

  default public <T> Optional<T> readOptional(final @NonNull Class klass) {
    final boolean isPresent = (this.readU8() != 0);
    return isPresent ? Optional.of(this.read(klass)) : Optional.empty();
  }

  public byte read();

  default public byte[] read(final int length) {
    if (length < 0) {
      throw new IndexOutOfBoundsException();
    }
    final byte[] result = new byte[length];
    this.read(result);
    return result;
  }

  default public void read(final @NonNull byte[] result) {
    this.read(result, 0, result.length);
  }

  public void read(@NonNull byte[] result, int offset, int length);
}
