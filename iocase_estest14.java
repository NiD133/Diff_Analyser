package org.apache.commons.io;

import org.junit.Test;

/**
 * Tests for {@link IOCase}.
 * This test focuses on the handling of null inputs for the checkCompareTo method.
 */
public class IOCase_ESTestTest14 extends IOCase_ESTest_scaffolding {

    /**
     * Verifies that checkCompareTo throws a NullPointerException when given a null argument,
     * as per its contract.
     */
    @Test(expected = NullPointerException.class)
    public void checkCompareTo_withNullArgument_shouldThrowNullPointerException() {
        // Arrange: Get any IOCase instance. The behavior with nulls is not case-dependent.
        final IOCase ioCase = IOCase.SENSITIVE;

        // Act: Call the method with null arguments.
        // The method contract states it throws NullPointerException if either string is null.
        ioCase.checkCompareTo(null, null);

        // Assert: The test passes if a NullPointerException is thrown,
        // as specified by the 'expected' attribute of the @Test annotation.
    }
}