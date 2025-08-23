package org.joda.time.convert;

import org.joda.time.Chronology;
import org.joda.time.chrono.ISOChronology;
import org.junit.Test;

/**
 * Unit tests for {@link StringConverter}.
 */
public class StringConverterTest {

    /**
     * Tests that getInstantMillis throws a ClassCastException if the input object
     * is not a String. The converter is designed to only handle String objects.
     */
    @Test(expected = ClassCastException.class)
    public void getInstantMillis_shouldThrowClassCastException_forNonStringInput() {
        // Arrange
        StringConverter converter = new StringConverter();
        
        // Use an arbitrary non-String object as the input to be converted.
        // The StringConverter expects a String, so this should fail.
        Object nonStringInput = new Object(); 
        
        // A dummy chronology is needed for the method signature, but its value is not relevant here.
        Chronology chronology = ISOChronology.getInstanceUTC();

        // Act & Assert
        // This call should throw a ClassCastException because the first argument is not a String.
        // The @Test(expected) annotation handles the assertion.
        converter.getInstantMillis(nonStringInput, chronology);
    }
}