package com.google.gson;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import org.junit.Test;

/**
 * Tests for {@link ToNumberPolicy#DOUBLE}.
 */
public class ToNumberPolicyDoubleTest {

    private final ToNumberStrategy doublePolicy = ToNumberPolicy.DOUBLE;

    private static JsonReader createJsonReader(String json) {
        return new JsonReader(new StringReader(json));
    }

    @Test
    public void readNumber_parsesValidDouble() throws IOException {
        JsonReader jsonReader = createJsonReader("10.1");
        Number result = doublePolicy.readNumber(jsonReader);
        assertThat(result).isEqualTo(10.1d);
    }

    @Test
    public void readNumber_handlesPrecisionLossForLargeDecimal() throws IOException {
        // This value has more precision than a standard double can represent.
        String longDecimal = "3.141592653589793238462643383279";
        JsonReader jsonReader = createJsonReader(longDecimal);

        Number result = doublePolicy.readNumber(jsonReader);

        // The policy parses it as a double, which results in precision loss.
        assertThat(result).isEqualTo(3.141592653589793D);
        // Verify it's not a BigDecimal, which would have retained precision.
        assertThat(result).isNotInstanceOf(BigDecimal.class);
    }

    @Test
    public void readNumber_throwsMalformedJsonForInfiniteValue() {
        // 1e400 is larger than Double.MAX_VALUE and is parsed as infinity by Java,
        // but the JSON specification forbids infinite values.
        JsonReader jsonReader = createJsonReader("1e400");

        MalformedJsonException e = assertThrows(MalformedJsonException.class, () -> {
            doublePolicy.readNumber(jsonReader);
        });

        // Check for the core reason in the message, without being brittle about the exact format.
        assertThat(e).hasMessageThat().contains("JSON forbids NaN and infinities");
    }

    @Test
    public void readNumber_throwsNumberFormatForJsonString() {
        // The policy expects a JSON number token, not a JSON string token.
        JsonReader jsonReader = createJsonReader("\"not-a-number\"");

        assertThrows(NumberFormatException.class, () -> {
            doublePolicy.readNumber(jsonReader);
        });
    }
}