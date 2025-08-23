package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the {@link Separators} class, focusing on its immutability
 * and "wither" methods.
 */
public class SeparatorsTest {

    /**
     * Verifies that "wither" methods, like {@code withObjectEntrySpacing},
     * create a new Separators instance with the specified property updated,
     * while leaving other properties unchanged.
     */
    @Test
    public void witherMethodsShouldCreateNewInstanceWithModifiedProperties() {
        // Arrange: Create an initial Separators instance with non-default values.
        Separators initialSeparators = new Separators(':', ',', ',');
        
        // Define the modifications to be applied.
        final String newRootSeparator = "";
        final Separators.Spacing newObjectEntrySpacing = Separators.Spacing.AFTER;

        // Act: Apply modifications using the chainable "wither" methods.
        // This should produce a new, distinct instance.
        Separators modifiedSeparators = initialSeparators
                .withRootSeparator(newRootSeparator)
                .withObjectEntrySpacing(newObjectEntrySpacing);

        // Assert: Verify the properties of the new, modified instance.
        assertNotSame("Wither methods should produce a new instance", initialSeparators, modifiedSeparators);

        // 1. Check the properties that were explicitly changed.
        assertEquals("The root separator should be updated",
                newRootSeparator, modifiedSeparators.getRootSeparator());
        assertEquals("The object entry spacing should be updated",
                newObjectEntrySpacing, modifiedSeparators.getObjectEntrySpacing());

        // 2. Check properties that were inherited from the initial instance.
        assertEquals(':', modifiedSeparators.getObjectFieldValueSeparator());
        assertEquals(',', modifiedSeparators.getObjectEntrySeparator());
        assertEquals(',', modifiedSeparators.getArrayValueSeparator());

        // 3. Check properties that retain their default values from the constructor.
        assertEquals(Separators.Spacing.BOTH, modifiedSeparators.getObjectFieldValueSpacing());
        assertEquals(Separators.Spacing.NONE, modifiedSeparators.getArrayValueSpacing());
        assertEquals(" ", modifiedSeparators.getObjectEmptySeparator());
        assertEquals(" ", modifiedSeparators.getArrayEmptySeparator());
    }
}