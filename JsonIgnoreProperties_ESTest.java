package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.LinkedHashSet;
import java.util.Set;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

/**
 * Test suite for JsonIgnoreProperties.Value class functionality.
 * Tests configuration merging, property ignoring, and various flag combinations.
 */
@RunWith(EvoRunner.class) 
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, 
                     resetStaticState = true, separateClassLoader = true) 
public class JsonIgnoreProperties_ESTest extends JsonIgnoreProperties_ESTest_scaffolding {

    // ========== Configuration Merging Tests ==========
    
    @Test(timeout = 4000)
    public void testMergeConfigurationsWithAllowSetters() throws Throwable {
        // Given: Base configuration with ignored properties
        String[] ignoredProperties = new String[1];
        JsonIgnoreProperties.Value baseConfig = JsonIgnoreProperties.Value.forIgnoredProperties(ignoredProperties);
        
        // When: Creating configuration with allowSetters and merging
        JsonIgnoreProperties.Value configWithSetters = baseConfig.withAllowSetters();
        JsonIgnoreProperties.Value mergedConfig = baseConfig.withOverrides(configWithSetters);
        
        // Then: Merged configuration should have expected properties
        assertFalse("Should not ignore unknown properties", mergedConfig.getIgnoreUnknown());
        assertFalse("Should not allow getters", mergedConfig.getAllowGetters());
        
        // And: Configuration without merge should behave correctly
        JsonIgnoreProperties.Value configWithoutMerge = baseConfig.withoutMerge();
        assertFalse("Should not have merge enabled", configWithoutMerge.getMerge());
        assertFalse("Should not allow setters", configWithoutMerge.getAllowSetters());
        
        // And: Merging multiple configurations should work
        JsonIgnoreProperties.Value[] configs = {configWithoutMerge, mergedConfig};
        JsonIgnoreProperties.Value finalConfig = JsonIgnoreProperties.Value.mergeAll(configs);
        assertTrue("Final config should allow setters", finalConfig.getAllowSetters());
    }

    @Test(timeout = 4000)
    public void testIgnoreUnknownConfiguration() throws Throwable {
        // Given: Configuration that ignores unknown properties
        JsonIgnoreProperties.Value config = JsonIgnoreProperties.Value.forIgnoreUnknown(true);
        
        // When: Using the configuration in a set operation (testing hashCode/equals)
        LinkedHashSet<String> testSet = new LinkedHashSet<>();
        testSet.remove(config); // This tests hashCode implementation
        
        // Then: Configuration should have expected properties
        assertFalse("Should not allow setters by default", config.getAllowSetters());
        assertTrue("Should ignore unknown properties", config.getIgnoreUnknown());
        assertTrue("Should have merge enabled by default", config.getMerge());
        assertFalse("Should not allow getters by default", config.getAllowGetters());
    }

    // ========== String Representation Tests ==========
    
    @Test(timeout = 4000)
    public void testToStringWithAllowGetters() throws Throwable {
        // Given: Empty configuration with specific flags
        JsonIgnoreProperties.Value emptyConfig = JsonIgnoreProperties.Value.EMPTY;
        Set<String> ignoredProperties = emptyConfig.findIgnoredForSerialization();
        JsonIgnoreProperties.Value config = JsonIgnoreProperties.Value.construct(
            ignoredProperties, false, true, false, true);
        
        // When: Converting to string
        String result = config.toString();
        
        // Then: String should contain expected format
        assertEquals("String representation should match expected format",
            "JsonIgnoreProperties.Value(ignored=[],ignoreUnknown=false,allowGetters=true,allowSetters=false,merge=true)", 
            result);
    }

