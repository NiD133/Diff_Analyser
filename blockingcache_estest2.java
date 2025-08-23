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

public class BlockingCache_ESTestTest2 extends BlockingCache_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        BlockingCache blockingCache0 = new BlockingCache((Cache) null);
        CountDownLatch countDownLatch0 = new CountDownLatch(6);
        // Undeclared exception!
        try {
            blockingCache0.putObject(countDownLatch0, (Object) null);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            //
            // Detected an attempt at releasing unacquired lock. This should never happen.
            //
            verifyException("org.apache.ibatis.cache.decorators.BlockingCache", e);
        }
    }
}
