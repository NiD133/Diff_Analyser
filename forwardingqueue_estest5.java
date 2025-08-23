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

public class ForwardingQueue_ESTestTest5 extends ForwardingQueue_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        EvictingQueue<Locale.Category> evictingQueue0 = EvictingQueue.create(5760);
        Locale.Category locale_Category0 = Locale.Category.DISPLAY;
        evictingQueue0.add(locale_Category0);
        Locale.Category locale_Category1 = evictingQueue0.peek();
        assertTrue(evictingQueue0.contains(locale_Category1));
    }
}
