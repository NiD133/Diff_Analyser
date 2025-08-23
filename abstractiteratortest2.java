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

public class AbstractIteratorTestTest2 extends TestCase {

    public void testDefaultBehaviorOfPeek() {
        /*
     * This sample AbstractIterator returns 0 on the first call, 1 on the
     * second, then signals that it's reached the end of the data
     */
        AbstractIterator<Integer> iter = new AbstractIterator<Integer>() {

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
        assertEquals(0, (int) iter.peek());
        assertEquals(0, (int) iter.peek());
        assertTrue(iter.hasNext());
        assertEquals(0, (int) iter.peek());
        assertEquals(0, (int) iter.next());
        assertEquals(1, (int) iter.peek());
        assertEquals(1, (int) iter.next());
        /*
     * We test peek() after various calls to make sure that one bad call doesn't interfere with its
     * ability to throw the correct exception in the future.
     */
        assertThrows(NoSuchElementException.class, iter::peek);
        assertThrows(NoSuchElementException.class, iter::peek);
        assertThrows(NoSuchElementException.class, iter::next);
        assertThrows(NoSuchElementException.class, iter::peek);
    }
}
