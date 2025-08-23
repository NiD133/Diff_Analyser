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

public class MultiInputStream_ESTestTest1 extends MultiInputStream_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        ByteSource byteSource0 = ByteSource.empty();
        ArrayDeque<ByteSource> arrayDeque0 = new ArrayDeque<ByteSource>();
        arrayDeque0.add(byteSource0);
        Iterator<ByteSource> iterator0 = arrayDeque0.iterator();
        MultiInputStream multiInputStream0 = new MultiInputStream(iterator0);
        long long0 = multiInputStream0.skip(0L);
        assertEquals(0L, long0);
    }
}
