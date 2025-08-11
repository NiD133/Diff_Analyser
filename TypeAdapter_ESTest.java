package com.google.gson;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.Test;

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.Assert.*;

/**
 * Readable tests for TypeAdapter and its nullSafe() wrapper.
 *
 * These tests focus on:
 * - Behavior of the nullSafe() wrapper when reading/writing nulls
 * - Convenience methods toJson / fromJson (String/Reader/Writer)
 * - Simple round-trips via JSON strings and trees
 * - Basic error handling for malformed or empty input
 * - Guarantee that FutureTypeAdapter fails before its delegate is set
 */
public class TypeAdapterReadableTest {

    /**
     * Minimal String adapter which does NOT handle nulls itself.
     * We wrap it with nullSafe() in tests to verify null handling is added.
     */
    private static final class StringAdapter extends TypeAdapter<String> {
        @Override
        public void write(JsonWriter out, String value) throws IOException {
            // No null handling here on purpose; nullSafe() will add it.
            out.value(value);
        }

        @Override
        public String read(JsonReader in) throws IOException {
            // No null handling here on purpose; nullSafe() will add it.
            return in.nextString();
        }
    }

    private TypeAdapter<String> newNullSafeStringAdapter() {
        return new StringAdapter().nullSafe();
    }

    @Test
    public void nullSafe_writesJsonNull_forNullValues() throws IOException {
        TypeAdapter<String> adapter = newNullSafeStringAdapter();

        // toJson(T)
        assertEquals("null", adapter.toJson(null));

        // toJson(Writer, T)
        StringWriter out = new StringWriter();
        adapter.toJson(out, null);
        assertEquals("null", out.toString());
    }

    @Test
    public void nullSafe_readsJsonNull_asJavaNull() throws IOException {
        TypeAdapter<String> adapter = newNullSafeStringAdapter();

        assertNull(adapter.fromJson("null"));

        StringReader in = new StringReader("null");
        assertNull(adapter.fromJson(in));
    }

    @Test
    public void nullSafe_jsonTree_conversionsOfNull() {
        TypeAdapter<String> adapter = newNullSafeStringAdapter();

        JsonElement tree = adapter.toJsonTree(null);
        assertTrue(tree.isJsonNull());

        assertNull(adapter.fromJsonTree(JsonNull.INSTANCE));
    }

    @Test
    public void nullSafe_isIdempotent() {
        TypeAdapter<String> adapter = new StringAdapter().nullSafe();
        assertSame(adapter, adapter.nullSafe());
    }

    @Test
    public void convenience_toJson_throwsIfWriterIsNull() {
        TypeAdapter<String> adapter = newNullSafeStringAdapter();

        NullPointerException npe = assertThrows(
                NullPointerException.class,
                () -> adapter.toJson((Writer) null, "x")
        );
        assertEquals("out == null", npe.getMessage());
    }

    @Test
    public void convenience_fromJson_throwsIfReaderIsNull() {
        TypeAdapter<String> adapter = newNullSafeStringAdapter();

        NullPointerException npe = assertThrows(
                NullPointerException.class,
                () -> adapter.fromJson((Reader) null)
        );
        assertEquals("in == null", npe.getMessage());
    }

    @Test
    public void reading_emptyInput_throwsEOFException() {
        TypeAdapter<String> adapter = newNullSafeStringAdapter();

        EOFException ex = assertThrows(
                EOFException.class,
                () -> adapter.fromJson("")
        );
        assertNotNull(ex.getMessage()); // Keep assertion resilient across versions
    }

    @Test
    public void reading_malformedJson_throwsIOException() {
        TypeAdapter<String> adapter = newNullSafeStringAdapter();

        IOException ex = assertThrows(
                IOException.class,
                () -> adapter.fromJson("not json")
        );
        assertNotNull(ex.getMessage());
    }

    @Test
    public void reading_fromClosedReader_throwsIOException() throws IOException {
        TypeAdapter<String> adapter = newNullSafeStringAdapter();
        StringReader in = new StringReader("");
        in.close();

        IOException ex = assertThrows(
                IOException.class,
                () -> adapter.fromJson(in)
        );
        // Thrown by StringReader
        assertEquals("Stream closed", ex.getMessage());
    }

    @Test
    public void roundTrip_string_nonNull() throws IOException {
        TypeAdapter<String> adapter = newNullSafeStringAdapter();

        String json = adapter.toJson("hello");
        assertEquals("\"hello\"", json);
        assertEquals("hello", adapter.fromJson(json));
    }

    @Test
    public void jsonTree_roundTrip_string_nonNull() {
        TypeAdapter<String> adapter = newNullSafeStringAdapter();

        JsonElement tree = adapter.toJsonTree("hello");
        assertTrue(tree.isJsonPrimitive());
        assertEquals("hello", tree.getAsString());

        assertEquals("hello", adapter.fromJsonTree(tree));
    }

    @Test
    public void futureTypeAdapter_failsUntilDelegateIsSet() {
        Gson.FutureTypeAdapter<Object> future = new Gson.FutureTypeAdapter<>();

        IllegalStateException ise = assertThrows(
                IllegalStateException.class,
                () -> future.toJson("x")
        );
        assertTrue(ise.getMessage().contains("Adapter for type with cyclic dependency"));
    }
}