    @Test(timeout = 4000)
    public void testToStringWithIgnoreUnknown() throws Throwable {
        // Given: Configuration that ignores unknown properties
        JsonIgnoreProperties.Value config = JsonIgnoreProperties.Value.forIgnoreUnknown(true);
        
        // When: Converting to string
        String result = config.toString();
        
        // Then: String should reflect ignore unknown setting
        assertEquals("String should show ignoreUnknown=true",
            "JsonIgnoreProperties.Value(ignored=[],ignoreUnknown=true,allowGetters=false,allowSetters=false,merge=true)", 
            result);
    }

    // ========== Getter/Setter Configuration Tests ==========
    
    @Test(timeout = 4000)
    public void testAllowSettersConfiguration() throws Throwable {
        // Given: Empty configuration
        JsonIgnoreProperties.Value emptyConfig = JsonIgnoreProperties.Value.EMPTY;
        Set<String> ignoredProperties = emptyConfig.findIgnoredForSerialization();
        
        // When: Creating configuration with allowSetters enabled
        JsonIgnoreProperties.Value config = new JsonIgnoreProperties.Value(
            ignoredProperties, false, false, true, true);
        
        // Then: Configuration should allow setters
        assertTrue("Should allow setters", config.getAllowSetters());
        assertFalse("Should not ignore unknown properties", config.getIgnoreUnknown());
        assertTrue("Should have merge enabled", config.getMerge());
        assertFalse("Should not allow getters", config.getAllowGetters());
    }

    @Test(timeout = 4000)
    public void testAllowGettersConfiguration() throws Throwable {
        // Given: Configuration that ignores unknown properties
        JsonIgnoreProperties.Value baseConfig = JsonIgnoreProperties.Value.forIgnoreUnknown(true);
        Set<String> ignoredProperties = baseConfig.getIgnored();
        
        // When: Creating configuration with allowGetters enabled
        JsonIgnoreProperties.Value config = new JsonIgnoreProperties.Value(
            ignoredProperties, true, false, true, true);
        
        // Then: Configuration should have expected properties
        assertFalse("Should not allow getters", config.getAllowGetters());
        assertTrue("Should allow setters", config.getAllowSetters());
        assertTrue("Should ignore unknown properties", config.getIgnoreUnknown());
        assertTrue("Should have merge enabled", config.getMerge());
    }

    // ========== Serialization Filtering Tests ==========
    
    @Test(timeout = 4000)
    public void testFindIgnoredForSerialization() throws Throwable {
        // Given: Configuration with ignored properties and allowSetters
        String[] ignoredProperties = new String[0];
        JsonIgnoreProperties.Value baseConfig = JsonIgnoreProperties.Value.forIgnoredProperties(ignoredProperties);
        JsonIgnoreProperties.Value config = baseConfig.withAllowSetters();
        
        // When: Finding ignored properties for serialization
        Set<String> ignoredForSerialization = config.findIgnoredForSerialization();
        
        // Then: Configuration should have expected properties
        assertTrue("Should allow setters", config.getAllowSetters());
        assertTrue("Should have merge enabled", config.getMerge());
        assertFalse("Should not allow getters", config.getAllowGetters());
        assertFalse("Should not ignore unknown properties", config.getIgnoreUnknown());
        assertNotNull("Ignored set should not be null", ignoredForSerialization);
    }

    // ========== Deserialization Tests ==========
    
    @Test(timeout = 4000)
    public void testReadResolveAfterDeserialization() throws Throwable {
        // Given: Configuration with specific settings
        JsonIgnoreProperties.Value emptyConfig = JsonIgnoreProperties.Value.EMPTY;
        Set<String> ignoredProperties = emptyConfig.findIgnoredForSerialization();
        JsonIgnoreProperties.Value config = JsonIgnoreProperties.Value.construct(
            ignoredProperties, false, true, true, true);
        
        // When: Removing allowGetters and deserializing
        JsonIgnoreProperties.Value configWithoutGetters = config.withoutAllowGetters();
        JsonIgnoreProperties.Value deserializedConfig = (JsonIgnoreProperties.Value) configWithoutGetters.readResolve();
        
        // Then: Deserialized configuration should maintain properties
        assertTrue("Should have merge enabled", deserializedConfig.getMerge());
        assertTrue("Should allow setters", deserializedConfig.getAllowSetters());
        assertFalse("Should not ignore unknown properties", deserializedConfig.getIgnoreUnknown());
        assertFalse("Should not allow getters", deserializedConfig.getAllowGetters());
    }

