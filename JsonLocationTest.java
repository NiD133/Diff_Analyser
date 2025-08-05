package com.fasterxml.jackson.core;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.io.ContentReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for verifying internal working of {@link JsonLocation} class itself,
 * as opposed to accuracy of reported location information by parsers.
 */
class JsonLocationTest extends JUnit5TestBase {
    
    // Test helper class for testing custom object source references
    static class CustomSourceType { }

    @Test
    void testBasicEqualityAndHashCode() {
        // Given: Two JsonLocation instances with different parameters
        JsonLocation locationWithSource = new JsonLocation(
            createStringSourceRef("src"), 
            10L, 10L, 1, 2  // totalBytes, totalChars, lineNr, columnNr
        );
        JsonLocation locationWithoutSource = new JsonLocation(
            null, 
            10L, 10L, 3, 2
        );

        // Then: Verify equality behavior
        assertEquals(locationWithSource, locationWithSource, "Location should equal itself");
        assertNotEquals(null, locationWithSource, "Location should not equal null");
        assertNotEquals(locationWithSource, locationWithoutSource, "Locations with different sources should not be equal");
        assertNotEquals(locationWithoutSource, locationWithSource, "Equality should be symmetric");

        // And: Hash codes should be non-zero for valid data
        assertTrue(locationWithSource.hashCode() != 0, "Hash code should not be zero for location with source");
        assertTrue(locationWithoutSource.hashCode() != 0, "Hash code should not be zero for location without source");
    }

    @Test
    void testToStringFormatting() throws Exception {
        // Test case 1: Location without source reference
        JsonLocation locationWithoutSource = new JsonLocation(null, 10L, 10L, 3, 2);
        assertEquals("[Source: UNKNOWN; line: 3, column: 2]", locationWithoutSource.toString(),
            "Location without source should show UNKNOWN");

        // Test case 2: String source
        JsonLocation stringSourceLocation = new JsonLocation(
            createStringSourceRef("string-source"), 10L, 10L, 1, 2);
        assertEquals("[Source: (String)\"string-source\"; line: 1, column: 2]", 
            stringSourceLocation.toString(),
            "String source should be properly formatted");

        // Test case 3: Character array source
        JsonLocation charArrayLocation = new JsonLocation(
            createCharArraySourceRef("chars-source".toCharArray()), 10L, 10L, 1, 2);
        assertEquals("[Source: (char[])\"chars-source\"; line: 1, column: 2]", 
            charArrayLocation.toString(),
            "Character array source should be properly formatted");

        // Test case 4: Byte array source
        JsonLocation byteArrayLocation = new JsonLocation(
            createByteArraySourceRef("bytes-source".getBytes("UTF-8")), 10L, 10L, 1, 2);
        assertEquals("[Source: (byte[])\"bytes-source\"; line: 1, column: 2]", 
            byteArrayLocation.toString(),
            "Byte array source should be properly formatted");

        // Test case 5: InputStream source
        JsonLocation inputStreamLocation = new JsonLocation(
            createInputStreamSourceRef(new ByteArrayInputStream(new byte[0])), 10L, 10L, 1, 2);
        assertEquals("[Source: (ByteArrayInputStream); line: 1, column: 2]", 
            inputStreamLocation.toString(),
            "InputStream source should show class name");

        // Test case 6: Class type source
        JsonLocation classTypeLocation = new JsonLocation(
            createRawSourceRef(true, InputStream.class), 10L, 10L, 1, 2);
        assertEquals("[Source: (InputStream); line: 1, column: 2]", 
            classTypeLocation.toString(),
            "Class type source should show class name");

        // Test case 7: Custom object source
        CustomSourceType customSource = new CustomSourceType();
        JsonLocation customObjectLocation = new JsonLocation(
            createRawSourceRef(true, customSource), 10L, 10L, 1, 2);
        assertEquals("[Source: (" + customSource.getClass().getName() + "); line: 1, column: 2]", 
            customObjectLocation.toString(),
            "Custom object source should show full class name");
    }

    @Test
    void testSourceContentTruncation() throws Exception {
        // Given: A string longer than the maximum allowed content length
        String longContent = createStringOfLength(ErrorReportConfiguration.DEFAULT_MAX_RAW_CONTENT_LENGTH);
        String extraLongContent = longContent + "yyy"; // Add 3 extra characters
        
        // When: Creating JsonLocation with long string source
        JsonLocation stringLocation = new JsonLocation(
            createStringSourceRef(extraLongContent), 0L, 0L, 1, 1);
        String stringDescription = stringLocation.sourceDescription();
        
        // Then: Content should be truncated with indication
        String expectedStringDesc = String.format("(String)\"%s\"[truncated 3 chars]", longContent);
        assertEquals(expectedStringDesc, stringDescription,
            "Long string content should be truncated with char count");

        // When: Creating JsonLocation with long byte array source
        JsonLocation byteArrayLocation = new JsonLocation(
            createByteArraySourceRef(extraLongContent.getBytes("UTF-8")), 0L, 0L, 1, 1);
        String byteArrayDescription = byteArrayLocation.sourceDescription();
        
        // Then: Content should be truncated with byte indication
        String expectedByteDesc = String.format("(byte[])\"%s\"[truncated 3 bytes]", longContent);
        assertEquals(expectedByteDesc, byteArrayDescription,
            "Long byte array content should be truncated with byte count");
    }

