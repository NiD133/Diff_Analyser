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

public class ToNumberPolicyTestTest2 {

    private static JsonReader fromString(String json) {
        return new JsonReader(new StringReader(json));
    }

    private static JsonReader fromStringLenient(String json) {
        JsonReader jsonReader = fromString(json);
        jsonReader.setStrictness(Strictness.LENIENT);
        return jsonReader;
    }

    @Test
    public void testLazilyParsedNumber() throws IOException {
        ToNumberStrategy strategy = ToNumberPolicy.LAZILY_PARSED_NUMBER;
        assertThat(strategy.readNumber(fromString("10.1"))).isEqualTo(new LazilyParsedNumber("10.1"));
        assertThat(strategy.readNumber(fromString("3.141592653589793238462643383279"))).isEqualTo(new LazilyParsedNumber("3.141592653589793238462643383279"));
        assertThat(strategy.readNumber(fromString("1e400"))).isEqualTo(new LazilyParsedNumber("1e400"));
    }
}
