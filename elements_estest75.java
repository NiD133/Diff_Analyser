package org.jsoup.select;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link Elements} class, focusing on selector-based methods.
 */
public class ElementsTest {

    /**
     * Verifies that calling the {@code prev(String query)} method with an empty query string
     * correctly throws an {@link IllegalStateException}. The underlying query parser requires
     * a non-empty selector.
     */
    @Test
    public void prevWithEmptyQueryThrowsIllegalStateException() {
        // Arrange: Create an instance of Elements. The test's outcome is independent of its contents.
        Elements elements = new Elements();

        // Act & Assert: Attempting the operation with an invalid query should throw an exception.
        try {
            elements.prev("");
            fail("Expected an IllegalStateException to be thrown for an empty query, but no exception occurred.");
        } catch (IllegalStateException e) {
            // The exception is expected. We verify its message to confirm the failure reason.
            assertEquals("String must not be empty", e.getMessage());
        }
    }
}