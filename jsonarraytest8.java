package com.google.gson;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Tests for {@link JsonArray} focusing on the behavior of "get as single element" methods.
 * These methods (e.g., {@code getAsByte()}) are expected to throw an exception
 * if the array does not contain exactly one element.
 */
public class JsonArrayTest {

    @Test
    public void getAsType_whenArrayIsEmpty_throwsIllegalStateException() {
        // Arrange
        JsonArray emptyArray = new JsonArray();

        // Act
        IllegalStateException exception =
            assertThrows(IllegalStateException.class, () -> emptyArray.getAsByte());

        // Assert
        assertThat(exception)
            .hasMessageThat()
            .isEqualTo("Array must have size 1, but has size 0");
    }

    @Test
    public void getAsType_whenArrayHasMultipleElements_throwsIllegalStateException() {
        // Arrange
        JsonArray multiElementArray = new JsonArray();
        multiElementArray.add(true);
        multiElementArray.add(false);

        // Act
        IllegalStateException exception =
            assertThrows(IllegalStateException.class, () -> multiElementArray.getAsByte());

        // Assert
        assertThat(exception)
            .hasMessageThat()
            .isEqualTo("Array must have size 1, but has size 2");
    }
}