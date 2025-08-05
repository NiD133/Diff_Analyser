package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.ErrorReportConfiguration;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.io.ContentReference;

/**
 * Test suite for JsonLocation class functionality including:
 * - Object equality and hash code
 * - Location information (line, column, byte/char offsets)
 * - String representation and descriptions
 * - Content reference handling
 */
public class JsonLocationTest {

    // Test data constants
    private static final int LINE_500 = 500;
    private static final int COLUMN_500 = 500;
    private static final long BYTE_OFFSET_500 = 500L;
    private static final long CHAR_OFFSET_500 = 500L;
    private static final int NEGATIVE_COLUMN = -42;
    private static final int LARGE_LINE = 3981;

    @Test
    public void testEquals_DifferentContentReferences_ShouldNotBeEqual() {
        JsonLocation locationWithNullSource = new JsonLocation((Object) null, CHAR_OFFSET_500, LINE_500, COLUMN_500);
        ContentReference redactedRef = ContentReference.redacted();
        JsonLocation locationWithRedactedSource = new JsonLocation(redactedRef, BYTE_OFFSET_500, CHAR_OFFSET_500, LINE_500, COLUMN_500);
        
        assertFalse("Locations with different content references should not be equal", 
                   locationWithRedactedSource.equals(locationWithNullSource));
        assertFalse("Equality should be symmetric", 
                   locationWithNullSource.equals(locationWithRedactedSource));
        
        // Verify location properties
        assertEquals(LINE_500, locationWithRedactedSource.getLineNr());
        assertEquals(COLUMN_500, locationWithRedactedSource.getColumnNr());
        assertEquals(BYTE_OFFSET_500, locationWithRedactedSource.getByteOffset());
        assertEquals(CHAR_OFFSET_500, locationWithNullSource.getCharOffset());
    }

    @Test
    public void testEquals_DifferentCharOffsets_ShouldNotBeEqual() {
        JsonLocation baseLocation = new JsonLocation(JsonLocation.NA, BYTE_OFFSET_500, CHAR_OFFSET_500, LINE_500, COLUMN_500);
        JsonLocation differentCharOffset = new JsonLocation(JsonLocation.NA, BYTE_OFFSET_500, 1L, LINE_500, COLUMN_500);
        
        assertFalse("Locations with different char offsets should not be equal", 
                   differentCharOffset.equals(baseLocation));
        assertFalse("Equality should be symmetric", 
                   baseLocation.equals(differentCharOffset));
        
        assertEquals(LINE_500, differentCharOffset.getLineNr());
        assertEquals(COLUMN_500, differentCharOffset.getColumnNr());
        assertEquals(BYTE_OFFSET_500, differentCharOffset.getByteOffset());
    }

    @Test
    public void testEquals_DifferentByteOffsets_ShouldNotBeEqual() {
        JsonLocation nullSourceLocation = new JsonLocation((Object) null, CHAR_OFFSET_500, LINE_500, COLUMN_500);
        ContentReference redactedRef = ContentReference.redacted();
        JsonLocation differentByteOffset = new JsonLocation((Object) redactedRef, 1127L, -304L, LINE_500, 92);
        
        assertFalse("Locations with different byte offsets should not be equal", 
                   differentByteOffset.equals(nullSourceLocation));
        
        assertEquals(1127L, differentByteOffset.getByteOffset());
        assertEquals(-304L, differentByteOffset.getCharOffset());
        assertEquals(92, differentByteOffset.getColumnNr());
        assertEquals(LINE_500, differentByteOffset.getLineNr());
    }

    @Test
    public void testEquals_DifferentColumns_ShouldNotBeEqual() {
        JsonLocation baseLocation = new JsonLocation(JsonLocation.NA, CHAR_OFFSET_500, LINE_500, COLUMN_500);
        JsonLocation differentColumn = new JsonLocation(JsonLocation.NA, CHAR_OFFSET_500, LINE_500, NEGATIVE_COLUMN);
        
        assertFalse("Locations with different columns should not be equal", 
                   baseLocation.equals(differentColumn));
        
        assertEquals(LINE_500, differentColumn.getLineNr());
        assertEquals(NEGATIVE_COLUMN, differentColumn.getColumnNr());
        assertEquals(CHAR_OFFSET_500, baseLocation.getCharOffset());
        assertEquals(-1L, baseLocation.getByteOffset());
    }

