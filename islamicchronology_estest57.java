package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test class contains tests for the equals() method of IslamicChronology.
 */
public class IslamicChronology_ESTestTest57 extends IslamicChronology_ESTest_scaffolding {

    /**
     * Tests that two IslamicChronology instances are not considered equal if they
     * are constructed with different internal parameters, even if other properties
     * like the leap year pattern and time zone are the same.
     */
    @Test
    public void equals_returnsFalse_whenInternalParametersDiffer() {
        // Arrange
        // 1. Get a standard instance of IslamicChronology. The factory method `getInstance()`
        //    creates an instance with a null internal 'param' field.
        IslamicChronology standardInstance = IslamicChronology.getInstance();

        // 2. Create a second instance using the package-private constructor.
        //    We explicitly provide the standard instance as the 'param', making it non-null.
        IslamicChronology customInstance = new IslamicChronology(
                standardInstance, standardInstance, standardInstance.getLeapYearPatternType());

        // Act
        // The equals() method in IslamicChronology delegates to BasicChronology, which
        // compares the time zone and the internal 'param' object.
        boolean areEqual = standardInstance.equals(customInstance);

        // Assert
        // The instances should not be equal because their internal 'param' fields differ.
        // The standardInstance has a null param, while the customInstance does not.
        assertFalse("Chronologies with different internal params should not be equal", areEqual);
    }
}