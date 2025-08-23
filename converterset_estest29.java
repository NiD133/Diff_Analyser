package org.joda.time.convert;

import org.junit.Test;

/**
 * Test suite for the {@link ConverterSet#select(Class)} method.
 */
public class ConverterSetTest {

    /**
     * Tests that calling select() with a null type argument throws a NullPointerException.
     * The implementation is expected to perform a non-null check on its input.
     */
    @Test(expected = NullPointerException.class)
    public void select_whenTypeIsNull_throwsNullPointerException() {
        // Arrange: Create a ConverterSet. Its internal state is not relevant for this test.
        // An empty set is sufficient to test the null-handling of the select method.
        ConverterSet converterSet = new ConverterSet(new Converter[0]);

        // Act: Call the select method with a null argument.
        // The @Test(expected) annotation will assert that a NullPointerException is thrown.
        converterSet.select(null);
    }
}