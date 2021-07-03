package com.solana.vendor.borsh;/* This is free and unencumbered software released into the public domain. */


import com.solana.vendor.borshj.BorshReader;

import org.junit.Test;

import java.io.ByteArrayInputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;


public class BorshReaderTests {
  private ByteArrayInputStream input;
  private BorshReader reader;

  protected BorshReader newReader(final byte[] bytes) {
    input = new ByteArrayInputStream(bytes);
    reader = new BorshReader(input);
    return reader;
  }

  @Test
  public void constructWithNull() {
    assertThrows(NullPointerException.class, () -> new BorshReader(null));
  }

  @Test
  public void parseInput() {
    assertEquals("Borsh", newReader(new byte[] {5, 0, 0, 0, 'B', 'o', 'r', 's', 'h'}).readString());
  }
}
