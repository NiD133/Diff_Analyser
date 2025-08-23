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

public class AbstractIteratorTestTest3 extends TestCase {

    /**
     * Tests that the iterator does not hold a strong reference to the object returned by {@code
     * next()} after the call completes. This is important to prevent memory leaks, as it allows the
     * returned object to be garbage collected if no other strong references to it exist.
     *
     * <p>We verify this by holding only a {@link WeakReference} to the returned object and then
     * asserting that the garbage collector eventually clears this reference.
     */
    @J2ktIncompatible // weak references
    @GwtIncompatible // weak references
    @AndroidIncompatible // depends on details of GC
    public void testNext_doesNotRetainReferenceToReturnedObject() {
        // Arrange: Create an iterator that produces a new object on each call.
        Iterator<Object> iterator = new AbstractIterator<Object>() {
            @Override
            protected Object computeNext() {
                return new Object();
            }
        };

        // Act: Retrieve an object from the iterator and hold only a weak reference to it.
        Object returnedObject = iterator.next();
        WeakReference<Object> weakReference = new WeakReference<>(returnedObject);

        // Assert: The object should be garbage-collectable because the iterator has released its
        // internal reference. GcFinalization.awaitClear() will time out and fail the test if the
        // weak reference is not cleared, which would happen if the iterator still held a strong
        // reference.
        GcFinalization.awaitClear(weakReference);
    }
}