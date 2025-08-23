package org.apache.commons.compress.archivers.zip;

import org.junit.Test;

import java.util.zip.ZipException;

/**
 * Unit tests for the {@link ExtraFieldUtils} class.
 *
 * This class replaces an auto-generated test to improve readability and maintainability.
 */
public class ExtraFieldUtilsTest {

    /**
     * Verifies that {@code ExtraFieldUtils.parse()} throws a NullPointerException
     * when a null {@code ExtraFieldParsingBehavior} is provided.
     */
    @Test(expected = NullPointerException.class)
    public void parseWithNullParsingBehaviorShouldThrowNullPointerException() throws ZipException {
        // Arrange: Create dummy data for the parse method. The content is irrelevant
        // for this test, as the exception is triggered by the null argument.
        final byte[] dummyData = new byte[9];
        final boolean isLocalHeader = false;

        // Act: Call the parse method with a null parsing behavior.
        // Assert: The @Test(expected) annotation handles the exception verification.
        ExtraFieldUtils.parse(dummyData, isLocalHeader, (ExtraFieldParsingBehavior) null);
    }
}