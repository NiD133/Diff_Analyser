package org.apache.commons.compress.archivers.zip;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.zip.ZipException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * JUnit tests for org.apache.commons.compress.archivers.zip.ExtraFieldUtils.
 */
class ExtraFieldUtilsTest implements UnixStat {

    // Inner class representing a ZipExtraField that throws an ArrayIndexOutOfBoundsException
    public static class AiobThrowingExtraField implements ZipExtraField {
        static final int LENGTH = 4;

        @Override
        public byte[] getCentralDirectoryData() {
            return getLocalFileDataData();
        }

        @Override
        public ZipShort getCentralDirectoryLength() {
            return getLocalFileDataLength();
        }

        @Override
        public ZipShort getHeaderId() {
            return AIOB_HEADER;
        }

        @Override
        public byte[] getLocalFileDataData() {
            return new byte[LENGTH];
        }

        @Override
        public ZipShort getLocalFileDataLength() {
            return new ZipShort(LENGTH);
        }

        @Override
        public void parseFromCentralDirectoryData(final byte[] buffer, final int offset, final int length) {
            parseFromLocalFileData(buffer, offset, length);
        }

        @Override
        public void parseFromLocalFileData(final byte[] buffer, final int offset, final int length) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    // Constants for unrecognized and test-specific header IDs
    static final ZipShort UNRECOGNIZED_HEADER = new ZipShort(0x5555);
    static final ZipShort AIOB_HEADER = new ZipShort(0x1000);

    // Fields used in tests
    private AsiExtraField asiExtraField;
    private UnrecognizedExtraField unrecognizedExtraField;
    private byte[] expectedData;
    private byte[] asiLocalData;

    @BeforeEach
    public void setUp() {
        // Initialize AsiExtraField with specific settings
        asiExtraField = new AsiExtraField();
        asiExtraField.setMode(0755);
        asiExtraField.setDirectory(true);

        // Initialize UnrecognizedExtraField with specific header ID and data
        unrecognizedExtraField = new UnrecognizedExtraField();
        unrecognizedExtraField.setHeaderId(UNRECOGNIZED_HEADER);
        unrecognizedExtraField.setLocalFileDataData(new byte[] { 0 });
        unrecognizedExtraField.setCentralDirectoryData(new byte[] { 0 });

        // Prepare expected data for tests
        asiLocalData = asiExtraField.getLocalFileDataData();
        final byte[] unrecognizedLocalData = unrecognizedExtraField.getLocalFileDataData();
        expectedData = new byte[4 + asiLocalData.length + 4 + unrecognizedLocalData.length];
        System.arraycopy(asiExtraField.getHeaderId().getBytes(), 0, expectedData, 0, 2);
        System.arraycopy(asiExtraField.getLocalFileDataLength().getBytes(), 0, expectedData, 2, 2);
        System.arraycopy(asiLocalData, 0, expectedData, 4, asiLocalData.length);
        System.arraycopy(unrecognizedExtraField.getHeaderId().getBytes(), 0, expectedData, 4 + asiLocalData.length, 2);
        System.arraycopy(unrecognizedExtraField.getLocalFileDataLength().getBytes(), 0, expectedData, 4 + asiLocalData.length + 2, 2);
        System.arraycopy(unrecognizedLocalData, 0, expectedData, 4 + asiLocalData.length + 4, unrecognizedLocalData.length);
    }

    /**
     * Test merging of local and central directory data.
     */
    @Test
    void testMerge() {
        // Test merging local file data
        final byte[] mergedLocalData = ExtraFieldUtils.mergeLocalFileDataData(new ZipExtraField[] { asiExtraField, unrecognizedExtraField });
        assertEquals(expectedData.length, mergedLocalData.length, "local length");
        for (int i = 0; i < mergedLocalData.length; i++) {
            assertEquals(expectedData[i], mergedLocalData[i], "local byte " + i);
        }

        // Prepare expected central directory data
        final byte[] unrecognizedCentralData = unrecognizedExtraField.getCentralDirectoryData();
        final byte[] expectedCentralData = new byte[4 + asiLocalData.length + 4 + unrecognizedCentralData.length];
        System.arraycopy(expectedData, 0, expectedCentralData, 0, 4 + asiLocalData.length + 2);
        System.arraycopy(unrecognizedExtraField.getCentralDirectoryLength().getBytes(), 0, expectedCentralData, 4 + asiLocalData.length + 2, 2);
        System.arraycopy(unrecognizedCentralData, 0, expectedCentralData, 4 + asiLocalData.length + 4, unrecognizedCentralData.length);

        // Test merging central directory data
        final byte[] mergedCentralData = ExtraFieldUtils.mergeCentralDirectoryData(new ZipExtraField[] { asiExtraField, unrecognizedExtraField });
        assertEquals(expectedCentralData.length, mergedCentralData.length, "central length");
        for (int i = 0; i < mergedCentralData.length; i++) {
            assertEquals(expectedCentralData[i], mergedCentralData[i], "central byte " + i);
        }
    }

