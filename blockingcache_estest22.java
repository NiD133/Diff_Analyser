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

public class BlockingCache_ESTestTest22 extends BlockingCache_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test21() throws Throwable {
        PerpetualCache perpetualCache0 = new PerpetualCache("1Y !<7S");
        BlockingCache blockingCache0 = new BlockingCache(perpetualCache0);
        // Undeclared exception!
        try {
            blockingCache0.removeObject("1Y !<7S");
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            //
            // Detected an attempt at releasing unacquired lock. This should never happen.
            //
            verifyException("org.apache.ibatis.cache.decorators.BlockingCache", e);
        }
    }
}