    @Test
    public void testEquals_NALocationVsCustomLocation_ShouldNotBeEqual() {
        JsonLocation naLocation = JsonLocation.NA;
        JsonLocation customLocation = new JsonLocation((Object) null, CHAR_OFFSET_500, LINE_500, COLUMN_500);
        
        assertFalse("NA location should not equal custom location", 
                   customLocation.equals(naLocation));
        
        assertEquals(CHAR_OFFSET_500, customLocation.getCharOffset());
        assertEquals(LINE_500, customLocation.getLineNr());
        assertEquals(-1L, customLocation.getByteOffset());
        assertEquals(COLUMN_500, customLocation.getColumnNr());
    }

    @Test
    public void testHashCode_WithContentReference() {
        ErrorReportConfiguration config = new ErrorReportConfiguration(1, 1);
        ContentReference contentRef = ContentReference.construct(false, null, 2879, 2879, config);
        JsonLocation location = new JsonLocation(contentRef, 314L, 0, -2189);
        
        // Should not throw exception
        int hashCode = location.hashCode();
        
        assertEquals(314L, location.getCharOffset());
        assertEquals(-1L, location.getByteOffset());
        assertEquals(-2189, location.getColumnNr());
        assertEquals(0, location.getLineNr());
    }

    @Test
    public void testHashCode_WithObjectContentReference() {
        Object sourceObject = new Object();
        ContentReference contentRef = JsonLocation._wrap(sourceObject);
        JsonLocation location = new JsonLocation((Object) contentRef, 0L, 0L, 1831, 0);
        
        // Should not throw exception
        int hashCode = location.hashCode();
        
        assertEquals(0, location.getColumnNr());
        assertEquals(1831, location.getLineNr());
        assertFalse(contentRef.hasTextualContent());
    }

    @Test
    public void testToString_WithByteAndCharOffsets() {
        Object sourceObject = new Object();
        JsonLocation location = new JsonLocation(sourceObject, -2650L, 0L, LARGE_LINE, 2);
        
        String result = location.toString();
        
        assertEquals(-2650L, location.getByteOffset());
        assertEquals(0L, location.getCharOffset());
        assertEquals("[Source: (Object); line: 3981, column: 2]", result);
    }

    @Test
    public void testToString_LineOnlyWhenColumnIsZero() {
        Object sourceObject = new Object();
        JsonLocation location = new JsonLocation(sourceObject, LARGE_LINE, 2, 2, 0);
        
        String result = location.toString();
        
        assertEquals(2L, location.getCharOffset());
        assertEquals("[Source: (Object); line: 2]", result);
        assertEquals(0, location.getColumnNr());
        assertEquals(LARGE_LINE, location.getByteOffset());
    }

    @Test
    public void testOffsetDescription_UnknownByteOffset() {
        Object sourceObject = new Object();
        ContentReference contentRef = JsonLocation._wrap(sourceObject);
        JsonLocation location = new JsonLocation(contentRef, -1L, 1L, 0, 0);
        
        String description = location.offsetDescription();
        
        assertEquals("byte offset: #UNKNOWN", description);
        assertEquals(1L, location.getCharOffset());
        assertEquals(0, location.getColumnNr());
        assertEquals(0, location.getLineNr());
    }

    @Test
    public void testGetColumnNr() {
        ContentReference contentRef = ContentReference.rawReference(true, null);
        JsonLocation location = new JsonLocation(contentRef, -2758L, 0L, -3036, 0);
        
        int columnNr = location.getColumnNr();
        
        assertEquals(-2758L, location.getByteOffset());
        assertEquals(0, columnNr);
        assertEquals(0L, location.getCharOffset());
        assertEquals(-3036, location.getLineNr());
    }

    @Test
    public void testGetLineNr() {
        Object sourceObject = new Object();
        ContentReference contentRef = ContentReference.rawReference(true, sourceObject);
        JsonLocation location = new JsonLocation(contentRef, -4198L, -4198L, -1143, 0);
        
        int lineNr = location.getLineNr();
        
        assertEquals(-4198L, location.getByteOffset());
        assertEquals(0, location.getColumnNr());
        assertEquals(-1143, lineNr);
        assertEquals(-4198L, location.getCharOffset());
    }

    @Test
    public void testSourceDescription_NALocation() {
        JsonLocation naLocation = JsonLocation.NA;
        
        String description = naLocation.sourceDescription();
        
        assertEquals("UNKNOWN", description);
    }

