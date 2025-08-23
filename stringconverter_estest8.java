package org.joda.time.convert;

import org.joda.time.Chronology;
import org.joda.time.MutableInterval;
import org.joda.time.ReadWritableInterval;
import org.joda.time.chrono.ISOChronology;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

// The test class name is inherited from the original. 
// A more descriptive name like `StringConverterTest` would be preferable in a real project.
public class StringConverter_ESTestTest8 extends StringConverter_ESTest_scaffolding {

    /**
     * Verifies that setInto() throws an IllegalArgumentException when attempting to parse
     * an interval from a string that is missing the required '/' separator.
     */
    @Test
    public void setIntoInterval_shouldThrowException_forMalformedStringMissingSeparator() {
        // Arrange: Set up the converter, a target interval, and a malformed input string.
        StringConverter converter = StringConverter.INSTANCE;
        ReadWritableInterval interval = new MutableInterval();
        Chronology chronology = ISOChronology.getInstanceUTC(); // A standard, neutral chronology.
        String malformedIntervalString = "2023-01-01T10:00:00Z_to_2023-01-01T12:00:00Z";

        // Act & Assert: Attempt the conversion and verify the expected exception.
        try {
            converter.setInto(interval, malformedIntervalString, chronology);
            fail("Expected an IllegalArgumentException to be thrown due to the malformed string.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message clearly states the parsing problem.
            String expectedMessage = "Format requires a '/' separator: " + malformedIntervalString;
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}