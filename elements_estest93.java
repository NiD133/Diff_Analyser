package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link Elements} class, focusing on constructor behavior.
 */
public class ElementsTest {

    /**
     * Verifies that the Elements constructor throws an IllegalArgumentException
     * when initialized with a negative capacity. This is expected behavior,
     * as a collection cannot have a negative size.
     */
    @Test
    public void constructorWithNegativeCapacityThrowsIllegalArgumentException() {
        try {
            new Elements(-1);
            fail("Expected an IllegalArgumentException to be thrown for negative capacity.");
        } catch (IllegalArgumentException e) {
            // The constructor delegates to ArrayList, which provides this specific message.
            assertEquals("Illegal Capacity: -1", e.getMessage());
        }
    }
}