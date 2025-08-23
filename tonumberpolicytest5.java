package com.google.gson;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link ToNumberPolicy}.
 * This class name has been corrected from the original 'ToNumberPolicyTestTest5'.
 */
public class ToNumberPolicyTest {

    private static JsonReader fromString(String json) {
        return new JsonReader(new StringReader(json));
    }

    /**
     * Provides the different ToNumberPolicy strategies and the expected type name
     * for their respective error messages when encountering a JSON null.
     */
    private static Stream<Arguments> policiesAndExpectedErrorTypes() {
        return Stream.of(
            Arguments.of(ToNumberPolicy.DOUBLE, "a double"),
            Arguments.of(ToNumberPolicy.LAZILY_PARSED_NUMBER, "a string"),
            Arguments.of(ToNumberPolicy.LONG_OR_DOUBLE, "a string"),
            Arguments.of(ToNumberPolicy.BIG_DECIMAL, "a string")
        );
    }

    @ParameterizedTest
    @MethodSource("policiesAndExpectedErrorTypes")
    void readNumber_whenJsonIsNull_throwsIllegalStateException(ToNumberPolicy policy, String expectedTypeName) {
        // Arrange
        JsonReader jsonReader = fromString("null");
        String expectedMessage = "Expected " + expectedTypeName + " but was NULL at line 1 column 5 path $\n"
            + "See https://github.com/google/gson/blob/main/Troubleshooting.md#adapter-not-null-safe";

        // Act & Assert
        IllegalStateException e = assertThrows(
            IllegalStateException.class,
            () -> policy.readNumber(jsonReader)
        );
        assertThat(e).hasMessageThat().isEqualTo(expectedMessage);
    }
}