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

public class CacheBuilder_ESTestTest9 extends CacheBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        CacheBuilder cacheBuilder0 = new CacheBuilder("org.apache.ibatis.reflection.SystemMetaObject");
        Class<SynchronizedCache> class0 = SynchronizedCache.class;
        cacheBuilder0.addDecorator(class0);
        Integer integer0 = new Integer((-1));
        CacheBuilder cacheBuilder1 = cacheBuilder0.size(integer0);
        Cache cache0 = cacheBuilder1.build();
        assertEquals("org.apache.ibatis.reflection.SystemMetaObject", cache0.getId());
    }
}
