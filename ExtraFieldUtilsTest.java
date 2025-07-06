package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.zip.ZipExtraField;
import org.apache.commons.compress.archivers.zip.UnparseableExtraField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ExtraFieldUtilsTest implements UnixStat {

    // Constants
    private static final ZipShort UNRECOGNIZED_HEADER = new ZipShort(0x5555);
    private static final ZipShort AIOB_HEADER = new ZipShort(0x1000);

    // Test data
    private AsiExtraField asiExtraField;
    private UnrecognizedExtraField unrecognizedExtraField;
    private byte[] data;
    private byte[] asiLocalFileData;

    @BeforeEach
    public void setUp() {
        // Initialize test data
        asiExtraField = new AsiExtraField();
        asiExtraField.setMode(0755);
        asiExtraField.setDirectory(true);

        unrecognizedExtraField = new UnrecognizedExtraField();
        unrecognizedExtraField.setHeaderId(UNRECOGNIZED_HEADER);
        unrecognizedExtraField.setLocalFileDataData(new byte[] { 0 });
        unrecognizedExtraField.setCentralDirectoryData(new byte[] { 0 });

        asiLocalFileData = asiExtraField.getLocalFileDataData();
        byte[] unrecognizedLocalFileData = unrecognizedExtraField.getLocalFileDataData();

        data = new byte[4 + asiLocalFileData.length + 4 + unrecognizedLocalFileData.length];
        System.arraycopy(asiExtraField.getHeaderId().getBytes(), 0, data, 0, 2);
        System.arraycopy(asiExtraField.getLocalFileDataLength().getBytes(), 0, data, 2, 2);
        System.arraycopy(asiLocalFileData, 0, data, 4, asiLocalFileData.length);
        System.arraycopy(unrecognizedExtraField.getHeaderId().getBytes(), 0, data, 4 + asiLocalFileData.length, 2);
        System.arraycopy(unrecognizedExtraField.getLocalFileDataLength().getBytes(), 0, data, 4 + asiLocalFileData.length + 2, 2);
        System.arraycopy(unrecognizedLocalFileData, 0, data, 4 + asiLocalFileData.length + 4, unrecognizedLocalFileData.length);
    }

    @Test
    public void testMergeLocalFileData() {
        // Merge local file data of multiple extra fields
        ZipExtraField[] extraFields = { asiExtraField, unrecognizedExtraField };
        byte[] mergedLocalFileData = ExtraFieldUtils.mergeLocalFileDataData(extraFields);

        // Verify the merged local file data
        assertEquals(data.length, mergedLocalFileData.length, "Merged local file data length");
        for (int i = 0; i < mergedLocalFileData.length; i++) {
            assertEquals(data[i], mergedLocalFileData[i], "Merged local file data byte " + i);
        }
    }

    @Test
    public void testMergeCentralDirectoryData() {
        // Merge central directory data of multiple extra fields
        ZipExtraField[] extraFields = { asiExtraField, unrecognizedExtraField };
        byte[] mergedCentralDirectoryData = ExtraFieldUtils.mergeCentralDirectoryData(extraFields);

        // Verify the merged central directory data
        byte[] expectedCentralDirectoryData = new byte[4 + asiLocalFileData.length + 4 + unrecognizedExtraField.getCentralDirectoryData().length];
        System.arraycopy(data, 0, expectedCentralDirectoryData, 0, 4 + asiLocalFileData.length + 2);
        System.arraycopy(unrecognizedExtraField.getCentralDirectoryLength().getBytes(), 0, expectedCentralDirectoryData, 4 + asiLocalFileData.length + 2, 2);
        System.arraycopy(unrecognizedExtraField.getCentralDirectoryData(), 0, expectedCentralDirectoryData, 4 + asiLocalFileData.length + 4, unrecognizedExtraField.getCentralDirectoryData().length);

        assertEquals(expectedCentralDirectoryData.length, mergedCentralDirectoryData.length, "Merged central directory data length");
        for (int i = 0; i < mergedCentralDirectoryData.length; i++) {
            assertEquals(expectedCentralDirectoryData[i], mergedCentralDirectoryData[i], "Merged central directory data byte " + i);
        }
    }

    @Test
    public void testParseLocalFileData() throws Exception {
        // Parse local file data into extra fields
        ZipExtraField[] extraFields = ExtraFieldUtils.parse(data);

        // Verify the parsed extra fields
        assertEquals(2, extraFields.length, "Number of extra fields");
        assertTrue(extraFields[0] instanceof AsiExtraField, "Type of first extra field");
        assertEquals(040755, ((AsiExtraField) extraFields[0]).getMode(), "Mode of first extra field");
        assertTrue(extraFields[1] instanceof UnrecognizedExtraField, "Type of second extra field");
        assertEquals(1, extraFields[1].getLocalFileDataLength().getValue(), "Data length of second extra field");
    }

    @Test
    public void testParseCentralDirectoryData() throws Exception {
        // Parse central directory data into extra fields
        ZipExtraField[] extraFields = ExtraFieldUtils.parse(data, false);

        // Verify the parsed extra fields
        assertEquals(2, extraFields.length, "Number of extra fields");
        assertTrue(extraFields[0] instanceof AsiExtraField, "Type of first extra field");
        assertEquals(040755, ((AsiExtraField) extraFields[0]).getMode(), "Mode of first extra field");
        assertTrue(extraFields[1] instanceof UnrecognizedExtraField, "Type of second extra field");
        assertEquals(1, extraFields[1].getCentralDirectoryLength().getValue(), "Data length of second extra field");
    }

    @Test
    public void testParseUnparseableExtraField() throws Exception {
        // Create an unparseable extra field
        UnparseableExtraFieldData unparseableExtraField = new UnparseableExtraFieldData();
        byte[] unparseableExtraFieldData = new byte[] { UNRECOGNIZED_HEADER.getBytes()[0], UNRECOGNIZED_HEADER.getBytes()[1], 1, 0 };
        unparseableExtraField.parseFromLocalFileData(unparseableExtraFieldData, 0, 4);

        // Parse the unparseable extra field
        ZipExtraField[] extraFields = ExtraFieldUtils.parse(data, true, UnparseableExtraField.READ);

        // Verify the parsed extra fields
        assertEquals(2, extraFields.length, "Number of extra fields");
        assertTrue(extraFields[0] instanceof AsiExtraField, "Type of first extra field");
        assertEquals(040755, ((AsiExtraField) extraFields[0]).getMode(), "Mode of first extra field");
        assertTrue(extraFields[1] instanceof UnrecognizedExtraField, "Type of second extra field");
        assertEquals(1, extraFields[1].getLocalFileDataLength().getValue(), "Data length of second extra field");
    }

    @Test
    public void testParseArrayIndexOutOfBoundsException() {
        // Register an extra field that throws ArrayIndexOutOfBoundsException
        ExtraFieldUtils.register(AiobThrowingExtraField.class);

        // Create data for the extra field
        AiobThrowingExtraField aiobThrowingExtraField = new AiobThrowingExtraField();
        byte[] data = new byte[4 + AiobThrowingExtraField.LENGTH];
        System.arraycopy(aiobThrowingExtraField.getHeaderId().getBytes(), 0, data, 0, 2);
        System.arraycopy(aiobThrowingExtraField.getLocalFileDataLength().getBytes(), 0, data, 2, 2);
        System.arraycopy(aiobThrowingExtraField.getLocalFileDataData(), 0, data, 4, AiobThrowingExtraField.LENGTH);

        // Parse the data and verify the exception
        ZipException exception = assertThrows(ZipException.class, () -> ExtraFieldUtils.parse(data));
        assertEquals("Failed to parse corrupt ZIP extra field of type 1000", exception.getMessage());
    }
}

// Supporting classes
class AiobThrowingExtraField implements ZipExtraField {
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

class UnparseableExtraFieldData implements ZipExtraField {
    private byte[] data;

    @Override
    public byte[] getCentralDirectoryData() {
        return data;
    }

    @Override
    public ZipShort getCentralDirectoryLength() {
        return new ZipShort(data.length);
    }

    @Override
    public ZipShort getHeaderId() {
        return UNRECOGNIZED_HEADER;
    }

    @Override
    public byte[] getLocalFileDataData() {
        return data;
    }

    @Override
    public ZipShort getLocalFileDataLength() {
        return new ZipShort(data.length);
    }

    @Override
    public void parseFromCentralDirectoryData(final byte[] buffer, final int offset, final int length) {
        data = new byte[length];
        System.arraycopy(buffer, offset, data, 0, length);
    }

    @Override
    public void parseFromLocalFileData(final byte[] buffer, final int offset, final int length) {
        data = new byte[length];
        System.arraycopy(buffer, offset, data, 0, length);
    }
}