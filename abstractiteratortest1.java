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

public class AbstractIteratorTestTest1 extends TestCase {

    public void testDefaultBehaviorOfNextAndHasNext() {
        // This sample AbstractIterator returns 0 on the first call, 1 on the
        // second, then signals that it's reached the end of the data
        Iterator<Integer> iter = new AbstractIterator<Integer>() {

            private int rep;

            @Override
            @Nullable
            public Integer computeNext() {
                switch(rep++) {
                    case 0:
                        return 0;
                    case 1:
                        return 1;
                    case 2:
                        return endOfData();
                    default:
                        throw new AssertionError("Should not have been invoked again");
                }
            }
        };
        assertTrue(iter.hasNext());
        assertEquals(0, (int) iter.next());
        // verify idempotence of hasNext()
        assertTrue(iter.hasNext());
        assertTrue(iter.hasNext());
        assertTrue(iter.hasNext());
        assertEquals(1, (int) iter.next());
        assertFalse(iter.hasNext());
        // Make sure computeNext() doesn't get invoked again
        assertFalse(iter.hasNext());
        assertThrows(NoSuchElementException.class, iter::next);
    }
}
