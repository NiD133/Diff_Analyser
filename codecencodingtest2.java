package org.apache.commons.compress.harmony.pack200;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link CodecEncoding}, which handles the translation between codec objects and their byte representations
 * according to the Pack200 specification.
 */
@DisplayName("Tests for CodecEncoding utility")
public class CodecEncodingTest {

    // --- Data Providers for Parameterized Tests ---

    /**
     * Provides arbitrary, non-canonical BHSD codecs and their expected byte encodings.
     * The specifier for these is always 116, followed by two bytes for (B,H) and (S,D).
     *
     * @return a stream of arguments: { expected string representation, encoded bytes }.
     */
    static Stream<Arguments> arbitraryBHSDCodecProvider() {
        return Stream.of(
            Arguments.of("(1,256)", new byte[]{0x00, (byte) 0xFF}),
            Arguments.of("(5,128,2,1)", new byte[]{0x25, (byte) 0x7F}),
            Arguments.of("(2,128,1,1)", new byte[]{0x0B, (byte) 0x7F})
        );
    }

    /**
     * Provides the 115 canonical codec encodings as defined by the Pack200 specification.
     * Each argument consists of the canonical integer value and its expected string representation.
     *
     * @return a stream of arguments for the canonical codec test.
     */
    static Stream<Arguments> canonicalCodecSpecifications() {
        return Stream.of(
            Arguments.of(1, "(1,256)"), Arguments.of(2, "(1,256,1)"), Arguments.of(3, "(1,256,0,1)"),
            Arguments.of(4, "(1,256,1,1)"), Arguments.of(5, "(2,256)"), Arguments.of(6, "(2,256,1)"),
            Arguments.of(7, "(2,256,0,1)"), Arguments.of(8, "(2,256,1,1)"), Arguments.of(9, "(3,256)"),
            Arguments.of(10, "(3,256,1)"), Arguments.of(11, "(3,256,0,1)"), Arguments.of(12, "(3,256,1,1)"),
            Arguments.of(13, "(4,256)"), Arguments.of(14, "(4,256,1)"), Arguments.of(15, "(4,256,0,1)"),
            Arguments.of(16, "(4,256,1,1)"), Arguments.of(17, "(5,4)"), Arguments.of(18, "(5,4,1)"),
            Arguments.of(19, "(5,4,2)"), Arguments.of(20, "(5,16)"), Arguments.of(21, "(5,16,1)"),
            Arguments.of(22, "(5,16,2)"), Arguments.of(23, "(5,32)"), Arguments.of(24, "(5,32,1)"),
            Arguments.of(25, "(5,32,2)"), Arguments.of(26, "(5,64)"), Arguments.of(27, "(5,64,1)"),
            Arguments.of(28, "(5,64,2)"), Arguments.of(29, "(5,128)"), Arguments.of(30, "(5,128,1)"),
            Arguments.of(31, "(5,128,2)"), Arguments.of(32, "(5,4,0,1)"), Arguments.of(33, "(5,4,1,1)"),
            Arguments.of(34, "(5,4,2,1)"), Arguments.of(35, "(5,16,0,1)"), Arguments.of(36, "(5,16,1,1)"),
            Arguments.of(37, "(5,16,2,1)"), Arguments.of(38, "(5,32,0,1)"), Arguments.of(39, "(5,32,1,1)"),
            Arguments.of(40, "(5,32,2,1)"), Arguments.of(41, "(5,64,0,1)"), Arguments.of(42, "(5,64,1,1)"),
            Arguments.of(43, "(5,64,2,1)"), Arguments.of(44, "(5,128,0,1)"), Arguments.of(45, "(5,128,1,1)"),
            Arguments.of(46, "(5,128,2,1)"), Arguments.of(47, "(2,192)"), Arguments.of(48, "(2,224)"),
            Arguments.of(49, "(2,240)"), Arguments.of(50, "(2,248)"), Arguments.of(51, "(2,252)"),
            Arguments.of(52, "(2,8,0,1)"), Arguments.of(53, "(2,8,1,1)"), Arguments.of(54, "(2,16,0,1)"),
            Arguments.of(55, "(2,16,1,1)"), Arguments.of(56, "(2,32,0,1)"), Arguments.of(57, "(2,32,1,1)"),
            Arguments.of(58, "(2,64,0,1)"), Arguments.of(59, "(2,64,1,1)"), Arguments.of(60, "(2,128,0,1)"),
            Arguments.of(61, "(2,128,1,1)"), Arguments.of(62, "(2,192,0,1)"), Arguments.of(63, "(2,192,1,1)"),
            Arguments.of(64, "(2,224,0,1)"), Arguments.of(65, "(2,224,1,1)"), Arguments.of(66, "(2,240,0,1)"),
            Arguments.of(67, "(2,240,1,1)"), Arguments.of(68, "(2,248,0,1)"), Arguments.of(69, "(2,248,1,1)"),
            Arguments.of(70, "(3,192)"), Arguments.of(71, "(3,224)"), Arguments.of(72, "(3,240)"),
            Arguments.of(73, "(3,248)"), Arguments.of(74, "(3,252)"), Arguments.of(75, "(3,8,0,1)"),
            Arguments.of(76, "(3,8,1,1)"), Arguments.of(77, "(3,16,0,1)"), Arguments.of(78, "(3,16,1,1)"),
            Arguments.of(79, "(3,32,0,1)"), Arguments.of(80, "(3,32,1,1)"), Arguments.of(81, "(3,64,0,1)"),
            Arguments.of(82, "(3,64,1,1)"), Arguments.of(83, "(3,128,0,1)"), Arguments.of(84, "(3,128,1,1)"),
            Arguments.of(85, "(3,192,0,1)"), Arguments.of(86, "(3,192,1,1)"), Arguments.of(87, "(3,224,0,1)"),
            Arguments.of(88, "(3,224,1,1)"), Arguments.of(89, "(3,240,0,1)"), Arguments.of(90, "(3,240,1,1)"),
            Arguments.of(91, "(3,248,0,1)"), Arguments.of(92, "(3,248,1,1)"), Arguments.of(93, "(4,192)"),
            Arguments.of(94, "(4,224)"), Arguments.of(95, "(4,240)"), Arguments.of(96, "(4,248)"),
            Arguments.of(97, "(4,252)"), Arguments.of(98, "(4,8,0,1)"), Arguments.of(99, "(4,8,1,1)"),
            Arguments.of(100, "(4,16,0,1)"), Arguments.of(101, "(4,16,1,1)"), Arguments.of(102, "(4,32,0,1)"),
            Arguments.of(103, "(4,32,1,1)"), Arguments.of(104, "(4,64,0,1)"), Arguments.of(105, "(4,64,1,1)"),
            Arguments.of(106, "(4,128,0,1)"), Arguments.of(107, "(4,128,1,1)"), Arguments.of(108, "(4,192,0,1)"),
            Arguments.of(109, "(4,192,1,1)"), Arguments.of(110, "(4,224,0,1)"), Arguments.of(111, "(4,224,1,1)"),
            Arguments.of(112, "(4,240,0,1)"), Arguments.of(113, "(4,240,1,1)"), Arguments.of(114, "(4,248,0,1)"),
            Arguments.of(115, "(4,248,1,1)"));
    }

