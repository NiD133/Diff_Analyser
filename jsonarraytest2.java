package com.google.gson;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.testing.EqualsTester;
import org.junit.Test;

/**
 * Test suite for the equals() and hashCode() methods of {@link JsonArray}.
 *
 * Note: The original class name was JsonArrayTestTest2. It has been renamed for clarity.
 */
public class JsonArrayEqualsTest {

    @Test
    public void testEqualsAndHashCode_forEmptyArrays() {
        // Two empty arrays should be equal and have the same hash code.
        // EqualsTester comprehensively checks the equals() and hashCode() contract.
        new EqualsTester()
            .addEqualityGroup(new JsonArray(), new JsonArray())
            .testEquals();
    }

    @Test
    public void testEqualsAndHashCode_forArraysWithSameElementsInOrder() {
        // Arrange: Create two arrays with the same elements in the same order.
        JsonArray array1 = new JsonArray();
        array1.add(new JsonObject());
        array1.add(new JsonPrimitive("test"));

        JsonArray array2 = new JsonArray();
        array2.add(new JsonObject());
        array2.add(new JsonPrimitive("test"));

        // Assert: The arrays should be equal and have the same hash code.
        assertThat(array1).isEqualTo(array2);
        assertThat(array1.hashCode()).isEqualTo(array2.hashCode());
    }

    @Test
    public void testNotEqual_whenSizesDiffer() {
        // Arrange: Create two arrays of different sizes.
        JsonArray arrayWithOneElement = new JsonArray();
        arrayWithOneElement.add(new JsonObject());

        JsonArray arrayWithTwoElements = new JsonArray();
        arrayWithTwoElements.add(new JsonObject());
        arrayWithTwoElements.add(JsonNull.INSTANCE);

        // Assert: The arrays should not be equal.
        assertThat(arrayWithOneElement).isNotEqualTo(arrayWithTwoElements);
    }

    @Test
    public void testNotEqual_whenElementsDiffer() {
        // Arrange: Create two arrays of the same size but with different elements.
        JsonArray array1 = new JsonArray();
        array1.add(new JsonObject());
        array1.add(new JsonObject());

        JsonArray array2 = new JsonArray();
        array2.add(new JsonObject());
        array2.add(JsonNull.INSTANCE); // Different element at index 1

        // Assert: The arrays should not be equal.
        assertThat(array1).isNotEqualTo(array2);
    }

    @Test
    public void testNotEqual_whenElementOrderDiffers() {
        // Arrange: Create two arrays with the same elements but in a different order.
        JsonArray array1 = new JsonArray();
        array1.add(new JsonPrimitive("a"));
        array1.add(new JsonPrimitive("b"));

        JsonArray array2 = new JsonArray();
        array2.add(new JsonPrimitive("b"));
        array2.add(new JsonPrimitive("a"));

        // Assert: The arrays should not be equal, as order is significant.
        assertThat(array1).isNotEqualTo(array2);
    }

    @Test
    public void testNotEqual_toOtherTypes() {
        // Arrange
        JsonArray array = new JsonArray();
        array.add(new JsonPrimitive(1));

        // Assert
        assertThat(array.equals(null)).isFalse();
        assertThat(array.equals(new JsonObject())).isFalse();
        assertThat(array.equals("[1]")).isFalse();
    }
}