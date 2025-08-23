package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;

// Note: EvoSuite-related imports and class structure are kept to match the original file.
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Separators_ESTestTest13 extends Separators_ESTest_scaffolding {

    /**
     * Tests that the canonical constructor correctly assigns all provided separator values,
     * particularly verifying that it handles `null` for string-based separators.
     */
    @Test
    public void constructorShouldCorrectlyAssignCustomSeparatorsAndHandleNulls() {
        // Arrange: Define a custom separator configuration using the canonical constructor.
        // This tests the constructor that takes all 9 separator properties.
        // We intentionally use null for some string-based separators to verify they are handled correctly.
        Separators separators = new Separators(
            null,                      // rootSeparator
            '6',                       // objectFieldValueSeparator
            Separators.Spacing.NONE,   // objectFieldValueSpacing
            'E',                       // objectEntrySeparator
            Separators.Spacing.NONE,   // objectEntrySpacing
            null,                      // objectEmptySeparator
            '3',                       // arrayValueSeparator
            Separators.Spacing.NONE,   // arrayValueSpacing
            null                       // arrayEmptySeparator
        );

        // Act & Assert: Verify that each getter returns the exact value provided to the constructor.
        assertEquals("Object field value separator should match the constructor argument.",
                '6', separators.getObjectFieldValueSeparator());
        assertEquals("Object entry separator should match the constructor argument.",
                'E', separators.getObjectEntrySeparator());
        assertEquals("Array value separator should match the constructor argument.",
                '3', separators.getArrayValueSeparator());
        assertNull("Object empty separator should be null as provided to the constructor.",
                separators.getObjectEmptySeparator());
    }
}