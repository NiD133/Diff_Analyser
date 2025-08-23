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

public class AbstractIteratorTestTest6 extends TestCase {

    public void testException() {
        SomeUncheckedException exception = new SomeUncheckedException();
        Iterator<Integer> iter = new AbstractIterator<Integer>() {

            @Override
            public Integer computeNext() {
                throw exception;
            }
        };
        // It should pass through untouched
        SomeUncheckedException e = assertThrows(SomeUncheckedException.class, iter::hasNext);
        assertSame(exception, e);
    }
}
