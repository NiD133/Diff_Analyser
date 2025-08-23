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

    // weak references, details of GC
    @J2ktIncompatible
    // weak references
    @GwtIncompatible
    // depends on details of GC
    @AndroidIncompatible
    public void testFreesNextReference() {
        Iterator<Object> itr = new AbstractIterator<Object>() {

            @Override
            public Object computeNext() {
                return new Object();
            }
        };
        WeakReference<Object> ref = new WeakReference<>(itr.next());
        GcFinalization.awaitClear(ref);
    }
}
