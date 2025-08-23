package org.apache.commons.compress.harmony.pack200;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the {@link CodecEncoding} utility class, which handles the mapping between
 * specifier values and concrete {@link Codec} implementations.
 */
@DisplayName("Tests for CodecEncoding utility class")
public class CodecEncodingTest {

    // According to the Pack200 specification, a value of 116 indicates
    // that the encoding is not a canonical one, but is instead specified
    // by the following bytes in the stream.
    private static final int NON_CANONICAL_SPECIFIER = 116;

    // A specifier of 0 indicates that the default codec should be used.
    private static final int DEFAULT_SPECIFIER = 0;

    /**
     * Provides test cases for non-canonical (arbitrary) codecs.
     * @return A stream of arguments: expected codec string and its byte representation.
     */
    static Stream<Arguments> provideNonCanonicalCodecBytes() {
        return Stream.of(
            Arguments.of("(1,256)", new byte[]{0x00, (byte) 0xFF}),
            Arguments.of("(5,128,2,1)", new byte[]{0x25, (byte) 0x7F}),
            Arguments.of("(2,128,1,1)", new byte[]{0x0B, (byte) 0x7F})
        );
    }

    /**
     * Provides the canonical encodings as defined in the Pack200 specification (JSR 200, Table 5-3).
     * @return A stream of arguments: the specifier (1-115) and its expected string representation.
     */
    static Stream<Arguments> provideCanonicalCodecEncodings() {
        return Stream.of(
            // (B,H) and (B,H,S,D) encodings for B=1..4, H=256
            Arguments.of(1, "(1,256)"), Arguments.of(2, "(1,256,1)"), Arguments.of(3, "(1,256,0,1)"), Arguments.of(4, "(1,256,1,1)"),
            Arguments.of(5, "(2,256)"), Arguments.of(6, "(2,256,1)"), Arguments.of(7, "(2,256,0,1)"), Arguments.of(8, "(2,256,1,1)"),
            Arguments.of(9, "(3,256)"), Arguments.of(10, "(3,256,1)"), Arguments.of(11, "(3,256,0,1)"), Arguments.of(12, "(3,256,1,1)"),
            Arguments.of(13, "(4,256)"), Arguments.of(14, "(4,256,1)"), Arguments.of(15, "(4,256,0,1)"), Arguments.of(16, "(4,256,1,1)"),
            // (5,H,S) encodings
            Arguments.of(17, "(5,4)"), Arguments.of(18, "(5,4,1)"), Arguments.of(19, "(5,4,2)"),
            Arguments.of(20, "(5,16)"), Arguments.of(21, "(5,16,1)"), Arguments.of(22, "(5,16,2)"),
            Arguments.of(23, "(5,32)"), Arguments.of(24, "(5,32,1)"), Arguments.of(25, "(5,32,2)"),
            Arguments.of(26, "(5,64)"), Arguments.of(27, "(5,64,1)"), Arguments.of(28, "(5,64,2)"),
            Arguments.of(29, "(5,128)"), Arguments.of(30, "(5,128,1)"), Arguments.of(31, "(5,128,2)"),
            // (5,H,S,1) encodings
            Arguments.of(32, "(5,4,0,1)"), Arguments.of(33, "(5,4,1,1)"), Arguments.of(34, "(5,4,2,1)"),
            Arguments.of(35, "(5,16,0,1)"), Arguments.of(36, "(5,16,1,1)"), Arguments.of(37, "(5,16,2,1)"),
            Arguments.of(38, "(5,32,0,1)"), Arguments.of(39, "(5,32,1,1)"), Arguments.of(40, "(5,32,2,1)"),
            Arguments.of(41, "(5,64,0,1)"), Arguments.of(42, "(5,64,1,1)"), Arguments.of(43, "(5,64,2,1)"),
            Arguments.of(44, "(5,128,0,1)"), Arguments.of(45, "(5,128,1,1)"), Arguments.of(46, "(5,128,2,1)"),
            // (B,H) and (B,H,S,D) encodings for B=2..4 with varying H
            Arguments.of(47, "(2,192)"), Arguments.of(48, "(2,224)"), Arguments.of(49, "(2,240)"), Arguments.of(50, "(2,248)"), Arguments.of(51, "(2,252)"),
            Arguments.of(52, "(2,8,0,1)"), Arguments.of(53, "(2,8,1,1)"), Arguments.of(54, "(2,16,0,1)"), Arguments.of(55, "(2,16,1,1)"),
            Arguments.of(56, "(2,32,0,1)"), Arguments.of(57, "(2,32,1,1)"), Arguments.of(58, "(2,64,0,1)"), Arguments.of(59, "(2,64,1,1)"),
            Arguments.of(60, "(2,128,0,1)"), Arguments.of(61, "(2,128,1,1)"), Arguments.of(62, "(2,192,0,1)"), Arguments.of(63, "(2,192,1,1)"),
            Arguments.of(64, "(2,224,0,1)"), Arguments.of(65, "(2,224,1,1)"), Arguments.of(66, "(2,240,0,1)"), Arguments.of(67, "(2,240,1,1)"),
            Arguments.of(68, "(2,248,0,1)"), Arguments.of(69, "(2,248,1,1)"),
            Arguments.of(70, "(3,192)"), Arguments.of(71, "(3,224)"), Arguments.of(72, "(3,240)"), Arguments.of(73, "(3,248)"), Arguments.of(74, "(3,252)"),
            Arguments.of(75, "(3,8,0,1)"), Arguments.of(76, "(3,8,1,1)"), Arguments.of(77, "(3,16,0,1)"), Arguments.of(78, "(3,16,1,1)"),
            Arguments.of(79, "(3,32,0,1)"), Arguments.of(80, "(3,32,1,1)"), Arguments.of(81, "(3,64,0,1)"), Arguments.of(82, "(3,64,1,1)"),
            Arguments.of(83, "(3,128,0,1)"), Arguments.of(84, "(3,128,1,1)"), Arguments.of(85, "(3,192,0,1)"), Arguments.of(86, "(3,192,1,1)"),
            Arguments.of(87, "(3,224,0,1)"), Arguments.of(88, "(3,224,1,1)"), Arguments.of(89, "(3,240,0,1)"), Arguments.of(90, "(3,240,1,1)"),
            Arguments.of(91, "(3,248,0,1)"), Arguments.of(92, "(3,248,1,1)"),
            Arguments.of(93, "(4,192)"), Arguments.of(94, "(4,224)"), Arguments.of(95, "(4,240)"), Arguments.of(96, "(4,248)"), Arguments.of(97, "(4,252)"),
            Arguments.of(98, "(4,8,0,1)"), Arguments.of(99, "(4,8,1,1)"), Arguments.of(100, "(4,16,0,1)"), Arguments.of(101, "(4,16,1,1)"),
            Arguments.of(102, "(4,32,0,1)"), Arguments.of(103, "(4,32,1,1)"), Arguments.of(104, "(4,64,0,1)"), Arguments.of(105, "(4,64,1,1)"),
            Arguments.of(106, "(4,128,0,1)"), Arguments.of(107, "(4,128,1,1)"), Arguments.of(108, "(4,192,0,1)"), Arguments.of(109, "(4,192,1,1)"),
            Arguments.of(110, "(4,224,0,1)"), Arguments.of(111, "(4,224,1,1)"), Arguments.of(112, "(4,240,0,1)"), Arguments.of(113, "(4,240,1,1)"),
            Arguments.of(114, "(4,248,0,1)"), Arguments.of(115, "(4,248,1,1)")
        );
    }

