package com.fasterxml.jackson.core;

import com.fasterxml.jackson.core.io.ContentReference;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

/**
 * Unit tests for the source description functionality of {@link JsonLocation},
 * focusing on how it handles and displays truncated content.
 */
public class JsonLocationSourceDescriptionTest {

    /**
     * Provides test cases for content that exceeds the maximum displayable length,
     * covering both String and byte array sources.
     *
     * @return A stream of arguments for the parameterized test.
     */
    private static Stream<Arguments> longContentSourceProvider() {
        // Arrange
        final int maxLength = ErrorReportConfiguration.DEFAULT_MAX_RAW_CONTENT_LENGTH;
        final String contentUpToLimit = "x".repeat(maxLength);
        final String extraContent = "yyy";
        final String fullContentString = contentUpToLimit + extraContent;
        final byte[] fullContentBytes = fullContentString.getBytes(StandardCharsets.UTF_8);

        // Create a ContentReference for a String source
        ContentReference stringContentRef = ContentReference.construct(
                true, fullContentString, 0, fullContentString.length(), ErrorReportConfiguration.defaults());

        // Create a ContentReference for a byte[] source
        ContentReference byteContentRef = ContentReference.construct(
                true, fullContentBytes, 0, fullContentBytes.length, ErrorReportConfiguration.defaults());

        return Stream.of(
                arguments(stringContentRef, "String", "chars", extraContent.length()),
                arguments(byteContentRef, "byte[]", "bytes", extraContent.length())
        );
    }

    /**
     * Verifies that {@link JsonLocation#sourceDescription()} correctly truncates the description
     * when the source content is longer than the configured maximum length. The test is
     * parameterized to run for both String and byte[] sources.
     */
    @ParameterizedTest(name = "for source type {1}")
    @MethodSource("longContentSourceProvider")
    void shouldTruncateSourceDescriptionWhenContentExceedsMaxLength(
            ContentReference contentRef, String expectedTypeName, String expectedUnit, int expectedTruncatedCount) {
        // Arrange
        final int maxLength = ErrorReportConfiguration.DEFAULT_MAX_RAW_CONTENT_LENGTH;
        final String contentUpToLimit = "x".repeat(maxLength);

        // A dummy location, as its specific coordinates are not relevant for this test.
        JsonLocation location = new JsonLocation(contentRef, 0L, 0L, 1, 1);

        String expectedDescription = String.format("(%s)\"%s\"[truncated %d %s]",
                expectedTypeName, contentUpToLimit, expectedTruncatedCount, expectedUnit);

        // Act
        String actualDescription = location.sourceDescription();

        // Assert
        assertThat(actualDescription).isEqualTo(expectedDescription);
    }
}