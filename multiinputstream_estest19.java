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

public class MultiInputStream_ESTestTest19 extends MultiInputStream_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test18() throws Throwable {
        LinkedList<ByteSource> linkedList0 = new LinkedList<ByteSource>();
        ByteSource[] byteSourceArray0 = new ByteSource[3];
        byte[] byteArray0 = new byte[8];
        ByteSource byteSource0 = ByteSource.wrap(byteArray0);
        byteSourceArray0[0] = byteSource0;
        linkedList0.add(byteSourceArray0[0]);
        Iterator<ByteSource> iterator0 = linkedList0.descendingIterator();
        MultiInputStream multiInputStream0 = new MultiInputStream(iterator0);
        int int0 = multiInputStream0.available();
        assertEquals(8, int0);
    }
}
