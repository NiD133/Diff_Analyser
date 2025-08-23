package org.joda.time.convert;

import org.joda.time.Chronology;
import org.junit.Test;

/**
 * Unit tests for the {@link StringConverter} class.
 */
public class StringConverterTest {

    private final StringConverter converter = new StringConverter();

    /**
     * Tests that getInstantMillis() throws a NullPointerException when the input object is null.
     * The public contract of the method implies that the input object must not be null,
     * and a NullPointerException is the expected behavior for such an invalid argument.
     */
    @Test(expected = NullPointerException.class)
    public void getInstantMillis_whenInputIsNull_throwsNullPointerException() {
        // Act: Call the method with a null object.
        // The Chronology argument can also be null for this test case.
        converter.getInstantMillis(null, (Chronology) null);

        // Assert: The @Test(expected) annotation handles the exception verification.
    }
}