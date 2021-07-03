package com.solana.vendor.borsh;/* This is free and unencumbered software released into the public domain. */


import com.solana.vendor.borshj.*;

import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class BorshBufferTests {
  private BorshBuffer buffer;

  void newBuffer() {
    buffer = BorshBuffer.allocate(256);
  }

  @Test
  public void readU8() {
    newBuffer();
    buffer = BorshBuffer.wrap(new byte[] {0x42});
    assertEquals(0x42, buffer.readU8());
  }

  @Test
  public void readU16() {
    newBuffer();
    buffer = BorshBuffer.wrap(new byte[] {0x11, 0x00});
    assertEquals(0x0011, buffer.readU16());
  }

  @Test
  public void readU32() {
    newBuffer();
    buffer = BorshBuffer.wrap(new byte[] {0x33, 0x22, 0x11, 0x00});
    assertEquals(0x00112233, buffer.readU32());
  }

  @Test
  public void readU64() {
    newBuffer();
    buffer = BorshBuffer.wrap(new byte[] {0x77, 0x66, 0x55, 0x44, 0x33, 0x22, 0x11, 0x00});
    assertEquals(0x0011223344556677L, buffer.readU64());
  }

  @Test
  public void readU128() {
    newBuffer();
    final byte[] input = new byte[] {
      0x77, 0x66, 0x55, 0x44, 0x33, 0x22, 0x11, 0x00,
      0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
    };
    buffer = BorshBuffer.wrap(input);
    assertEquals(BigInteger.valueOf(0x0011223344556677L), buffer.readU128());
  }

  @Test
  public void readF32() {
    newBuffer();
    assertEquals(0.0f, BorshBuffer.wrap(new byte[]{0, 0, 0, 0}).readF32(), 0.0);
    assertEquals(1.0f, BorshBuffer.wrap(new byte[]{0, 0, (byte) 0x80, (byte) 0x3f}).readF32(), 0.0);
  }

  @Test
  public void readF64() {
    newBuffer();
    assertEquals(0.0, BorshBuffer.wrap(new byte[]{0, 0, 0, 0, 0, 0, 0, 0}).readF64(), 0.0);
    assertEquals(1.0, BorshBuffer.wrap(new byte[]{0, 0, 0, 0, 0, 0, (byte) 0xf0, (byte) 0x3f}).readF64(), 0.0);
  }

  @Test
  public void readString() {
    newBuffer();
    final byte[] input = new byte[] {5, 0, 0, 0, 'B', 'o', 'r', 's', 'h'};
    buffer = BorshBuffer.wrap(input);
    assertEquals("Borsh", buffer.readString());
  }

  @Test
  public void readFixedArray() {
    newBuffer();
    final byte[] input = new byte[]{1, 2, 3, 4, 5};
    buffer = BorshBuffer.wrap(input);
    assertEquals(0, buffer.reset().readFixedArray(0).length);
    assertEquals(1, buffer.reset().readFixedArray(1).length);
    assertEquals(5, buffer.reset().readFixedArray(5).length);
    assertArrayEquals(input, buffer.reset().readFixedArray(5));
  }

  @Test
  public void readArray() {
    newBuffer();
    final byte[] input = new byte[]{3, 0, 0, 0, 1, 0, 2, 0, 3, 0};
    buffer = BorshBuffer.wrap(input);
    assertArrayEquals(new Short[]{1, 2, 3}, buffer.readArray(Short.class));
  }

  @Test
  public void readBoolean() {
    newBuffer();
    assertFalse(BorshBuffer.wrap(new byte[]{0}).readBoolean());
    assertTrue(BorshBuffer.wrap(new byte[]{1}).readBoolean());
  }

  @Test
  public void readOptional() {
    newBuffer();
    assertEquals(Optional.empty(), BorshBuffer.wrap(new byte[]{0}).readOptional());
    assertEquals(Optional.of(42), BorshBuffer.wrap(new byte[]{1, 42, 0, 0, 0}).readOptional(Integer.class));
  }

  @Test
  public void writeU8() {
    newBuffer();
    final byte[] actual = buffer.writeU8(0x42).toByteArray();
    final byte[] expected = new byte[] {0x42};
    assertArrayEquals(expected, actual);
  }

  @Test
  public void writeU16() {
    newBuffer();
    final byte[] actual = buffer.writeU16(0x0011).toByteArray();
    final byte[] expected = new byte[] {0x11, 0x00};
    assertArrayEquals(expected, actual);
  }

  @Test
  public void writeU32() {
    newBuffer();
    final byte[] actual = buffer.writeU32(0x00112233).toByteArray();
    final byte[] expected = new byte[] {0x33, 0x22, 0x11, 0x00};
    assertArrayEquals(expected, actual);
  }

  @Test
  public void writeU64() {
    newBuffer();
    final byte[] actual = buffer.writeU64(0x0011223344556677L).toByteArray();
    final byte[] expected = new byte[] {
      0x77, 0x66, 0x55, 0x44, 0x33, 0x22, 0x11, 0x00,
    };
    assertArrayEquals(expected, actual);
  }

  @Test
  public void writeU128() {
    newBuffer();
    final byte[] actual = buffer.writeU128(0x0011223344556677L).toByteArray();
    final byte[] expected = new byte[] {
      0x77, 0x66, 0x55, 0x44, 0x33, 0x22, 0x11, 0x00,
      0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
    };
    assertArrayEquals(expected, actual);
  }

  @Test
  public void writeF32() {
    newBuffer();
    final byte[] actual = buffer.writeF32(1.0f).toByteArray();
    final byte[] expected = new byte[] {0, 0, (byte)0x80, (byte)0x3f};
    assertArrayEquals(expected, actual);
  }

  @Test
  public void writeF64() {
    newBuffer();
    final byte[] actual = buffer.writeF64(1.0).toByteArray();
    final byte[] expected = new byte[] {0, 0, 0, 0, 0, 0, (byte)0xf0, (byte)0x3f};
    assertArrayEquals(expected, actual);
  }

  @Test
  public void writeString() {
    newBuffer();
    final byte[] actual = buffer.writeString("Borsh").toByteArray();
    final byte[] expected = new byte[] {5, 0, 0, 0, 'B', 'o', 'r', 's', 'h'};
    assertArrayEquals(expected, actual);
  }

  @Test
  public void writeFixedArray() {
    newBuffer();
    buffer.writeFixedArray(new byte[]{1, 2, 3, 4, 5});
    final byte[] expected = new byte[]{1, 2, 3, 4, 5};
    final byte[] actual = buffer.toByteArray();
    assertArrayEquals(expected, actual);
  }

  @Test
  public void writeArray() {
    newBuffer();
    buffer.writeArray(new Short[]{1, 2, 3});
    final byte[] expected = new byte[]{3, 0, 0, 0, 1, 0, 2, 0, 3, 0};
    final byte[] actual = buffer.toByteArray();
    assertArrayEquals(expected, actual);
  }

  @Test
  public void writeArrayOfList() {
    newBuffer();
    buffer.writeArray(Arrays.asList(new Short[]{1, 2, 3}));
    final byte[] expected = new byte[]{3, 0, 0, 0, 1, 0, 2, 0, 3, 0};
    final byte[] actual = buffer.toByteArray();
    assertArrayEquals(expected, actual);
  }

  @Test
  public void writeBoolean() {
    newBuffer();
    assertArrayEquals(new byte[] {0}, buffer.reset().writeBoolean(false).toByteArray());
    assertArrayEquals(new byte[] {1}, buffer.reset().writeBoolean(true).toByteArray());
  }

  @Test
  public void writeOptional() {
    newBuffer();
    assertArrayEquals(new byte[]{0}, buffer.reset().writeOptional(Optional.empty()).toByteArray());
    assertArrayEquals(new byte[]{1, 42, 0, 0, 0}, buffer.reset().writeOptional(Optional.of(42)).toByteArray());
  }

  @Test
  public void testF32() {
    newBuffer();
    final float value = 3.1415f;
    assertEquals(value, BorshBuffer.wrap(buffer.writeF32(value).toByteArray()).readF32(), 0.0);
  }

  @Test
  public void testF64() {
    newBuffer();
    final double value = 3.1415;
    assertEquals(value, BorshBuffer.wrap(buffer.writeF64(value).toByteArray()).readF64(), 0.0);
  }
}
