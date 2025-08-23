package org.apache.commons.compress.compressors.gzip;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipException;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.compress.compressors.gzip.ExtraField.SubField;
import org.apache.commons.compress.compressors.gzip.GzipParameters.OS;
import org.apache.commons.lang3.ArrayFill;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import shaded.org.apache.commons.io.IOUtils;

public class GzipCompressorOutputStreamTestTest4 {

    private static final String EXPECTED_BASE_NAME = "\u6D4B\u8BD5\u4E2D\u6587\u540D\u79F0";

    private static final String EXPECTED_FILE_NAME = EXPECTED_BASE_NAME + ".xml";

    private void testChineseFileName(final String expected, final String sourceFile, final Charset fileNameCharset) throws IOException {
        final Path tempSourceFile = Files.createTempFile(sourceFile, sourceFile);
        final byte[] bytes = "<text>Hello World!</text>".getBytes(StandardCharsets.ISO_8859_1);
        Files.write(tempSourceFile, bytes);
        final Path targetFile = Files.createTempFile(EXPECTED_BASE_NAME, ".gz");
        final GzipParameters parameters = new GzipParameters();
        // If your system is Windows with Chinese, and your file name is Chinese, you need set the fileNameCharset to "GBK"
        // otherwise your file name is different using GzipCompressorOutputStream without a GzipParameters.
        // On Linux, set the fileNameCharset to UTF-8.
        parameters.setFileNameCharset(fileNameCharset);
        assertEquals(fileNameCharset, parameters.getFileNameCharset());
        parameters.setFileName(EXPECTED_FILE_NAME);
        parameters.setComment("Comment on " + EXPECTED_FILE_NAME);
        try (OutputStream fos = Files.newOutputStream(targetFile);
            GzipCompressorOutputStream gos = new GzipCompressorOutputStream(fos, parameters)) {
            gos.write(tempSourceFile);
        }
        // Old construction doesn't allow configuration of reading the file name and comment Charset.
        try (GzipCompressorInputStream gis = new GzipCompressorInputStream(Files.newInputStream(targetFile))) {
            final byte[] fileNameBytes = gis.getMetaData().getFileName().getBytes(StandardCharsets.ISO_8859_1);
            final String unicodeFileName = new String(fileNameBytes, fileNameCharset);
            assertEquals(expected, unicodeFileName);
            assertArrayEquals(bytes, IOUtils.toByteArray(gis));
        }
        // Construction allows configuration of reading the file name and comment Charset.
        // @formatter:off
        try (GzipCompressorInputStream gis = GzipCompressorInputStream.builder().setPath(targetFile).setFileNameCharset(fileNameCharset).get()) {
            // @formatter:on
            final byte[] fileNameBytes = gis.getMetaData().getFileName().getBytes(fileNameCharset);
            final String unicodeFileName = new String(fileNameBytes, fileNameCharset);
            assertEquals(expected, unicodeFileName);
            assertArrayEquals(bytes, IOUtils.toByteArray(gis));
            // reset trailer values for a simple assertion.
            gis.getMetaData().setTrailerCrc(0);
            gis.getMetaData().setTrailerISize(0);
            assertEquals(parameters, gis.getMetaData());
        }
    }

    /**
     * Tests the gzip extra header containing subfields.
     *
     * @throws IOException When the test has issues with the underlying file system or unexpected gzip operations.
     */
    @ParameterizedTest
    // @formatter:off
    @CsvSource({ "0,    42, false", "1,      , true", "1,     0, false", "1, 65531, false", "1, 65532, true", "2,     0, false", "2, 32764, true", "2, 32763, false" })
    // @formatter:on
    void testExtraSubfields(final int subFieldCount, final Integer payloadSize, final boolean shouldFail) throws IOException {
        final Path tempSourceFile = Files.createTempFile("test_gzip_extra_", ".txt");
        final Path targetFile = Files.createTempFile("test_gzip_extra_", ".txt.gz");
        Files.write(tempSourceFile, "Hello World!".getBytes(StandardCharsets.ISO_8859_1));
        final GzipParameters parameters = new GzipParameters();
        final ExtraField extra = new ExtraField();
        boolean failed = false;
        final byte[][] payloads = new byte[subFieldCount][];
        for (int i = 0; i < subFieldCount; i++) {
            if (payloadSize != null) {
                payloads[i] = ArrayFill.fill(new byte[payloadSize], (byte) ('a' + i));
            }
            try {
                extra.addSubField("z" + i, payloads[i]);
            } catch (final NullPointerException | IOException e) {
                failed = true;
                break;
            }
        }
        assertEquals(shouldFail, failed, "add subfield " + (shouldFail ? "succes" : "failure") + " was not expected.");
        if (shouldFail) {
            return;
        }
        if (subFieldCount > 0) {
            assertThrows(UnsupportedOperationException.class, () -> extra.iterator().remove());
        }
        parameters.setExtraField(extra);
        try (OutputStream fos = Files.newOutputStream(targetFile);
            GzipCompressorOutputStream gos = new GzipCompressorOutputStream(fos, parameters)) {
            gos.write(tempSourceFile);
            gos.close();
            assertTrue(gos.isClosed());
        }
        try (GzipCompressorInputStream gis = new GzipCompressorInputStream(Files.newInputStream(targetFile))) {
            final ExtraField extra2 = gis.getMetaData().getExtraField();
            assertEquals(parameters, gis.getMetaData());
            assertEquals(subFieldCount == 0, extra2.isEmpty());
            assertEquals(subFieldCount, extra2.size());
            assertEquals(4 * subFieldCount + subFieldCount * payloadSize, extra2.getEncodedSize());
            final ArrayList<SubField> listCopy = new ArrayList<>();
            extra2.forEach(listCopy::add);
            assertEquals(subFieldCount, listCopy.size());
            for (int i = 0; i < subFieldCount; i++) {
                final SubField sf = extra2.getSubField(i);
                assertSame(sf, listCopy.get(i));
                assertSame(sf, extra2.findFirstSubField("z" + i));
                // id was saved/loaded correctly
                assertEquals("z" + i, sf.getId());
                assertArrayEquals(payloads[i], sf.getPayload(), "field " + i + " has wrong payload");
            }
            extra2.clear();
            assertTrue(extra2.isEmpty());
        }
    }

    private void testFileName(final String expected, final String sourceFile) throws IOException {
        final Path tempSourceFile = Files.createTempFile(sourceFile, sourceFile);
        final byte[] bytes = "<text>Hello World!</text>".getBytes(StandardCharsets.ISO_8859_1);
        Files.write(tempSourceFile, bytes);
        final Path targetFile = Files.createTempFile("test", ".gz");
        final GzipParameters parameters = new GzipParameters();
        parameters.setFilename(sourceFile);
        assertEquals(parameters.getFilename(), parameters.getFileName());
        parameters.setFileName(sourceFile);
        assertEquals(parameters.getFilename(), parameters.getFileName());
        try (OutputStream fos = Files.newOutputStream(targetFile);
            GzipCompressorOutputStream gos = new GzipCompressorOutputStream(fos, parameters)) {
            gos.write(tempSourceFile);
        }
        try (GzipCompressorInputStream gis = new GzipCompressorInputStream(Files.newInputStream(targetFile))) {
            assertEquals(expected, gis.getMetaData().getFileName());
            assertEquals(expected, gis.getMetaData().getFilename());
            assertArrayEquals(bytes, IOUtils.toByteArray(gis));
        }
    }

    @Test
    void testFileNameAscii() throws IOException {
        testFileName("ASCII.xml", "ASCII.xml");
    }
}
