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

public class ForwardingQueue_ESTestTest15 extends ForwardingQueue_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        EvictingQueue<Locale.FilteringMode> evictingQueue0 = EvictingQueue.create(126);
        evictingQueue0.poll();
    }
}
