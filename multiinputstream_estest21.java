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

public class MultiInputStream_ESTestTest21 extends MultiInputStream_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test20() throws Throwable {
        LinkedList<ByteSource> linkedList0 = new LinkedList<ByteSource>();
        Iterator<ByteSource> iterator0 = linkedList0.descendingIterator();
        MultiInputStream multiInputStream0 = new MultiInputStream(iterator0);
        boolean boolean0 = multiInputStream0.markSupported();
        assertFalse(boolean0);
    }
}