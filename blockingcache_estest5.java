package org.apache.ibatis.cache.decorators;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.concurrent.CountDownLatch;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class BlockingCache_ESTestTest5 extends BlockingCache_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        PerpetualCache perpetualCache0 = new PerpetualCache("cy0]f56gGY%Vb");
        ScheduledCache scheduledCache0 = new ScheduledCache(perpetualCache0);
        LruCache lruCache0 = new LruCache(scheduledCache0);
        LoggingCache loggingCache0 = new LoggingCache(lruCache0);
        BlockingCache blockingCache0 = new BlockingCache(loggingCache0);
        blockingCache0.setTimeout((-2805L));
        long long0 = blockingCache0.getTimeout();
        assertEquals((-2805L), long0);
    }
}
