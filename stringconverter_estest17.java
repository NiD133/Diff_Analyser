package org.joda.time.convert;

import org.junit.Test;

/**
 * Unit tests for {@link StringConverter}.
 */
public class StringConverterTest {

    private final StringConverter converter = StringConverter.INSTANCE;

    /**
     * Tests that getDurationMillis() throws a NullPointerException when the input object is null,
     * as the method's contract requires a non-null input.
     */
    @Test(expected = NullPointerException.class)
    public void getDurationMillis_shouldThrowNullPointerException_whenInputIsNull() {
        converter.getDurationMillis(null);
    }
}