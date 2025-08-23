package org.jsoup.nodes;

import org.junit.Test;

import java.io.StringWriter;

import static org.junit.Assert.assertThrows;

/**
 * Tests for the static html rendering methods in the {@link Attribute} class.
 */
public class AttributeTest {

    /**
     * Verifies that the static {@code Attribute.html()} method throws a NullPointerException
     * when provided with null OutputSettings. The settings are essential for determining
     * the correct HTML or XML syntax, so a null value is an invalid state.
     */
    @Test
    public void htmlShouldThrowNullPointerExceptionForNullOutputSettings() {
        // Arrange
        StringWriter writer = new StringWriter();
        String key = "id";
        String value = "test";

        // Act & Assert
        // The html() method requires OutputSettings to function; passing null should fail fast.
        assertThrows(NullPointerException.class, () -> {
            // The deprecated version of the method is being tested here.
            Attribute.html(key, value, writer, null);
        });
    }
}