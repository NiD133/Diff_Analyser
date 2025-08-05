/*
 * Refactored test suite for JsonIgnoreProperties.Value
 * Changes include:
 *   - Descriptive test method names
 *   - Meaningful variable names
 *   - Grouped related tests
 *   - Added explanatory comments
 *   - Simplified assertions
 */
package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Predicate;
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
public class JsonIgnoreProperties_ESTest extends JsonIgnoreProperties_ESTest_scaffolding {

    // ========================================================================
    // Test Group: Core Functionality & Merging
    // ========================================================================

    @Test(timeout = 4000)
    public void testMergeAllWithMultipleTransformations() throws Throwable {
        // Setup: Create base value with one ignored property
        String[] ignoredProperties = {"property"};
        JsonIgnoreProperties.Value baseValue = JsonIgnoreProperties.Value.forIgnoredProperties(ignoredProperties);
        
        // Exercise: Apply transformations
        JsonIgnoreProperties.Value withSetters = baseValue.withAllowSetters();
        JsonIgnoreProperties.Value withOverrides = baseValue.withOverrides(withSetters);
        JsonIgnoreProperties.Value withoutMerge = baseValue.withoutMerge();
        
        // Create merge inputs
        JsonIgnoreProperties.Value[] valuesToMerge = {withoutMerge, withOverrides};
        
        // Verify merge results
        JsonIgnoreProperties.Value merged = JsonIgnoreProperties.Value.mergeAll(valuesToMerge);
        assertTrue(merged.getAllowSetters());
        assertTrue(merged.equals(withSetters));
        assertNotSame(merged, withSetters);
    }

    @Test(timeout = 4000)
    public void testMergeNullWithValue() throws Throwable {
        // Setup: Create value with ignored properties
        String[] ignoredProperties = {"prop1", "prop2"};
        JsonIgnoreProperties.Value original = JsonIgnoreProperties.Value.forIgnoredProperties(ignoredProperties);
        
        // Exercise: Merge null with value
        JsonIgnoreProperties.Value merged = JsonIgnoreProperties.Value.merge(null, original);
        
        // Verify: Should return original value
        assertSame(merged, original);
    }

    @Test(timeout = 4000)
    public void testMergeAllWithTrueSettings() throws Throwable {
        // Setup: Create values with different configurations
        Set<String> emptySet = new LinkedHashSet<>();
        JsonIgnoreProperties.Value trueSettings = JsonIgnoreProperties.Value.construct(
            emptySet, true, true, true, true
        );
        String[] ignored = new String[0];
        JsonIgnoreProperties.Value falseSettings = JsonIgnoreProperties.Value.forIgnoredProperties(ignored);
        
        // Prepare merge array
        JsonIgnoreProperties.Value[] values = new JsonIgnoreProperties.Value[8];
        values[0] = falseSettings;
        values[5] = trueSettings;
        
        // Exercise: Merge all values
        JsonIgnoreProperties.Value merged = JsonIgnoreProperties.Value.mergeAll(values);
        
        // Verify: True settings should dominate
        assertNotNull(merged);
        assertTrue(merged.getIgnoreUnknown());
        assertTrue(merged.getAllowGetters());
        assertTrue(merged.getAllowSetters());
    }

    // ========================================================================
    // Test Group: Value Construction & Factory Methods
    // ========================================================================

    @Test(timeout = 4000)
    public void testConstructWithParameters() throws Throwable {
        // Setup: Empty ignored set
        Set<String> emptyIgnored = Collections.emptySet();
        
        // Exercise: Construct with specific parameters
        JsonIgnoreProperties.Value value = JsonIgnoreProperties.Value.construct(
            emptyIgnored, false, true, false, true
        );
        
        // Verify: toString reflects configuration
        assertEquals(
            "JsonIgnoreProperties.Value(ignored=[],ignoreUnknown=false,allowGetters=true,allowSetters=false,merge=true)",
            value.toString()
        );
    }

    @Test(timeout = 4000)
    public void testForIgnoreUnknownTrue() throws Throwable {
        // Exercise
        JsonIgnoreProperties.Value value = JsonIgnoreProperties.Value.forIgnoreUnknown(true);
        
        // Verify
        assertTrue(value.getIgnoreUnknown());
        assertEquals(
            "JsonIgnoreProperties.Value(ignored=[],ignoreUnknown=true,allowGetters=false,allowSetters=false,merge=true)",
            value.toString()
        );
    }

    @Test(timeout = 4000)
    public void testFromAnnotation() throws Throwable {
        // Setup: Mock annotation with specific configuration
        JsonIgnoreProperties annotation = mock(JsonIgnoreProperties.class);
        doReturn(new String[]{"prop"}).when(annotation).value();
        doReturn(true).when(annotation).ignoreUnknown();
        doReturn(false).when(annotation).allowGetters();
        doReturn(false).when(annotation).allowSetters();
        
        // Exercise: Create Value from annotation
        JsonIgnoreProperties.Value value = JsonIgnoreProperties.Value.from(annotation);
        
        // Verify
        assertEquals(1, value.getIgnored().size());
        assertTrue(value.getIgnoreUnknown());
    }

