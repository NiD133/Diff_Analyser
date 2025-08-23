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

    public void testSneakyThrow() throws Exception {
        Iterator<Integer> iter = new AbstractIterator<Integer>() {

            boolean haveBeenCalled;

            @Override
            public Integer computeNext() {
                if (haveBeenCalled) {
                    throw new AssertionError("Should not have been called again");
                } else {
                    haveBeenCalled = true;
                    throw sneakyThrow(new SomeCheckedException());
                }
            }
        };
        // The first time, the sneakily-thrown exception comes out
        assertThrows(SomeCheckedException.class, iter::hasNext);
        // But the second time, AbstractIterator itself throws an ISE
        assertThrows(IllegalStateException.class, iter::hasNext);
    }
}
