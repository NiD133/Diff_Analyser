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

public class CacheBuilder_ESTestTest11 extends CacheBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        CacheBuilder cacheBuilder0 = new CacheBuilder("");
        CacheBuilder cacheBuilder1 = cacheBuilder0.addDecorator((Class<? extends Cache>) null);
        assertSame(cacheBuilder0, cacheBuilder1);
    }
}
