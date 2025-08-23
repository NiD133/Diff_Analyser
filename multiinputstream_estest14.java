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

public class MultiInputStream_ESTestTest14 extends MultiInputStream_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        ArrayDeque<ByteSource> arrayDeque0 = new ArrayDeque<ByteSource>();
        ByteSource[] byteSourceArray0 = new ByteSource[3];
        ByteSource byteSource0 = ByteSource.empty();
        byteSourceArray0[0] = byteSource0;
        byte[] byteArray0 = new byte[6];
        ByteSource byteSource1 = ByteSource.wrap(byteArray0);
        byteSourceArray0[1] = byteSource1;
        byteSourceArray0[2] = byteSource0;
        ByteSource byteSource2 = ByteSource.concat(byteSourceArray0);
        arrayDeque0.add(byteSource2);
        Iterator<ByteSource> iterator0 = arrayDeque0.iterator();
        MultiInputStream multiInputStream0 = new MultiInputStream(iterator0);
        long long0 = multiInputStream0.skip((byte) 60);
        assertEquals(6L, long0);
    }
}
