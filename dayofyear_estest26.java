package org.threeten.extra;

import org.junit.Test;
import java.time.ZoneOffset;
import static org.junit.Assert.assertFalse;

/**
 * This test class contains an improved version of a generated test case
 * for the {@link DayOfYear} class.
 */
// The original class name and inheritance are kept to match the user's request context.
public class DayOfYear_ESTestTest26 extends DayOfYear_ESTest_scaffolding {

    /**
     * Tests that the equals() method correctly returns false when comparing
     * a DayOfYear instance with an object of a different type.
     * <p>
     * A robust equals() implementation must handle comparison with any object
     * type without throwing an exception and return false if the types do not match.
     */
    @Test
    public void equals_shouldReturnFalse_whenComparedWithObjectOfDifferentType() {
        // Arrange: Create a DayOfYear instance and an object of an unrelated type.
        // Using DayOfYear.of() makes the test deterministic and independent of the system clock.
        DayOfYear dayOfYear = DayOfYear.of(123);
        Object nonDayOfYearObject = ZoneOffset.MAX;

        // Act: Compare the DayOfYear instance with the other object.
        boolean isEqual = dayOfYear.equals(nonDayOfYearObject);

        // Assert: The result should be false.
        assertFalse("DayOfYear.equals() must return false for a non-DayOfYear object.", isEqual);
    }
}