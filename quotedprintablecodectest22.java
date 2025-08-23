package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for QuotedPrintableCodec focusing on UTF-8 encoding and decoding of multi-byte characters.
 */
@DisplayName("QuotedPrintableCodec with UTF-8")
class QuotedPrintableCodecTest {

    private QuotedPrintableCodec utf8Codec;

    @BeforeEach
    void setUp() {
        // The default constructor uses UTF-8, which is the focus of these tests.
        utf8Codec = new QuotedPrintableCodec();
    }

    @Test
    @DisplayName("Should encode a Russian string with multi-byte characters correctly")
    void shouldEncodeRussianString() throws EncoderException {
        // Arrange
        // The Russian phrase "Всем_привет" means "Hello_everyone".
        final String russianMessage = "Всем_привет";
        final String expectedEncoded = "=D0=92=D1=81=D0=B5=D0=BC_=D0=BF=D1=80=D0=B8=D0=B2=D0=B5=D1=82";

        // Act
        final String actualEncoded = utf8Codec.encode(russianMessage);

        // Assert
        assertEquals(expectedEncoded, actualEncoded);
    }

    @Test
    @DisplayName("Should encode a Swiss-German string with multi-byte characters correctly")
    void shouldEncodeSwissGermanString() throws EncoderException {
        // Arrange
        // The Swiss-German phrase "Grüezi_zämä" means "Hello_together".
        final String swissGermanMessage = "Grüezi_zämä";
        final String expectedEncoded = "Gr=C3=BCezi_z=C3=A4m=C3=A4";

        // Act
        final String actualEncoded = utf8Codec.encode(swissGermanMessage);

        // Assert
        assertEquals(expectedEncoded, actualEncoded);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "Всем_привет", // Russian for "Hello_everyone"
        "Grüezi_zämä"   // Swiss-German for "Hello_together"
    })
    @DisplayName("Should correctly round-trip multi-byte strings")
    void shouldRoundTripMultiByteStrings(final String originalMessage) throws EncoderException, DecoderException {
        // Act
        final String encodedMessage = utf8Codec.encode(originalMessage);
        final String decodedMessage = utf8Codec.decode(encodedMessage);

        // Assert
        assertEquals(originalMessage, decodedMessage, "Message should be unchanged after encoding and decoding");
    }
}