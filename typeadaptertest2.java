package com.google.gson;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringReader;
import org.junit.Test;

public class TypeAdapterTestTest2 {

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

    @Test
    public void testNullSafe_ReturningSameInstanceOnceNullSafe() {
        TypeAdapter<?> nullSafeAdapter = assertionErrorAdapter.nullSafe();
        assertThat(nullSafeAdapter.nullSafe()).isSameInstanceAs(nullSafeAdapter);
        assertThat(nullSafeAdapter.nullSafe().nullSafe()).isSameInstanceAs(nullSafeAdapter);
        assertThat(nullSafeAdapter.nullSafe().nullSafe().nullSafe()).isSameInstanceAs(nullSafeAdapter);
    }
}
