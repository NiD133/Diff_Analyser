package org.example;

import org.junit.jupiter.api.Test; // Use JUnit 5 for clarity and features
import static org.junit.jupiter.api.Assertions.*; // Improved assertion style

import java.util.Date;

// No longer need EvoSuite annotations unless you are actively using EvoSuite features
// @RunWith(EvoRunner.class)
// @EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useJEE = true)
public class GeneratedTestCase {

    @Test
    void testGetUpperMillisReturnsZeroForZeroRange() {
        // Arrange: Create a DateRange with the same start and end values (0.0).
        DateRange dateRange = new DateRange(0.0, 0.0);

        // Act: Call the getUpperMillis() method to get the upper bound in milliseconds.
        long upperMillis = dateRange.getUpperMillis();

        // Assert: Verify that the upper bound in milliseconds is indeed 0.
        assertEquals(0L, upperMillis, "The upper millis should be 0 for a range starting and ending at 0.0");
    }
}