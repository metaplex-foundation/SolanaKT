/* This is free and unencumbered software released into the public domain. */

package com.solana.vendor.borshj;

import androidx.annotation.NonNull;

import static java.util.Objects.requireNonNull;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class BorshBuffer implements BorshInput, BorshOutput<BorshBuffer> {
  protected final @NonNull ByteBuffer buffer;

  protected BorshBuffer(final @NonNull ByteBuffer buffer) {
    this.buffer = requireNonNull(buffer);
    this.buffer.order(ByteOrder.LITTLE_ENDIAN);
    this.buffer.mark();
  }

  protected byte[] array() {
    assert(this.buffer.hasArray());
    return this.buffer.array();
  }

  public static @NonNull BorshBuffer allocate(final int capacity) {
    return new BorshBuffer(ByteBuffer.allocate(capacity));
  }

  public static @NonNull BorshBuffer allocateDirect(final int capacity) {
    return new BorshBuffer(ByteBuffer.allocateDirect(capacity));
  }

  public static @NonNull BorshBuffer wrap(final byte[] array) {
    return new BorshBuffer(ByteBuffer.wrap(array));
  }

  public @NonNull byte[] toByteArray() {
    assert(this.buffer.hasArray());
    final int arrayOffset = this.buffer.arrayOffset();
    return Arrays.copyOfRange(this.buffer.array(),
      arrayOffset, arrayOffset + this.buffer.position());
  }

  public int capacity() {
    return this.buffer.capacity();
  }

  public @NonNull BorshBuffer reset() {
    this.buffer.reset();
    return this;
  }

  @Override
  public short readU16() {
    return this.buffer.getShort();
  }

  @Override
  public int readU32() {
    return this.buffer.getInt();
  }

  @Override
  public long readU64() {
    return this.buffer.getLong();
  }

  @Override
  public float readF32() {
    return this.buffer.getFloat();
  }

  @Override
  public double readF64() {
    return this.buffer.getDouble();
  }

  @Override
  public byte read() {
    return this.buffer.get();
  }

  @Override
  public void read(final @NonNull byte[] result, final int offset, final int length) {
    this.buffer.get(result, offset, length);
  }

  @Override
  public @NonNull BorshBuffer writeU16(final short value) {
    this.buffer.putShort(value);
    return this;
  }

  @Override
  public @NonNull BorshBuffer writeU32(final int value) {
    this.buffer.putInt(value);
    return this;
  }

  @Override
  public @NonNull BorshBuffer writeU64(final long value) {
    this.buffer.putLong(value);
    return this;
  }

  @Override
  public @NonNull BorshBuffer writeF32(final float value) {
    this.buffer.putFloat(value);
    return this;
  }

  @Override
  public @NonNull BorshBuffer writeF64(final double value) {
    this.buffer.putDouble(value);
    return this;
  }

  @Override
  public @NonNull BorshBuffer write(final @NonNull byte[] bytes) {
    this.buffer.put(bytes);
    return this;
  }

  @Override
  public @NonNull BorshBuffer write(final byte b) {
    this.buffer.put(b);
    return this;
  }
}
