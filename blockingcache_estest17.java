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

public class BlockingCache_ESTestTest17 extends BlockingCache_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test16() throws Throwable {
        SynchronizedCache synchronizedCache0 = new SynchronizedCache((Cache) null);
        FifoCache fifoCache0 = new FifoCache(synchronizedCache0);
        TransactionalCache transactionalCache0 = new TransactionalCache(fifoCache0);
        BlockingCache blockingCache0 = new BlockingCache(transactionalCache0);
        CountDownLatch countDownLatch0 = new CountDownLatch(600);
        // Undeclared exception!
        try {
            blockingCache0.getObject(countDownLatch0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.ibatis.cache.decorators.SynchronizedCache", e);
        }
    }
}
