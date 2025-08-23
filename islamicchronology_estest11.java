package org.joda.time.chrono;

import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertFalse;

/**
 * Test suite for the equals() and hashCode() methods of IslamicChronology.
 */
public class IslamicChronologyEqualsTest {

    @Test
    public void chronologiesWithDifferentLeapYearPatternsShouldNotBeEqual() {
        // Arrange: Create two IslamicChronology instances for the same time zone
        // but with different leap year calculation patterns.
        DateTimeZone defaultZone = DateTimeZone.getDefault();
        
        IslamicChronology chrono16Based = IslamicChronology.getInstance(
            defaultZone, IslamicChronology.LEAP_YEAR_16_BASED);
            
        IslamicChronology chrono15Based = IslamicChronology.getInstance(
            defaultZone, IslamicChronology.LEAP_YEAR_15_BASED);

        // Assert: The two chronologies should not be considered equal because their
        // underlying leap year patterns are different.
        // The equals() method should be symmetric.
        assertFalse("Chronologies with different leap year patterns should not be equal", 
                    chrono16Based.equals(chrono15Based));
        assertFalse("The equals() method should be symmetric", 
                    chrono15Based.equals(chrono16Based));

        // As per the equals/hashCode contract, their hash codes should also be different.
        assertNotEquals("Hash codes should differ for unequal objects", 
                        chrono16Based.hashCode(), chrono15Based.hashCode());
    }
}