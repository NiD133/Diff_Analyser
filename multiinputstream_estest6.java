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

public class MultiInputStream_ESTestTest6 extends MultiInputStream_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        ByteSource byteSource0 = ByteSource.empty();
        LinkedList<ByteSource> linkedList0 = new LinkedList<ByteSource>();
        linkedList0.add(byteSource0);
        Iterator<ByteSource> iterator0 = linkedList0.descendingIterator();
        MultiInputStream multiInputStream0 = new MultiInputStream(iterator0);
        byte[] byteArray0 = new byte[0];
        // Undeclared exception!
        try {
            multiInputStream0.read(byteArray0, 408, 408);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("java.io.ByteArrayInputStream", e);
        }
    }
}
