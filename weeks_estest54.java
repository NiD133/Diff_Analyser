package org.joda.time;

import org.junit.Test;

/**
 * This test class focuses on the behavior of the Weeks.weeksBetween method
 * when provided with invalid ReadablePartial arguments.
 */
public class WeeksTest {

    /**
     * Tests that weeksBetween throws a NullPointerException when given a Partial
     * that was constructed with an array of null DateTimeFieldTypes.
     *
     * <p><b>Note:</b> This specific Partial state (containing null field types and
     * mismatched internal arrays) cannot be created using public constructors, which
     * perform validation. This test covers the defensive behavior of weeksBetween
     * when encountering such a malformed object, ensuring it fails fast.
     */
    @Test(expected = NullPointerException.class)
    public void weeksBetween_givenPartialWithNullFieldTypes_throwsNullPointerException() {
        // Arrange: Create a malformed Partial object.
        // This is done by invoking a package-private constructor that bypasses the
        // validation present in the public constructors. The public API would normally
        // prevent creating a Partial with nulls in its type array.
        DateTimeFieldType[] fieldTypesWithNulls = new DateTimeFieldType[1]; // An array containing a null
        int[] emptyValues = new int[0]; // Mismatched length to create an invalid state
        Partial malformedPartial = new Partial(null, fieldTypesWithNulls, emptyValues);

        // Act & Assert: Call weeksBetween with the malformed partial.
        // The method is expected to throw a NullPointerException when it tries to
        // process the invalid (null) field type within the Partial.
        Weeks.weeksBetween(malformedPartial, malformedPartial);
    }
}