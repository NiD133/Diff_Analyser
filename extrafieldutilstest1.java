package org.apache.commons.compress.archivers.zip;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.zip.ZipException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ExtraFieldUtilsTestTest1 implements UnixStat {

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

    /**
     * Test merge methods
     */
    @Test
    void testMerge() {
        final byte[] local = ExtraFieldUtils.mergeLocalFileDataData(new ZipExtraField[] { a, dummy });
        assertEquals(data.length, local.length, "local length");
        for (int i = 0; i < local.length; i++) {
            assertEquals(data[i], local[i], "local byte " + i);
        }
        final byte[] dummyCentral = dummy.getCentralDirectoryData();
        final byte[] data2 = new byte[4 + aLocal.length + 4 + dummyCentral.length];
        System.arraycopy(data, 0, data2, 0, 4 + aLocal.length + 2);
        System.arraycopy(dummy.getCentralDirectoryLength().getBytes(), 0, data2, 4 + aLocal.length + 2, 2);
        System.arraycopy(dummyCentral, 0, data2, 4 + aLocal.length + 4, dummyCentral.length);
        final byte[] central = ExtraFieldUtils.mergeCentralDirectoryData(new ZipExtraField[] { a, dummy });
        assertEquals(data2.length, central.length, "central length");
        for (int i = 0; i < central.length; i++) {
            assertEquals(data2[i], central[i], "central byte " + i);
        }
    }
}
