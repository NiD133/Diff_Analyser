package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Minutes} class.
 */
public class MinutesTest {

    /**
     * Tests that parsing a null string returns a Minutes object representing zero.
     * The Javadoc for parseMinutes specifies that a null input should result in Minutes.ZERO.
     */
    @Test
    public void parseMinutes_shouldReturnZero_whenInputIsNull() {
        // Act
        Minutes parsedMinutes = Minutes.parseMinutes(null);

        // Assert
        assertEquals(Minutes.ZERO, parsedMinutes);
    }
}