    @Test
    public void testGetSourceRef() {
        Object sourceObject = new Object();
        JsonLocation location = new JsonLocation(sourceObject, 0L, 0L, 0, 0);
        
        Object sourceRef = location.getSourceRef();
        
        assertNotNull(sourceRef);
        assertEquals(0, location.getColumnNr());
        assertEquals(0, location.getLineNr());
    }

    @Test
    public void testGetters_BasicValues() {
        JsonLocation location = new JsonLocation((Object) null, -219L, 314, 314);
        
        assertEquals(314, location.getLineNr());
        assertEquals(314, location.getColumnNr());
        assertEquals(-219L, location.getCharOffset());
        assertEquals(-1L, location.getByteOffset());
    }

    @Test
    public void testGetColumnNr_NALocation() {
        JsonLocation naLocation = JsonLocation.NA;
        
        int columnNr = naLocation.getColumnNr();
        
        assertEquals(-1, columnNr);
    }

    @Test
    public void testGetCharOffset() {
        Object sourceObject = new Object();
        JsonLocation location = new JsonLocation(sourceObject, 0L, 0L, -1678, -1678);
        
        long charOffset = location.getCharOffset();
        
        assertEquals(0L, charOffset);
        assertEquals(-1678, location.getLineNr());
        assertEquals(-1678, location.getColumnNr());
    }

    @Test
    public void testGetCharOffset_WithContentReference() {
        JsonLocation location = new JsonLocation((ContentReference) null, 2675L, 2675L, -356, -356);
        
        long charOffset = location.getCharOffset();
        
        assertEquals(2675L, charOffset);
        assertEquals(-356, location.getLineNr());
        assertEquals(-356, location.getColumnNr());
        assertEquals(2675L, location.getByteOffset());
    }

    @Test
    public void testGetByteOffset() {
        Object sourceObject = new Object();
        JsonLocation location = new JsonLocation(sourceObject, 0L, 0L, -1678, -1678);
        
        long byteOffset = location.getByteOffset();
        
        assertEquals(0L, byteOffset);
        assertEquals(-1678, location.getColumnNr());
        assertEquals(-1678, location.getLineNr());
    }

    @Test
    public void testGetByteOffset_WithRedactedContent() {
        ContentReference redactedRef = ContentReference.redacted();
        JsonLocation location = new JsonLocation(redactedRef, 1852L, -413L, 0, 74);
        
        long byteOffset = location.getByteOffset();
        
        assertEquals(1852L, byteOffset);
        assertEquals(0, location.getLineNr());
        assertEquals(-413L, location.getCharOffset());
        assertEquals(74, location.getColumnNr());
    }

    @Test
    public void testContentReference_WithNestedContentReference() {
        ContentReference redactedRef = ContentReference.redacted();
        ErrorReportConfiguration config = ErrorReportConfiguration.defaults();
        ContentReference nestedRef = ContentReference.construct(true, redactedRef, 0, 1929, config);
        JsonLocation location = new JsonLocation((Object) nestedRef, -578L, LINE_500, COLUMN_500);
        
        ContentReference result = location.contentReference();
        
        assertEquals(LINE_500, location.getLineNr());
        assertEquals(COLUMN_500, location.getColumnNr());
        assertEquals(-1L, location.getByteOffset());
        assertTrue(result.hasTextualContent());
        assertEquals(-578L, location.getCharOffset());
    }

    @Test
    public void testContentReference_WithComplexNesting() {
        Object sourceObject = new Object();
        ErrorReportConfiguration config = new ErrorReportConfiguration(-1262, -1262);
        ContentReference baseRef = ContentReference.construct(true, sourceObject, config);
        ContentReference nestedRef = ContentReference.construct(true, baseRef, 4615, 0, config);
        JsonLocation location = new JsonLocation(nestedRef, 3039L, 575, -559);
        
        ContentReference result = location.contentReference();
        
        assertEquals(575, location.getLineNr());
        assertEquals(0, result.contentLength());
        assertEquals(-559, location.getColumnNr());
        assertEquals(-1L, location.getByteOffset());
        assertEquals(3039L, location.getCharOffset());
    }

    @Test
    public void testContentReference_WithErrorReportConfiguration() {
        ErrorReportConfiguration config = new ErrorReportConfiguration(1, 1);
        ContentReference contentRef = ContentReference.construct(false, null, 2879, 2879, config);
        JsonLocation location = new JsonLocation(contentRef, 314L, 0, -2189);
        
        ContentReference result = location.contentReference();
        
        assertEquals(314L, location.getCharOffset());
        assertEquals(0, location.getLineNr());
        assertEquals(-2189, location.getColumnNr());
        assertEquals(2879, result.contentOffset());
        assertEquals(-1L, location.getByteOffset());
    }

