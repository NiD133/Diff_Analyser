package org.jfree.chart.block;

import org.junit.jupiter.api.Test;
import java.awt.Paint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for the {@link LabelBlock} class, focusing on exception handling.
 */
class LabelBlockTest {

    @Test
    void setPaint_shouldThrowIllegalArgumentException_whenPaintIsNull() {
        // Arrange: Create a LabelBlock instance.
        LabelBlock labelBlock = new LabelBlock("Test Label");
        String expectedErrorMessage = "Null 'paint' argument.";

        // Act & Assert: Verify that calling setPaint(null) throws the expected exception.
        IllegalArgumentException thrownException = assertThrows(
            IllegalArgumentException.class,
            () -> labelBlock.setPaint((Paint) null),
            "Expected setPaint(null) to throw IllegalArgumentException, but it didn't."
        );

        // Assert: Verify that the exception message is correct.
        assertEquals(expectedErrorMessage, thrownException.getMessage());
    }
}