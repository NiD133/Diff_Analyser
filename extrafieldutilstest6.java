package org.apache.commons.compress.archivers.zip;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.zip.ZipException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ExtraFieldUtilsTestTest6 implements UnixStat {

    /**
     * Header-ID of a ZipExtraField not supported by Commons Compress.
     *
     * <p>
     * Used to be ZipShort(1) but this is the ID of the Zip64 extra field.
     * </p>
     */
    static final ZipShort UNRECOGNIZED_HEADER = new ZipShort(0x5555);

    /**
     * Header-ID of a ZipExtraField not supported by Commons Compress used for the ArrayIndexOutOfBoundsTest.
     */
    static final ZipShort AIOB_HEADER = new ZipShort(0x1000);

    private AsiExtraField a;

    private UnrecognizedExtraField dummy;

    private byte[] data;

    private byte[] aLocal;

    @BeforeEach
    public void setUp() {
        a = new AsiExtraField();
        a.setMode(0755);
        a.setDirectory(true);
        dummy = new UnrecognizedExtraField();
        dummy.setHeaderId(UNRECOGNIZED_HEADER);
        dummy.setLocalFileDataData(new byte[] { 0 });
        dummy.setCentralDirectoryData(new byte[] { 0 });
        aLocal = a.getLocalFileDataData();
        final byte[] dummyLocal = dummy.getLocalFileDataData();
        data = new byte[4 + aLocal.length + 4 + dummyLocal.length];
        System.arraycopy(a.getHeaderId().getBytes(), 0, data, 0, 2);
        System.arraycopy(a.getLocalFileDataLength().getBytes(), 0, data, 2, 2);
        System.arraycopy(aLocal, 0, data, 4, aLocal.length);
        System.arraycopy(dummy.getHeaderId().getBytes(), 0, data, 4 + aLocal.length, 2);
        System.arraycopy(dummy.getLocalFileDataLength().getBytes(), 0, data, 4 + aLocal.length + 2, 2);
        System.arraycopy(dummyLocal, 0, data, 4 + aLocal.length + 4, dummyLocal.length);
    }

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

    @Test
    void testParseWithRead() throws Exception {
        ZipExtraField[] ze = ExtraFieldUtils.parse(data, true, ExtraFieldUtils.UnparseableExtraField.READ);
        assertEquals(2, ze.length, "number of fields");
        assertTrue(ze[0] instanceof AsiExtraField, "type field 1");
        assertEquals(040755, ((AsiExtraField) ze[0]).getMode(), "mode field 1");
        assertTrue(ze[1] instanceof UnrecognizedExtraField, "type field 2");
        assertEquals(1, ze[1].getLocalFileDataLength().getValue(), "data length field 2");
        final byte[] data2 = new byte[data.length - 1];
        System.arraycopy(data, 0, data2, 0, data2.length);
        ze = ExtraFieldUtils.parse(data2, true, ExtraFieldUtils.UnparseableExtraField.READ);
        assertEquals(2, ze.length, "number of fields");
        assertTrue(ze[0] instanceof AsiExtraField, "type field 1");
        assertEquals(040755, ((AsiExtraField) ze[0]).getMode(), "mode field 1");
        assertTrue(ze[1] instanceof UnparseableExtraFieldData, "type field 2");
        assertEquals(4, ze[1].getLocalFileDataLength().getValue(), "data length field 2");
        for (int i = 0; i < 4; i++) {
            assertEquals(data2[data.length - 5 + i], ze[1].getLocalFileDataData()[i], "byte number " + i);
        }
    }
}
