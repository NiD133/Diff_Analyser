package com.google.gson;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringReader;
import org.junit.Test;

public class TypeAdapterTestTest4 {

    private static final TypeAdapter<String> assertionErrorAdapter = new TypeAdapter<>() {

        @Override
        public void write(JsonWriter out, String value) {
            throw new AssertionError("unexpected call");
        }

        @Override
        public String read(JsonReader in) {
            throw new AssertionError("unexpected call");
        }

        @Override
        public String toString() {
            return "assertionErrorAdapter";
        }
    };

    private static final TypeAdapter<String> adapter = new TypeAdapter<>() {

        @Override
        public void write(JsonWriter out, String value) throws IOException {
            out.value(value);
        }

        @Override
        public String read(JsonReader in) throws IOException {
            return in.nextString();
        }
    };

    /**
     * Tests behavior when {@link TypeAdapter#write(JsonWriter, Object)} manually throws {@link
     * IOException} which is not caused by writer usage.
     */
    @Test
    public void testToJson_ThrowingIOException() {
        IOException exception = new IOException("test");
        TypeAdapter<Integer> adapter = new TypeAdapter<>() {

            @Override
            public void write(JsonWriter out, Integer value) throws IOException {
                throw exception;
            }

            @Override
            public Integer read(JsonReader in) {
                throw new AssertionError("not needed by this test");
            }
        };
        JsonIOException e = assertThrows(JsonIOException.class, () -> adapter.toJson(1));
        assertThat(e).hasCauseThat().isEqualTo(exception);
        e = assertThrows(JsonIOException.class, () -> adapter.toJsonTree(1));
        assertThat(e).hasCauseThat().isEqualTo(exception);
    }
}
