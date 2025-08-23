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

public class MultiInputStream_ESTestTest9 extends MultiInputStream_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        ArrayList<ByteSource> arrayList0 = new ArrayList<ByteSource>();
        arrayList0.add((ByteSource) null);
        Iterator<ByteSource> iterator0 = arrayList0.iterator();
        MultiInputStream multiInputStream0 = null;
        try {
            multiInputStream0 = new MultiInputStream(iterator0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("com.google.common.io.MultiInputStream", e);
        }
    }
}
