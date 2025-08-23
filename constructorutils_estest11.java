package org.apache.commons.lang3.reflect;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Unit tests for {@link ConstructorUtils}.
 */
public class ConstructorUtilsTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Tests that getMatchingAccessibleConstructor throws a NullPointerException
     * when the class parameter is null.
     */
    @Test
    public void getMatchingAccessibleConstructorWithNullClassThrowsNullPointerException() {
        // Arrange: Define expectations for the exception
        thrown.expect(NullPointerException.class);
        // The method under test uses Objects.requireNonNull(cls, "cls"), which results in this specific message.
        thrown.expectMessage("cls");

        // Act: Call the method with a null class, which should trigger the exception
        ConstructorUtils.getMatchingAccessibleConstructor(null, String.class);
    }
}