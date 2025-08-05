package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.io.ContentReference;

/**
 * Unit tests for the {@link JsonLocation} class, focusing on its constructors,
 * accessors, equality, and string representation.
 */
public class JsonLocationTest {

    private static final String TEST_SOURCE_DESC = "test-source";
    private static final long TEST_BYTE_OFFSET = 1000L;
    private static final long TEST_CHAR_OFFSET = 500L;
    private static final int TEST_LINE = 42;
    private static final int TEST_COL = 10;

    /*
    /**********************************************************
    /* Accessor & State Tests
    /**********************************************************
    */

    @Test
    public void accessors_onFullyPopulatedLocation_shouldReturnConstructorValues() {
        // Arrange
        ContentReference contentRef = ContentReference.construct(false, TEST_SOURCE_DESC, 0, 0, null);
        JsonLocation location = new JsonLocation(contentRef, TEST_BYTE_OFFSET, TEST_CHAR_OFFSET, TEST_LINE, TEST_COL);

        // Act & Assert
        assertEquals(TEST_BYTE_OFFSET, location.getByteOffset());
        assertEquals(TEST_CHAR_OFFSET, location.getCharOffset());
        assertEquals(TEST_LINE, location.getLineNr());
        assertEquals(TEST_COL, location.getColumnNr());
        assertSame(contentRef, location.contentReference());
        assertEquals(TEST_SOURCE_DESC, location.getSourceRef()); // Deprecated method check
    }

    @Test
    public void accessors_onNotAvailableLocation_shouldReturnDefaultValues() {
        // Arrange
        JsonLocation naLocation = JsonLocation.NA;

        // Act & Assert
        assertEquals(-1L, naLocation.getByteOffset());
        assertEquals(-1L, naLocation.getCharOffset());
        assertEquals(-1, naLocation.getLineNr());
        assertEquals(-1, naLocation.getColumnNr());
        assertEquals("UNKNOWN", naLocation.sourceDescription());
        assertNotNull(naLocation.contentReference());
    }

    /*
    /**********************************************************
    /* Equality & Hashing Tests
    /**********************************************************
    */

    @Test
    public void equals_shouldAdhereToContract() {
        // Arrange
        JsonLocation location1 = new JsonLocation(TEST_SOURCE_DESC, TEST_BYTE_OFFSET, TEST_CHAR_OFFSET, TEST_LINE, TEST_COL);
        JsonLocation location2 = new JsonLocation(TEST_SOURCE_DESC, TEST_BYTE_OFFSET, TEST_CHAR_OFFSET, TEST_LINE, TEST_COL);
        JsonLocation location3 = new JsonLocation(TEST_SOURCE_DESC, TEST_BYTE_OFFSET, TEST_CHAR_OFFSET, TEST_LINE, TEST_COL + 1);

        // Assert on the equals contract
        // 1. Reflexive
        assertTrue("A location must be equal to itself.", location1.equals(location1));

        // 2. Symmetric
        assertTrue("Two identical locations should be equal.", location1.equals(location2));
        assertTrue("Symmetric equality should hold.", location2.equals(location1));

        // 3. Not equal to different object
        assertFalse("A location should not be equal to a different location.", location1.equals(location3));

        // 4. Not equal to null
        assertFalse("A location should not be equal to null.", location1.equals(null));

        // 5. Not equal to different type
        assertFalse("A location should not be equal to an object of a different type.", location1.equals(new Object()));

        // 6. Not equal to NA constant
        assertFalse("A populated location should not be equal to JsonLocation.NA.", location1.equals(JsonLocation.NA));
        assertFalse("JsonLocation.NA should not be equal to a populated location.", JsonLocation.NA.equals(location1));
    }

    @Test
    public void equals_withDifferentFields_shouldReturnFalse() {
        // Arrange
        JsonLocation base = new JsonLocation(TEST_SOURCE_DESC, TEST_BYTE_OFFSET, TEST_CHAR_OFFSET, TEST_LINE, TEST_COL);

        // Act & Assert
        JsonLocation differentSource = new JsonLocation("other-source", TEST_BYTE_OFFSET, TEST_CHAR_OFFSET, TEST_LINE, TEST_COL);
        assertFalse("Should be false for different source", base.equals(differentSource));

        JsonLocation differentByteOffset = new JsonLocation(TEST_SOURCE_DESC, TEST_BYTE_OFFSET + 1, TEST_CHAR_OFFSET, TEST_LINE, TEST_COL);
        assertFalse("Should be false for different byte offset", base.equals(differentByteOffset));

        JsonLocation differentCharOffset = new JsonLocation(TEST_SOURCE_DESC, TEST_BYTE_OFFSET, TEST_CHAR_OFFSET + 1, TEST_LINE, TEST_COL);
        assertFalse("Should be false for different char offset", base.equals(differentCharOffset));

        JsonLocation differentLine = new JsonLocation(TEST_SOURCE_DESC, TEST_BYTE_OFFSET, TEST_CHAR_OFFSET, TEST_LINE + 1, TEST_COL);
        assertFalse("Should be false for different line number", base.equals(differentLine));

        JsonLocation differentCol = new JsonLocation(TEST_SOURCE_DESC, TEST_BYTE_OFFSET, TEST_CHAR_OFFSET, TEST_LINE, TEST_COL + 1);
        assertFalse("Should be false for different column number", base.equals(differentCol));
    }