    @Test
    void testMergeWithUnparseableData() throws Exception {
        // Create an unparseable extra field and parse it
        final ZipExtraField unparseableField = new UnparseableExtraFieldData();
        final byte[] headerBytes = UNRECOGNIZED_HEADER.getBytes();
        unparseableField.parseFromLocalFileData(new byte[] { headerBytes[0], headerBytes[1], 1, 0 }, 0, 4);

        // Test merging local file data with unparseable field
        final byte[] mergedLocalData = ExtraFieldUtils.mergeLocalFileDataData(new ZipExtraField[] { asiExtraField, unparseableField });
        assertEquals(expectedData.length - 1, mergedLocalData.length, "local length");
        for (int i = 0; i < mergedLocalData.length; i++) {
            assertEquals(expectedData[i], mergedLocalData[i], "local byte " + i);
        }

        // Prepare expected central directory data
        final byte[] unparseableCentralData = unparseableField.getCentralDirectoryData();
        final byte[] expectedCentralData = new byte[4 + asiLocalData.length + unparseableCentralData.length];
        System.arraycopy(expectedData, 0, expectedCentralData, 0, 4 + asiLocalData.length + 2);
        System.arraycopy(unparseableCentralData, 0, expectedCentralData, 4 + asiLocalData.length, unparseableCentralData.length);

        // Test merging central directory data with unparseable field
        final byte[] mergedCentralData = ExtraFieldUtils.mergeCentralDirectoryData(new ZipExtraField[] { asiExtraField, unparseableField });
        assertEquals(expectedCentralData.length, mergedCentralData.length, "central length");
        for (int i = 0; i < mergedCentralData.length; i++) {
            assertEquals(expectedCentralData[i], mergedCentralData[i], "central byte " + i);
        }
    }

    /**
     * Test parsing of extra fields.
     */
    @Test
    void testParse() throws Exception {
        // Parse the expected data
        final ZipExtraField[] parsedFields = ExtraFieldUtils.parse(expectedData);
        assertEquals(2, parsedFields.length, "number of fields");
        assertTrue(parsedFields[0] instanceof AsiExtraField, "type field 1");
        assertEquals(040755, ((AsiExtraField) parsedFields[0]).getMode(), "mode field 1");
        assertTrue(parsedFields[1] instanceof UnrecognizedExtraField, "type field 2");
        assertEquals(1, parsedFields[1].getLocalFileDataLength().getValue(), "data length field 2");

        // Test parsing with invalid data
        final byte[] invalidData = new byte[expectedData.length - 1];
        System.arraycopy(expectedData, 0, invalidData, 0, invalidData.length);
        final Exception exception = assertThrows(Exception.class, () -> ExtraFieldUtils.parse(invalidData), "data should be invalid");
        assertEquals("Bad extra field starting at " + (4 + asiLocalData.length) + ".  Block length of 1 bytes exceeds remaining data of 0 bytes.", exception.getMessage(), "message");
    }

    @Test
    void testParseCentral() throws Exception {
        // Parse the expected data as central directory data
        final ZipExtraField[] parsedFields = ExtraFieldUtils.parse(expectedData, false);
        assertEquals(2, parsedFields.length, "number of fields");
        assertTrue(parsedFields[0] instanceof AsiExtraField, "type field 1");
        assertEquals(040755, ((AsiExtraField) parsedFields[0]).getMode(), "mode field 1");
        assertTrue(parsedFields[1] instanceof UnrecognizedExtraField, "type field 2");
        assertEquals(1, parsedFields[1].getCentralDirectoryLength().getValue(), "data length field 2");
    }

