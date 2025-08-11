/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.commons.compress.archivers.zip;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.zip.ZipException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for ExtraFieldUtils.
 *
 * Notes:
 * - Uses assertArrayEquals instead of byte-by-byte loops.
 * - Extracts helper methods to encode fields and concatenate blocks.
 * - Uses descriptive variable names and inline comments to explain intent.
 */
class ExtraFieldUtilsTest {

    /**
     * A ZipExtraField that throws AIOOBE when asked to parse.
     * Used to verify that ExtraFieldUtils wraps it into ZipException.
     */
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
     * Header-ID of a ZipExtraField not supported by Commons Compress.
     *
     * Used to be ZipShort(1) but this is the ID of the Zip64 extra field.
     */
    static final ZipShort UNRECOGNIZED_HEADER = new ZipShort(0x5555);

    /**
     * Header-ID of a ZipExtraField not supported by Commons Compress used for the ArrayIndexOutOfBoundsTest.
     */
    static final ZipShort AIOB_HEADER = new ZipShort(0x1000);

    private AsiExtraField asi;
    private UnrecognizedExtraField unrecognized;
    private byte[] expectedLocalData; // full local extra data for [asi, unrecognized]
    private byte[] asiLocalPayload;   // asi.getLocalFileDataData()

    @BeforeEach
    void setUp() {
        // asi: directory with mode 0755
        asi = new AsiExtraField();
        asi.setMode(0755);
        asi.setDirectory(true);

        // unrecognized: an extra field with header UNRECOGNIZED_HEADER and one byte payload (both local and central)
        unrecognized = new UnrecognizedExtraField();
        unrecognized.setHeaderId(UNRECOGNIZED_HEADER);
        unrecognized.setLocalFileDataData(new byte[] { 0 });
        unrecognized.setCentralDirectoryData(new byte[] { 0 });

        asiLocalPayload = asi.getLocalFileDataData();

        // Expected local extra data = encode(asi as local) + encode(unrecognized as local)
        expectedLocalData = concat(
                encodeLocalField(asi),
                encodeLocalField(unrecognized)
        );
    }

    /**
     * mergeLocalFileDataData and mergeCentralDirectoryData should concatenate encoded extra field blocks.
     */
    @Test
    void testMerge() {
        final byte[] actualLocal = ExtraFieldUtils.mergeLocalFileDataData(new ZipExtraField[] { asi, unrecognized });
        assertArrayEquals(expectedLocalData, actualLocal, "local extra data");

        final byte[] expectedCentralData = concat(
                encodeCentralField(asi),
                encodeCentralField(unrecognized)
        );
        final byte[] actualCentral = ExtraFieldUtils.mergeCentralDirectoryData(new ZipExtraField[] { asi, unrecognized });
        assertArrayEquals(expectedCentralData, actualCentral, "central extra data");
    }

    /**
     * When local extra data contains an incomplete block (header + length but missing payload),
     * UnparseableExtraFieldData stores those raw bytes and mergeLocalFileDataData should write them as-is.
     */
    @Test
    void testMergeWithUnparseableData() throws Exception {
        // Build an "unparseable" local block: header + length(=1) but no payload byte
        final ZipExtraField unparseable = new UnparseableExtraFieldData();
        final byte[] headerBytes = UNRECOGNIZED_HEADER.getBytes();
        unparseable.parseFromLocalFileData(new byte[] { headerBytes[0], headerBytes[1], 1, 0 }, 0, 4);

        // Local: expected is the original local data without the missing last payload byte
        final byte[] actualLocal = ExtraFieldUtils.mergeLocalFileDataData(new ZipExtraField[] { asi, unparseable });
        final byte[] expectedLocalWithoutLastByte = Arrays.copyOf(expectedLocalData, expectedLocalData.length - 1);
        assertArrayEquals(expectedLocalWithoutLastByte, actualLocal, "local extra data with unparseable tail");

        // Central: expected is [asi-block] + [unknown-header] + [central-length] + [central-data]
        final byte[] unparseableCentral = unparseable.getCentralDirectoryData();
        final int asiBlockEnd = 4 + asiLocalPayload.length; // 2(header) + 2(length) + payload
        final byte[] expectedCentral = new byte[asiBlockEnd + 4 + unparseableCentral.length];

        // Copy: everything up to and including the unknown header (2 bytes) of the second block
        System.arraycopy(expectedLocalData, 0, expectedCentral, 0, asiBlockEnd + 2);
        // Replace length with the central directory length of the unparseable field
        System.arraycopy(unparseable.getCentralDirectoryLength().getBytes(), 0, expectedCentral, asiBlockEnd + 2, 2);
        // Append the central directory data of the unparseable field
        System.arraycopy(unparseableCentral, 0, expectedCentral, asiBlockEnd + 4, unparseableCentral.length);

        final byte[] actualCentral = ExtraFieldUtils.mergeCentralDirectoryData(new ZipExtraField[] { asi, unparseable });
        assertArrayEquals(expectedCentral, actualCentral, "central extra data with unparseable tail");
    }

