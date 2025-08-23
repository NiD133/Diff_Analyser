package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.temporal.TemporalAccessor;
import static org.junit.Assert.assertThrows;

/**
 * This test suite contains tests for the {@link JulianChronology} class.
 * This specific test focuses on the behavior of the zonedDateTime method.
 */
public class JulianChronology_ESTestTest14 extends JulianChronology_ESTest_scaffolding {

    /**
     * Tests that calling zonedDateTime with a null TemporalAccessor throws a NullPointerException.
     * The method's contract, defined in its Javadoc, specifies that the temporal accessor must not be null.
     */
    @Test
    public void zonedDateTime_withNullTemporalAccessor_throwsNullPointerException() {
        // Arrange: Get the singleton instance of the JulianChronology.
        JulianChronology chronology = JulianChronology.INSTANCE;
        TemporalAccessor nullTemporal = null;

        // Act & Assert: Verify that a NullPointerException is thrown when the method is called with null.
        // Using assertThrows is a modern and clear way to test for expected exceptions,
        // ensuring that the exception is thrown by the specific line of code under test.
        assertThrows(NullPointerException.class, () -> {
            chronology.zonedDateTime(nullTemporal);
        });
    }
}