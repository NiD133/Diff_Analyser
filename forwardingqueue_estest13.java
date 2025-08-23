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

public class ForwardingQueue_ESTestTest13 extends ForwardingQueue_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        EvictingQueue<Object> evictingQueue0 = EvictingQueue.create(0);
        Object object0 = evictingQueue0.peek();
        assertNull(object0);
    }
}