    @Test
    void testParseTurnsArrayIndexOutOfBoundsIntoZipException() {
        // Register a field that throws ArrayIndexOutOfBoundsException
        ExtraFieldUtils.register(AiobThrowingExtraField.class);
        final AiobThrowingExtraField throwingField = new AiobThrowingExtraField();
        final byte[] data = new byte[4 + AiobThrowingExtraField.LENGTH];
        System.arraycopy(throwingField.getHeaderId().getBytes(), 0, data, 0, 2);
        System.arraycopy(throwingField.getLocalFileDataLength().getBytes(), 0, data, 2, 2);
        System.arraycopy(throwingField.getLocalFileDataData(), 0, data, 4, AiobThrowingExtraField.LENGTH);

        // Test parsing that should throw a ZipException
        final ZipException exception = assertThrows(ZipException.class, () -> ExtraFieldUtils.parse(data), "data should be invalid");
        assertEquals("Failed to parse corrupt ZIP extra field of type 1000", exception.getMessage(), "message");
    }

    @Test
    void testParseWithRead() throws Exception {
        // Parse data with READ behavior for unparseable fields
        ZipExtraField[] parsedFields = ExtraFieldUtils.parse(expectedData, true, ExtraFieldUtils.UnparseableExtraField.READ);
        assertEquals(2, parsedFields.length, "number of fields");
        assertTrue(parsedFields[0] instanceof AsiExtraField, "type field 1");
        assertEquals(040755, ((AsiExtraField) parsedFields[0]).getMode(), "mode field 1");
        assertTrue(parsedFields[1] instanceof UnrecognizedExtraField, "type field 2");
        assertEquals(1, parsedFields[1].getLocalFileDataLength().getValue(), "data length field 2");

        // Test parsing with invalid data and READ behavior
        final byte[] invalidData = new byte[expectedData.length - 1];
        System.arraycopy(expectedData, 0, invalidData, 0, invalidData.length);
        parsedFields = ExtraFieldUtils.parse(invalidData, true, ExtraFieldUtils.UnparseableExtraField.READ);
        assertEquals(2, parsedFields.length, "number of fields");
        assertTrue(parsedFields[0] instanceof AsiExtraField, "type field 1");
        assertEquals(040755, ((AsiExtraField) parsedFields[0]).getMode(), "mode field 1");
        assertTrue(parsedFields[1] instanceof UnparseableExtraFieldData, "type field 2");
        assertEquals(4, parsedFields[1].getLocalFileDataLength().getValue(), "data length field 2");
        for (int i = 0; i < 4; i++) {
            assertEquals(invalidData[expectedData.length - 5 + i], parsedFields[1].getLocalFileDataData()[i], "byte number " + i);
        }
    }

    @Test
    void testParseWithSkip() throws Exception {
        // Parse data with SKIP behavior for unparseable fields
        ZipExtraField[] parsedFields = ExtraFieldUtils.parse(expectedData, true, ExtraFieldUtils.UnparseableExtraField.SKIP);
        assertEquals(2, parsedFields.length, "number of fields");
        assertTrue(parsedFields[0] instanceof AsiExtraField, "type field 1");
        assertEquals(040755, ((AsiExtraField) parsedFields[0]).getMode(), "mode field 1");
        assertTrue(parsedFields[1] instanceof UnrecognizedExtraField, "type field 2");
        assertEquals(1, parsedFields[1].getLocalFileDataLength().getValue(), "data length field 2");

        // Test parsing with invalid data and SKIP behavior
        final byte[] invalidData = new byte[expectedData.length - 1];
        System.arraycopy(expectedData, 0, invalidData, 0, invalidData.length);
        parsedFields = ExtraFieldUtils.parse(invalidData, true, ExtraFieldUtils.UnparseableExtraField.SKIP);
        assertEquals(1, parsedFields.length, "number of fields");
        assertTrue(parsedFields[0] instanceof AsiExtraField, "type field 1");
        assertEquals(040755, ((AsiExtraField) parsedFields[0]).getMode(), "mode field 1");
    }
}