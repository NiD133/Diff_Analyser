package com.fasterxml.jackson.core;

import com.fasterxml.jackson.core.io.ContentReference;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Readable and focused tests for JsonLocation.
 *
 * Intent:
 * - Exercise main construction styles (bytes+chars vs chars-only)
 * - Verify getters, NA constant, equals/hashCode
 * - Verify formatting helpers: toString, sourceDescription, offsetDescription, appendOffsetDescription
 * - Avoid EvoSuite-specific scaffolding and redundant assertions
 */
public class JsonLocationReadableTest {

    // ---------------------------------------------------------------------
    // Construction and basic getters
    // ---------------------------------------------------------------------

    @Test
    public void charsOnlyConstructor_setsByteOffsetToUnknown() {
        ContentReference ref = ContentReference.redacted();
        JsonLocation loc = new JsonLocation(ref, 314L, 10, 20);

        assertSame(ref, loc.contentReference());
        assertEquals(314L, loc.getCharOffset());
        assertEquals(-1L, loc.getByteOffset());
        assertEquals(10, loc.getLineNr());
        assertEquals(20, loc.getColumnNr());
    }

    @Test
    public void bytesAndCharsConstructor_populatesAllFields() {
        ContentReference ref = ContentReference.redacted();
        JsonLocation loc = new JsonLocation(ref, 1852L, 400L, 7, 13);

        assertSame(ref, loc.contentReference());
        assertEquals(1852L, loc.getByteOffset());
        assertEquals(400L, loc.getCharOffset());
        assertEquals(7, loc.getLineNr());
        assertEquals(13, loc.getColumnNr());
    }

    @Test
    @SuppressWarnings("deprecation")
    public void deprecatedObjectCtor_wrapsSource_andPopulatesFields() {
        Object src = new Object();
        JsonLocation loc = new JsonLocation(src, 0L, 0L, 1, 2);

        assertNotNull(loc.contentReference());
        assertSame(src, loc.getSourceRef());
        assertEquals(0L, loc.getByteOffset());
        assertEquals(0L, loc.getCharOffset());
        assertEquals(1, loc.getLineNr());
        assertEquals(2, loc.getColumnNr());
    }

    // ---------------------------------------------------------------------
    // NA constant
    // ---------------------------------------------------------------------

    @Test
    public void na_hasAllUnknowns_andIsReflexive() {
        JsonLocation na = JsonLocation.NA;

        assertEquals(-1L, na.getByteOffset());
        assertEquals(-1L, na.getCharOffset());
        assertEquals(-1, na.getLineNr());
        assertEquals(-1, na.getColumnNr());
        assertTrue(na.equals(na));
        assertFalse(na.equals(null));
        assertFalse(na.equals(new Object()));
    }

    // ---------------------------------------------------------------------
    // Equality and hashCode
    // ---------------------------------------------------------------------