    // ========== Configuration Chaining Tests ==========
    
    @Test(timeout = 4000)
    public void testConfigurationChaining() throws Throwable {
        // Given: Base configuration that ignores unknown properties
        JsonIgnoreProperties.Value baseConfig = JsonIgnoreProperties.Value.forIgnoreUnknown(true);
        
        // When: Chaining configuration changes
        JsonIgnoreProperties.Value configWithGetters = baseConfig.withAllowGetters();
        JsonIgnoreProperties.Value finalConfig = configWithGetters.withoutMerge();
        
        // Then: Final configuration should have expected properties
        assertTrue("Should allow getters", finalConfig.getAllowGetters());
        assertTrue("Should ignore unknown properties", finalConfig.getIgnoreUnknown());
        assertFalse("Should not allow setters", finalConfig.getAllowSetters());
        assertFalse("Should not have merge enabled", finalConfig.getMerge());
    }

    // ========== Merge Functionality Tests ==========
    
    @Test(timeout = 4000)
    public void testMergeWithNullValues() throws Throwable {
        // Given: Configuration with ignored properties
        String[] ignoredProperties = new String[5];
        JsonIgnoreProperties.Value config = JsonIgnoreProperties.Value.forIgnoredProperties(ignoredProperties);
        
        // When: Merging with null
        JsonIgnoreProperties.Value mergedConfig = JsonIgnoreProperties.Value.merge(null, config);
        
        // Then: Result should be the non-null configuration
        assertNotNull("Merged config should not be null", mergedConfig);
        assertSame("Should return the non-null config", config, mergedConfig);
        assertFalse("Should not ignore unknown properties", mergedConfig.getIgnoreUnknown());
        assertFalse("Should not allow getters", mergedConfig.getAllowGetters());
        assertFalse("Should not allow setters", mergedConfig.getAllowSetters());
        assertTrue("Should have merge enabled", mergedConfig.getMerge());
    }

    // ========== Equality Tests ==========
    
    @Test(timeout = 4000)
    public void testEqualityWithSameConfiguration() throws Throwable {
        // Given: Configuration that ignores unknown properties
        JsonIgnoreProperties.Value config = JsonIgnoreProperties.Value.forIgnoreUnknown(true);
        JsonIgnoreProperties.Value configWithoutIgnored = config.withoutIgnored();
        
        // When: Comparing configurations
        boolean areEqual = config.equals(configWithoutIgnored);
        
        // Then: Configurations should be equal (no ignored properties to remove)
        assertTrue("Configurations should be equal", areEqual);
        assertTrue("Should have merge enabled", configWithoutIgnored.getMerge());
        assertFalse("Should not allow getters", configWithoutIgnored.getAllowGetters());
        assertFalse("Should not allow setters", configWithoutIgnored.getAllowSetters());
        assertTrue("Should ignore unknown properties", config.getIgnoreUnknown());
    }

    @Test(timeout = 4000)
    public void testEqualityWithDifferentConfigurations() throws Throwable {
        // Given: Two different configurations
        String[] ignoredProperties = new String[0];
        JsonIgnoreProperties.Value config1 = JsonIgnoreProperties.Value.forIgnoredProperties(ignoredProperties);
        JsonIgnoreProperties.Value config2 = JsonIgnoreProperties.Value.forIgnoreUnknown(true);
        
        // When: Comparing configurations
        boolean areEqual = config2.equals(config1);
        
        // Then: Configurations should not be equal
        assertFalse("Configurations should not be equal", areEqual);
    }

