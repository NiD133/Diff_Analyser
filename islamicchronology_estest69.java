package org.joda.time.chrono;

import org.joda.time.DateTimeZone;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * A more understandable test for the {@link IslamicChronology} class,
 * focusing on the contract between equals() and hashCode().
 */
public class IslamicChronologyTest {

    /**
     * Tests that the hashCode() method is consistent with the equals() method,
     * as required by the Object.hashCode() contract.
     * <p>
     * The contract states:
     * 1. If two objects are equal according to the equals(Object) method,
     *    then calling the hashCode method on each of the two objects must produce
     *    the same integer result.
     * 2. The hashCode must be consistent, returning the same value for an object
     *    across multiple invocations.
     */
    @Test
    public void hashCode_fulfillsEqualsContract() {
        // Arrange: Create two identical instances of IslamicChronology.
        // Equality for IslamicChronology is determined by the time zone and the leap year pattern.
        // We use the default leap year pattern by calling getInstance(DateTimeZone).
        IslamicChronology chronology1 = IslamicChronology.getInstance(DateTimeZone.UTC);
        IslamicChronology chronology2 = IslamicChronology.getInstance(DateTimeZone.UTC);

        // Assert: Verify the equals and hashCode contract

        // 1. Check for consistency: hashCode() should return the same value on multiple calls.
        assertEquals("hashCode() must be consistent.",
                chronology1.hashCode(), chronology1.hashCode());

        // 2. Check for equality: The two separately created instances should be equal.
        assertTrue("Instances with the same zone and leap year pattern should be equal.",
                chronology1.equals(chronology2));

        // 3. Check the core contract: Equal objects must have equal hash codes.
        assertEquals("Equal objects must have the same hash code.",
                chronology1.hashCode(), chronology2.hashCode());
    }
}