package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.io.ContentReference;

public class JsonLocationTest {

    private static final long BYTE_OFFSET_UNKNOWN = -1L;
    private static final int LINE_UNKNOWN = -1;
    private static final int COLUMN_UNKNOWN = -1;
    private static final long CHAR_OFFSET_UNKNOWN = -1L;
    private static final int TIMEOUT = 4000;

    @Test(timeout = TIMEOUT)
    public void testJsonLocationEqualityWithDifferentContentReferences() {
        JsonLocation location1 = new JsonLocation((Object) null, 500, 500, 500);
        ContentReference contentRef = ContentReference.redacted();
        JsonLocation location2 = new JsonLocation(contentRef, 500L, 500L, 500, 500);

        assertFalse(location1.equals(location2));
        assertEquals(500, location2.getLineNr());
        assertEquals(500, location2.getColumnNr());
        assertEquals(500L, location2.getByteOffset());
        assertEquals(500L, location1.getCharOffset());
    }

    @Test(timeout = TIMEOUT)
    public void testJsonLocationEqualityWithSameContentReference() {
        JsonLocation location1 = JsonLocation.NA;
        JsonLocation location2 = new JsonLocation(location1, 500, 500, 500, 500);
        JsonLocation location3 = new JsonLocation(location1, 500, 1L, 500, 500);

        assertFalse(location2.equals(location3));
        assertEquals(500L, location2.getCharOffset());
        assertEquals(500, location3.getLineNr());
        assertEquals(500L, location3.getByteOffset());
    }

    @Test(timeout = TIMEOUT)
    public void testJsonLocationWithNegativeOffsets() {
        JsonLocation location1 = new JsonLocation((Object) null, 500, 500, 500);
        ContentReference contentRef = ContentReference.redacted();
        JsonLocation location2 = new JsonLocation((Object) contentRef, 1127L, -304L, 500, 92);

        assertFalse(location2.equals(location1));
        assertEquals(1127L, location2.getByteOffset());
        assertEquals(-304L, location2.getCharOffset());
        assertEquals(92, location2.getColumnNr());
        assertEquals(500, location2.getLineNr());
    }

    @Test(timeout = TIMEOUT)
    public void testJsonLocationNAEquality() {
        JsonLocation location1 = JsonLocation.NA;
        JsonLocation location2 = new JsonLocation(location1, 500, 500, 500);

        assertFalse(location1.equals(location2));
        assertEquals(500L, location2.getCharOffset());
        assertEquals(500, location2.getLineNr());
        assertEquals(BYTE_OFFSET_UNKNOWN, location2.getByteOffset());
        assertEquals(500, location2.getColumnNr());
    }

    @Test(timeout = TIMEOUT)
    public void testJsonLocationHashCode() {
        ErrorReportConfiguration errorConfig = new ErrorReportConfiguration(1, 1);
        ContentReference contentRef = ContentReference.construct(false, (Object) null, 2879, 2879, errorConfig);
        JsonLocation location = new JsonLocation(contentRef, 314L, 0, -2189);

        location.hashCode();
        assertEquals(314L, location.getCharOffset());
        assertEquals(BYTE_OFFSET_UNKNOWN, location.getByteOffset());
        assertEquals(-2189, location.getColumnNr());
        assertEquals(0, location.getLineNr());
    }

    @Test(timeout = TIMEOUT)
    public void testJsonLocationToString() {
        Object source = new Object();
        JsonLocation location = new JsonLocation(source, -2650L, 0L, 3981, 2);

        String description = location.toString();
        assertEquals(-2650L, location.getByteOffset());
        assertEquals(0L, location.getCharOffset());
        assertEquals("[Source: (Object); line: 3981, column: 2]", description);
    }

    @Test(timeout = TIMEOUT)
    public void testJsonLocationOffsetDescription() {
        Object source = new Object();
        ContentReference contentRef = JsonLocation._wrap(source);
        JsonLocation location = new JsonLocation(contentRef, -1L, 1L, 0, 0);

        String offsetDescription = location.offsetDescription();
        assertEquals("byte offset: #UNKNOWN", offsetDescription);
        assertEquals(1L, location.getCharOffset());
        assertEquals(0, location.getColumnNr());
        assertEquals(0, location.getLineNr());
    }

    @Test(timeout = TIMEOUT)
    public void testJsonLocationSourceDescription() {
        JsonLocation location = JsonLocation.NA;
        String description = location.sourceDescription();

        assertEquals("UNKNOWN", description);
    }

    @Test(timeout = TIMEOUT)
    public void testJsonLocationAppendOffsetDescription() {
        JsonLocation location = new JsonLocation((Object) null, 0L, 1840L, -201, 93);
        StringBuilder sb = new StringBuilder("_K|FUenM:'d");

        location.appendOffsetDescription(sb);
        assertEquals("_K|FUenM:'dbyte offset: #0", sb.toString());
    }

    @Test(timeout = TIMEOUT)
    public void testJsonLocationEqualsSelf() {
        JsonLocation location = JsonLocation.NA;
        assertTrue(location.equals(location));
    }

    @Test(timeout = TIMEOUT)
    public void testJsonLocationEqualsNull() {
        JsonLocation location = JsonLocation.NA;
        assertFalse(location.equals(null));
    }

    @Test(timeout = TIMEOUT)
    public void testJsonLocationEqualsDifferentType() {
        JsonLocation location = JsonLocation.NA;
        Object other = new Object();
        assertFalse(location.equals(other));
    }

    // Additional tests can be added here following the same pattern
}