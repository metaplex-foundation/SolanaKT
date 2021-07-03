package com.solana.vendor.borsh;/* This is free and unencumbered software released into the public domain. */


import com.solana.vendor.borshj.Borsh;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BorshTests {
  @Test
  public void roundtripPoint2Df() {
    final Point2Df point = new Point2Df(123, 456);
    assertEquals(point, Borsh.deserialize(Borsh.serialize(point), Point2Df.class));
  }

  @Test
  public void roundtripRect2Df() {
    final Point2Df topLeft = new Point2Df(-123, -456);
    final Point2Df bottomRight = new Point2Df(123, 456);
    final Rect2Df rect = new Rect2Df(topLeft, bottomRight);
    assertEquals(rect, Borsh.deserialize(Borsh.serialize(rect), Rect2Df.class));
  }

  static public class Point2Df implements Borsh {
    private float x;
    private float y;

    public Point2Df() {}

    public Point2Df(final float x, final float y) {
      this.x = x;
      this.y = y;
    }

    @NotNull
    @Override
    public String toString() {
      return String.format("Point2Df(%f, %f)", this.x, this.y);
    }

    @Override
    public boolean equals(final Object object) {
      if (object == null || object.getClass() != this.getClass()) return false;
      final Point2Df other = (Point2Df)object;
      return this.x == other.x && this.y == other.y;
    }
  }

  static public class Rect2Df implements Borsh {
    private Point2Df topLeft;
    private Point2Df bottomRight;

    public Rect2Df() {}

    public Rect2Df(final Point2Df topLeft, final Point2Df bottomRight) {
      this.topLeft = topLeft;
      this.bottomRight = bottomRight;
    }

    @NotNull
    @Override
    public String toString() {
      return String.format("Rect2Df(%s, %s)", this.topLeft.toString(), this.bottomRight.toString());
    }

    @Override
    public boolean equals(final Object object) {
      if (object == null || object.getClass() != this.getClass()) return false;
      final Rect2Df other = (Rect2Df)object;
      return this.topLeft.equals(other.topLeft) && this.bottomRight.equals(other.bottomRight);
    }
  }
}
