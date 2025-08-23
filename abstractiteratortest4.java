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

public class AbstractIteratorTestTest4 extends TestCase {

    public void testDefaultBehaviorOfPeekForEmptyIteration() {
        AbstractIterator<Integer> empty = new AbstractIterator<Integer>() {

            private boolean alreadyCalledEndOfData;

            @Override
            @Nullable
            public Integer computeNext() {
                if (alreadyCalledEndOfData) {
                    fail("Should not have been invoked again");
                }
                alreadyCalledEndOfData = true;
                return endOfData();
            }
        };
        /*
     * We test multiple calls to peek() to make sure that one bad call doesn't interfere with its
     * ability to throw the correct exception in the future.
     */
        assertThrows(NoSuchElementException.class, empty::peek);
        assertThrows(NoSuchElementException.class, empty::peek);
    }
}
