package com.google.gson;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.StringReader;
import org.junit.Test;

/**
 * Tests for {@link ToNumberPolicy#LONG_OR_DOUBLE}.
 */
public class LongOrDoubleToNumberPolicyTest {

    private final ToNumberStrategy strategy = ToNumberPolicy.LONG_OR_DOUBLE;

    private static JsonReader fromString(String json) {
        return new JsonReader(new StringReader(json));
    }

    private static JsonReader fromStringLenient(String json) {
        JsonReader jsonReader = fromString(json);
        jsonReader.setStrictness(Strictness.LENIENT);
        return jsonReader;
    }

    @Test
    public void parsesIntegerAsLong() throws IOException {
        Number result = strategy.readNumber(fromString("10"));
        assertThat(result).isEqualTo(10L);
    }

    @Test
    public void parsesDecimalAsDouble() throws IOException {
        Number result = strategy.readNumber(fromString("10.1"));
        assertThat(result).isEqualTo(10.1);
    }

    @Test
    public void parsesHighPrecisionDecimalAsDoubleWithPrecisionLoss() throws IOException {
        String highPrecisionDecimal = "3.141592653589793238462643383279";
        Number result = strategy.readNumber(fromString(highPrecisionDecimal));
        // The policy uses Double.parseDouble(), which has limited precision.
        assertThat(result).isEqualTo(3.141592653589793D);
    }

    @Test
    public void throwsJsonParseExceptionForNonNumericString() {
        JsonParseException e = assertThrows(JsonParseException.class,
            () -> strategy.readNumber(fromString("\"not-a-number\"")));
        assertThat(e).hasMessageThat().isEqualTo("Cannot parse not-a-number; at path $");
    }

    @Test
    public void strictMode_throwsOnLargeNumberExceedingDouble() {
        // "1e400" is parsed as Double.POSITIVE_INFINITY
        MalformedJsonException e = assertThrows(MalformedJsonException.class,
            () -> strategy.readNumber(fromString("1e400")));
        assertThat(e).hasMessageThat()
            .isEqualTo("JSON forbids NaN and infinities: Infinity; at path $");
    }

    @Test
    public void strictMode_throwsOnNaN() {
        MalformedJsonException e = assertThrows(MalformedJsonException.class,
            () -> strategy.readNumber(fromString("NaN")));
        assertThat(e).hasMessageThat().startsWith("Use JsonReader.setStrictness(Strictness.LENIENT) to accept malformed JSON");
    }

    @Test
    public void strictMode_throwsOnInfinity() {
        MalformedJsonException e = assertThrows(MalformedJsonException.class,
            () -> strategy.readNumber(fromString("Infinity")));
        assertThat(e).hasMessageThat().startsWith("Use JsonReader.setStrictness(Strictness.LENIENT) to accept malformed JSON");
    }

    @Test
    public void strictMode_throwsOnNegativeInfinity() {
        MalformedJsonException e = assertThrows(MalformedJsonException.class,
            () -> strategy.readNumber(fromString("-Infinity")));
        assertThat(e).hasMessageThat().startsWith("Use JsonReader.setStrictness(Strictness.LENIENT) to accept malformed JSON");
    }

    @Test
    public void lenientMode_parsesNaN() throws IOException {
        Number result = strategy.readNumber(fromStringLenient("NaN"));
        assertThat(result.doubleValue()).isNaN();
    }

    @Test
    public void lenientMode_parsesInfinity() throws IOException {
        Number result = strategy.readNumber(fromStringLenient("Infinity"));
        assertThat(result).isEqualTo(Double.POSITIVE_INFINITY);
    }

    @Test
    public void lenientMode_parsesNegativeInfinity() throws IOException {
        Number result = strategy.readNumber(fromStringLenient("-Infinity"));
        assertThat(result).isEqualTo(Double.NEGATIVE_INFINITY);
    }
}