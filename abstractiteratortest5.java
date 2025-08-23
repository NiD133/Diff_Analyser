package com.google.common.collect;

import static com.google.common.collect.ReflectionFreeAssertThrows.assertThrows;
import static com.google.common.collect.SneakyThrows.sneakyThrow;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import com.google.common.collect.TestExceptions.SomeCheckedException;
import com.google.common.collect.TestExceptions.SomeUncheckedException;
import com.google.common.testing.GcFinalization;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.NoSuchElementException;
import junit.framework.TestCase;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

public class AbstractIteratorTestTest5 extends TestCase {

    /**
     * Tests that when {@code computeNext()} throws an exception, the iterator propagates that
     * exception on the first call, then enters a failed state where subsequent calls throw
     * {@code IllegalStateException}.
     */
    public void testIterator_whenComputeNextThrows_propagatesExceptionThenFails() {
        // Arrange: Create an iterator designed to throw an exception from computeNext().
        Iterator<Integer> iteratorThatThrows =
                new AbstractIterator<Integer>() {
                    private boolean hasThrown = false;

                    @Override
                    protected Integer computeNext() {
                        if (hasThrown) {
                            // This assertion confirms that AbstractIterator does not call computeNext()
                            // again after the first exception.
                            throw new AssertionError("computeNext() should not be called after it throws.");
                        }
                        hasThrown = true;
                        // Use sneakyThrow to test that AbstractIterator correctly handles any Throwable,
                        // including checked exceptions that aren't declared.
                        throw sneakyThrow(new SomeCheckedException());
                    }
                };

        // Act & Assert: The first call to hasNext() should trigger computeNext() and
        // propagate the original checked exception.
        assertThrows(SomeCheckedException.class, iteratorThatThrows::hasNext);

        // Act & Assert: The iterator should now be in a FAILED state. Any subsequent call
        // to hasNext() must throw an IllegalStateException, not the original exception.
        assertThrows(IllegalStateException.class, iteratorThatThrows::hasNext);
    }
}