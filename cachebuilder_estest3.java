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

public class CacheBuilder_ESTestTest3 extends CacheBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        CacheBuilder cacheBuilder0 = new CacheBuilder((String) null);
        // Undeclared exception!
        try {
            cacheBuilder0.build();
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            //
            // Error building standard cache decorators.  Cause: org.evosuite.runtime.mock.java.lang.MockThrowable: Error creating logger for logger null.  Cause: java.lang.reflect.InvocationTargetException
            //
            verifyException("org.apache.ibatis.mapping.CacheBuilder", e);
        }
    }
}
