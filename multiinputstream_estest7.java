package com.google.common.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.ListIterator;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class MultiInputStream_ESTestTest7 extends MultiInputStream_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        ArrayList<ByteSource> arrayList0 = new ArrayList<ByteSource>();
        ByteSource byteSource0 = ByteSource.empty();
        arrayList0.add(byteSource0);
        ListIterator<ByteSource> listIterator0 = arrayList0.listIterator();
        MultiInputStream multiInputStream0 = new MultiInputStream(listIterator0);
        LinkedHashSet<ByteSource> linkedHashSet0 = new LinkedHashSet<ByteSource>();
        linkedHashSet0.add(byteSource0);
        arrayList0.addAll((Collection<? extends ByteSource>) linkedHashSet0);
        // Undeclared exception!
        try {
            multiInputStream0.read();
            fail("Expecting exception: ConcurrentModificationException");
        } catch (ConcurrentModificationException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("java.util.ArrayList$Itr", e);
        }
    }
}
