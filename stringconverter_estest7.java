package org.joda.time.convert;

import org.joda.time.Chronology;
import org.joda.time.ReadWritableInterval;
import org.joda.time.chrono.JulianChronology;
import org.junit.Test;

/**
 * This class contains an improved version of a test case for the StringConverter.
 * The original test was auto-generated and lacked clarity.
 *
 * Note: The original class name "StringConverter_ESTestTest7" and the scaffolding
 * are preserved to maintain context. A more conventional name would be "StringConverterTest".
 */
public class StringConverter_ESTestTest7 extends StringConverter_ESTest_scaffolding {

    /**
     * Verifies that calling setInto() for an interval with a null object for conversion
     * throws a NullPointerException.
     *
     * The method under test is `setInto(ReadWritableInterval, Object, Chronology)`.
     * The exception is expected because the `Object` to be converted (the string) is null.
     */
    @Test(expected = NullPointerException.class)
    public void setIntoInterval_withNullObject_shouldThrowNullPointerException() {
        // Arrange: Create a converter instance and necessary arguments.
        StringConverter converter = new StringConverter();
        Chronology chronology = JulianChronology.getInstanceUTC();
        
        // The object to be converted is explicitly null to trigger the exception.
        Object objectToConvert = null;
        
        // The interval can also be null for this test, as it's not the cause of the NPE.
        ReadWritableInterval interval = null;

        // Act & Assert: Call the method and expect a NullPointerException.
        // The @Test(expected) annotation handles the assertion, making the test fail
        // if a NullPointerException is not thrown.
        converter.setInto(interval, objectToConvert, chronology);
    }
}