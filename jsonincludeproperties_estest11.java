package com.fasterxml.jackson.annotation;

import org.junit.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * Tests for {@link JsonIncludeProperties.Value}.
 */
// The original test class name and runner are preserved for context.
@RunWith(org.evosuite.runtime.EvoRunner.class)
@org.evosuite.runtime.EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class JsonIncludeProperties_ESTestTest11 extends JsonIncludeProperties_ESTest_scaffolding {

    /**
     * Verifies that when a Value instance is overridden with itself,
     * the result is a new instance that is equal in content to the original.
     */
    @Test
    public void withOverrides_whenOverridingWithSelf_returnsEqualButNotSameInstance() {
        // Arrange: Create a Value instance with a single property to include.
        Set<String> propertiesToInclude = Collections.singleton("id");
        JsonIncludeProperties.Value originalValue = new JsonIncludeProperties.Value(propertiesToInclude);

        // Act: Call withOverrides using the instance itself as the override.
        JsonIncludeProperties.Value resultValue = originalValue.withOverrides(originalValue);

        // Assert: The new value should be equal to the original but be a different object.
        // The content (the set of properties) should be identical.
        assertEquals("The resulting value should have the same included properties", originalValue, resultValue);

        // The withOverrides method is expected to create a new instance.
        assertNotSame("The withOverrides method should return a new instance", originalValue, resultValue);
    }
}