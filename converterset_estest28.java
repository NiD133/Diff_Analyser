package org.joda.time.convert;

import org.joda.time.Interval;
import org.joda.time.chrono.CopticChronology;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This test class focuses on the interaction between Joda-Time's conversion
 * framework and various object types.
 */
public class JodaTimeConverterTest {

    /**
     * Tests that the Interval constructor throws an IllegalArgumentException when
     * provided with an object of a type that has no corresponding registered converter.
     * It uses ConverterSet.Entry as an example of such an unsupported type.
     */
    @Test
    public void constructingIntervalFromUnsupportedTypeShouldThrowException() {
        // Arrange: Create an object of a type that is not expected to be convertible to an Interval.
        // ConverterSet.Entry is an internal implementation class and serves as a good example.
        Object unsupportedObject = new ConverterSet.Entry(String.class, null);
        CopticChronology copticChronology = CopticChronology.getInstance();

        // Act & Assert
        try {
            new Interval(unsupportedObject, copticChronology);
            fail("Expected an IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message clearly indicates the problem.
            String expectedMessage = "No interval converter found for type: org.joda.time.convert.ConverterSet$Entry";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}