package com.solana.vendor.borshj;

import static java.util.Objects.requireNonNull;

import androidx.annotation.NonNull;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

public interface BorshOutput<Self> {
  default public @NonNull Self write(final @NonNull Object object) {
    requireNonNull(object);
    if (object instanceof Byte) {
      return this.writeU8((byte)object);
    }
    else if (object instanceof Short) {
      return this.writeU16((short)object);
    }
    else if (object instanceof Integer) {
      return this.writeU32((int)object);
    }
    else if (object instanceof Long) {
      return this.writeU64((long)object);
    }
    else if (object instanceof Float) {
      return this.writeF32((float)object);
    }
    else if (object instanceof Double) {
      return this.writeF64((double)object);
    }
    else if (object instanceof BigInteger) {
      return this.writeU128((BigInteger)object);
    }
    else if (object instanceof String) {
      return this.writeString((String)object);
    }
    else if (object instanceof List) {
      return (Self)this.writeArray((List)object);
    }
    else if (object instanceof Boolean) {
      return (Self)this.writeBoolean((Boolean)object);
    }
    else if (object instanceof Optional) {
      return (Self)this.writeOptional((Optional)object);
    }
    else if (object instanceof Borsh) {
      return this.writePOJO(object);
    }
    throw new IllegalArgumentException();
  }

  default public @NonNull Self writePOJO(final @NonNull Object object) {
    try {
      for (final Field field : object.getClass().getDeclaredFields()) {
        field.setAccessible(true);
        this.write(field.get(object));
      }
    }
    catch (IllegalAccessException error) {
      throw new RuntimeException(error);
    }
    return (Self)this;
  }

  default public @NonNull Self writeU8(final int value) {
    return this.writeU8((byte)value);
  }

  default public @NonNull Self writeU8(final byte value) {
    return this.write(value);
  }

  default public @NonNull Self writeU16(final int value) {
    return this.writeU16((short)value);
  }

  default public @NonNull Self writeU16(final short value) {
    return this.writeBuffer(BorshBuffer.allocate(2).writeU16(value));
  }

  default public @NonNull Self writeU32(final int value) {
    return this.writeBuffer(BorshBuffer.allocate(4).writeU32(value));
  }

  default public @NonNull Self writeU64(final long value) {
    return this.writeBuffer(BorshBuffer.allocate(8).writeU64(value));
  }

  default public @NonNull Self writeU128(final long value) {
    return this.writeU128(BigInteger.valueOf(value));
  }

  default public @NonNull Self writeU128(final @NonNull BigInteger value) {
    if (value.signum() == -1) {
      throw new ArithmeticException("integer underflow");
    }
    if (value.bitLength() > 128) {
      throw new ArithmeticException("integer overflow");
    }
    final byte[] bytes = value.toByteArray();
    for (int i = bytes.length - 1; i >= 0; i--) {
      this.write(bytes[i]);
    }
    for (int i = 0; i < 16 - bytes.length; i++) {
      this.write((byte)0);
    }
    return (Self)this;
  }

  default public @NonNull Self writeF32(final float value) {
    return this.writeBuffer(BorshBuffer.allocate(4).writeF32(value));
  }

  default public @NonNull Self writeF64(final double value) {
    return this.writeBuffer(BorshBuffer.allocate(8).writeF64(value));
  }

  default public @NonNull Self writeString(final @NonNull String string) {
    final byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
    this.writeU32(bytes.length);
    return this.write(bytes);
  }

  default public @NonNull Self writeFixedArray(final @NonNull byte[] array) {
    return this.write(array);
  }

  default public @NonNull <T> Self writeArray(final @NonNull T[] array) {
    this.writeU32(array.length);
    for (final T element : array) {
      this.write(element);
    }
    return (Self)this;
  }

  default public @NonNull <T> Self writeArray(final @NonNull List<T> list) {
    this.writeU32(list.size());
    for (final T element : list) {
      this.write(element);
    }
    return (Self)this;
  }

  default public @NonNull <T> Self writeBoolean(final boolean value) {
    return this.writeU8(value ? 1 : 0);
  }

  default public @NonNull <T> Self writeOptional(final @NonNull Optional<T> optional) {
    if (optional.isPresent()) {
      this.writeU8(1);
      return this.write(optional.get());
    }
    else {
      return this.writeU8(0);
    }
  }

  default public @NonNull Self writeBuffer(final @NonNull BorshBuffer buffer) {
    return this.write(buffer.toByteArray());  // TODO: optimize
  }

  public @NonNull Self write(final @NonNull byte[] bytes);

  public @NonNull Self write(final byte b);
}