    /**
     * Provides a stream of integer values representing the canonical codecs (1-115).
     */
    static Stream<Arguments> canonicalCodecValueProvider() {
        return IntStream.rangeClosed(1, 115).mapToObj(Arguments::of);
    }

    /**
     * Provides instances of arbitrary, non-canonical BHSD codecs.
     */
    static Stream<Arguments> arbitraryBHSDCodecInstanceProvider() {
        return Stream.of(
            Arguments.of(new BHSDCodec(2, 125, 0, 1)),
            Arguments.of(new BHSDCodec(3, 125, 2, 1)),
            Arguments.of(new BHSDCodec(4, 125)),
            Arguments.of(new BHSDCodec(5, 125, 2, 0)),
            Arguments.of(new BHSDCodec(3, 5, 2, 1))
        );
    }

    @Nested
    @DisplayName("For Canonical Codecs")
    class CanonicalCodecTests {

        @DisplayName("getCodec() should return the correct canonical codec for a given value")
        @ParameterizedTest(name = "Value {0} -> Codec {1}")
        @MethodSource("org.apache.commons.compress.harmony.pack200.CodecEncodingTest#canonicalCodecSpecifications")
        void getCodecShouldReturnCorrectCanonicalCodec(final int canonicalValue, final String expectedCodecString)
            throws IOException, Pack200Exception {
            final Codec codec = CodecEncoding.getCodec(canonicalValue, null, null);
            assertEquals(expectedCodecString, codec.toString());
        }

        @DisplayName("getSpecifier() should return the original value for a canonical codec")
        @ParameterizedTest(name = "Codec from value {0}")
        @MethodSource("org.apache.commons.compress.harmony.pack200.CodecEncodingTest#canonicalCodecValueProvider")
        void getSpecifierShouldReturnOriginalValueForCanonicalCodec(final int canonicalValue)
            throws Pack200Exception, IOException {
            final Codec codec = CodecEncoding.getCodec(canonicalValue, null, null);
            final int[] specifier = CodecEncoding.getSpecifier(codec, null);
            assertEquals(canonicalValue, specifier[0]);
        }
    }

