package org.apache.commons.codec.digest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class XXHash32Test {

    private Path filePath;
    private String expectedChecksum;

    /**
     * Copies data from an InputStream to an OutputStream using a specified buffer size.
     *
     * @param input the input stream
     * @param output the output stream
     * @param bufferSize the buffer size
     * @return the number of bytes copied
     * @throws IOException if an I/O error occurs
     */
    private static long copyStream(final InputStream input, final OutputStream output, final int bufferSize) throws IOException {
        return IOUtils.copyLarge(input, output, new byte[bufferSize]);
    }

    /**
     * Provides test data for parameterized tests.
     *
     * @return a stream of arguments containing file paths and expected checksums
     */
    public static Stream<Arguments> provideTestData() {
        return Stream.of(
            Arguments.of("org/apache/commons/codec/bla.tar", "fbb5c8d1"),
            Arguments.of("org/apache/commons/codec/bla.tar.xz", "4106a208"),
            Arguments.of("org/apache/commons/codec/small.bin", "f66c26f8")
        );
    }

    /**
     * Converts an InputStream to a byte array.
     *
     * @param input the input stream
     * @return the byte array
     * @throws IOException if an I/O error occurs
     */
    private static byte[] convertToByteArray(final InputStream input) throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        copyStream(input, output, 10240);
        return output.toByteArray();
    }

    /**
     * Initializes the test data by setting the file path and expected checksum.
     *
     * @param path the file path
     * @param checksum the expected checksum
     * @throws Exception if the file is not found or an error occurs
     */
    public void initializeTestData(final String path, final String checksum) throws Exception {
        final URL url = XXHash32Test.class.getClassLoader().getResource(path);
        if (url == null) {
            throw new FileNotFoundException("File not found: " + path);
        }
        filePath = Paths.get(url.toURI());
        expectedChecksum = checksum;
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    public void testChecksumVerification(final String path, final String checksum) throws Exception {
        initializeTestData(path, checksum);
        final XXHash32 hasher = new XXHash32();
        try (InputStream inputStream = Files.newInputStream(filePath)) {
            final byte[] fileBytes = convertToByteArray(inputStream);
            hasher.update(fileBytes, 0, fileBytes.length);
        }
        assertEquals(expectedChecksum, Long.toHexString(hasher.getValue()), "Checksum mismatch for file: " + filePath);
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    public void testIncrementalChecksumVerification(final String path, final String checksum) throws Exception {
        initializeTestData(path, checksum);
        final XXHash32 hasher = new XXHash32();
        try (InputStream inputStream = Files.newInputStream(filePath)) {
            final byte[] fileBytes = convertToByteArray(inputStream);
            // Test resetting the hash
            hasher.update(fileBytes[0]);
            hasher.reset();
            // Update hash in chunks
            hasher.update(fileBytes[0]);
            hasher.update(fileBytes, 1, fileBytes.length - 2);
            hasher.update(fileBytes, fileBytes.length - 1, 1);
            // Test hash with negative length (should be ignored)
            hasher.update(fileBytes, 0, -1);
        }
        assertEquals(expectedChecksum, Long.toHexString(hasher.getValue()), "Checksum mismatch for file: " + filePath);
    }
}