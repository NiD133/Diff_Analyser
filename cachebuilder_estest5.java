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

public class CacheBuilder_ESTestTest5 extends CacheBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        CacheBuilder cacheBuilder0 = new CacheBuilder("F");
        Properties properties0 = new Properties();
        properties0.put(cacheBuilder0, "F");
        CacheBuilder cacheBuilder1 = cacheBuilder0.properties(properties0);
        // Undeclared exception!
        try {
            cacheBuilder1.build();
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            //
            // class org.apache.ibatis.mapping.CacheBuilder cannot be cast to class java.lang.String (org.apache.ibatis.mapping.CacheBuilder is in unnamed module of loader org.evosuite.instrumentation.InstrumentingClassLoader @5d8eae10; java.lang.String is in module java.base of loader 'bootstrap')
            //
            verifyException("org.apache.ibatis.mapping.CacheBuilder", e);
        }
    }
}
