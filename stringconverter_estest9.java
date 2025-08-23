package org.joda.time.convert;

import org.joda.time.MutableInterval;
import org.junit.Test;

/**
 * Unit tests for {@link StringConverter}.
 */
public class StringConverterTest {

    /**
     * Tests that setInto() for an interval throws a ClassCastException when the
     * object to be converted is not a String. The converter is specifically designed
     * to handle Strings, so any other type should result in an error.
     */
    @Test(expected = ClassCastException.class)
    public void setIntoInterval_withNonStringObject_throwsClassCastException() {
        // Arrange
        StringConverter converter = new StringConverter();
        MutableInterval intervalToUpdate = new MutableInterval();
        
        // This is the invalid input object. The converter expects a String.
        Object invalidInput = new MutableInterval();

        // Act
        converter.setInto(intervalToUpdate, invalidInput, null);

        // Assert: The test expects a ClassCastException, as declared in the @Test annotation.
    }
}