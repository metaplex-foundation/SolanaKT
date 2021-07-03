package com.solana.vendor.borshj;

import static java.util.Objects.requireNonNull;

import androidx.annotation.NonNull;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;

public class BorshWriter implements BorshOutput<BorshWriter>, Closeable, Flushable {
  protected final OutputStream stream;

  public BorshWriter(final @NonNull OutputStream stream) {
    this.stream = requireNonNull(stream);
  }

  @Override
  public void close() throws IOException {
    this.stream.close();
  }

  @Override
  public void flush() throws IOException {
    this.stream.flush();
  }

  @Override
  public @NonNull BorshWriter write(final @NonNull byte[] array) {
    try {
      this.stream.write(array);
      return this;
    }
    catch (final IOException error) {
      throw new RuntimeException(error);
    }
  }

  @Override
  public @NonNull BorshWriter write(final byte b) {
    try {
      this.stream.write(b);
      return this;
    }
    catch (final IOException error) {
      throw new RuntimeException(error);
    }
  }
}
