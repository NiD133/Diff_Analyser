package com.google.common.collect;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.nio.CharBuffer;
import java.util.Locale;
import java.util.NoSuchElementException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class ForwardingQueue_ESTestTest11 extends ForwardingQueue_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        EvictingQueue<Comparable<CharBuffer>> evictingQueue0 = EvictingQueue.create(0);
        Comparable<CharBuffer> comparable0 = evictingQueue0.standardPeek();
        assertNull(comparable0);
    }
}
