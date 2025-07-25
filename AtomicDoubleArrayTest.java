/**
 * Unit test for {@link AtomicDoubleArray}.
 */
public class AtomicDoubleArrayTest extends JSR166TestCase {

    // Constants and setup

    private static final int SIZE = 10;
    private static final double[] VALUES = {
            Double.NEGATIVE_INFINITY,
            -Double.MAX_VALUE,
            (double) Long.MIN_VALUE,
            (double) Integer.MIN_VALUE,
            -Math.PI,
            -1.0,
            -Double.MIN_VALUE,
            -0.0,
            +0.0,
            Double.MIN_VALUE,
            1.0,
            Math.PI,
            (double) Integer.MAX_VALUE,
            (double) Long.MAX_VALUE,
            Double.MAX_VALUE,
            Double.POSITIVE_INFINITY,
            Double.NaN,
            Float.MAX_VALUE
    };

    // Helper methods

    /**
     * The notion of equality used by AtomicDoubleArray.
     *
     * @param x the first double value
     * @param y the second double value
     * @return true if the two doubles are bitwise equal
     */
    static boolean bitEquals(double x, double y) {
        return Double.doubleToRawLongBits(x) == Double.doubleToRawLongBits(y);
    }

    /**
     * Asserts that two doubles are bitwise equal.
     *
     * @param x the expected double value
     * @param y the actual double value
     */
    static void assertBitEquals(double x, double y) {
        assertEquals(Double.doubleToRawLongBits(x), Double.doubleToRawLongBits(y));
    }

    // Tests

    /**
     * Tests that the constructor creates an array of the given size with all elements zero.
     */
    public void testConstructor() {
        AtomicDoubleArray aa = new AtomicDoubleArray(SIZE);
        for (int i = 0; i < SIZE; i++) {
            assertBitEquals(0.0, aa.get(i));
        }
    }

    /**
     * Tests that the constructor with a null array throws a NullPointerException.
     */
    public void testConstructorNullArray() {
        double[] a = null;
        assertThrows(NullPointerException.class, () -> new AtomicDoubleArray(a));
    }

    /**
     * Tests that the constructor with an array is of the same size and has all elements.
     */
    public void testConstructorArray() {
        AtomicDoubleArray aa = new AtomicDoubleArray(VALUES);
        assertEquals(VALUES.length, aa.length());
        for (int i = 0; i < VALUES.length; i++) {
            assertBitEquals(VALUES[i], aa.get(i));
        }
    }

    /**
     * Tests that get and set for out-of-bounds indices throw IndexOutOfBoundsException.
     */
    public void testIndexing() {
        AtomicDoubleArray aa = new AtomicDoubleArray(SIZE);
        for (int index : new int[]{-1, SIZE}) {
            assertThrows(IndexOutOfBoundsException.class, () -> aa.get(index));
            assertThrows(IndexOutOfBoundsException.class, () -> aa.set(index, 1.0));
            assertThrows(IndexOutOfBoundsException.class, () -> aa.lazySet(index, 1.0));
            assertThrows(IndexOutOfBoundsException.class, () -> aa.compareAndSet(index, 1.0, 2.0));
            assertThrows(IndexOutOfBoundsException.class, () -> aa.weakCompareAndSet(index, 1.0, 2.0));
            assertThrows(IndexOutOfBoundsException.class, () -> aa.getAndAdd(index, 1.0));
            assertThrows(IndexOutOfBoundsException.class, () -> aa.addAndGet(index, 1.0));
        }
    }

    /**
     * Tests that get returns the last value set at the index.
     */
    public void testGetSet() {
        AtomicDoubleArray aa = new AtomicDoubleArray(VALUES.length);
        for (int i = 0; i < VALUES.length; i++) {
            assertBitEquals(0.0, aa.get(i));
            aa.set(i, VALUES[i]);
            assertBitEquals(VALUES[i], aa.get(i));
            aa.set(i, -3.0);
            assertBitEquals(-3.0, aa.get(i));
        }
    }

    /**
     * Tests that get returns the last value lazySet at the index by the same thread.
     */
    public void testGetLazySet() {
        AtomicDoubleArray aa = new AtomicDoubleArray(VALUES.length);
        for (int i = 0; i < VALUES.length; i++) {
            assertBitEquals(0.0, aa.get(i));
            aa.lazySet(i, VALUES[i]);
            assertBitEquals(VALUES[i], aa.get(i));
            aa.lazySet(i, -3.0);
            assertBitEquals(-3.0, aa.get(i));
        }
    }

    /**
     * Tests that compareAndSet succeeds in changing the value if equal to the expected value, else fails.
     */
    public void testCompareAndSet() {
        AtomicDoubleArray aa = new AtomicDoubleArray(SIZE);
        for (int i : new int[]{0, SIZE - 1}) {
            double prev = 0.0;
            double unused = Math.E + Math.PI;
            for (double x : VALUES) {
                assertBitEquals(prev, aa.get(i));
                assertFalse(aa.compareAndSet(i, unused, x));
                assertBitEquals(prev, aa.get(i));
                assertTrue(aa.compareAndSet(i, prev, x));
                assertBitEquals(x, aa.get(i));
                prev = x;
            }
        }
    }

    /**
     * Tests that compareAndSet in one thread enables another waiting thread for the value to succeed.
     */
    public void testCompareAndSetInMultipleThreads() throws InterruptedException {
        AtomicDoubleArray a = new AtomicDoubleArray(1);
        a.set(0, 1.0);
        Thread t = newStartedThread(new CheckedRunnable() {
            @Override
            public void realRun() {
                while (!a.compareAndSet(0, 2.0, 3.0)) {
                    Thread.yield();
                }
            }
        });

        assertTrue(a.compareAndSet(0, 1.0, 2.0));
        awaitTermination(t);
        assertBitEquals(3.0, a.get(0));
    }

    // Additional tests...

    /**
     * Tests that a deserialized serialized array holds the same values.
     */
    public void testSerialization() throws Exception {
        AtomicDoubleArray x = new AtomicDoubleArray(SIZE);
        for (int i = 0; i < SIZE; i++) {
            x.set(i, (double) -i);
        }
        AtomicDoubleArray y = serialClone(x);
        assertTrue(x != y);
        assertEquals(x.length(), y.length());
        for (int i = 0; i < SIZE; i++) {
            assertBitEquals(x.get(i), y.get(i));
        }
    }

    /**
     * Tests that toString returns the current value.
     */
    public void testToString() {
        AtomicDoubleArray aa = new AtomicDoubleArray(VALUES);
        assertEquals(Arrays.toString(VALUES), aa.toString());
        assertEquals("[]", new AtomicDoubleArray(0).toString());
        assertEquals("[]", new AtomicDoubleArray(new double[0]).toString());
    }
}