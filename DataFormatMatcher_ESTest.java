package com.fasterxml.jackson.core.format;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

/**
 * Focused, readable tests for DataFormatMatcher.
 *
 * Organization:
 * - Constructor validation
 * - Simple accessors (hasMatch, getMatch, getMatchStrength, getMatchedFormatName)
 * - Stream handling (getDataStream)
 * - Parser creation (createParserWithMatch)
 */
public class DataFormatMatcherTest {

    // ------------------------------------------------------------
    // Constructor validation
    // ------------------------------------------------------------

    @Test(expected = IllegalArgumentException.class)
    public void constructorRejectsNegativeLength() {
        byte[] buf = new byte[4];
        // Negative length must be rejected
        new DataFormatMatcher(null, buf, 0, -1, new JsonFactory(), MatchStrength.WEAK_MATCH);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorRejectsWhenStartPlusLengthExceedsBuffer() {
        byte[] buf = new byte[2];
        // start(1) + length(2) > buffer.length(2) -> must be rejected
        new DataFormatMatcher(null, buf, 1, 2, new JsonFactory(), MatchStrength.FULL_MATCH);
    }

    @Test(expected = NullPointerException.class)
    public void constructorRejectsNullBuffer() {
        // Null buffer is not allowed
        new DataFormatMatcher(null, null, 0, 0, new JsonFactory(), MatchStrength.INCONCLUSIVE);
    }

    // ------------------------------------------------------------
    // Accessors
    // ------------------------------------------------------------

    @Test
    public void hasMatchAndAccessors_withMatch() {
        JsonFactory factory = new JsonFactory();
        DataFormatMatcher m = new DataFormatMatcher(
                null,
                new byte[0],
                0,
                0,
                factory,
                MatchStrength.SOLID_MATCH
        );

        assertTrue("Expected hasMatch() to be true when factory is provided", m.hasMatch());
        assertSame("getMatch() should return the same factory instance", factory, m.getMatch());
        assertEquals(MatchStrength.SOLID_MATCH, m.getMatchStrength());

        // For Jackson's JsonFactory the format name should be "JSON"
        assertEquals("JSON", m.getMatchedFormatName());
    }

    @Test
    public void hasMatchAndAccessors_withoutMatch() {
        DataFormatMatcher m = new DataFormatMatcher(
                null,
                new byte[0],
                0,
                0,
                null,
                MatchStrength.NO_MATCH
        );

        assertFalse("Expected hasMatch() to be false when factory is null", m.hasMatch());
        assertNull("getMatch() should be null when there is no match", m.getMatch());
        // Implementation returns the provided strength (or INCONCLUSIVE if null)
        assertEquals(MatchStrength.NO_MATCH, m.getMatchStrength());
        assertNull("No match -> no matched format name", m.getMatchedFormatName());
    }

    // ------------------------------------------------------------
    // Stream handling
    // ------------------------------------------------------------

    @Test
    public void getDataStream_returnsBufferedWhenNoOriginalStream() throws Exception {
        String json = "[1,2]";
        byte[] buf = json.getBytes(StandardCharsets.UTF_8);

        DataFormatMatcher m = new DataFormatMatcher(
                null,
                buf,
                0,
                buf.length,
                null,
                MatchStrength.INCONCLUSIVE
        );

        String roundTripped = readAllString(m.getDataStream());
        assertEquals(json, roundTripped);
    }

    @Test
    public void getDataStream_mergesBufferedAndOriginal() throws Exception {
        byte[] buffered = "{".getBytes(StandardCharsets.UTF_8);
        InputStream original = new ByteArrayInputStream("}".getBytes(StandardCharsets.UTF_8));

        DataFormatMatcher m = new DataFormatMatcher(
                original,
                buffered,
                0,
                buffered.length,
                null,
                MatchStrength.INCONCLUSIVE
        );

        String roundTripped = readAllString(m.getDataStream());
        assertEquals("{}", roundTripped);
    }

    // ------------------------------------------------------------
    // Parser creation
    // ------------------------------------------------------------

    @Test
    public void createParserWithMatch_parsesValidJsonFromBufferedBytes() throws Exception {
        String json = "{\"x\":1}";
        byte[] buf = json.getBytes(StandardCharsets.UTF_8);

        DataFormatMatcher m = new DataFormatMatcher(
                null,
                buf,
                0,
                buf.length,
                new JsonFactory(),
                MatchStrength.SOLID_MATCH
        );

        JsonParser p = m.createParserWithMatch();
        assertNotNull("Parser should be created when there is a match", p);

        assertEquals(JsonToken.START_OBJECT, p.nextToken());
        assertEquals(JsonToken.FIELD_NAME, p.nextToken());
        assertEquals("x", p.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, p.nextToken());
        assertEquals(1, p.getIntValue());
        assertEquals(JsonToken.END_OBJECT, p.nextToken());
        assertNull(p.nextToken());
        p.close();
    }

    @Test
    public void createParserWithMatch_returnsNullWhenNoMatch() throws Exception {
        String json = "{\"x\":1}";
        byte[] buf = json.getBytes(StandardCharsets.UTF_8);

        DataFormatMatcher m = new DataFormatMatcher(
                null,
                buf,
                0,
                buf.length,
                null, // no factory -> no match
                MatchStrength.NO_MATCH
        );

        assertNull("Expected null parser when there is no match", m.createParserWithMatch());
    }

    // ------------------------------------------------------------
    // Helpers
    // ------------------------------------------------------------

    private static String readAllString(InputStream in) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] tmp = new byte[64];
        int r;
        while ((r = in.read(tmp)) != -1) {
            out.write(tmp, 0, r);
        }
        return out.toString(StandardCharsets.UTF_8.name());
    }
}