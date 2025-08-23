package org.apache.ibatis.mapping;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.Properties;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.decorators.BlockingCache;
import org.apache.ibatis.cache.decorators.SynchronizedCache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CacheBuilder_ESTestTest13 extends CacheBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        CacheBuilder cacheBuilder0 = new CacheBuilder("");
        Long long0 = new Long(1L);
        cacheBuilder0.clearInterval(long0);
        Cache cache0 = cacheBuilder0.build();
        assertEquals("", cache0.getId());
    }
}
