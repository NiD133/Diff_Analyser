package org.threeten.extra;

import org.junit.Test;

/**
 * This class contains an improved version of a test case for the {@link DayOfYear} class.
 * The original test was auto-generated and has been refactored for better understandability.
 */
public class DayOfYear_ESTestTest16 extends DayOfYear_ESTest_scaffolding {

    /**
     * Tests that calling compareTo() with a null argument throws a NullPointerException.
     * This behavior is mandated by the java.lang.Comparable interface contract.
     */
    @Test(expected = NullPointerException.class)
    public void compareTo_withNullArgument_throwsNullPointerException() {
        // Arrange: Create an arbitrary DayOfYear instance. The specific value is not important.
        DayOfYear dayOfYear = DayOfYear.of(150);

        // Act & Assert: The @Test(expected) annotation asserts that a
        // NullPointerException is thrown when compareTo(null) is called.
        dayOfYear.compareTo(null);
    }
}