package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class contains tests for the inner class {@link LinkedTreeMap.Node}.
 * This specific test file was improved from an auto-generated test.
 */
public class LinkedTreeMap_ESTestTest38 {

    /**
     * Tests that calling setValue on a newly created Node correctly updates its value
     * and returns the previous value, which should be null.
     */
    @Test
    public void setValueOnNewNode_shouldUpdateValueAndReturnNull() {
        // Arrange
        // A Node is an inner class of LinkedTreeMap. We create a "header" node,
        // which has a null key, similar to the original test's setup.
        LinkedTreeMap.Node<String, Integer> node = new LinkedTreeMap.Node<>(true);
        Integer newValue = 123;

        // Pre-condition check: A new node should have a null value.
        assertNull("A newly created node should have a null value.", node.getValue());

        // Act
        // Call setValue, which should return the old value.
        Integer oldValue = node.setValue(newValue);

        // Assert
        // The old value returned by setValue should be null.
        assertNull("The old value should be null for a new node.", oldValue);
        // The node's value should now be updated to the new value.
        assertEquals("The node's value should have been updated.", newValue, node.getValue());
    }
}