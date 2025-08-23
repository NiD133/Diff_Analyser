package org.apache.ibatis.cache;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CacheKey_ESTestTest11 extends CacheKey_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        CacheKey cacheKey0 = new CacheKey();
        Object[] objectArray0 = new Object[0];
        CacheKey cacheKey1 = new CacheKey(objectArray0);
        boolean boolean0 = cacheKey1.equals(cacheKey0);
        assertTrue(boolean0);
    }
}
