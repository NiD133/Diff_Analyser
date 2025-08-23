package org.apache.commons.compress.harmony.pack200;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("CodecEncoding Tests")
public class CodecEncodingTest {

    /**
     * Specifier for a non-canonical BHSD codec, requiring a two-byte descriptor.
     */
    private static final int NON_CANONICAL_BHSD_CODEC = 116;

    /**
     * The range of specifiers used for RunCodecs.
     */
    private static final int RUN_CODEC_SPECIFIER_MIN = 117;
    private static final int RUN_CODEC_SPECIFIER_MAX = 140;

    // --- Test Data Providers ---

    static Stream<Arguments> arbitraryBHSDCodecAndEncodedBytesProvider() {
        return Stream.of(
            // Each argument is: expected Codec.toString(), byte representation
            Arguments.of("(1,256)", new byte[]{0x00, (byte) 0xFF}),
            Arguments.of("(5,128,2,1)", new byte[]{0x25, (byte) 0x7F}),
            Arguments.of("(2,128,1,1)", new byte[]{0x0B, (byte) 0x7F})
        );
    }

    static Stream<BHSDCodec> arbitraryBHSDCodecProvider() {
        return Stream.of(
            new BHSDCodec(2, 125, 0, 1),
            new BHSDCodec(3, 125, 2, 1),
            new BHSDCodec(4, 125),
            new BHSDCodec(5, 125, 2, 0),
            new BHSDCodec(3, 5, 2, 1)
        );
    }

    static IntStream canonicalCodecIdProvider() {
        // The Pack200 spec defines 115 canonical codecs, numbered 1-115.
        return IntStream.rangeClosed(1, 115);
    }

    // --- Test Cases ---

    @DisplayName("getCodec() should return correct canonical codec for its ID")
    @ParameterizedTest(name = "ID {0} -> {1}")
    @CsvFileSource(resources = "canonical_bhsd_codecs.csv", numLinesToSkip = 1)
    void getCodecShouldReturnCorrectCanonicalCodec(final int id, final String expectedCodecString)
        throws IOException, Pack200Exception {
        final Codec codec = CodecEncoding.getCodec(id, null, null);
        assertEquals(expectedCodecString, codec.toString());
    }

    @DisplayName("getCodec() should decode arbitrary BHSD codec from byte stream")
    @ParameterizedTest(name = "Codec {0}")
    @MethodSource("arbitraryBHSDCodecAndEncodedBytesProvider")
    void getCodecShouldDecodeArbitraryBHSD(final String expected, final byte[] bytes) throws IOException, Pack200Exception {
        final InputStream in = new ByteArrayInputStream(bytes);
        final Codec codec = CodecEncoding.getCodec(NON_CANONICAL_BHSD_CODEC, in, null);
        assertEquals(expected, codec.toString());
    }

    @DisplayName("getSpecifier() should return correct ID for canonical codecs")
    @ParameterizedTest(name = "ID = {0}")
    @MethodSource("canonicalCodecIdProvider")
    void getSpecifierShouldReturnIdForCanonicalCodec(final int id) throws Pack200Exception, IOException {
        final Codec canonicalCodec = CodecEncoding.getCodec(id, null, null);
        final int[] specifier = CodecEncoding.getSpecifier(canonicalCodec, null);
        assertEquals(id, specifier[0]);
    }

    @DisplayName("getSpecifier() for arbitrary BHSD codec should support round-trip encoding/decoding")
    @ParameterizedTest(name = "Codec {0}")
    @MethodSource("arbitraryBHSDCodecProvider")
    void getSpecifierForBHSDCodecShouldRoundTrip(final BHSDCodec originalCodec) throws IOException, Pack200Exception {
        final int[] specifier = CodecEncoding.getSpecifier(originalCodec, null);

        // A non-canonical BHSD codec is always encoded as 3 integers.
        assertEquals(3, specifier.length);
        assertEquals(NON_CANONICAL_BHSD_CODEC, specifier[0]);

        // Recreate the codec from the specifier's payload and verify it's identical.
        final byte[] bytes = {(byte) specifier[1], (byte) specifier[2]};
        final InputStream in = new ByteArrayInputStream(bytes);
        final Codec reconstructedCodec = CodecEncoding.getCodec(NON_CANONICAL_BHSD_CODEC, in, null);

        assertEquals(originalCodec, reconstructedCodec);
    }

