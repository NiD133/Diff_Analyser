package org.joda.time.base;

import org.joda.time.Chronology;
import org.joda.time.LocalDate;
import org.joda.time.chrono.GregorianChronology;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * This is a refactored version of an auto-generated test for the AbstractPartial class.
 * The original test class name "AbstractPartial_ESTestTest44" is kept for context,
 * but a more descriptive name like "AbstractPartialHashCodeTest" would be preferable.
 */
public class AbstractPartial_ESTestTest44 { // Note: Original class name is preserved.

    /**
     * Tests that the hashCode() implementation in AbstractPartial (via its subclass LocalDate)
     * adheres to the general contract for Object.hashCode().
     * <p>
     * The contract states:
     * 1. If two objects are equal according to the equals() method, then calling hashCode()
     *    on each of the two objects must produce the same integer result.
     * 2. The hashCode() must be consistent, returning the same value for an object
     *    across multiple invocations.
     */
    @Test
    public void hashCodeShouldAdhereToEqualsContract() {
        // Arrange: Create two equal LocalDate instances and one that is different.
        // LocalDate is a concrete subclass of AbstractPartial, so this tests the
        // inherited hashCode() implementation.
        Chronology chronology = GregorianChronology.getInstanceUTC();
        
        // Two instances representing the same partial date: January 1, 1970.
        // These two objects should be equal.
        LocalDate date1 = new LocalDate(1L, chronology);
        LocalDate date2 = new LocalDate(1L, chronology);

        // A third instance representing a different date: January 2, 1970.
        long oneDayInMillis = 24 * 60 * 60 * 1000;
        LocalDate differentDate = new LocalDate(1L + oneDayInMillis, chronology);

        // Act & Assert

        // Pre-condition check: ensure the objects have the expected equality.
        assertEquals("Instances created with the same parameters should be equal", date1, date2);
        assertNotEquals("Instances created with different parameters should not be equal", date1, differentDate);

        // 1. Test the core hashCode contract: equal objects must have equal hash codes.
        assertEquals("Equal objects must have the same hash code", date1.hashCode(), date2.hashCode());

        // 2. Test for consistency: hashCode must return the same value on multiple invocations.
        assertEquals("hashCode must be consistent across multiple calls", date1.hashCode(), date1.hashCode());

        // 3. (Optional but good practice) Test that unequal objects have different hash codes.
        // While not a strict requirement of the contract, it's a key property of a good hash function.
        assertNotEquals("Unequal objects should ideally have different hash codes", date1.hashCode(), differentDate.hashCode());
    }
}