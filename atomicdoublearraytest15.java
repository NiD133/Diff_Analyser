package com.google.common.util.concurrent;

import static java.lang.Math.max;
import static org.junit.Assert.assertThrows;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import com.google.common.testing.NullPointerTester;
import java.util.Arrays;
import org.jspecify.annotations.NullUnmarked;

public class AtomicDoubleArrayTestTest15 extends JSR166TestCase {

    private static final double[] VALUES = { Double.NEGATIVE_INFINITY, -Double.MAX_VALUE, (double) Long.MIN_VALUE, (double) Integer.MIN_VALUE, -Math.PI, -1.0, -Double.MIN_VALUE, -0.0, +0.0, Double.MIN_VALUE, 1.0, Math.PI, (double) Integer.MAX_VALUE, (double) Long.MAX_VALUE, Double.MAX_VALUE, Double.POSITIVE_INFINITY, Double.NaN, Float.MAX_VALUE };

    static final long COUNTDOWN = 100000;

    /**
     * The notion of equality used by AtomicDoubleArray
     */
    static boolean bitEquals(double x, double y) {
        return Double.doubleToRawLongBits(x) == Double.doubleToRawLongBits(y);
    }

    static void assertBitEquals(double x, double y) {
        assertEquals(Double.doubleToRawLongBits(x), Double.doubleToRawLongBits(y));
    }

    class Counter extends CheckedRunnable {

        final AtomicDoubleArray aa;

        volatile long counts;

        Counter(AtomicDoubleArray a) {
            aa = a;
        }

        @Override
        public void realRun() {
            for (; ; ) {
                boolean done = true;
                for (int i = 0; i < aa.length(); i++) {
                    double v = aa.get(i);
                    assertTrue(v >= 0);
                    if (v != 0) {
                        done = false;
                        if (aa.compareAndSet(i, v, v - 1.0)) {
                            ++counts;
                        }
                    }
                }
                if (done) {
                    break;
                }
            }
        }
    }

    /**
     * addAndGet adds given value to current, and returns current value
     */
    public void testAddAndGet() {
        AtomicDoubleArray aa = new AtomicDoubleArray(SIZE);
        for (int i : new int[] { 0, SIZE - 1 }) {
            for (double x : VALUES) {
                for (double y : VALUES) {
                    aa.set(i, x);
                    double z = aa.addAndGet(i, y);
                    assertBitEquals(x + y, z);
                    assertBitEquals(x + y, aa.get(i));
                }
            }
        }
    }
}
