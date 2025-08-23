package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test class contains an improved version of a test for the Seconds class.
 */
public class Seconds_ESTestTest29 extends Seconds_ESTest_scaffolding {

    /**
     * Tests that Seconds.secondsBetween() throws a NullPointerException when given a
     * Partial object that has been constructed in an invalid state.
     *
     * <p>The test intentionally creates a malformed {@link Partial} object by using its
     * package-private constructor. This bypasses the validation in the public API,
     * resulting in a Partial where the internal 'types' and 'values' arrays have
     * mismatched lengths. The {@code secondsBetween} method is not designed to handle
     * such an inconsistent object and is expected to fail with an NPE when it tries
     * to process it.
     */
    @Test(expected = NullPointerException.class)
    public void secondsBetween_whenCalledWithMalformedPartial_throwsNullPointerException() {
        // Arrange: Create a malformed Partial object with mismatched internal array lengths.
        // This invalid state is only possible by using the package-private constructor,
        // as the public API would prevent it.
        DateTimeFieldType[] fieldTypesWithMismatchedLength = new DateTimeFieldType[7];
        fieldTypesWithMismatchedLength[0] = DateTimeFieldType.yearOfEra();
        int[] emptyValues = new int[0];

        // This constructor creates an invalid object where the types array (length 7)
        // and values array (length 0) do not match.
        Partial malformedPartial = new Partial((Chronology) null, fieldTypesWithMismatchedLength, emptyValues);

        // Act: Attempt to calculate the seconds between two instances of the malformed Partial.
        // This is expected to trigger the exception.
        Seconds.secondsBetween(malformedPartial, malformedPartial);

        // Assert: The @Test(expected = NullPointerException.class) annotation handles the assertion.
    }
}