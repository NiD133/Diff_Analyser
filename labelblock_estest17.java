package org.jfree.chart.block;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link LabelBlock} class, focusing on constructor validation.
 */
public class LabelBlockTest {

    /**
     * Verifies that the constructor throws an IllegalArgumentException when the
     * 'text' argument is null. A null text argument is not permitted.
     */
    @Test
    public void constructorShouldThrowExceptionForNullText() {
        try {
            new LabelBlock(null);
            fail("Expected an IllegalArgumentException to be thrown for a null 'text' argument.");
        } catch (IllegalArgumentException e) {
            // This is the expected behavior.
            // We can also verify the exception message for more precise testing.
            assertEquals("Null 'text' argument.", e.getMessage());
        }
    }
}