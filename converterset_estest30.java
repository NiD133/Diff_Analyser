package org.joda.time.convert;

import org.junit.Test;

/**
 * This test suite contains tests for the {@link ConverterSet} class.
 * The original test was auto-generated and has been improved for clarity and maintainability.
 */
public class ConverterSetTest {

    /**
     * Verifies that calling size() on a ConverterSet that was initialized with a
     * null array of converters throws a NullPointerException.
     * <p>
     * This test ensures that the class correctly handles this edge case, where
     * a subsequent operation fails due to invalid initial state.
     */
    @Test(expected = NullPointerException.class)
    public void size_shouldThrowNullPointerException_whenConstructedWithNullArray() {
        // Arrange: Instantiate ConverterSet with a null array. The constructor is
        // package-private, so this test must reside in the same package.
        ConverterSet converterSet = new ConverterSet(null);

        // Act: Call the size() method. This is expected to throw a NullPointerException
        // because the internal array `iConverters` is null and size() likely tries
        // to access its length.
        converterSet.size();

        // Assert: The test passes if the expected NullPointerException is thrown.
        // This is handled declaratively by the @Test annotation's 'expected' parameter.
    }
}