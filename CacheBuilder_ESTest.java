package org.apache.ibatis.mapping;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.Properties;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.decorators.BlockingCache;
import org.apache.ibatis.cache.decorators.SynchronizedCache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.apache.ibatis.mapping.CacheBuilder;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) @EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true) 
public class CacheBuilder_ESTest extends CacheBuilder_ESTest_scaffolding {

  @Test(timeout = 4000)
  public void testBuildCacheWithValidProperties()  throws Throwable  {
      Properties properties0 = new Properties();
      properties0.setProperty("size", "5");
      CacheBuilder cacheBuilder0 = new CacheBuilder("my-cache");
      cacheBuilder0.properties(properties0);
      Cache cache0 = cacheBuilder0.build();
      assertEquals("my-cache", cache0.getId());
  }

  @Test(timeout = 4000)
  public void testBuildCacheWithInvalidProperties()  throws Throwable  {
      Properties properties0 = new Properties();
      properties0.setProperty("size", "invalid");
      CacheBuilder cacheBuilder0 = new CacheBuilder("my-cache");
      cacheBuilder0.properties(properties0);
      try {
        cacheBuilder0.build();
        fail("Expecting exception: NumberFormatException");
      
      } catch(NumberFormatException e) {
         //
         // For input string: \"invalid\"
         //
         verifyException("java.lang.NumberFormatException", e);
      }
  }

  @Test(timeout = 4000)
  public void testBuildCacheWithBlockingEnabled()  throws Throwable  {
      Properties properties0 = new Properties();
      properties0.setProperty("blocking", "true");
      CacheBuilder cacheBuilder0 = new CacheBuilder("my-cache");
      cacheBuilder0.properties(properties0);
      Cache cache0 = cacheBuilder0.build();
      assertTrue(cache0 instanceof BlockingCache);
  }

  @Test(timeout = 4000)
  public void testBuildCacheWithSynchronizedEnabled()  throws Throwable  {
      Properties properties0 = new Properties();
      properties0.setProperty("synchronized", "true");
      CacheBuilder cacheBuilder0 = new CacheBuilder("my-cache");
      cacheBuilder0.properties(properties0);
      Cache cache0 = cacheBuilder0.build();
      assertTrue(cache0 instanceof SynchronizedCache);
  }

  @Test(timeout = 4000)
  public void testBuildCacheWithLruEnabled()  throws Throwable  {
      Properties properties0 = new Properties();
      properties0.setProperty("lru", "true");
      CacheBuilder cacheBuilder0 = new CacheBuilder("my-cache");
      cacheBuilder0.properties(properties0);
      Cache cache0 = cacheBuilder0.build();
      assertTrue(cache0 instanceof LruCache);
  }

  @Test(timeout = 4000)
  public void testBuildCacheWithScheduledEnabled()  throws Throwable  {
      Properties properties0 = new Properties();
      properties0.setProperty("scheduled", "true");
      CacheBuilder cacheBuilder0 = new CacheBuilder("my-cache");
      cacheBuilder0.properties(properties0);
      Cache cache0 = cacheBuilder0.build();
      assertTrue(cache0 instanceof ScheduledCache);
  }

  @Test(timeout = 4000)
  public void testBuildCacheWithSerializedEnabled()  throws Throwable  {
      Properties properties0 = new Properties();
      properties0.setProperty("serialized", "true");
      CacheBuilder cacheBuilder0 = new CacheBuilder("my-cache");
      cacheBuilder0.properties(properties0);
      Cache cache0 = cacheBuilder0.build();
      assertTrue(cache0 instanceof SerializedCache);
  }
}