    /**
     * parse(byte[]) should return the fields and fail with a clear message if the input is truncated.
     */
    @Test
    void testParse() throws Exception {
        final ZipExtraField[] fields = ExtraFieldUtils.parse(expectedLocalData);
        assertEquals(2, fields.length, "number of fields");
        assertTrue(fields[0] instanceof AsiExtraField, "first field type");
        assertEquals(040755, ((AsiExtraField) fields[0]).getMode(), "asi mode");
        assertTrue(fields[1] instanceof UnrecognizedExtraField, "second field type");
        assertEquals(1, fields[1].getLocalFileDataLength().getValue(), "unrecognized local length");

        // Truncate by one byte to force a "block length exceeds remaining data" error
        final byte[] truncated = Arrays.copyOf(expectedLocalData, expectedLocalData.length - 1);
        final Exception ex = assertThrows(Exception.class, () -> ExtraFieldUtils.parse(truncated), "data should be invalid");
        final int secondBlockStart = 4 + asiLocalPayload.length; // after the asi block
        assertEquals(
                "Bad extra field starting at " + secondBlockStart + ".  Block length of 1 bytes exceeds remaining data of 0 bytes.",
                ex.getMessage(),
                "exception message"
        );
    }

    /**
     * parse(data, false) should interpret data as central directory.
     */
    @Test
    void testParseCentral() throws Exception {
        final ZipExtraField[] fields = ExtraFieldUtils.parse(expectedLocalData, false);
        assertEquals(2, fields.length, "number of fields");
        assertTrue(fields[0] instanceof AsiExtraField, "first field type");
        assertEquals(040755, ((AsiExtraField) fields[0]).getMode(), "asi mode");
        assertTrue(fields[1] instanceof UnrecognizedExtraField, "second field type");
        assertEquals(1, fields[1].getCentralDirectoryLength().getValue(), "unrecognized central length");
    }

    /**
     * Any ArrayIndexOutOfBoundsException thrown by a ZipExtraField parser must be wrapped as ZipException.
     */
    @Test
    void testParseTurnsArrayIndexOutOfBoundsIntoZipException() {
        ExtraFieldUtils.register(AiobThrowingExtraField.class);
        final AiobThrowingExtraField f = new AiobThrowingExtraField();
        final byte[] input = encodeLocalField(f);

        final ZipException ex = assertThrows(ZipException.class, () -> ExtraFieldUtils.parse(input), "data should be invalid");
        assertEquals("Failed to parse corrupt ZIP extra field of type 1000", ex.getMessage(), "exception message");
    }

