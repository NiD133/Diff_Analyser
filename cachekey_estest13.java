package org.apache.ibatis.cache;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CacheKey_ESTestTest13 extends CacheKey_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        CacheKey cacheKey0 = new CacheKey();
        cacheKey0.update(cacheKey0);
        CacheKey cacheKey1 = cacheKey0.clone();
        boolean boolean0 = cacheKey0.equals(cacheKey1);
        assertNotSame(cacheKey1, cacheKey0);
        assertTrue(boolean0);
    }
}
