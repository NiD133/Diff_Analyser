package org.jfree.chart.labels;

import org.junit.jupiter.api.Test;
import java.text.NumberFormat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for the {@link IntervalCategoryItemLabelGenerator} class.
 */
class IntervalCategoryItemLabelGeneratorTest {

    /**
     * Verifies that the constructor throws an IllegalArgumentException when a null
     * NumberFormat is provided. A non-null formatter is a contract requirement.
     */
    @Test
    void constructor_WithNullNumberFormat_ShouldThrowIllegalArgumentException() {
        // Arrange: Define the arguments for the constructor call, with a null formatter.
        String labelFormat = "{2}";
        NumberFormat nullFormatter = null;

        // Act & Assert: Call the constructor and verify that the expected exception is thrown.
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new IntervalCategoryItemLabelGenerator(labelFormat, nullFormatter)
        );

        // Assert: Further verify that the exception message is correct.
        assertEquals("Null 'formatter' argument.", exception.getMessage());
    }
}