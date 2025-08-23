package org.apache.ibatis.cache;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CacheKey_ESTestTest4 extends CacheKey_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        CacheKey cacheKey0 = null;
        try {
            cacheKey0 = new CacheKey((Object[]) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.ibatis.cache.CacheKey", e);
        }
    }
}
