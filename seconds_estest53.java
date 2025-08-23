package org.joda.time;

import org.junit.Test;

// Note: The original test class name "Seconds_ESTestTest53" and its scaffolding superclass
// were preserved. In a real-world scenario, these would be renamed to something more
// conventional, like "SecondsTest".
public class Seconds_ESTestTest53 extends Seconds_ESTest_scaffolding {

    /**
     * Verifies that {@code Seconds.secondsBetween(ReadablePartial, ReadablePartial)} throws a
     * {@code NullPointerException} when provided with a {@code Partial} instance that was
     * improperly constructed with a null array for its field types.
     * <p>
     * The {@code secondsBetween} method must inspect the fields of the partials to perform its
     * calculation. This test ensures that it fails as expected when it encounters such a
     * malformed {@code Partial}, which would cause an NPE upon trying to access its fields.
     */
    @Test(expected = NullPointerException.class)
    public void secondsBetween_whenGivenPartialWithNullFieldTypes_throwsNullPointerException() {
        // Arrange: Create an invalid Partial instance. A Partial is considered invalid for
        // this operation if its internal array of DateTimeFieldType is null.
        DateTimeFieldType[] nullFieldTypes = null;
        int[] dummyValues = new int[4];
        Partial invalidPartial = new Partial(nullFieldTypes, dummyValues, null);

        // Act: Call the method under test with the invalid partial. This is expected to throw.
        Seconds.secondsBetween(invalidPartial, invalidPartial);

        // Assert: The test succeeds if a NullPointerException is thrown, as declared
        // in the @Test annotation.
    }
}