    @Nested
    @DisplayName("getSpecifier() for RunCodec")
    class RunCodecSpecifierTest {

        @Test
        @DisplayName("should support round-trip for a simple RunCodec")
        void shouldEncodeAndDecodeSimpleRunCodec() throws Pack200Exception, IOException {
            final RunCodec original = new RunCodec(25, Codec.DELTA5, Codec.BYTE1);
            final int[] specifier = CodecEncoding.getSpecifier(original, null);

            final RunCodec reconstructed = (RunCodec) recreateCodecFromSpecifier(specifier, null);

            assertRunCodecsAreEqual(original, reconstructed);
        }

        @Test
        @DisplayName("should support round-trip when a component is the default codec")
        void shouldEncodeAndDecodeRunCodecWithDefaultComponent() throws Pack200Exception, IOException {
            final RunCodec original = new RunCodec(4096, Codec.DELTA5, Codec.BYTE1);
            final Codec defaultCodec = Codec.DELTA5;
            final int[] specifier = CodecEncoding.getSpecifier(original, defaultCodec);

            final RunCodec reconstructed = (RunCodec) recreateCodecFromSpecifier(specifier, defaultCodec);

            assertRunCodecsAreEqual(original, reconstructed);
        }

        @Test
        @DisplayName("should support round-trip for a nested RunCodec")
        void shouldEncodeAndDecodeNestedRunCodec() throws Pack200Exception, IOException {
            final RunCodec original = new RunCodec(64, Codec.SIGNED5, new RunCodec(25, Codec.UDELTA5, Codec.DELTA5));
            final int[] specifier = CodecEncoding.getSpecifier(original, null);

            final RunCodec reconstructed = (RunCodec) recreateCodecFromSpecifier(specifier, null);

            assertRunCodecsAreEqual(original, reconstructed);
        }

        @Test
        @DisplayName("should support round-trip for a nested RunCodec with a default component")
        void shouldEncodeAndDecodeNestedRunCodecWithDefaultComponent() throws Pack200Exception, IOException {
            final RunCodec original = new RunCodec(64, Codec.SIGNED5, new RunCodec(25, Codec.UDELTA5, Codec.DELTA5));
            final Codec defaultCodec = Codec.UDELTA5;
            final int[] specifier = CodecEncoding.getSpecifier(original, defaultCodec);

            final RunCodec reconstructed = (RunCodec) recreateCodecFromSpecifier(specifier, defaultCodec);

            assertRunCodecsAreEqual(original, reconstructed);
        }

        private Codec recreateCodecFromSpecifier(final int[] specifier, final Codec defaultCodec)
            throws IOException, Pack200Exception {
            final int specifierValue = specifier[0];
            assertTrue(specifierValue >= RUN_CODEC_SPECIFIER_MIN && specifierValue <= RUN_CODEC_SPECIFIER_MAX,
                "Specifier for RunCodec should be in the correct range");

            final byte[] bytes = new byte[specifier.length - 1];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = (byte) specifier[i + 1];
            }
            final InputStream in = new ByteArrayInputStream(bytes);
            return CodecEncoding.getCodec(specifierValue, in, defaultCodec);
        }

        private void assertRunCodecsAreEqual(final RunCodec expected, final RunCodec actual) {
            assertEquals(expected.getK(), actual.getK());
            assertEquals(expected.getACodec(), actual.getACodec());

            final Codec expectedB = expected.getBCodec();
            final Codec actualB = actual.getBCodec();

            // Handle nested RunCodecs recursively
            if (expectedB instanceof RunCodec && actualB instanceof RunCodec) {
                assertRunCodecsAreEqual((RunCodec) expectedB, (RunCodec) actualB);
            } else {
                assertEquals(expectedB, actualB);
            }
        }
    }
}