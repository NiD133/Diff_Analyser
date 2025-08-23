package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import org.apache.commons.codec.DecoderException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the abstract {@link RFC1522Codec} class, focusing on the
 * validation logic within the decodeText method.
 */
public class RFC1522CodecTest {

    /**
     * A minimal concrete implementation of RFC1522Codec for testing purposes.
     * It uses a fixed encoding character 'T' (for Test).
     */
    static class RFC1522TestCodec extends RFC1522Codec {

        RFC1522TestCodec() {
            super(StandardCharsets.UTF_8);
        }

        @Override
        protected byte[] doDecoding(final byte[] bytes) {
            // No-op for this test
            return bytes;
        }

        @Override
        protected byte[] doEncoding(final byte[] bytes) {
            // No-op for this test
            return bytes;
        }

        @Override
        protected String getEncoding() {
            // 'T' for Test, to check encoding validation.
            return "T";
        }
    }

    /**
     * Provides a stream of malformed "encoded-word" strings and a description
     * of why each is invalid.
     *
     * @return A stream of arguments for the parameterized test.
     */
    private static Stream<Arguments> provideMalformedEncodedStrings() {
        return Stream.of(
            // Cases with invalid structure or syntax
            Arguments.of("whatever", "does not start with prefix '=?'"),
            Arguments.of("=?UTF-8?T?stuff", "does not end with postfix '?='"),
            Arguments.of("=?", "is too short to be valid"),
            Arguments.of("?=", "is too short to be valid"),
            Arguments.of("==", "is too short to be valid"),
            Arguments.of("=??=", "is missing charset and encoding"),

            // Cases with missing components
            Arguments.of("=?stuff?=", "is missing encoding and text sections"),
            Arguments.of("=?UTF-8??=", "is missing encoding and text sections"),
            Arguments.of("=??T?stuff?=", "is missing charset section"),
            Arguments.of("=?UTF-8??stuff?=", "is missing encoding section"),

            // Cases with an invalid encoding character
            Arguments.of("=?UTF-8?stuff?=", "has an invalid multi-character encoding 'stuff'"),
            Arguments.of("=?UTF-8?W?stuff?=", "has encoding 'W' which does not match codec's expected 'T'")
        );
    }

    @ParameterizedTest(name = "Input \"{0}\" should be rejected because it {1}")
    @MethodSource("provideMalformedEncodedStrings")
    void decodeTextShouldThrowDecoderExceptionForMalformedInput(final String malformedText, final String reason) {
        final RFC1522TestCodec codec = new RFC1522TestCodec();
        assertThrows(DecoderException.class, () -> codec.decodeText(malformedText));
    }
}