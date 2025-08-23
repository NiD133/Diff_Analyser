package com.google.common.util.concurrent;

import static java.lang.Math.max;
import static org.junit.Assert.assertThrows;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import com.google.common.testing.NullPointerTester;
import java.util.Arrays;
import org.jspecify.annotations.NullUnmarked;

public class AtomicDoubleArrayTestTest24 extends JSR166TestCase {

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
     * Multiple threads using same array of counters successfully update a number of times equal to
     * total count
     */
    public void testCountingInMultipleThreads() throws InterruptedException {
        AtomicDoubleArray aa = new AtomicDoubleArray(SIZE);
        for (int i = 0; i < SIZE; i++) {
            aa.set(i, (double) COUNTDOWN);
        }
        Counter c1 = new Counter(aa);
        Counter c2 = new Counter(aa);
        Thread t1 = newStartedThread(c1);
        Thread t2 = newStartedThread(c2);
        awaitTermination(t1);
        awaitTermination(t2);
        assertEquals(SIZE * COUNTDOWN, c1.counts + c2.counts);
    }
}
