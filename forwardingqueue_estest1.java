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

public class ForwardingQueue_ESTestTest1 extends ForwardingQueue_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        EvictingQueue<Object> evictingQueue0 = EvictingQueue.create((int) 'b');
        Object object0 = new Object();
        evictingQueue0.standardOffer(object0);
        Object object1 = evictingQueue0.standardPoll();
        assertSame(object1, object0);
    }
}