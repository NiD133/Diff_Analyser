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

public class ForwardingQueue_ESTestTest2 extends ForwardingQueue_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        EvictingQueue<Locale.FilteringMode> evictingQueue0 = EvictingQueue.create(1);
        Locale.FilteringMode locale_FilteringMode0 = Locale.FilteringMode.EXTENDED_FILTERING;
        evictingQueue0.add(locale_FilteringMode0);
        Locale.FilteringMode locale_FilteringMode1 = evictingQueue0.standardPeek();
        assertSame(locale_FilteringMode1, locale_FilteringMode0);
    }
}
