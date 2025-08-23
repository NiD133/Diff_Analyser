package org.jfree.chart.axis;

import org.junit.Test;
import java.text.NumberFormat;

/**
 * Tests for the equals() method in the {@link QuarterDateFormat} class.
 */
public class QuarterDateFormatTest {

    /**
     * Verifies that calling equals() on an instance with a null NumberFormat
     * throws a NullPointerException. This behavior is inherited from the
     * superclass, java.text.DateFormat, which does not perform a null check
     * on its internal numberFormat field during comparison.
     */
    @Test(expected = NullPointerException.class)
    public void equals_whenNumberFormatIsNull_shouldThrowNullPointerException() {
        // Arrange: Create two formatters, and set the NumberFormat of one to null.
        QuarterDateFormat formatterWithNullNumberFormat = new QuarterDateFormat();
        formatterWithNullNumberFormat.setNumberFormat(null); // This is allowed by the parent class API.

        QuarterDateFormat otherFormatter = new QuarterDateFormat();

        // Act & Assert: Comparing the two should trigger an NPE from the superclass's equals method.
        formatterWithNullNumberFormat.equals(otherFormatter);
    }
}