    // ========================================================================
    // Test Group: With/Without Transformations
    // ========================================================================

    @Test(timeout = 4000)
    public void testWithAllowSettersTransformation() throws Throwable {
        // Setup: Base value
        JsonIgnoreProperties.Value base = JsonIgnoreProperties.Value.EMPTY;
        
        // Exercise: Apply transformations
        JsonIgnoreProperties.Value withSetters = base.withAllowSetters();
        JsonIgnoreProperties.Value withoutSetters = withSetters.withoutAllowSetters();
        
        // Verify
        assertTrue(withSetters.getAllowSetters());
        assertFalse(withoutSetters.getAllowSetters());
        assertEquals(base, withoutSetters);
    }

    @Test(timeout = 4000)
    public void testWithIgnoreUnknownTransformation() throws Throwable {
        // Setup: Base value with ignoreUnknown=true
        JsonIgnoreProperties.Value base = JsonIgnoreProperties.Value.forIgnoreUnknown(true);
        
        // Exercise: Remove ignoreUnknown
        JsonIgnoreProperties.Value withoutUnknown = base.withoutIgnoreUnknown();
        
        // Verify
        assertFalse(withoutUnknown.getIgnoreUnknown());
        assertNotSame(withoutUnknown, base);
    }

    @Test(timeout = 4000)
    public void testWithIgnoredProperties() throws Throwable {
        // Setup: Base value
        JsonIgnoreProperties.Value base = JsonIgnoreProperties.Value.EMPTY;
        
        // Exercise: Add ignored properties
        String[] newIgnored = {"newProp"};
        JsonIgnoreProperties.Value withIgnored = base.withIgnored(newIgnored);
        
        // Verify
        assertEquals(1, withIgnored.getIgnored().size());
        assertTrue(withIgnored.getIgnored().contains("newProp"));
    }

    // ========================================================================
    // Test Group: Equality & Hash Code
    // ========================================================================

    @Test(timeout = 4000)
    public void testEqualsSameInstance() throws Throwable {
        // Setup
        JsonIgnoreProperties.Value value = JsonIgnoreProperties.Value.EMPTY;
        
        // Verify
        assertTrue(value.equals(value));
    }

    @Test(timeout = 4000)
    public void testEqualsDifferentSettings() throws Throwable {
        // Setup: Two different configurations
        JsonIgnoreProperties.Value empty = JsonIgnoreProperties.Value.EMPTY;
        JsonIgnoreProperties.Value ignoreUnknown = JsonIgnoreProperties.Value.forIgnoreUnknown(true);
        
        // Verify
        assertFalse(empty.equals(ignoreUnknown));
    }

    @Test(timeout = 4000)
    public void testEqualsAfterWithoutIgnored() throws Throwable {
        // Setup: Value with ignored properties
        String[] ignored = {"prop"};
        JsonIgnoreProperties.Value original = JsonIgnoreProperties.Value.forIgnoredProperties(ignored);
        
        // Exercise: Remove ignored properties
        JsonIgnoreProperties.Value withoutIgnored = original.withoutIgnored();
        
        // Verify: Should equal EMPTY
        assertTrue(withoutIgnored.equals(JsonIgnoreProperties.Value.EMPTY));
    }

    // ========================================================================
    // Test Group: Special Cases & Edge Conditions
    // ========================================================================

    @Test(timeout = 4000)
    public void testReadResolve() throws Throwable {
        // Setup: Value with ignoreUnknown=true
        JsonIgnoreProperties.Value original = JsonIgnoreProperties.Value.forIgnoreUnknown(true);
        
        // Exercise: Serialization resolution
        JsonIgnoreProperties.Value resolved = (JsonIgnoreProperties.Value) original.readResolve();
        
        // Verify: Should maintain configuration
        assertTrue(resolved.getIgnoreUnknown());
        assertEquals(original, resolved);
    }

    @Test(timeout = 4000)
    public void testFindIgnoredForSerialization() throws Throwable {
        // Setup: Value with ignored properties and allowGetters=true
        String[] ignored = {"prop"};
        JsonIgnoreProperties.Value base = JsonIgnoreProperties.Value.forIgnoredProperties(ignored);
        JsonIgnoreProperties.Value withGetters = base.withAllowGetters();
        
        // Verify: When allowGetters=true, no properties ignored for serialization
        assertTrue(withGetters.findIgnoredForSerialization().isEmpty());
    }