    @Test
    void testNonPrintableCharacterEscaping() throws Exception {
        // Given: JSON content with non-printable characters (tab and null)
        final String jsonWithNonPrintables = "[ \"tab:[\t]/null:[\0]\" ]";
        
        // When: Creating JsonLocation with non-printable characters
        JsonLocation location = new JsonLocation(
            createStringSourceRef(jsonWithNonPrintables), 0L, 0L, -1, -1);
        String sourceDescription = location.sourceDescription();
        
        // Then: Non-printable characters should be escaped
        String expectedDescription = String.format("(String)\"[ \"tab:[%s]/null:[%s]\" ]\"",
                "\\u0009", "\\u0000");
        assertEquals(expectedDescription, sourceDescription,
            "Non-printable characters should be escaped in Unicode format");
    }

    @Test
    void testSourceInclusionCanBeDisabled() throws Exception {
        // Given: JsonFactory with source inclusion disabled
        JsonFactory factoryWithoutSourceInclusion = JsonFactory.builder()
                .disable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION)
                .build();

        // Test with string input
        testSourceInclusionDisabledForInput(factoryWithoutSourceInclusion, "[ foobar ]");
        
        // Test with byte array input
        testSourceInclusionDisabledForInput(factoryWithoutSourceInclusion, "[ foobar ]".getBytes("UTF-8"));
    }

    private void testSourceInclusionDisabledForInput(JsonFactory factory, Object input) throws Exception {
        JsonParser parser = (input instanceof String) 
            ? factory.createParser((String) input)
            : factory.createParser((byte[]) input);
            
        try (parser) {
            // When: Parsing valid token
            assertToken(JsonToken.START_ARRAY, parser.nextToken());
            
            // Then: Invalid token should throw exception with redacted source
            try {
                parser.nextToken();
                fail("Should have thrown JsonParseException for invalid token 'foobar'");
            } catch (JsonParseException parseException) {
                verifySourceContentIsRedacted(parseException);
            }
        }
    }

    private void verifySourceContentIsRedacted(JsonParseException exception) {
        verifyException(exception, "unrecognized token");
        
        JsonLocation location = exception.getLocation();
        assertNull(location.contentReference().getRawContent(),
            "Raw content should be null when source inclusion is disabled");
        assertThat(location.sourceDescription()).startsWith("REDACTED",
            "Source description should start with REDACTED when source inclusion is disabled");
    }

    @Test
    void testLocationEqualityWithDifferentButEquivalentSources() throws Exception {
        // Given: Two separate but equal File instances
        File file1 = new File("/tmp/foo");
        File file2 = new File("/tmp/foo");
        assertEquals(file1, file2, "File instances should be equal");

        // When: Creating JsonLocations with these equivalent sources
        JsonLocation location1 = new JsonLocation(createFileSourceRef(file1), 10L, 10L, 1, 2);
        JsonLocation location2 = new JsonLocation(createFileSourceRef(file2), 10L, 10L, 1, 2);
        
        // Then: Locations should be equal
        assertEquals(location1, location2,
            "Locations with equivalent sources should be equal");

        // Test equality with same byte array content and offset/length
        final byte[] contentBytes = "BOGUS".getBytes();
        
        JsonLocation sameOffsetLocation1 = new JsonLocation(
            createByteArraySourceRef(contentBytes, 0, 5), 5L, 0L, 1, 2);
        JsonLocation sameOffsetLocation2 = new JsonLocation(
            createByteArraySourceRef(contentBytes, 0, 5), 5L, 0L, 1, 2);
        assertEquals(sameOffsetLocation1, sameOffsetLocation2,
            "Locations with same byte array and offset/length should be equal");

        // Test inequality with different offset/length
        JsonLocation differentOffsetLocation1 = new JsonLocation(
            createByteArraySourceRef(contentBytes, 0, 5), 5L, 0L, 1, 2);
        JsonLocation differentOffsetLocation2 = new JsonLocation(
            createByteArraySourceRef(contentBytes, 1, 4), 5L, 0L, 1, 2);
        assertNotEquals(differentOffsetLocation1, differentOffsetLocation2,
            "Locations with different offset/length should not be equal");
        assertNotEquals(differentOffsetLocation2, differentOffsetLocation1,
            "Inequality should be symmetric");
    }

    // Helper methods for creating ContentReference instances
    private ContentReference createStringSourceRef(String content) {
        return ContentReference.construct(true, content, 0, content.length(),
                ErrorReportConfiguration.defaults());
    }

    private ContentReference createCharArraySourceRef(char[] content) {
        return ContentReference.construct(true, content, 0, content.length,
                ErrorReportConfiguration.defaults());
    }

    private ContentReference createByteArraySourceRef(byte[] content) {
        return ContentReference.construct(true, content, 0, content.length,
                ErrorReportConfiguration.defaults());
    }

    private ContentReference createByteArraySourceRef(byte[] content, int offset, int length) {
        return ContentReference.construct(true, content, offset, length,
                ErrorReportConfiguration.defaults());
    }

    private ContentReference createInputStreamSourceRef(InputStream inputStream) {
        return ContentReference.construct(true, inputStream, -1, -1,
                ErrorReportConfiguration.defaults());
    }

    private ContentReference createFileSourceRef(File file) {
        return ContentReference.construct(true, file, -1, -1,
                ErrorReportConfiguration.defaults());
    }

    private ContentReference createRawSourceRef(boolean textual, Object source) {
        return ContentReference.rawReference(textual, source);
    }

    private String createStringOfLength(int length) {
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            builder.append("x");
        }
        return builder.toString();
    }
}