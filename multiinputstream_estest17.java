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

public class MultiInputStream_ESTestTest17 extends MultiInputStream_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test16() throws Throwable {
        ArrayDeque<ByteSource> arrayDeque0 = new ArrayDeque<ByteSource>();
        byte[] byteArray0 = new byte[3];
        ByteSource byteSource0 = ByteSource.wrap(byteArray0);
        arrayDeque0.add(byteSource0);
        Iterator<ByteSource> iterator0 = arrayDeque0.iterator();
        ByteSource byteSource1 = ByteSource.concat(iterator0);
        boolean boolean0 = byteSource1.contentEquals(byteSource1);
        assertTrue(boolean0);
    }
}
