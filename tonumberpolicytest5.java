package com.google.gson;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;
import com.google.gson.internal.LazilyParsedNumber;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import org.junit.Test;

public class ToNumberPolicyTestTest5 {

    private static JsonReader fromString(String json) {
        return new JsonReader(new StringReader(json));
    }

    private static JsonReader fromStringLenient(String json) {
        JsonReader jsonReader = fromString(json);
        jsonReader.setStrictness(Strictness.LENIENT);
        return jsonReader;
    }

    @Test
    public void testNullsAreNeverExpected() throws IOException {
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> ToNumberPolicy.DOUBLE.readNumber(fromString("null")));
        assertThat(e).hasMessageThat().isEqualTo("Expected a double but was NULL at line 1 column 5 path $\n" + "See https://github.com/google/gson/blob/main/Troubleshooting.md#adapter-not-null-safe");
        e = assertThrows(IllegalStateException.class, () -> ToNumberPolicy.LAZILY_PARSED_NUMBER.readNumber(fromString("null")));
        assertThat(e).hasMessageThat().isEqualTo("Expected a string but was NULL at line 1 column 5 path $\n" + "See https://github.com/google/gson/blob/main/Troubleshooting.md#adapter-not-null-safe");
        e = assertThrows(IllegalStateException.class, () -> ToNumberPolicy.LONG_OR_DOUBLE.readNumber(fromString("null")));
        assertThat(e).hasMessageThat().isEqualTo("Expected a string but was NULL at line 1 column 5 path $\n" + "See https://github.com/google/gson/blob/main/Troubleshooting.md#adapter-not-null-safe");
        e = assertThrows(IllegalStateException.class, () -> ToNumberPolicy.BIG_DECIMAL.readNumber(fromString("null")));
        assertThat(e).hasMessageThat().isEqualTo("Expected a string but was NULL at line 1 column 5 path $\n" + "See https://github.com/google/gson/blob/main/Troubleshooting.md#adapter-not-null-safe");
    }
}
