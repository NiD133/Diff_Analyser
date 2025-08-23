package com.google.gson;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class JsonArrayTestTest3 {

    @Test
    public void removeByIndex_fromEmptyArray_throwsException() {
        // Arrange
        JsonArray array = new JsonArray();

        // Act & Assert
        assertThrows(IndexOutOfBoundsException.class, () -> array.remove(0));
    }

    @Test
    public void removeByElement_existingElement_removesElementAndReturnsTrue() {
        // Arrange
        JsonPrimitive elementA = new JsonPrimitive("a");
        JsonPrimitive elementB = new JsonPrimitive("b");
        JsonArray array = new JsonArray();
        array.add(elementA);
        array.add(elementB);

        // Act
        boolean wasRemoved = array.remove(elementA);

        // Assert
        assertThat(wasRemoved).isTrue();
        assertThat(array).hasSize(1);
        assertThat(array.contains(elementA)).isFalse();
        assertThat(array.get(0)).isEqualTo(elementB); // Verify the correct element remains
    }

    @Test
    public void removeByElement_nonexistentElement_returnsFalseAndDoesNotModifyArray() {
        // Arrange
        JsonPrimitive elementA = new JsonPrimitive("a");
        JsonPrimitive nonexistentElement = new JsonPrimitive("nonexistent");
        JsonArray array = new JsonArray();
        array.add(elementA);

        // Act
        boolean wasRemoved = array.remove(nonexistentElement);

        // Assert
        assertThat(wasRemoved).isFalse();
        assertThat(array).hasSize(1);
        assertThat(array.get(0)).isEqualTo(elementA);
    }

    @Test
    public void removeByIndex_validIndex_removesElementAndReturnsIt() {
        // Arrange
        JsonPrimitive elementA = new JsonPrimitive("a");
        JsonPrimitive elementB = new JsonPrimitive("b");
        JsonArray array = new JsonArray();
        array.add(elementA);
        array.add(elementB);

        // Act
        JsonElement removedElement = array.remove(1);

        // Assert
        assertThat(removedElement).isEqualTo(elementB);
        assertThat(array).hasSize(1);
        assertThat(array.contains(elementB)).isFalse();
        assertThat(array.get(0)).isEqualTo(elementA); // Verify the correct element remains
    }
}