    @Nested
    @DisplayName("For Arbitrary (non-canonical) BHSD Codecs")
    class ArbitraryBHSDCodecTests {

        private static final int ARBITRARY_BHSD_CODEC_SPECIFIER = 116;

        @DisplayName("getCodec() should correctly decode an arbitrary BHSD codec from a byte stream")
        @ParameterizedTest(name = "Bytes {1} -> Codec {0}")
        @MethodSource("org.apache.commons.compress.harmony.pack200.CodecEncodingTest#arbitraryBHSDCodecProvider")
        void getCodecShouldDecodeArbitraryBHSDCodec(final String expectedCodecString, final byte[] encodedBytes)
            throws IOException, Pack200Exception {
            final InputStream inputStream = new ByteArrayInputStream(encodedBytes);
            final Codec codec = CodecEncoding.getCodec(ARBITRARY_BHSD_CODEC_SPECIFIER, inputStream, null);
            assertEquals(expectedCodecString, codec.toString());
        }

        @DisplayName("getSpecifier() and getCodec() should round-trip for arbitrary BHSD codecs")
        @ParameterizedTest(name = "{0}")
        @MethodSource("org.apache.commons.compress.harmony.pack200.CodecEncodingTest#arbitraryBHSDCodecInstanceProvider")
        void shouldRoundTripArbitraryBHSDCodec(final Codec originalCodec) throws IOException, Pack200Exception {
            // Encode the codec to its specifier and byte representation
            final int[] specifiers = CodecEncoding.getSpecifier(originalCodec, null);

            // Arbitrary BHSD Codec specifiers have a fixed structure
            assertEquals(3, specifiers.length, "Specifier should contain 3 parts: value, byte1, byte2");
            assertEquals(ARBITRARY_BHSD_CODEC_SPECIFIER, specifiers[0], "Specifier value should be for arbitrary BHSD codec");

            final byte[] encodedBytes = {(byte) specifiers[1], (byte) specifiers[2]};
            final InputStream inputStream = new ByteArrayInputStream(encodedBytes);

            // Decode back to a codec and assert equality
            final Codec decodedCodec = CodecEncoding.getCodec(ARBITRARY_BHSD_CODEC_SPECIFIER, inputStream, null);
            assertEquals(originalCodec, decodedCodec);
        }
    }

    @Nested
    @DisplayName("For Population Codecs")
    class PopulationCodecTests {

        private static final int MIN_POPULATION_CODEC_SPECIFIER = 141;
        private static final int MAX_POPULATION_CODEC_SPECIFIER = 188;

        @Test
        @DisplayName("getSpecifier() and getCodec() should round-trip for PopulationCodecs")
        void shouldRoundTripPopulationCodec() throws IOException, Pack200Exception {
            // Create a sample PopulationCodec
            final PopulationCodec originalCodec = new PopulationCodec(Codec.BYTE1, Codec.CHAR3, Codec.UNSIGNED5);

            // Encode it into a specifier array
            final int[] specifiers = CodecEncoding.getSpecifier(originalCodec, null);
            final int specifierValue = specifiers[0];

            // Assert that the specifier value is within the valid range for PopulationCodecs
            assertTrue(specifierValue >= MIN_POPULATION_CODEC_SPECIFIER && specifierValue <= MAX_POPULATION_CODEC_SPECIFIER,
                "Specifier value should be in the PopulationCodec range");

            // Extract the encoded data (all elements after the specifier value)
            final byte[] encodedData = new byte[specifiers.length - 1];
            for (int i = 0; i < encodedData.length; i++) {
                encodedData[i] = (byte) specifiers[i + 1];
            }

            // Decode the data back into a codec
            final InputStream inputStream = new ByteArrayInputStream(encodedData);
            final Codec decodedCodec = CodecEncoding.getCodec(specifierValue, inputStream, null);

            // Assert that the decoded codec is a PopulationCodec and matches the original
            assertInstanceOf(PopulationCodec.class, decodedCodec);
            final PopulationCodec decodedPopulationCodec = (PopulationCodec) decodedCodec;

            assertAll("Decoded PopulationCodec should match original",
                () -> assertEquals(originalCodec.getFavouredCodec(), decodedPopulationCodec.getFavouredCodec()),
                () -> assertEquals(originalCodec.getTokenCodec(), decodedPopulationCodec.getTokenCodec()),
                () -> assertEquals(originalCodec.getUnfavouredCodec(), decodedPopulationCodec.getUnfavouredCodec())
            );
        }
    }
}