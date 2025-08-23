package org.joda.time.chrono;

import org.joda.time.Chronology;
import org.junit.Test;
import static org.junit.Assert.assertNotEquals;

/**
 * Tests for the {@link CopticChronology} class, focusing on object equality.
 */
public class CopticChronologyTest {

    @Test
    public void equals_returnsFalse_forInstancesWithDifferentInternalParameters() {
        // ARRANGE
        // Get a standard, cached instance of CopticChronology.
        // Its internal 'param' object, used for equality checks, is null.
        CopticChronology standardInstance = CopticChronology.getInstance();

        // Create a second instance using a package-private constructor.
        // We deliberately pass the first instance as the 'param' object, making its
        // internal state different from the standard instance.
        CopticChronology instanceWithCustomParam = new CopticChronology(
                null,               // Base chronology
                standardInstance,   // 'param' object for equality check
                1                   // minDaysInFirstWeek
        );

        // ACT & ASSERT
        // The two instances should not be equal because the equals() method in the
        // base class compares the internal 'param' object.
        assertNotEquals(
                "Chronology instances with different internal parameters should not be equal.",
                standardInstance,
                instanceWithCustomParam
        );
    }
}