    /**
     * With UnparseableExtraField.READ, the unparseable tail should be preserved as an UnparseableExtraFieldData instance.
     */
    @Test
    void testParseWithRead() throws Exception {
        ZipExtraField[] fields = ExtraFieldUtils.parse(expectedLocalData, true, ExtraFieldUtils.UnparseableExtraField.READ);
        assertEquals(2, fields.length, "number of fields");
        assertTrue(fields[0] instanceof AsiExtraField, "first field type");
        assertEquals(040755, ((AsiExtraField) fields[0]).getMode(), "asi mode");
        assertTrue(fields[1] instanceof UnrecognizedExtraField, "second field type");
        assertEquals(1, fields[1].getLocalFileDataLength().getValue(), "unrecognized local length");

        // Truncate input to create an unparseable tail (the last field lacks its payload byte).
        final byte[] truncated = Arrays.copyOf(expectedLocalData, expectedLocalData.length - 1);
        fields = ExtraFieldUtils.parse(truncated, true, ExtraFieldUtils.UnparseableExtraField.READ);
        assertEquals(2, fields.length, "number of fields after READ");
        assertTrue(fields[0] instanceof AsiExtraField, "first field type after READ");
        assertEquals(040755, ((AsiExtraField) fields[0]).getMode(), "asi mode after READ");
        assertTrue(fields[1] instanceof UnparseableExtraFieldData, "second field type after READ");
        assertEquals(4, fields[1].getLocalFileDataLength().getValue(), "unparseable tail length");

        // The preserved raw tail equals the last 4 bytes of the truncated array (header + length).
        final byte[] expectedRawTail = Arrays.copyOfRange(truncated, truncated.length - 4, truncated.length);
        assertArrayEquals(expectedRawTail, fields[1].getLocalFileDataData(), "raw tail bytes");
    }

    /**
     * With UnparseableExtraField.SKIP, the unparseable tail should be discarded.
     */
    @Test
    void testParseWithSkip() throws Exception {
        ZipExtraField[] fields = ExtraFieldUtils.parse(expectedLocalData, true, ExtraFieldUtils.UnparseableExtraField.SKIP);
        assertEquals(2, fields.length, "number of fields");
        assertTrue(fields[0] instanceof AsiExtraField, "first field type");
        assertEquals(040755, ((AsiExtraField) fields[0]).getMode(), "asi mode");
        assertTrue(fields[1] instanceof UnrecognizedExtraField, "second field type");
        assertEquals(1, fields[1].getLocalFileDataLength().getValue(), "unrecognized local length");

        final byte[] truncated = Arrays.copyOf(expectedLocalData, expectedLocalData.length - 1);
        fields = ExtraFieldUtils.parse(truncated, true, ExtraFieldUtils.UnparseableExtraField.SKIP);
        assertEquals(1, fields.length, "unparseable field skipped");
        assertTrue(fields[0] instanceof AsiExtraField, "remaining field type");
        assertEquals(040755, ((AsiExtraField) fields[0]).getMode(), "remaining asi mode");
    }

    // ----------------------------
    // Helpers
    // ----------------------------

    private static byte[] encodeLocalField(final ZipExtraField field) {
        return encodeField(field, true);
    }

    private static byte[] encodeCentralField(final ZipExtraField field) {
        return encodeField(field, false);
    }

    /**
     * Encodes a ZipExtraField into [header(2), length(2), data(...)].
     * If local == true, uses local file data; otherwise uses central directory data.
     */
    private static byte[] encodeField(final ZipExtraField field, final boolean local) {
        final byte[] header = field.getHeaderId().getBytes();
        final byte[] length = (local ? field.getLocalFileDataLength() : field.getCentralDirectoryLength()).getBytes();
        final byte[] payload = local ? field.getLocalFileDataData() : field.getCentralDirectoryData();

        final byte[] out = new byte[header.length + length.length + payload.length];
        System.arraycopy(header, 0, out, 0, 2);
        System.arraycopy(length, 0, out, 2, 2);
        System.arraycopy(payload, 0, out, 4, payload.length);
        return out;
    }

    private static byte[] concat(final byte[]... parts) {
        int total = 0;
        for (byte[] p : parts) {
            total += p.length;
        }
        final byte[] result = new byte[total];
        int pos = 0;
        for (byte[] p : parts) {
            System.arraycopy(p, 0, result, pos, p.length);
            pos += p.length;
        }
        return result;
    }
}