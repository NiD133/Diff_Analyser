package org.joda.time.convert;

import org.junit.Test;
import static org.junit.Assert.assertThrows;

/**
 * Unit tests for the {@link StringConverter} class.
 */
public class StringConverterTest {

    /**
     * Tests that calling setInto() with a null ReadWritablePeriod argument
     * correctly throws a NullPointerException.
     */
    @Test
    public void setIntoPeriodWithNullPeriodShouldThrowNullPointerException() {
        // Arrange
        StringConverter converter = new StringConverter();

        // Act & Assert
        // The method is expected to throw a NullPointerException because it
        // attempts to call a method on the 'period' object, which is null.
        assertThrows(NullPointerException.class, () -> {
            converter.setInto(null, "PT1H", null);
        });
    }
}