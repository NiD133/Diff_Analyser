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

public class BlockingCache_ESTestTest11 extends BlockingCache_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        PerpetualCache perpetualCache0 = new PerpetualCache((String) null);
        BlockingCache blockingCache0 = new BlockingCache(perpetualCache0);
        // Undeclared exception!
        try {
            blockingCache0.putObject(perpetualCache0, perpetualCache0);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            //
            // Cache instances require an ID.
            //
            verifyException("org.apache.ibatis.cache.impl.PerpetualCache", e);
        }
    }
}
