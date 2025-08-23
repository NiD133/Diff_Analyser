package org.threeten.extra.chrono;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import org.junit.Test;

/**
 * Tests for {@link BritishCutoverChronology}.
 */
public class BritishCutoverChronologyTest {

    /**
     * Tests that getCutover() returns the correct, documented cutover date.
     * <p>
     * The British calendar cutover occurred on September 14, 1752. This test
     * verifies that the method returns this specific date, which is also
     * exposed as a public constant.
     */
    @Test
    public void getCutover_shouldReturnTheDefinedCutoverDate() {
        // Arrange
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;
        LocalDate expectedCutoverDate = BritishCutoverChronology.CUTOVER;

        // Act
        LocalDate actualCutoverDate = chronology.getCutover();

        // Assert
        assertEquals("The getCutover() method should return the value of the public CUTOVER constant.",
                     expectedCutoverDate, actualCutoverDate);
    }
}