package com.google.gson;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringReader;
import org.junit.Test;

public class TypeAdapterTestTest5 {

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

    // whether that behavior is actually desired
    @Test
    public void testFromJson_Reader_TrailingData() throws IOException {
        assertThat(adapter.fromJson(new StringReader("\"a\"1"))).isEqualTo("a");
    }
}