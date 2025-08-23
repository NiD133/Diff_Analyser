package org.joda.time.convert;

import org.junit.Test;

/**
 * Unit tests for {@link ConverterSet}.
 */
public class ConverterSetTest {

    /**
     * Tests that calling remove() with a null converter throws a NullPointerException,
     * as specified by the method's contract.
     */
    @Test(expected = NullPointerException.class)
    public void remove_whenConverterIsNull_throwsNullPointerException() {
        // Arrange: Create a ConverterSet instance. Its initial content is irrelevant
        // for this test, as the null check should happen before any processing.
        // An empty set is the simplest valid state.
        ConverterSet converterSet = new ConverterSet(new Converter[0]);

        // Act: Attempt to remove a null converter.
        // The second argument is for capturing the removed converter, which is not
        // relevant to this exception test, so we can pass null.
        converterSet.remove(null, null);

        // Assert: The @Test(expected) annotation asserts that a NullPointerException is thrown.
    }
}