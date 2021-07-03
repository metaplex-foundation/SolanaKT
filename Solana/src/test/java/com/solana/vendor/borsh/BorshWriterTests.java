package com.solana.vendor.borsh;/* This is free and unencumbered software released into the public domain. */


import com.solana.vendor.borshj.BorshWriter;

import org.junit.Test;

import java.io.ByteArrayOutputStream;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;


public class BorshWriterTests {
  private ByteArrayOutputStream output;
  private BorshWriter writer;

  void newWriter() {
    output = new ByteArrayOutputStream();
    writer = new BorshWriter(output);
  }

  @Test
  public void constructWithNull() {
    newWriter();
    assertThrows(NullPointerException.class, () -> new BorshWriter(null));
  }

  @Test
  public void captureOutput() {
    newWriter();
    writer.writeString("Borsh");
    assertArrayEquals(new byte[] {5, 0, 0, 0, 'B', 'o', 'r', 's', 'h'}, output.toByteArray());
  }
}
