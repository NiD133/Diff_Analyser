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

    // Custom ZipExtraField implementation for testing ArrayIndexOutOfBoundsException
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

    // Constants for unrecognized and test-specific headers
    static final ZipShort UNRECOGNIZED_HEADER = new ZipShort(0x5555);
    static final ZipShort AIOB_HEADER = new ZipShort(0x1000);

    private AsiExtraField asiExtraField;
    private UnrecognizedExtraField unrecognizedExtraField;
    private byte[] testData;
    private byte[] asiLocalData;

    @BeforeEach
    public void setUp() {
        // Initialize AsiExtraField
        asiExtraField = new AsiExtraField();
        asiExtraField.setMode(0755);
        asiExtraField.setDirectory(true);

        // Initialize UnrecognizedExtraField
        unrecognizedExtraField = new UnrecognizedExtraField();
        unrecognizedExtraField.setHeaderId(UNRECOGNIZED_HEADER);
        unrecognizedExtraField.setLocalFileDataData(new byte[] { 0 });
        unrecognizedExtraField.setCentralDirectoryData(new byte[] { 0 });

        // Prepare test data
        asiLocalData = asiExtraField.getLocalFileDataData();
        final byte[] unrecognizedLocalData = unrecognizedExtraField.getLocalFileDataData();
        testData = new byte[4 + asiLocalData.length + 4 + unrecognizedLocalData.length];
        System.arraycopy(asiExtraField.getHeaderId().getBytes(), 0, testData, 0, 2);
        System.arraycopy(asiExtraField.getLocalFileDataLength().getBytes(), 0, testData, 2, 2);
        System.arraycopy(asiLocalData, 0, testData, 4, asiLocalData.length);
        System.arraycopy(unrecognizedExtraField.getHeaderId().getBytes(), 0, testData, 4 + asiLocalData.length, 2);
        System.arraycopy(unrecognizedExtraField.getLocalFileDataLength().getBytes(), 0, testData, 4 + asiLocalData.length + 2, 2);
        System.arraycopy(unrecognizedLocalData, 0, testData, 4 + asiLocalData.length + 4, unrecognizedLocalData.length);
    }

    @Test
    void testMergeLocalAndCentralDirectoryData() {
        // Test merging local file data
        final byte[] localData = ExtraFieldUtils.mergeLocalFileDataData(new ZipExtraField[] { asiExtraField, unrecognizedExtraField });
        assertEquals(testData.length, localData.length, "Local data length mismatch");
        for (int i = 0; i < localData.length; i++) {
            assertEquals(testData[i], localData[i], "Mismatch at local data byte " + i);
        }

        // Test merging central directory data
        final byte[] unrecognizedCentralData = unrecognizedExtraField.getCentralDirectoryData();
        final byte[] expectedCentralData = new byte[4 + asiLocalData.length + 4 + unrecognizedCentralData.length];
        System.arraycopy(testData, 0, expectedCentralData, 0, 4 + asiLocalData.length + 2);
        System.arraycopy(unrecognizedExtraField.getCentralDirectoryLength().getBytes(), 0, expectedCentralData, 4 + asiLocalData.length + 2, 2);
        System.arraycopy(unrecognizedCentralData, 0, expectedCentralData, 4 + asiLocalData.length + 4, unrecognizedCentralData.length);

        final byte[] centralData = ExtraFieldUtils.mergeCentralDirectoryData(new ZipExtraField[] { asiExtraField, unrecognizedExtraField });
        assertEquals(expectedCentralData.length, centralData.length, "Central data length mismatch");
        for (int i = 0; i < centralData.length; i++) {
            assertEquals(expectedCentralData[i], centralData[i], "Mismatch at central data byte " + i);
        }
    }

    @Test
    void testMergeWithUnparseableData() throws Exception {
        final ZipExtraField unparseableField = new UnparseableExtraFieldData();
        final byte[] unrecognizedHeaderBytes = UNRECOGNIZED_HEADER.getBytes();
        unparseableField.parseFromLocalFileData(new byte[] { unrecognizedHeaderBytes[0], unrecognizedHeaderBytes[1], 1, 0 }, 0, 4);

        final byte[] localData = ExtraFieldUtils.mergeLocalFileDataData(new ZipExtraField[] { asiExtraField, unparseableField });
        assertEquals(testData.length - 1, localData.length, "Local data length mismatch with unparseable field");
        for (int i = 0; i < localData.length; i++) {
            assertEquals(testData[i], localData[i], "Mismatch at local data byte " + i);
        }

        final byte[] unparseableCentralData = unparseableField.getCentralDirectoryData();
        final byte[] expectedCentralData = new byte[4 + asiLocalData.length + unparseableCentralData.length];
        System.arraycopy(testData, 0, expectedCentralData, 0, 4 + asiLocalData.length + 2);
        System.arraycopy(unparseableCentralData, 0, expectedCentralData, 4 + asiLocalData.length, unparseableCentralData.length);

        final byte[] centralData = ExtraFieldUtils.mergeCentralDirectoryData(new ZipExtraField[] { asiExtraField, unparseableField });
        assertEquals(expectedCentralData.length, centralData.length, "Central data length mismatch with unparseable field");
        for (int i = 0; i < centralData.length; i++) {
            assertEquals(expectedCentralData[i], centralData[i], "Mismatch at central data byte " + i);
        }
    }

    @Test
    void testParseExtraFields() throws Exception {
        final ZipExtraField[] parsedFields = ExtraFieldUtils.parse(testData);
        assertEquals(2, parsedFields.length, "Incorrect number of parsed fields");
        assertTrue(parsedFields[0] instanceof AsiExtraField, "First field is not AsiExtraField");
        assertEquals(040755, ((AsiExtraField) parsedFields[0]).getMode(), "Incorrect mode for AsiExtraField");
        assertTrue(parsedFields[1] instanceof UnrecognizedExtraField, "Second field is not UnrecognizedExtraField");
        assertEquals(1, parsedFields[1].getLocalFileDataLength().getValue(), "Incorrect data length for UnrecognizedExtraField");

        final byte[] invalidData = new byte[testData.length - 1];
        System.arraycopy(testData, 0, invalidData, 0, invalidData.length);
        final Exception exception = assertThrows(Exception.class, () -> ExtraFieldUtils.parse(invalidData), "Expected exception for invalid data");
        assertEquals("Bad extra field starting at " + (4 + asiLocalData.length) + ".  Block length of 1 bytes exceeds remaining data of 0 bytes.", exception.getMessage(), "Incorrect exception message");
    }

    @Test
    void testParseCentralDirectoryFields() throws Exception {
        final ZipExtraField[] parsedFields = ExtraFieldUtils.parse(testData, false);
        assertEquals(2, parsedFields.length, "Incorrect number of parsed fields");
        assertTrue(parsedFields[0] instanceof AsiExtraField, "First field is not AsiExtraField");
        assertEquals(040755, ((AsiExtraField) parsedFields[0]).getMode(), "Incorrect mode for AsiExtraField");
        assertTrue(parsedFields[1] instanceof UnrecognizedExtraField, "Second field is not UnrecognizedExtraField");
        assertEquals(1, parsedFields[1].getCentralDirectoryLength().getValue(), "Incorrect data length for UnrecognizedExtraField");
    }

    @Test
    void testParseArrayIndexOutOfBoundsToZipException() {
        ExtraFieldUtils.register(AiobThrowingExtraField.class);
        final AiobThrowingExtraField throwingField = new AiobThrowingExtraField();
        final byte[] data = new byte[4 + AiobThrowingExtraField.LENGTH];
        System.arraycopy(throwingField.getHeaderId().getBytes(), 0, data, 0, 2);
        System.arraycopy(throwingField.getLocalFileDataLength().getBytes(), 0, data, 2, 2);
        System.arraycopy(throwingField.getLocalFileDataData(), 0, data, 4, AiobThrowingExtraField.LENGTH);
        final ZipException exception = assertThrows(ZipException.class, () -> ExtraFieldUtils.parse(data), "Expected ZipException for invalid data");
        assertEquals("Failed to parse corrupt ZIP extra field of type 1000", exception.getMessage(), "Incorrect exception message");
    }

    @Test
    void testParseWithReadBehavior() throws Exception {
        ZipExtraField[] parsedFields = ExtraFieldUtils.parse(testData, true, ExtraFieldUtils.UnparseableExtraField.READ);
        assertEquals(2, parsedFields.length, "Incorrect number of parsed fields");
        assertTrue(parsedFields[0] instanceof AsiExtraField, "First field is not AsiExtraField");
        assertEquals(040755, ((AsiExtraField) parsedFields[0]).getMode(), "Incorrect mode for AsiExtraField");
        assertTrue(parsedFields[1] instanceof UnrecognizedExtraField, "Second field is not UnrecognizedExtraField");
        assertEquals(1, parsedFields[1].getLocalFileDataLength().getValue(), "Incorrect data length for UnrecognizedExtraField");

        final byte[] invalidData = new byte[testData.length - 1];
        System.arraycopy(testData, 0, invalidData, 0, invalidData.length);
        parsedFields = ExtraFieldUtils.parse(invalidData, true, ExtraFieldUtils.UnparseableExtraField.READ);
        assertEquals(2, parsedFields.length, "Incorrect number of parsed fields");
        assertTrue(parsedFields[0] instanceof AsiExtraField, "First field is not AsiExtraField");
        assertEquals(040755, ((AsiExtraField) parsedFields[0]).getMode(), "Incorrect mode for AsiExtraField");
        assertTrue(parsedFields[1] instanceof UnparseableExtraFieldData, "Second field is not UnparseableExtraFieldData");
        assertEquals(4, parsedFields[1].getLocalFileDataLength().getValue(), "Incorrect data length for UnparseableExtraFieldData");
        for (int i = 0; i < 4; i++) {
            assertEquals(invalidData[testData.length - 5 + i], parsedFields[1].getLocalFileDataData()[i], "Mismatch at byte " + i);
        }
    }

    @Test
    void testParseWithSkipBehavior() throws Exception {
        ZipExtraField[] parsedFields = ExtraFieldUtils.parse(testData, true, ExtraFieldUtils.UnparseableExtraField.SKIP);
        assertEquals(2, parsedFields.length, "Incorrect number of parsed fields");
        assertTrue(parsedFields[0] instanceof AsiExtraField, "First field is not AsiExtraField");
        assertEquals(040755, ((AsiExtraField) parsedFields[0]).getMode(), "Incorrect mode for AsiExtraField");
        assertTrue(parsedFields[1] instanceof UnrecognizedExtraField, "Second field is not UnrecognizedExtraField");
        assertEquals(1, parsedFields[1].getLocalFileDataLength().getValue(), "Incorrect data length for UnrecognizedExtraField");

        final byte[] invalidData = new byte[testData.length - 1];
        System.arraycopy(testData, 0, invalidData, 0, invalidData.length);
        parsedFields = ExtraFieldUtils.parse(invalidData, true, ExtraFieldUtils.UnparseableExtraField.SKIP);
        assertEquals(1, parsedFields.length, "Incorrect number of parsed fields");
        assertTrue(parsedFields[0] instanceof AsiExtraField, "First field is not AsiExtraField");
        assertEquals(040755, ((AsiExtraField) parsedFields[0]).getMode(), "Incorrect mode for AsiExtraField");
    }
}