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

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class JsonIgnoreProperties_ESTest extends JsonIgnoreProperties_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testMergeWithAllowSetters() throws Throwable {
        // Arrange
        String[] ignoredProperties = new String[1];
        JsonIgnoreProperties.Value baseValue = JsonIgnoreProperties.Value.forIgnoredProperties(ignoredProperties);
        JsonIgnoreProperties.Value overrideValue = baseValue.withAllowSetters();

        // Act
        JsonIgnoreProperties.Value mergedValue = baseValue.withOverrides(overrideValue);

        // Assert
        assertFalse(mergedValue.getIgnoreUnknown());
        assertFalse(mergedValue.getAllowGetters());
        assertTrue(mergedValue.getAllowSetters());
    }

    @Test(timeout = 4000)
    public void testIgnoreUnknownTrue() throws Throwable {
        // Arrange
        JsonIgnoreProperties.Value value = JsonIgnoreProperties.Value.forIgnoreUnknown(true);

        // Act & Assert
        assertTrue(value.getIgnoreUnknown());
        assertFalse(value.getAllowSetters());
        assertFalse(value.getAllowGetters());
    }

    @Test(timeout = 4000)
    public void testEmptyValueToString() throws Throwable {
        // Arrange
        JsonIgnoreProperties.Value emptyValue = JsonIgnoreProperties.Value.EMPTY;

        // Act
        String result = emptyValue.toString();

        // Assert
        assertEquals("JsonIgnoreProperties.Value(ignored=[],ignoreUnknown=false,allowGetters=false,allowSetters=false,merge=true)", result);
    }

    @Test(timeout = 4000)
    public void testConstructWithIgnoredProperties() throws Throwable {
        // Arrange
        Set<String> ignoredProperties = new LinkedHashSet<>();
        JsonIgnoreProperties.Value value = JsonIgnoreProperties.Value.construct(ignoredProperties, false, true, false, true);

        // Act
        boolean allowSetters = value.getAllowSetters();

        // Assert
        assertFalse(value.getIgnoreUnknown());
        assertTrue(value.getMerge());
        assertFalse(value.getAllowGetters());
        assertTrue(allowSetters);
    }

    @Test(timeout = 4000)
    public void testMergeAllValues() throws Throwable {
        // Arrange
        JsonIgnoreProperties.Value[] values = new JsonIgnoreProperties.Value[2];
        JsonIgnoreProperties.Value value1 = JsonIgnoreProperties.Value.forIgnoreUnknown(false);
        JsonIgnoreProperties.Value value2 = JsonIgnoreProperties.Value.construct(value1.findIgnoredForSerialization(), true, false, true, false);

        values[0] = value1;
        values[1] = value2;

        // Act
        JsonIgnoreProperties.Value mergedValue = JsonIgnoreProperties.Value.mergeAll(values);

        // Assert
        assertTrue(mergedValue.getAllowSetters());
        assertFalse(mergedValue.getAllowGetters());
        assertTrue(mergedValue.getMerge());
        assertTrue(mergedValue.getIgnoreUnknown());
    }

    // Additional refactored tests can follow the same pattern
}