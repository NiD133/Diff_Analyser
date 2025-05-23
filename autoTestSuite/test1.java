package com.google.common.base;

import static com.google.common.base.ReflectionFreeAssertThrows.assertThrows;
import static com.google.common.base.SneakyThrows.sneakyThrow;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import com.google.common.base.TestExceptions.SomeCheckedException;
import com.google.common.base.TestExceptions.SomeUncheckedException;
import com.google.common.testing.GcFinalization;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.NoSuchElementException;
import junit.framework.TestCase;
import org.jspecify.annotations.NullUnmarked;
import org.jspecify.annotations.Nullable;

/**
 * Unit tests for the {@link AbstractIterator} class.
 */
@GwtCompatible(emulated = true)
@NullUnmarked
public class AbstractIteratorTest extends TestCase {

    /**
     * Tests the normal iteration behavior.
     * The iterator returns 0, then 1, then ends.
     */
    public void testDefaultBehaviorOfNextAndHasNext() {
        Iterator<Integer> iter = new AbstractIterator<Integer>() {
            private int counter = 0;

            @Override
            public @Nullable Integer computeNext() {
                if (counter == 0) return counter++;
                if (counter == 1) return counter++;
                if (counter == 2) return endOfData();
                throw new AssertionError("computeNext() called after end");
            }
        };

        assertTrue("Expected more elements", iter.hasNext());
        assertEquals("First value", 0, (int) iter.next());

        // Repeated calls to hasNext() should not change state
        assertTrue(iter.hasNext());
        assertTrue(iter.hasNext());
        assertTrue(iter.hasNext());
        assertEquals("Second value", 1, (int) iter.next());

        // No more elements should be available
        assertFalse("No more elements expected", iter.hasNext());
        assertFalse("Repeated hasNext should still be false", iter.hasNext());

        // Calling next() after iteration ends should throw
        assertThrows(NoSuchElementException.class, iter::next);
    }

    /**
     * Verifies that checked exceptions can be thrown using sneakyThrow().
     */
    public void testSneakyThrow() {
        Iterator<Integer> iter = new AbstractIterator<Integer>() {
            boolean alreadyCalled = false;

            @Override
            public Integer computeNext() {
                if (alreadyCalled) {
                    throw new AssertionError("computeNext() should not be called again");
                }
                alreadyCalled = true;
                throw sneakyThrow(new SomeCheckedException());
            }
        };

        // The first hasNext() throws the checked exception
        assertThrows(SomeCheckedException.class, iter::hasNext);

        // Further calls result in IllegalStateException
        assertThrows(IllegalStateException.class, iter::hasNext);
    }

    /**
     * Tests that unchecked exceptions propagate as-is from computeNext().
     */
    public void testException() {
        SomeUncheckedException thrown = new SomeUncheckedException();

        Iterator<Integer> iter = new AbstractIterator<Integer>() {
            @Override
            public Integer computeNext() {
                throw thrown;
            }
        };

        SomeUncheckedException caught = assertThrows(SomeUncheckedException.class, iter::hasNext);
        assertSame("Expected original exception to propagate", thrown, caught);
    }

    /**
     * Ensures that computeNext() doesn't silently swallow exceptions after endOfData().
     */
    public void testExceptionAfterEndOfData() {
        Iterator<Integer> iter = new AbstractIterator<Integer>() {
            @Override
            public Integer computeNext() {
                endOfData();
                throw new SomeUncheckedException();
            }
        };

        assertThrows(SomeUncheckedException.class, iter::hasNext);
    }

    /**
     * Ensures that remove() is unsupported by default.
     */
    public void testCantRemove() {
        Iterator<Integer> iter = new AbstractIterator<Integer>() {
            boolean emitted = false;

            @Override
            public Integer computeNext() {
                if (!emitted) {
                    emitted = true;
                    return 0;
                }
                return endOfData();
            }
        };

        assertEquals(0, (int) iter.next());
        assertThrows(UnsupportedOperationException.class, iter::remove);
    }

    /**
     * Verifies that next() does not retain references unnecessarily.
     */
    @GwtIncompatible // weak references
    @J2ktIncompatible
    @AndroidIncompatible // depends on GC internals
    public void testFreesNextReference() {
        Iterator<Object> iter = new AbstractIterator<Object>() {
            @Override
            public Object computeNext() {
                return new Object();
            }
        };

        WeakReference<Object> ref = new WeakReference<>(iter.next());
        GcFinalization.awaitClear(ref);
    }

    /**
     * Tests that calling hasNext() within computeNext() causes an error.
     */
    public void testReentrantHasNext() {
        Iterator<Integer> iter = new AbstractIterator<Integer>() {
            @Override
            protected Integer computeNext() {
                hasNext(); // illegal: reentrant call
                throw new AssertionError();
            }
        };

        assertThrows(IllegalStateException.class, iter::hasNext);
    }
}
