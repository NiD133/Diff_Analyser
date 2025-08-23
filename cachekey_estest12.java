package org.apache.ibatis.cache;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CacheKey_ESTestTest12 extends CacheKey_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        Object[] objectArray0 = new Object[4];
        CacheKey cacheKey0 = new CacheKey(objectArray0);
        // Undeclared exception!
        try {
            cacheKey0.NULL_CACHE_KEY.updateAll(objectArray0);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            //
            // Not allowed to update a null cache key instance.
            //
            verifyException("org.apache.ibatis.cache.CacheKey$1", e);
        }
    }
}
