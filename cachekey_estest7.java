package org.apache.ibatis.cache;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CacheKey_ESTestTest7 extends CacheKey_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        Object[] objectArray0 = new Object[5];
        CacheKey cacheKey0 = new CacheKey(objectArray0);
        objectArray0[2] = (Object) cacheKey0;
        CacheKey cacheKey1 = new CacheKey(objectArray0);
        boolean boolean0 = cacheKey1.equals(objectArray0[2]);
        assertEquals(5, cacheKey1.getUpdateCount());
        assertFalse(boolean0);
    }
}