    @Test
    public void hashCode_shouldBeConsistentForEqualObjects() {
        // Arrange
        JsonLocation location1 = new JsonLocation(TEST_SOURCE_DESC, TEST_BYTE_OFFSET, TEST_CHAR_OFFSET, TEST_LINE, TEST_COL);
        JsonLocation location2 = new JsonLocation(TEST_SOURCE_DESC, TEST_BYTE_OFFSET, TEST_CHAR_OFFSET, TEST_LINE, TEST_COL);

        // Act & Assert
        assertTrue("Equal objects must have equal hash codes", location1.equals(location2));
        assertEquals("Equal objects must have equal hash codes", location1.hashCode(), location2.hashCode());
        assertEquals("NA hash code should be consistent", JsonLocation.NA.hashCode(), JsonLocation.NA.hashCode());
    }

    /*
    /**********************************************************
    /* String Representation Tests
    /**********************************************************
    */

    @Test
    public void toString_withAllFields_shouldReturnFullDescription() {
        // Arrange
        JsonLocation location = new JsonLocation(TEST_SOURCE_DESC, TEST_BYTE_OFFSET, TEST_CHAR_OFFSET, TEST_LINE, TEST_COL);
        String expected = "[Source: (String)\"" + TEST_SOURCE_DESC + "\"; line: " + TEST_LINE + ", column: " + TEST_COL + "]";

        // Act
        String actual = location.toString();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void toString_withUnknownSource_shouldIndicateUnknown() {
        // Arrange
        JsonLocation location = new JsonLocation(null, -1L, TEST_CHAR_OFFSET, TEST_LINE, TEST_COL);
        String expected = "[Source: UNKNOWN; line: " + TEST_LINE + ", column: " + TEST_COL + "]";

        // Act
        String actual = location.toString();

        // Assert
        assertEquals(expected, actual);
    }
    
    @Test
    public void toString_withUnknownLineOrColumn_shouldOmitThem() {
        // Arrange
        JsonLocation locationWithUnknownLine = new JsonLocation(TEST_SOURCE_DESC, TEST_BYTE_OFFSET, TEST_CHAR_OFFSET, -1, TEST_COL);
        String expectedWithUnknownLine = "[Source: (String)\"" + TEST_SOURCE_DESC + "\"; line: UNKNOWN, column: " + TEST_COL + "]";
        assertEquals(expectedWithUnknownLine, locationWithUnknownLine.toString());

        JsonLocation locationWithUnknownCol = new JsonLocation(TEST_SOURCE_DESC, TEST_BYTE_OFFSET, TEST_CHAR_OFFSET, TEST_LINE, -1);
        String expectedWithUnknownCol = "[Source: (String)\"" + TEST_SOURCE_DESC + "\"; line: " + TEST_LINE + "]";
        assertEquals(expectedWithUnknownCol, locationWithUnknownCol.toString());
    }

    @Test
    public void offsetDescription_shouldProvideBriefSummary() {
        // Arrange
        JsonLocation locationWithLineCol = new JsonLocation(null, -1L, TEST_CHAR_OFFSET, TEST_LINE, TEST_COL);
        JsonLocation locationWithByteOffset = new JsonLocation(null, TEST_BYTE_OFFSET, -1L, -1, -1);
        JsonLocation naLocation = JsonLocation.NA;

        // Act & Assert
        assertEquals("line: 42, column: 10", locationWithLineCol.offsetDescription());
        assertEquals("byte offset: #1000", locationWithByteOffset.offsetDescription());
        assertEquals("byte offset: #UNKNOWN", naLocation.offsetDescription());
    }

    @Test
    public void appendOffsetDescription_shouldAppendToBuilder() {
        // Arrange
        StringBuilder sb = new StringBuilder("Location: ");
        JsonLocation location = new JsonLocation(null, -1L, TEST_CHAR_OFFSET, TEST_LINE, TEST_COL);
        String expected = "Location: line: 42, column: 10";

        // Act
        location.appendOffsetDescription(sb);

        // Assert
        assertEquals(expected, sb.toString());
    }

    /*
    /**********************************************************
    /* Edge Case Tests
    /**********************************************************
    */

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void toString_withInvalidSnippetConfig_shouldThrowException() {
        // This test verifies behavior when the source is a StringBuilder and the
        // ErrorReportConfiguration requests a snippet that is out of bounds.
        
        // Arrange
        StringBuilder source = new StringBuilder("some text");
        // Create a config that will try to read a snippet of length -1, causing an exception.
        ErrorReportConfiguration config = new ErrorReportConfiguration(500, -1);
        ContentReference contentRef = ContentReference.construct(true, source, config);
        JsonLocation location = new JsonLocation(contentRef, 500L, 500L, 1, 1);

        // Act
        location.toString(); // This should trigger the exception
    }
}