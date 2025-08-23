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

public class ToNumberPolicyTestTest3 {

    private static JsonReader fromString(String json) {
        return new JsonReader(new StringReader(json));
    }

    private static JsonReader fromStringLenient(String json) {
        JsonReader jsonReader = fromString(json);
        jsonReader.setStrictness(Strictness.LENIENT);
        return jsonReader;
    }

    @Test
    public void testLongOrDouble() throws IOException {
        ToNumberStrategy strategy = ToNumberPolicy.LONG_OR_DOUBLE;
        assertThat(strategy.readNumber(fromString("10"))).isEqualTo(10L);
        assertThat(strategy.readNumber(fromString("10.1"))).isEqualTo(10.1);
        assertThat(strategy.readNumber(fromString("3.141592653589793238462643383279"))).isEqualTo(3.141592653589793D);
        Exception e = assertThrows(MalformedJsonException.class, () -> strategy.readNumber(fromString("1e400")));
        assertThat(e).hasMessageThat().isEqualTo("JSON forbids NaN and infinities: Infinity; at path $");
        e = assertThrows(JsonParseException.class, () -> strategy.readNumber(fromString("\"not-a-number\"")));
        assertThat(e).hasMessageThat().isEqualTo("Cannot parse not-a-number; at path $");
        assertThat(strategy.readNumber(fromStringLenient("NaN"))).isEqualTo(Double.NaN);
        assertThat(strategy.readNumber(fromStringLenient("Infinity"))).isEqualTo(Double.POSITIVE_INFINITY);
        assertThat(strategy.readNumber(fromStringLenient("-Infinity"))).isEqualTo(Double.NEGATIVE_INFINITY);
        e = assertThrows(MalformedJsonException.class, () -> strategy.readNumber(fromString("NaN")));
        assertThat(e).hasMessageThat().isEqualTo("Use JsonReader.setStrictness(Strictness.LENIENT) to accept malformed JSON at line 1" + " column 1 path $\n" + "See https://github.com/google/gson/blob/main/Troubleshooting.md#malformed-json");
        e = assertThrows(MalformedJsonException.class, () -> strategy.readNumber(fromString("Infinity")));
        assertThat(e).hasMessageThat().isEqualTo("Use JsonReader.setStrictness(Strictness.LENIENT) to accept malformed JSON at line 1" + " column 1 path $\n" + "See https://github.com/google/gson/blob/main/Troubleshooting.md#malformed-json");
        e = assertThrows(MalformedJsonException.class, () -> strategy.readNumber(fromString("-Infinity")));
        assertThat(e).hasMessageThat().isEqualTo("Use JsonReader.setStrictness(Strictness.LENIENT) to accept malformed JSON at line 1" + " column 1 path $\n" + "See https://github.com/google/gson/blob/main/Troubleshooting.md#malformed-json");
    }
}
