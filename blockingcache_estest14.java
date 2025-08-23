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

public class BlockingCache_ESTestTest14 extends BlockingCache_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        FifoCache fifoCache0 = new FifoCache((Cache) null);
        SerializedCache serializedCache0 = new SerializedCache(fifoCache0);
        LruCache lruCache0 = new LruCache(serializedCache0);
        SynchronizedCache synchronizedCache0 = new SynchronizedCache(lruCache0);
        BlockingCache blockingCache0 = new BlockingCache(synchronizedCache0);
        // Undeclared exception!
        try {
            blockingCache0.getSize();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.ibatis.cache.decorators.FifoCache", e);
        }
    }
}
