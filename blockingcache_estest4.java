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

public class BlockingCache_ESTestTest4 extends BlockingCache_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        PerpetualCache perpetualCache0 = new PerpetualCache("Cn");
        BlockingCache blockingCache0 = new BlockingCache(perpetualCache0);
        blockingCache0.getObject("Cn");
        Object object0 = blockingCache0.removeObject("Cn");
        assertNull(object0);
    }
}
