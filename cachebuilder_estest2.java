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

public class CacheBuilder_ESTestTest2 extends CacheBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        CacheBuilder cacheBuilder0 = new CacheBuilder("w");
        Class<SynchronizedCache> class0 = SynchronizedCache.class;
        cacheBuilder0.implementation(class0);
        // Undeclared exception!
        try {
            cacheBuilder0.build();
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            //
            // Invalid base cache implementation (class org.apache.ibatis.cache.decorators.SynchronizedCache).  Base cache implementations must have a constructor that takes a String id as a parameter.  Cause: java.lang.NoSuchMethodException: org.apache.ibatis.cache.decorators.SynchronizedCache.<init>(java.lang.String)
            //
            verifyException("org.apache.ibatis.mapping.CacheBuilder", e);
        }
    }
}
