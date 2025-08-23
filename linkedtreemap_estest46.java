package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the inner class {@link LinkedTreeMap.Node}.
 */
public class LinkedTreeMapNodeTest {

    @Test
    public void setValueOnNewNodeShouldReturnNull() {
        // Arrange
        // Create a new "header" node that allows null values. The specific key and
        // value types (e.g., String, Integer) are not important for this test.
        LinkedTreeMap.Node<String, Integer> node = new LinkedTreeMap.Node<>(true);

        // Act
        // Set a value on the newly created node and capture the returned (previous) value.
        Integer previousValue = node.setValue(null);

        // Assert
        // For a new node, the previous value should always be null.
        assertNull("The previous value of a new node should be null", previousValue);
    }
}