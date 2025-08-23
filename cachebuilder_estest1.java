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

public class CacheBuilder_ESTestTest1 extends CacheBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        Properties properties0 = new Properties();
        properties0.setProperty("size", "");
        CacheBuilder cacheBuilder0 = new CacheBuilder(",Fpc9HVO,j|b");
        CacheBuilder cacheBuilder1 = cacheBuilder0.properties(properties0);
        // Undeclared exception!
        try {
            cacheBuilder1.build();
            fail("Expecting exception: NumberFormatException");
        } catch (NumberFormatException e) {
            //
            // For input string: \"\"
            //
            verifyException("java.lang.NumberFormatException", e);
        }
    }
}