    @Test
    public void equalsAndHashCode_identicalValuesAreEqual() {
        ContentReference ref = ContentReference.redacted();

        JsonLocation a = new JsonLocation(ref, 500L, 500L, 5, 6);
        JsonLocation b = new JsonLocation(ref, 500L, 500L, 5, 6);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_differentOffsetsNotEqual() {
        ContentReference ref = ContentReference.redacted();

        JsonLocation a = new JsonLocation(ref, 500L, 500L, 5, 6);
        JsonLocation b = new JsonLocation(ref, 344L, 500L, 5, 6);

        assertNotEquals(a, b);
    }

    @Test
    public void equals_nullOrDifferentTypeIsFalse() {
        ContentReference ref = ContentReference.redacted();
        JsonLocation a = new JsonLocation(ref, 1L, 1L, 1, 1);

        assertFalse(a.equals(null));
        assertFalse(a.equals("not a JsonLocation"));
    }

    // ---------------------------------------------------------------------
    // toString and source description
    // ---------------------------------------------------------------------

    @Test
    @SuppressWarnings("deprecation")
    public void toString_prefersLineAndColumn_whenAvailable() {
        Object src = new Object();
        JsonLocation loc = new JsonLocation(src, -2650L, 0L, 3981, 2);

        assertEquals("[Source: (Object); line: 3981, column: 2]", loc.toString());
    }

    @Test
    @SuppressWarnings("deprecation")
    public void toString_showsOnlyLine_whenColumnIsZero() {
        Object src = new Object();
        JsonLocation loc = new JsonLocation(src, 3981L, 2L, 2, 0);

        assertEquals("[Source: (Object); line: 2]", loc.toString());
    }

    @Test
    @SuppressWarnings("deprecation")
    public void toString_usesUnknownSourceAndByteOffset_whenLineIsUnknown() {
        // Negative line/column indicate "unknown" in textual output
        JsonLocation loc = new JsonLocation((Object) null, 62L, 500L, -2898, -26);

        assertEquals("[Source: UNKNOWN; byte offset: #62]", loc.toString());
    }

    @Test
    public void sourceDescription_ofNA_isUNKNOWN() {
        assertEquals("UNKNOWN", JsonLocation.NA.sourceDescription());
    }

    // ---------------------------------------------------------------------
    // Offset description helpers
    // ---------------------------------------------------------------------

    @Test
    @SuppressWarnings("deprecation")
    public void offsetDescription_fallsBackToUnknownByteOffset_whenLineInfoUnavailable() {
        // With line=0, column=0, offsetDescription prefers byte offset if known, otherwise UNKNOWN
        JsonLocation loc = new JsonLocation(new Object(), -1L, 1L, 0, 0);

        assertEquals("byte offset: #UNKNOWN", loc.offsetDescription());
    }

    @Test
    public void appendOffsetDescription_withKnownLineAndUnknownColumn() {
        ContentReference ref = ContentReference.rawReference(true, null);
        JsonLocation loc = new JsonLocation(ref, -3036L, 500, -1291);

        StringBuilder sb = new StringBuilder();
        loc.appendOffsetDescription(sb);

        assertEquals("line: 500, column: UNKNOWN", sb.toString());
    }

    @Test
    public void appendOffsetDescription_withUnknownLineAndKnownColumn() {
        ContentReference ref = ContentReference.rawReference(true, new Object());
        JsonLocation loc = new JsonLocation(ref, -4198L, -4198L, -1143, 0);

        StringBuilder sb = new StringBuilder(32);
        loc.appendOffsetDescription(sb);

        assertEquals("line: UNKNOWN, column: 0", sb.toString());
    }

    @Test
    public void appendOffsetDescription_withZeroLineAndColumnPrintedAsIs() {
        ContentReference ref = ContentReference.rawReference(true, new Object());
        JsonLocation loc = new JsonLocation(ref, 0L, 0, 0);

        StringBuilder sb = new StringBuilder();
        loc.appendOffsetDescription(sb);

        assertEquals("line: 0, column: 0", sb.toString());
    }

    // ---------------------------------------------------------------------
    // Content reference exposure
    // ---------------------------------------------------------------------

    @Test
    public void contentReference_isReturnedAsProvided() {
        ContentReference ref = ContentReference.redacted();
        JsonLocation loc = new JsonLocation(ref, -578L, 500, 500);

        assertSame(ref, loc.contentReference());
        assertEquals(-578L, loc.getCharOffset());
        assertEquals(-1L, loc.getByteOffset());
        assertEquals(500, loc.getLineNr());
        assertEquals(500, loc.getColumnNr());
    }

    @Test
    @SuppressWarnings("deprecation")
    public void getSourceRef_returnsOriginalObjectForDeprecatedCtor() {
        Object src = new Object();
        JsonLocation loc = new JsonLocation(src, 0L, 0L, 0, 0);

        assertSame(src, loc.getSourceRef());
    }
}