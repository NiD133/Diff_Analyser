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

public class BlockingCache_ESTestTest24 extends BlockingCache_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test23() throws Throwable {
        PerpetualCache perpetualCache0 = new PerpetualCache("Detected an attempt at releasing unacquired lock. This should never happen.");
        SoftCache softCache0 = new SoftCache(perpetualCache0);
        BlockingCache blockingCache0 = new BlockingCache(softCache0);
        int int0 = blockingCache0.getSize();
        assertEquals(0, int0);
    }
}
