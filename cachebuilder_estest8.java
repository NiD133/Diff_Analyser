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

public class CacheBuilder_ESTestTest8 extends CacheBuilder_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        CacheBuilder cacheBuilder0 = new CacheBuilder("iFLF@Zzpqzdl1Iv-&4p");
        cacheBuilder0.readWrite(true);
        Cache cache0 = cacheBuilder0.build();
        assertEquals("iFLF@Zzpqzdl1Iv-&4p", cache0.getId());
    }
}
