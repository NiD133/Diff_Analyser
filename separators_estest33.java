package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;

// The original test class name is kept to match the request context.
public class Separators_ESTestTest33 extends Separators_ESTest_scaffolding {

    /**
     * Tests that the `withRootSeparator` method returns the same instance
     * when called with a value that is already set. This verifies an
     * important optimization for this immutable class to avoid needless
     * object creation.
     */
    @Test
    public void withRootSeparator_whenSeparatorIsUnchanged_returnsSameInstance() {
        // ARRANGE: Create an initial Separators instance.
        final Separators originalSeparators = new Separators(
                "", 'J', Separators.Spacing.NONE,
                'J', Separators.Spacing.NONE, "",
                'J', Separators.Spacing.NONE, "2U#AD9");
        
        final String newRootSeparator = " ";

        // ACT
        // First, create a new instance by changing the root separator.
        final Separators updatedSeparators = originalSeparators.withRootSeparator(newRootSeparator);
        
        // Then, call the method again with the same separator value.
        final Separators sameInstanceSeparators = updatedSeparators.withRootSeparator(newRootSeparator);

        // ASSERT
        // 1. Verify that changing the separator initially created a new object.
        assertNotSame("A new instance should be created when the separator changes.",
                originalSeparators, updatedSeparators);
        assertEquals("The root separator should be updated.",
                newRootSeparator, updatedSeparators.getRootSeparator());

        // 2. Verify the core behavior: the second call returned the *same* instance.
        assertSame("The same instance should be returned when the separator value is not changed.",
                updatedSeparators, sameInstanceSeparators);

        // 3. Verify that other properties were preserved in the updated instance.
        assertEquals('J', updatedSeparators.getArrayValueSeparator());
        assertEquals("2U#AD9", updatedSeparators.getArrayEmptySeparator());
        assertEquals('J', updatedSeparators.getObjectEntrySeparator());
    }
}