    /**
     * Provides all valid canonical specifier values (1-115).
     */
    static Stream<Arguments> provideCanonicalSpecifiers() {
        // The spec defines canonical codecs for specifiers 1 through 115.
        return IntStream.rangeClosed(1, 115).mapToObj(Arguments::of);
    }

    /**
     * Provides instances of non-canonical codecs for testing specifier generation.
     */
    static Stream<Arguments> provideNonCanonicalCodecInstances() {
        return Stream.of(
            Arguments.of(new BHSDCodec(2, 125, 0, 1)),
            Arguments.of(new BHSDCodec(3, 125, 2, 1)),
            Arguments.of(new BHSDCodec(4, 125)),
            Arguments.of(new BHSDCodec(5, 125, 2, 0)),
            Arguments.of(new BHSDCodec(3, 5, 2, 1))
        );
    }

    @DisplayName("getCodec() should decode non-canonical codecs from a byte stream")
    @ParameterizedTest(name = "Bytes {1} should decode to codec {0}")
    @MethodSource("provideNonCanonicalCodecBytes")
    void getCodec_forNonCanonicalEncoding_shouldReturnCorrectCodec(final String expectedCodecString, final byte[] encodedBytes)
        throws IOException, Pack200Exception {
        // Arrange
        final InputStream inputStream = new ByteArrayInputStream(encodedBytes);

        // Act
        final Codec decodedCodec = CodecEncoding.getCodec(NON_CANONICAL_SPECIFIER, inputStream, null);

        // Assert
        assertEquals(expectedCodecString, decodedCodec.toString());
    }

