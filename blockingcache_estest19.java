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

public class BlockingCache_ESTestTest19 extends BlockingCache_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test18() throws Throwable {
        BlockingCache blockingCache0 = new BlockingCache((Cache) null);
        // Undeclared exception!
        try {
            blockingCache0.getId();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.ibatis.cache.decorators.BlockingCache", e);
        }
    }
}
