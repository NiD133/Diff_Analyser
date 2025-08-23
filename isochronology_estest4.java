package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link ISOChronology} class.
 */
public class ISOChronologyTest {

    /**
     * Tests that the equals() method returns false when comparing an ISOChronology
     * instance with an object of a completely different type.
     */
    @Test
    public void equals_shouldReturnFalse_whenComparedWithDifferentTypeObject() {
        // Arrange: Create an instance of the class under test and an object of a different type.
        ISOChronology chronology = ISOChronology.getInstanceUTC();
        Object nonChronologyObject = new Object();

        // Act: Call the equals() method.
        boolean isEqual = chronology.equals(nonChronologyObject);

        // Assert: Verify that the result is false, as the types are incompatible.
        assertFalse(isEqual);
    }
}