    @DisplayName("getCodec() should return the correct canonical codec for a given specifier")
    @ParameterizedTest(name = "Specifier {0} should map to codec {1}")
    @MethodSource("provideCanonicalCodecEncodings")
    void getCodec_forCanonicalEncoding_shouldReturnCorrectCodec(final int specifier, final String expectedCodecString)
        throws IOException, Pack200Exception {
        // Act
        final Codec actualCodec = CodecEncoding.getCodec(specifier, null, null);

        // Assert
        assertEquals(expectedCodecString, actualCodec.toString());
    }

    @DisplayName("getSpecifier() for a canonical codec should return the original specifier")
    @ParameterizedTest(name = "Codec for specifier {0} should encode back to {0}")
    @MethodSource("provideCanonicalSpecifiers")
    void getSpecifier_forCanonicalCodec_shouldReturnOriginalSpecifier(final int specifier) throws Pack200Exception, IOException {
        // Arrange: Get a canonical codec for a given specifier
        final Codec canonicalCodec = CodecEncoding.getCodec(specifier, null, null);

        // Act: Get its specifier back
        final int[] specifierResult = CodecEncoding.getSpecifier(canonicalCodec, null);

        // Assert: The original specifier should be returned in a single-element array
        assertEquals(1, specifierResult.length);
        assertEquals(specifier, specifierResult[0]);
    }

    @DisplayName("getSpecifier() for a non-canonical codec should be round-trippable")
    @ParameterizedTest(name = "Codec {0}")
    @MethodSource("provideNonCanonicalCodecInstances")
    void getSpecifier_forNonCanonicalCodec_shouldBeRoundTrippable(final Codec nonCanonicalCodec) throws IOException, Pack200Exception {
        // Act: Get the specifier for a non-canonical codec
        final int[] specifiers = CodecEncoding.getSpecifier(nonCanonicalCodec, null);

        // Assert: The specifier should indicate a non-canonical encoding with two additional bytes
        assertEquals(3, specifiers.length);
        assertEquals(NON_CANONICAL_SPECIFIER, specifiers[0]);

        // Arrange: Reconstruct the codec from the specifier bytes to test the round trip
        final byte[] codecData = {(byte) specifiers[1], (byte) specifiers[2]};
        final InputStream in = new ByteArrayInputStream(codecData);

        // Act: Decode the bytes back into a codec
        final Codec reconstructedCodec = CodecEncoding.getCodec(NON_CANONICAL_SPECIFIER, in, null);

        // Assert: The reconstructed codec should be equal to the original
        assertEquals(nonCanonicalCodec, reconstructedCodec);
    }

    @Test
    @DisplayName("getCodec() with default specifier should return the provided default codec")
    void getCodec_withDefaultSpecifier_shouldReturnDefaultCodec() throws Pack200Exception, IOException {
        // Arrange
        final Codec defaultCodec = new BHSDCodec(2, 16, 0, 0);

        // Act
        final Codec result = CodecEncoding.getCodec(DEFAULT_SPECIFIER, null, defaultCodec);

        // Assert
        assertEquals(defaultCodec, result);
    }
}