package com.fasterxml.jackson.annotation;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;

/**
 * Unit tests for the {@link JacksonInject.Value} class, focusing on its immutability
 * and factory methods.
 */
public class JacksonInjectValueTest {

    @Test
    public void withUseInput_whenSetToNull_revertsToEmptyState() {
        // Arrange: Start with the default EMPTY value, where all properties are null.
        final JacksonInject.Value emptyValue = JacksonInject.Value.EMPTY;

        // Act:
        // 1. Create a new Value instance by setting 'useInput' to false.
        // This should result in a value that is different from the empty state.
        JacksonInject.Value valueWithUseInputSet = emptyValue.withUseInput(false);

        // 2. Create another instance by reverting 'useInput' back to null.
        JacksonInject.Value revertedValue = valueWithUseInputSet.withUseInput(null);

        // Assert:
        // Verify that setting 'useInput' initially created a distinct value.
        assertNotEquals("Setting useInput should create a value different from EMPTY",
                emptyValue, valueWithUseInputSet);

        // Verify that reverting 'useInput' to null creates a new object instance.
        assertNotSame("Reverting to null should produce a new instance",
                valueWithUseInputSet, revertedValue);

        // Verify that the reverted value is now equal to the original EMPTY value,
        // as the 'useInput' property is back to its default (null) state.
        assertEquals("Reverting useInput to null should be equal to the EMPTY value",
                emptyValue, revertedValue);
    }
}