    @Test
    public void testWrap_WithContentReference() {
        ContentReference redactedRef = ContentReference.redacted();
        ErrorReportConfiguration config = ErrorReportConfiguration.defaults();
        ContentReference nestedRef = ContentReference.construct(true, redactedRef, 0, 1929, config);
        
        ContentReference result = JsonLocation._wrap(nestedRef);
        
        assertTrue(result.hasTextualContent());
    }

    @Test
    public void testWrap_SameContentReference() {
        Object sourceObject = new Object();
        ErrorReportConfiguration config = new ErrorReportConfiguration(0, 0);
        ContentReference contentRef = ContentReference.construct(true, sourceObject, 2492, 0, config);
        
        ContentReference result = JsonLocation._wrap(contentRef);
        
        assertSame(contentRef, result);
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testToString_WithInvalidErrorConfiguration_ShouldThrowException() {
        StringBuilder stringBuilder = new StringBuilder(500);
        ErrorReportConfiguration invalidConfig = new ErrorReportConfiguration(500, -1);
        ContentReference contentRef = ContentReference.construct(true, stringBuilder, invalidConfig);
        JsonLocation location = new JsonLocation(contentRef, 500L, 500L, 1571, 500);
        
        location.toString(); // Should throw StringIndexOutOfBoundsException
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testSourceDescription_WithInvalidErrorConfiguration_ShouldThrowException() {
        StringBuilder stringBuilder = new StringBuilder("");
        ErrorReportConfiguration invalidConfig = new ErrorReportConfiguration(8, -1);
        ContentReference contentRef = ContentReference.construct(true, stringBuilder, invalidConfig);
        JsonLocation location = new JsonLocation(contentRef, 8L, 500L, 1571, 500);
        
        location.sourceDescription(); // Should throw StringIndexOutOfBoundsException
    }

    @Test
    public void testAppendOffsetDescription_WithByteOffset() {
        JsonLocation location = new JsonLocation((Object) null, 0L, 1840L, -201, 93);
        StringBuilder sb = new StringBuilder("_K|FUenM:'d");
        
        location.appendOffsetDescription(sb);
        
        assertEquals("_K|FUenM:'dbyte offset: #0", sb.toString());
    }

    @Test
    public void testAppendOffsetDescription_WithLineOnly() {
        JsonLocation naLocation = JsonLocation.NA;
        Object sourceRef = naLocation.getSourceRef();
        StringBuilder sb = new StringBuilder("byte offset: #UNKNOWN");
        JsonLocation location = new JsonLocation(sourceRef, -1L, 2, -4651);
        
        location.appendOffsetDescription(sb);
        
        assertEquals("byte offset: #UNKNOWNline: 2", sb.toString());
    }

    @Test
    public void testAppendOffsetDescription_WithLineAndColumn() {
        JsonLocation location = new JsonLocation((Object) null, -219L, 314, 314);
        StringBuilder sb = new StringBuilder("; ");
        
        location.appendOffsetDescription(sb);
        
        assertEquals("; line: 314, column: 314", sb.toString());
        assertEquals(314, location.getColumnNr());
    }

    @Test(expected = NullPointerException.class)
    public void testAppendOffsetDescription_WithNullStringBuilder_ShouldThrowException() {
        Object sourceObject = new Object();
        JsonLocation location = new JsonLocation(sourceObject, 0L, 759, -1047);
        
        location.appendOffsetDescription(null); // Should throw NullPointerException
    }

    @Test
    public void testAppendOffsetDescription_WithUnknownColumn() {
        ContentReference contentRef = ContentReference.rawReference(true, null);
        StringBuilder sb = new StringBuilder();
        JsonLocation location = new JsonLocation(contentRef, -3036L, LINE_500, -1291);
        
        location.appendOffsetDescription(sb);
        
        assertEquals("line: 500, column: UNKNOWN", sb.toString());
        assertEquals(-1L, location.getByteOffset());
    }

    @Test
    public void testAppendOffsetDescription_WithUnknownLine() {
        Object sourceObject = new Object();
        ContentReference contentRef = ContentReference.rawReference(true, sourceObject);
        JsonLocation location = new JsonLocation(contentRef, -4198L, -4198L, -1143, 0);
        StringBuilder sb = new StringBuilder(863);
        
        location.appendOffsetDescription(sb);
        
        assertEquals("line: UNKNOWN, column: 0", sb.toString());
    }

    @Test
    public void testAppendOffsetDescription_NALocation() {
        JsonLocation naLocation = JsonLocation.NA;
        StringBuilder sb = new StringBuilder("");
        
        naLocation.appendOffsetDescription(sb);
        
        assertEquals("byte offset: #UNKNOWN", sb.toString());
    }

    @Test
    public void testAppendOffsetDescription_WithZeroLineAndColumn() {
        Object sourceObject = new Object();
        ContentReference contentRef = ContentReference.rawReference(true, sourceObject);
        JsonLocation location = new JsonLocation((Object) contentRef, 0L, 0, 0);
        StringBuilder sb = new StringBuilder(863);
        
        location.appendOffsetDescription(sb);
        
        assertEquals("line: 0, column: 0", sb.toString());
    }

    @Test
    public void testEquals_IdenticalLocations_ShouldBeEqual() {
        JsonLocation naLocation = JsonLocation.NA;
        JsonLocation location1 = new JsonLocation(naLocation, BYTE_OFFSET_500, CHAR_OFFSET_500, LINE_500, COLUMN_500);
        JsonLocation location2 = new JsonLocation(naLocation, BYTE_OFFSET_500, CHAR_OFFSET_500, LINE_500, COLUMN_500);
        
        assertTrue("Identical locations should be equal", location2.equals(location1));
        assertEquals(LINE_500, location2.getLineNr());
        assertEquals(CHAR_OFFSET_500, location2.getCharOffset());
        assertEquals(BYTE_OFFSET_500, location2.getByteOffset());
        assertFalse("Location should not equal NA", location2.equals(naLocation));
        assertEquals(COLUMN_500, location2.getColumnNr());
    }

    @Test
    public void testEquals_DifferentByteOffsets_ShouldNotBeEqual() {
        JsonLocation location1 = new JsonLocation((Object) null, CHAR_OFFSET_500, LINE_500, COLUMN_500);
        ContentReference unknownRef = ContentReference.unknown();
        JsonLocation location2 = new JsonLocation(unknownRef, 344L, 0L, LINE_500, COLUMN_500);
        
        assertFalse("Locations with different byte offsets should not be equal", 
                   location1.equals(location2));
        assertEquals(LINE_500, location2.getLineNr());
        assertEquals(COLUMN_500, location2.getColumnNr());
        assertEquals(CHAR_OFFSET_500, location1.getCharOffset());
        assertEquals(344L, location2.getByteOffset());
    }

    @Test
    public void testEquals_DifferentByteOffsetsWithRedactedContent_ShouldNotBeEqual() {
        JsonLocation location1 = new JsonLocation((Object) null, CHAR_OFFSET_500, LINE_500, COLUMN_500);
        ContentReference redactedRef = ContentReference.redacted();
        JsonLocation location2 = new JsonLocation((Object) redactedRef, -1513L, CHAR_OFFSET_500, LINE_500, COLUMN_500);
        
        assertFalse("Locations with different byte offsets should not be equal", 
                   location2.equals(location1));
        assertFalse("Equality should be symmetric", 
                   location1.equals(location2));
        assertEquals(LINE_500, location2.getLineNr());
        assertEquals(CHAR_OFFSET_500, location2.getCharOffset());
        assertEquals(-1513L, location2.getByteOffset());
        assertEquals(COLUMN_500, location2.getColumnNr());
    }

    @Test
    public void testEquals_NALocationVsDifferentLocation_ShouldNotBeEqual() {
        JsonLocation naLocation = JsonLocation.NA;
        ContentReference contentRef = ContentReference.rawReference(naLocation);
        JsonLocation customLocation = new JsonLocation((Object) contentRef, 1795L, 500L, 2, LARGE_LINE);
        
        assertFalse("NA location should not equal custom location", 
                   naLocation.equals(customLocation));
        assertEquals(LARGE_LINE, customLocation.getColumnNr());
        assertEquals(1795L, customLocation.getByteOffset());
        assertEquals(500L, customLocation.getCharOffset());
        assertEquals(2, customLocation.getLineNr());
    }

    @Test
    public void testEquals_WithDifferentObjectType_ShouldNotBeEqual() {
        JsonLocation naLocation = JsonLocation.NA;
        Object differentObject = new Object();
        
        assertFalse("JsonLocation should not equal different object type", 
                   naLocation.equals(differentObject));
    }

    @Test
    public void testEquals_WithNull_ShouldNotBeEqual() {
        JsonLocation naLocation = JsonLocation.NA;
        
        assertFalse("JsonLocation should not equal null", naLocation.equals(null));
    }

    @Test
    public void testEquals_SameInstance_ShouldBeEqual() {
        JsonLocation naLocation = JsonLocation.NA;
        
        assertTrue("JsonLocation should equal itself", naLocation.equals(naLocation));
    }

    @Test
    public void testEquals_NALocationVsCustomLocation_ShouldNotBeEqual2() {
        JsonLocation naLocation = JsonLocation.NA;
        JsonLocation customLocation = new JsonLocation((Object) null, CHAR_OFFSET_500, LINE_500, COLUMN_500);
        
        assertFalse("NA location should not equal custom location", 
                   naLocation.equals(customLocation));
        assertEquals(COLUMN_500, customLocation.getColumnNr());
        assertEquals(CHAR_OFFSET_500, customLocation.getCharOffset());
        assertEquals(-1L, customLocation.getByteOffset());
        assertEquals(LINE_500, customLocation.getLineNr());
    }

    @Test
    public void testToString_WithByteOffsetOnly() {
        JsonLocation location = new JsonLocation((Object) null, 62, LINE_500, -2898, -26);
        
        String result = location.toString();
        
        assertEquals(-26, location.getColumnNr());
        assertEquals(CHAR_OFFSET_500, location.getCharOffset());
        assertEquals("[Source: UNKNOWN; byte offset: #62]", result);
        assertEquals(-2898, location.getLineNr());
    }

    @Test
    public void testToString_WithUnknownLine() {
        JsonLocation naLocation = JsonLocation.NA;
        ErrorReportConfiguration config = ErrorReportConfiguration.defaults();
        ContentReference contentRef = ContentReference.construct(true, naLocation, config);
        JsonLocation location = new JsonLocation(contentRef, 500L, -1, 256);
        
        String result = location.toString();
        
        assertEquals(-1L, location.getByteOffset());
        assertEquals(-1, location.getLineNr());
        assertEquals(500L, location.getCharOffset());
        assertEquals("[Source: (com.fasterxml.jackson.core.JsonLocation); line: UNKNOWN, column: 256]", result);
    }

    @Test
    public void testGetLineNr_WithZeroLine() {
        Object sourceObject = new Object();
        ContentReference contentRef = ContentReference.rawReference(true, sourceObject);
        JsonLocation location = new JsonLocation((Object) contentRef, 0L, 0, 0);
        
        int lineNr = location.getLineNr();
        
        assertEquals(0, lineNr);
        assertEquals(0L, location.getCharOffset());
        assertEquals(0, location.getColumnNr());
        assertEquals(-1L, location.getByteOffset());
    }

    @Test
    public void testGetCharOffset_WithRedactedContent() {
        ContentReference redactedRef = ContentReference.redacted();
        JsonLocation location = new JsonLocation((Object) redactedRef, -578L, LINE_500, COLUMN_500);
        
        long charOffset = location.getCharOffset();
        
        assertEquals(-1L, location.getByteOffset());
        assertEquals(LINE_500, location.getLineNr());
        assertEquals(COLUMN_500, location.getColumnNr());
        assertEquals(-578L, charOffset);
    }

    @Test
    public void testGetColumnNr_WithRedactedContent() {
        ContentReference redactedRef = ContentReference.redacted();
        JsonLocation location = new JsonLocation((Object) redactedRef, -578L, LINE_500, COLUMN_500);
        
        int columnNr = location.getColumnNr();
        
        assertEquals(-578L, location.getCharOffset());
        assertEquals(LINE_500, location.getLineNr());
        assertEquals(COLUMN_500, columnNr);
        assertEquals(-1L, location.getByteOffset());
    }

    @Test
    public void testContentReference_WithRedactedContent() {
        ContentReference redactedRef = ContentReference.redacted();
        JsonLocation location = new JsonLocation((Object) redactedRef, -578L, LINE_500, COLUMN_500);
        
        ContentReference result = location.contentReference();
        
        assertNotNull(result);
        assertEquals(-578L, location.getCharOffset());
        assertEquals(COLUMN_500, location.getColumnNr());
        assertSame(redactedRef, result);
        assertEquals(-1L, location.getByteOffset());
        assertEquals(LINE_500, location.getLineNr());
    }

    @Test
    public void testGetByteOffset_NALocation() {
        JsonLocation naLocation = JsonLocation.NA;
        
        long byteOffset = naLocation.getByteOffset();
        
        assertEquals(-1L, byteOffset);
    }
}