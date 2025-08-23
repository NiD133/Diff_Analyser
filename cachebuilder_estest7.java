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

public class CacheBuilder_ESTestTest7 extends CacheBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        CacheBuilder cacheBuilder0 = new CacheBuilder("org.apache.ibatis.mapping.CacheBuilder");
        CacheBuilder cacheBuilder1 = cacheBuilder0.blocking(true);
        BlockingCache blockingCache0 = (BlockingCache) cacheBuilder1.build();
        assertEquals(0L, blockingCache0.getTimeout());
    }
}
