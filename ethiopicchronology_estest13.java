package org.joda.time.chrono;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * Contains tests for the EthiopicChronology class, focusing on its object equality.
 */
public class EthiopicChronologyTest {

    @Test
    public void equals_returnsFalse_forInstancesWithDifferentInternalConfigurations() {
        // This test verifies that two EthiopicChronology instances are not considered equal
        // if they have different internal configurations, such as a different base
        // chronology or minimum days in the first week.

        // Arrange: Create a standard EthiopicChronology instance using the factory method.
        // This instance will have default internal parameters.
        DateTimeZone zone = DateTimeZone.forOffsetMillis(-1962);
        EthiopicChronology standardInstance = EthiopicChronology.getInstance(zone);

        // Arrange: Create a second, custom EthiopicChronology instance using its
        // package-private constructor. This allows us to provide a different internal
        // configuration for the purpose of this test.
        Chronology baseForCustomInstance = LenientChronology.getInstance(standardInstance);
        Object dummyParam = new Object(); // A non-null parameter for the constructor.
        int minDaysInFirstWeek = 1;       // Different from the default of 4.

        EthiopicChronology customInstance = new EthiopicChronology(
                baseForCustomInstance,
                dummyParam,
                minDaysInFirstWeek
        );

        // Act & Assert: The two instances should not be equal because their underlying
        // configurations (base chronology, params, minDaysInFirstWeek) are different.
        assertFalse(
            "Instances with different configurations should not be equal",
            customInstance.equals(standardInstance)
        );
    }
}