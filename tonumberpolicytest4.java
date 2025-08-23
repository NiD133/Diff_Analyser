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

public class ToNumberPolicyTestTest4 {

    private static JsonReader fromString(String json) {
        return new JsonReader(new StringReader(json));
    }

    private static JsonReader fromStringLenient(String json) {
        JsonReader jsonReader = fromString(json);
        jsonReader.setStrictness(Strictness.LENIENT);
        return jsonReader;
    }

    @Test
    public void testBigDecimal() throws IOException {
        ToNumberStrategy strategy = ToNumberPolicy.BIG_DECIMAL;
        assertThat(strategy.readNumber(fromString("10.1"))).isEqualTo(new BigDecimal("10.1"));
        assertThat(strategy.readNumber(fromString("3.141592653589793238462643383279"))).isEqualTo(new BigDecimal("3.141592653589793238462643383279"));
        assertThat(strategy.readNumber(fromString("1e400"))).isEqualTo(new BigDecimal("1e400"));
        JsonParseException e = assertThrows(JsonParseException.class, () -> strategy.readNumber(fromString("\"not-a-number\"")));
        assertThat(e).hasMessageThat().isEqualTo("Cannot parse not-a-number; at path $");
    }
}
