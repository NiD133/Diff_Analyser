package org.apache.ibatis.cache;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CacheKey_ESTestTest1 extends CacheKey_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        CacheKey cacheKey0 = new CacheKey();
        CacheKey cacheKey1 = new CacheKey();
        assertTrue(cacheKey1.equals((Object) cacheKey0));
        cacheKey1.update(cacheKey0);
        boolean boolean0 = cacheKey0.equals(cacheKey1);
        assertFalse(boolean0);
    }
}
