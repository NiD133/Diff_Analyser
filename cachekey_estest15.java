package org.apache.ibatis.cache;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CacheKey_ESTestTest15 extends CacheKey_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        CacheKey cacheKey0 = new CacheKey();
        CacheKey cacheKey1 = cacheKey0.clone();
        assertNotSame(cacheKey1, cacheKey0);
    }
}
