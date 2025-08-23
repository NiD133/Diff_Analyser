package org.apache.ibatis.cache;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CacheKey_ESTestTest5 extends CacheKey_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        CacheKey cacheKey0 = new CacheKey();
        Object[] objectArray0 = new Object[7];
        cacheKey0.updateAll(objectArray0);
        assertEquals(7, cacheKey0.getUpdateCount());
    }
}
