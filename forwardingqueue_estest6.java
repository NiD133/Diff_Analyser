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

public class ForwardingQueue_ESTestTest6 extends ForwardingQueue_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        EvictingQueue<Object> evictingQueue0 = EvictingQueue.create(613);
        LinkedListMultimap<Object, Object> linkedListMultimap0 = LinkedListMultimap.create();
        boolean boolean0 = evictingQueue0.offer(linkedListMultimap0);
        assertTrue(boolean0);
    }
}
