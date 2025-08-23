package org.apache.commons.lang3.text.translate;

import org.junit.Test;

/**
 * Tests for {@link NumericEntityUnescaper}.
 */
public class NumericEntityUnescaperTest {

    /**
     * Tests that the constructor throws a NullPointerException when the options array contains a null element.
     * This is because the constructor internally uses EnumSet.copyOf, which does not permit null elements.
     */
    @Test(expected = NullPointerException.class)
    public void constructorShouldThrowNPEWhenOptionsArrayContainsNull() {
        // Arrange: Create an array of options that includes a null value.
        final NumericEntityUnescaper.OPTION[] optionsWithNull = { null };

        // Act: Attempt to create an instance with the invalid options.
        // An exception is expected.
        new NumericEntityUnescaper(optionsWithNull);
    }
}