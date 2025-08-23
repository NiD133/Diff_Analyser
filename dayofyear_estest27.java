package org.threeten.extra;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * This test class contains tests for the {@link DayOfYear} class.
 * Note: The original test class name and inheritance are preserved as requested.
 * In a typical project, this class would be named DayOfYearTest.
 */
public class DayOfYear_ESTestTest27 extends DayOfYear_ESTest_scaffolding {

    /**
     * Tests the reflexivity property of the equals() method.
     * This ensures that an object is always considered equal to itself.
     */
    @Test
    public void equals_returnsTrue_whenComparingInstanceToItself() {
        // Arrange
        // Create a DayOfYear instance with a fixed value to ensure the test is deterministic.
        // The original test used DayOfYear.now(), which is non-deterministic and can cause
        // tests to fail depending on the date they are run.
        DayOfYear dayOfYear = DayOfYear.of(150);

        // Act
        // The original test checked if an object was equal to itself. We replicate that
        // fundamental check here in a reliable and clear way.
        boolean isEqualToItself = dayOfYear.equals(dayOfYear);

        // Assert
        // Verify that the equals() method returns true, satisfying the reflexivity contract.
        assertTrue("An instance of DayOfYear should always be equal to itself.", isEqualToItself);
    }
}