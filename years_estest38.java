package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Years} class.
 */
public class YearsTest {

    @Test
    public void parseYears_withNullString_shouldReturnZeroYears() {
        // The Javadoc for parseYears states that a null input string 
        // should result in a zero Years object.
        
        // Act: Call the method under test with a null input.
        Years result = Years.parseYears(null);

        // Assert: Verify that the result is the constant for zero years.
        assertEquals(Years.ZERO, result);
    }
}