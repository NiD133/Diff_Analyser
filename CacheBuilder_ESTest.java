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

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class CacheBuilder_ESTest extends CacheBuilder_ESTest_scaffolding {

    // ======================================================
    // Tests for invalid property configurations
    // ======================================================
    
    @Test(timeout = 4000, expected = NumberFormatException.class)
    public void build_withEmptySizeProperty_throwsNumberFormatException() throws Throwable {
        Properties props = new Properties();
        props.setProperty("size", ""); // Invalid empty string for size
        
        CacheBuilder builder = new CacheBuilder(",Fpc9HVO,j|b")
            .properties(props);
        
        builder.build(); // Should throw
    }

    @Test(timeout = 4000, expected = RuntimeException.class)
    public void build_withInvalidBaseImplementation_throwsRuntimeException() throws Throwable {
        CacheBuilder builder = new CacheBuilder("w")
            .implementation(SynchronizedCache.class); // Missing String-id constructor
        
        builder.build(); // Should throw
    }

    @Test(timeout = 4000, expected = RuntimeException.class)
    public void build_withNullId_throwsRuntimeException() throws Throwable {
        new CacheBuilder(null).build(); // Null ID not allowed
    }

    @Test(timeout = 4000, expected = StringIndexOutOfBoundsException.class)
    public void build_withUnrecognizedProperty_throwsStringException() throws Throwable {
        Properties props = new Properties();
        props.setProperty("xPxjnuTbn[", "xPxjnuTbn[");
        
        new CacheBuilder("xPxjnuTbn[")
            .properties(props)
            .build(); // Should throw
    }

    @Test(timeout = 4000, expected = ClassCastException.class)
    public void build_withNonStringPropertyKey_throwsClassCastException() throws Throwable {
        Properties props = new Properties();
        props.put(new CacheBuilder("F"), "F"); // Invalid key type
        
        new CacheBuilder("F")
            .properties(props)
            .build(); // Should throw
    }

    // ======================================================
    // Tests for decorator handling
    // ======================================================
    
    @Test(timeout = 4000, expected = RuntimeException.class)
    public void build_withInvalidDecoratorClass_throwsRuntimeException() throws Throwable {
        CacheBuilder builder = new CacheBuilder("")
            .addDecorator(PerpetualCache.class); // Missing Cache-param constructor
        
        builder.build(); // Should throw
    }

    @Test(timeout = 4000)
    public void addDecorator_withNullParameter_doesNothing() {
        CacheBuilder builder = new CacheBuilder("");
        CacheBuilder result = builder.addDecorator(null);
        assertSame(builder, result); // Should return same instance
    }

    @Test(timeout = 4000)
    public void build_withValidDecoratorAndSize_succeeds() throws Throwable {
        Cache cache = new CacheBuilder("org.apache.ibatis.reflection.SystemMetaObject")
            .addDecorator(SynchronizedCache.class)
            .size(-1)
            .build();
        
        assertEquals("org.apache.ibatis.reflection.SystemMetaObject", cache.getId());
    }

    // ======================================================
    // Tests for valid configurations
    // ======================================================
    
    @Test(timeout = 4000)
    public void build_withEmptyIdAndProperties_succeeds() {
        Properties props = new Properties();
        props.setProperty("", ""); // Empty but valid properties
        
        Cache cache = new CacheBuilder("")
            .properties(props)
            .build();
        
        assertEquals("", cache.getId());
    }

    @Test(timeout = 4000)
    public void build_withBlockingEnabled_succeeds() {
        Cache cache = new CacheBuilder("org.apache.ibatis.mapping.CacheBuilder")
            .blocking(true)
            .build();
        
        assertTrue(cache instanceof BlockingCache);
        assertEquals(0L, ((BlockingCache) cache).getTimeout());
    }

    @Test(timeout = 4000)
    public void build_withReadWriteEnabled_succeeds() {
        Cache cache = new CacheBuilder("iFLF@Zzpqzdl1Iv-&4p")
            .readWrite(true)
            .build();
        
        assertEquals("iFLF@Zzpqzdl1Iv-&4p", cache.getId());
    }

    @Test(timeout = 4000)
    public void build_withSizeOne_succeeds() {
        Cache cache = new CacheBuilder("")
            .size(1)
            .build();
        
        assertEquals("", cache.getId());
    }

    @Test(timeout = 4000)
    public void build_withClearInterval_succeeds() {
        Cache cache = new CacheBuilder("")
            .clearInterval(1L)
            .build();
        
        assertEquals("", cache.getId());
    }
}