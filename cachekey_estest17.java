package org.apache.ibatis.cache;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CacheKey_ESTestTest17 extends CacheKey_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test16() throws Throwable {
        CacheKey cacheKey0 = new CacheKey();
        int int0 = cacheKey0.getUpdateCount();
        assertEquals(0, int0);
    }
}