    @Test(timeout = 4000)
    public void testEqualityWithNull() throws Throwable {
        // Given: Empty configuration
        JsonIgnoreProperties.Value config = JsonIgnoreProperties.Value.empty();
        
        // When: Comparing with null
        boolean isEqual = config.equals(null);
        
        // Then: Should not be equal to null
        assertFalse("Configuration should not equal null", isEqual);
    }

    @Test(timeout = 4000)
    public void testEqualityWithSameInstance() throws Throwable {
        // Given: Empty configuration
        JsonIgnoreProperties.Value config = JsonIgnoreProperties.Value.EMPTY;
        
        // When: Comparing with itself using predicate
        boolean isEqual = java.util.function.Predicate.isEqual(config).test(config);
        
        // Then: Should be equal to itself
        assertTrue("Configuration should equal itself", isEqual);
    }

    @Test(timeout = 4000)
    public void testEqualityWithDifferentType() throws Throwable {
        // Given: Configuration and different object type
        JsonIgnoreProperties.Value config = JsonIgnoreProperties.Value.forIgnoreUnknown(true);
        Object differentObject = new Object();
        
        // When: Comparing with different type
        boolean isEqual = config.equals(differentObject);
        
        // Then: Should not be equal
        assertFalse("Configuration should not equal different type", isEqual);
    }

    // ========== Mock-based Tests ==========
    
    @Test(timeout = 4000)
    public void testCreateFromMockedAnnotation() throws Throwable {
        // Given: Mocked JsonIgnoreProperties annotation
        JsonIgnoreProperties mockAnnotation = mock(JsonIgnoreProperties.class, CALLS_REAL_METHODS);
        doReturn(false).when(mockAnnotation).allowGetters();
        doReturn(false).when(mockAnnotation).allowSetters();
        doReturn(false).when(mockAnnotation).ignoreUnknown();
        doReturn((String[]) null).when(mockAnnotation).value();
        
        // When: Creating Value from annotation
        JsonIgnoreProperties.Value config = JsonIgnoreProperties.Value.from(mockAnnotation);
        JsonIgnoreProperties.Value configWithSetters = config.withAllowSetters();
        JsonIgnoreProperties.Value finalConfig = configWithSetters.withoutAllowSetters();
        
        // Then: Configuration should have expected properties
        assertFalse("Should not allow getters", finalConfig.getAllowGetters());
        assertFalse("Should not ignore unknown properties", finalConfig.getIgnoreUnknown());
        assertFalse("Should not allow setters", finalConfig.getAllowSetters());
        assertFalse("Should not have merge enabled", finalConfig.getMerge());
    }

    // ========== Utility Method Tests ==========
    
    @Test(timeout = 4000)
    public void testValueForMethod() throws Throwable {
        // Given: Configuration with ignored properties
        LinkedHashSet<String> ignoredProperties = new LinkedHashSet<>();
        JsonIgnoreProperties.Value config = JsonIgnoreProperties.Value.forIgnoredProperties(ignoredProperties);
        
        // When: Getting value for annotation class
        Class<JsonIgnoreProperties> annotationClass = config.valueFor();
        
        // Then: Should return correct annotation class
        assertEquals("Should return JsonIgnoreProperties class", 
                    JsonIgnoreProperties.class, annotationClass);
    }

    @Test(timeout = 4000)
    public void testGetMergeProperty() throws Throwable {
        // Given: Configuration with ignored properties
        LinkedHashSet<String> ignoredProperties = new LinkedHashSet<>();
        JsonIgnoreProperties.Value config = JsonIgnoreProperties.Value.forIgnoredProperties(ignoredProperties);
        
        // When: Getting merge property
        boolean hasMerge = config.getMerge();
        
        // Then: Should have merge enabled by default
        assertTrue("Should have merge enabled", hasMerge);
        assertFalse("Should not ignore unknown properties", config.getIgnoreUnknown());
        assertFalse("Should not allow getters", config.getAllowGetters());
        assertFalse("Should not allow setters", config.getAllowSetters());
    }
}