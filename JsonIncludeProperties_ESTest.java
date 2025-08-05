package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import java.util.LinkedHashSet;
import java.util.Set;
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
public class JsonIncludeProperties_ESTest extends JsonIncludeProperties_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testGetIncludedReturnsNullForAllValue() {
        JsonIncludeProperties.Value value = JsonIncludeProperties.Value.ALL;
        Set<String> includedProperties = value.getIncluded();
        assertNull(includedProperties);
    }

    @Test(timeout = 4000)
    public void testFromReturnsNonNullValue() {
        String[] properties = new String[5];
        JsonIncludeProperties mockAnnotation = mock(JsonIncludeProperties.class, CALLS_REAL_METHODS);
        doReturn(properties).when(mockAnnotation).value();

        JsonIncludeProperties.Value value = JsonIncludeProperties.Value.from(mockAnnotation);
        assertNotNull(value);
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentValues() {
        JsonIncludeProperties.Value allValue = JsonIncludeProperties.Value.ALL;
        JsonIncludeProperties mockAnnotation = mock(JsonIncludeProperties.class, CALLS_REAL_METHODS);
        doReturn((String[]) null).when(mockAnnotation).value();

        JsonIncludeProperties.Value fromValue = JsonIncludeProperties.Value.from(mockAnnotation);
        assertFalse(allValue.equals(fromValue));
        assertFalse(fromValue.equals(allValue));
    }

    @Test(timeout = 4000)
    public void testEqualsWithNullIncludedSet() {
        JsonIncludeProperties.Value allValue = JsonIncludeProperties.Value.ALL;
        JsonIncludeProperties.Value nullIncludedValue = new JsonIncludeProperties.Value(null);

        assertTrue(nullIncludedValue.equals(allValue));
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentObjectType() {
        JsonIncludeProperties.Value allValue = JsonIncludeProperties.Value.all();
        Object differentObject = new Object();

        assertFalse(allValue.equals(differentObject));
    }

    @Test(timeout = 4000)
    public void testEqualsWithNullObject() {
        JsonIncludeProperties.Value allValue = JsonIncludeProperties.Value.all();

        assertFalse(allValue.equals(null));
    }

    @Test(timeout = 4000)
    public void testEqualsWithSameInstance() {
        JsonIncludeProperties.Value allValue = JsonIncludeProperties.Value.all();

        assertTrue(allValue.equals(allValue));
    }

    @Test(timeout = 4000)
    public void testWithOverridesReturnsSameInstance() {
        JsonIncludeProperties.Value allValue = JsonIncludeProperties.Value.ALL;
        JsonIncludeProperties mockAnnotation = mock(JsonIncludeProperties.class, CALLS_REAL_METHODS);
        doReturn((String[]) null).when(mockAnnotation).value();

        JsonIncludeProperties.Value fromValue = JsonIncludeProperties.Value.from(mockAnnotation);
        JsonIncludeProperties.Value overriddenValue = fromValue.withOverrides(allValue);

        assertSame(overriddenValue, fromValue);
    }

    @Test(timeout = 4000)
    public void testRemoveFromEmptySet() {
        JsonIncludeProperties.Value allValue = JsonIncludeProperties.Value.ALL;
        LinkedHashSet<String> emptySet = new LinkedHashSet<>();

        assertFalse(emptySet.remove(allValue));
    }

    @Test(timeout = 4000)
    public void testRemoveFromEmptySetWithMockedValue() {
        String[] properties = new String[0];
        JsonIncludeProperties mockAnnotation = mock(JsonIncludeProperties.class, CALLS_REAL_METHODS);
        doReturn(properties).when(mockAnnotation).value();

        JsonIncludeProperties.Value value = JsonIncludeProperties.Value.from(mockAnnotation);
        LinkedHashSet<String> emptySet = new LinkedHashSet<>();

        assertFalse(emptySet.remove(value));
    }

    @Test(timeout = 4000)
    public void testWithOverridesCreatesNewInstance() {
        LinkedHashSet<String> propertiesSet = new LinkedHashSet<>();
        propertiesSet.add("LI }f.Ax<09fr");

        JsonIncludeProperties.Value originalValue = new JsonIncludeProperties.Value(propertiesSet);
        JsonIncludeProperties.Value overriddenValue = originalValue.withOverrides(originalValue);

        assertTrue(overriddenValue.equals(originalValue));
        assertNotSame(overriddenValue, originalValue);
    }

    @Test(timeout = 4000)
    public void testWithOverridesWithDifferentValue() {
        String[] properties = new String[0];
        JsonIncludeProperties mockAnnotation = mock(JsonIncludeProperties.class, CALLS_REAL_METHODS);
        doReturn(properties).when(mockAnnotation).value();

        JsonIncludeProperties.Value value = JsonIncludeProperties.Value.from(mockAnnotation);
        LinkedHashSet<String> propertiesSet = new LinkedHashSet<>();
        propertiesSet.add("K|D{$!:2zda");

        JsonIncludeProperties.Value differentValue = new JsonIncludeProperties.Value(propertiesSet);
        JsonIncludeProperties.Value overriddenValue = value.withOverrides(differentValue);

        assertNotSame(overriddenValue, value);
        assertTrue(overriddenValue.equals(value));
    }

    @Test(timeout = 4000)
    public void testWithOverridesReturnsOverrideInstance() {
        JsonIncludeProperties.Value allValue = JsonIncludeProperties.Value.ALL;
        JsonIncludeProperties mockAnnotation = mock(JsonIncludeProperties.class, CALLS_REAL_METHODS);
        doReturn((String[]) null).when(mockAnnotation).value();

        JsonIncludeProperties.Value fromValue = JsonIncludeProperties.Value.from(mockAnnotation);
        JsonIncludeProperties.Value overriddenValue = allValue.withOverrides(fromValue);

        assertSame(overriddenValue, fromValue);
    }

    @Test(timeout = 4000)
    public void testFromWithNullAnnotation() {
        JsonIncludeProperties.Value value = JsonIncludeProperties.Value.from(null);

        assertNotNull(value);
    }

    @Test(timeout = 4000)
    public void testToStringRepresentation() {
        JsonIncludeProperties.Value allValue = JsonIncludeProperties.Value.ALL;
        String expectedString = "JsonIncludeProperties.Value(included=null)";

        assertEquals(expectedString, allValue.toString());
    }

    @Test(timeout = 4000)
    public void testWithOverridesWithNull() {
        JsonIncludeProperties.Value allValue = JsonIncludeProperties.Value.all();
        JsonIncludeProperties.Value overriddenValue = allValue.withOverrides(null);

        assertSame(overriddenValue, allValue);
    }

    @Test(timeout = 4000)
    public void testValueForReturnsCorrectClass() {
        JsonIncludeProperties.Value allValue = JsonIncludeProperties.Value.ALL;
        Class<JsonIncludeProperties> expectedClass = JsonIncludeProperties.class;

        assertEquals(expectedClass, allValue.valueFor());
    }
}