package com.fasterxml.jackson.core;

import com.fasterxml.jackson.core.io.ContentReference;
import com.fasterxml.jackson.core.io.ErrorReportConfiguration;
import org.junit.Test;

/**
 * Unit tests for the {@link JsonLocation} class, focusing on its {@code toString()} method.
 */
public class JsonLocationToStringTest {

    /**
     * Tests that {@link JsonLocation#toString()} throws a {@link StringIndexOutOfBoundsException}
     * when the underlying {@link ContentReference} is constructed with an
     * {@link ErrorReportConfiguration} that has a negative {@code maxErrorTokenLength}.
     * <p>
     * This scenario can lead to an invalid index calculation when {@code toString()} tries to generate a
     * source snippet for the location's string representation.
     */
    @Test(expected = StringIndexOutOfBoundsException.class)
    public void toStringShouldThrowExceptionWhenConfiguredWithNegativeMaxErrorTokenLength() {
        // Arrange: Set up an ErrorReportConfiguration with an invalid negative value.
        final int invalidMaxErrorTokenLength = -1;
        final int maxRawContentLength = 500;
        ErrorReportConfiguration invalidConfig = new ErrorReportConfiguration(
                maxRawContentLength, invalidMaxErrorTokenLength);

        // Create a ContentReference with this invalid configuration.
        // The actual source object (an empty StringBuilder) is not the cause of the error.
        Object source = new StringBuilder();
        ContentReference contentRef = ContentReference.construct(true, source, invalidConfig);

        // Create a JsonLocation instance using the configured ContentReference.
        // The specific location offsets are not critical for triggering the exception.
        JsonLocation location = new JsonLocation(contentRef, 500L, 500L, 1571, 500);

        // Act: Call toString(), which will attempt to build a source description.
        // This is expected to fail due to the invalid configuration.
        // Assert: The @Test(expected) annotation asserts that a StringIndexOutOfBoundsException is thrown.
        location.toString();
    }
}