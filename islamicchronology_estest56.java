package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link IslamicChronology} class.
 */
public class IslamicChronologyTest {

    /**
     * Tests that an instance of IslamicChronology is equal to itself,
     * verifying the reflexive property of the equals() method.
     */
    @Test
    public void testEqualsIsReflexive() {
        // Arrange
        IslamicChronology chronology = IslamicChronology.getInstanceUTC();

        // Assert: An object must be equal to itself.
        assertTrue("A chronology instance must be equal to itself.", chronology.equals(chronology));
        
        // A reflexive object's hash code must be consistent with equals.
        assertEquals(chronology.hashCode(), chronology.hashCode());
    }
}