    @Test(timeout = 4000)
    public void testEmptyValueBehavior() throws Throwable {
        // Setup
        JsonIgnoreProperties.Value empty = JsonIgnoreProperties.Value.empty();
        
        // Verify default state
        assertFalse(empty.getIgnoreUnknown());
        assertFalse(empty.getAllowGetters());
        assertFalse(empty.getAllowSetters());
        assertTrue(empty.getMerge());
        assertTrue(empty.getIgnored().isEmpty());
    }

    // ========================================================================
    // Test Group: Set Operations
    // ========================================================================

    @Test(timeout = 4000)
    public void testSetOperationsWithValue() throws Throwable {
        // Setup: Value and Set
        JsonIgnoreProperties.Value value = JsonIgnoreProperties.Value.forIgnoreUnknown(true);
        Set<Object> testSet = new LinkedHashSet<>();
        
        // Exercise: Add and remove from set
        testSet.add(value);
        boolean removed = testSet.remove(value);
        
        // Verify
        assertTrue(removed);
        assertTrue(testSet.isEmpty());
    }

    // ========================================================================
    // Test Group: ToString Implementations
    // ========================================================================

    @Test(timeout = 4000)
    public void testToStringVariations() throws Throwable {
        // Configuration 1: Allow getters
        Set<String> ignored = Collections.emptySet();
        JsonIgnoreProperties.Value withGetters = JsonIgnoreProperties.Value.construct(
            ignored, false, true, false, true
        );
        assertEquals(
            "JsonIgnoreProperties.Value(ignored=[],ignoreUnknown=false,allowGetters=true,allowSetters=false,merge=true)",
            withGetters.toString()
        );
        
        // Configuration 2: Ignore unknown
        JsonIgnoreProperties.Value ignoreUnknown = JsonIgnoreProperties.Value.forIgnoreUnknown(true);
        assertEquals(
            "JsonIgnoreProperties.Value(ignored=[],ignoreUnknown=true,allowGetters=false,allowSetters=false,merge=true)",
            ignoreUnknown.toString()
        );
    }

    // ========================================================================
    // Test Group: Additional Core Tests
    // ========================================================================

    @Test(timeout = 4000)
    public void testWithOverrides() throws Throwable {
        // Setup: Base and override values
        JsonIgnoreProperties.Value base = JsonIgnoreProperties.Value.EMPTY;
        JsonIgnoreProperties.Value override = base.withAllowSetters();
        
        // Exercise: Apply overrides
        JsonIgnoreProperties.Value result = base.withOverrides(override);
        
        // Verify: Should use override settings
        assertTrue(result.getAllowSetters());
    }

    @Test(timeout = 4000)
    public void testGetMergeAfterTransformations() throws Throwable {
        // Setup: Base value
        JsonIgnoreProperties.Value base = JsonIgnoreProperties.Value.EMPTY;
        
        // Exercise: Apply merge transformations
        JsonIgnoreProperties.Value withoutMerge = base.withoutMerge();
        JsonIgnoreProperties.Value withMerge = withoutMerge.withMerge();
        
        // Verify
        assertFalse(withoutMerge.getMerge());
        assertTrue(withMerge.getMerge());
    }

    @Test(timeout = 4000)
    public void testFindIgnoredForDeserialization() throws Throwable {
        // Setup: Value with ignored properties
        String[] ignored = {"deserializationProp"};
        JsonIgnoreProperties.Value value = JsonIgnoreProperties.Value.forIgnoredProperties(ignored);
        JsonIgnoreProperties.Value withSetters = value.withAllowSetters();
        
        // Verify: When allowSetters=true, no properties ignored for deserialization
        assertTrue(withSetters.findIgnoredForDeserialization().isEmpty());
    }

    // ========================================================================
    // Test Group: Additional Edge Cases
    // ========================================================================

    @Test(timeout = 4000)
    public void testMergeAllWithNullElements() throws Throwable {
        // Setup: Array with null elements
        JsonIgnoreProperties.Value[] values = new JsonIgnoreProperties.Value[3];
        values[0] = JsonIgnoreProperties.Value.EMPTY;
        values[1] = null;
        values[2] = JsonIgnoreProperties.Value.forIgnoreUnknown(true);
        
        // Exercise
        JsonIgnoreProperties.Value merged = JsonIgnoreProperties.Value.mergeAll(values);
        
        // Verify: Should combine non-null values
        assertTrue(merged.getIgnoreUnknown());
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentObjectType() throws Throwable {
        // Setup
        JsonIgnoreProperties.Value value = JsonIgnoreProperties.Value.EMPTY;
        Object other = new Object();
        
        // Verify
        assertFalse(value.equals(other));
    }

    @Test(timeout = 4000)
    public void testValueForAnnotationInterface() throws Throwable {
        // Setup
        JsonIgnoreProperties.Value value = JsonIgnoreProperties.Value.EMPTY;
        
        // Exercise
        Class<JsonIgnoreProperties> annotationClass = value.valueFor();
        
        // Verify
        assertEquals(JsonIgnoreProperties.class, annotationClass);
    }
}