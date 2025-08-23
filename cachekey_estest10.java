package org.apache.ibatis.cache;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CacheKey_ESTestTest10 extends CacheKey_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        CacheKey cacheKey0 = new CacheKey();
        boolean boolean0 = cacheKey0.equals(cacheKey0);
        assertTrue(boolean0);
    }
}
