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

import java.util.zip.ZipException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * JUnit tests for {@link ExtraFieldUtils}.
 *
 * <p>
 *   This class tests the functionality for parsing, merging, and handling zip extra fields.
 *   It includes tests for standard extra fields, unrecognized extra fields, and error handling.
 * </p>
 */
class ExtraFieldUtilsTest implements UnixStat {

    /**
     * A custom ZipExtraField implementation that throws ArrayIndexOutOfBoundsException when parsing.
     * This is used to test exception handling in ExtraFieldUtils.
     */
    public static class AiobThrowingExtraField implements ZipExtraField {
        private static final int LENGTH = 4;
        private static final ZipShort AIOB_HEADER = new ZipShort(0x1000);

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
            throw new ArrayIndexOutOfBoundsException("Simulated exception during parsing.");
        }
    }

    /**
     * Header-ID of a ZipExtraField not supported by Commons Compress.
     */
    private static final ZipShort UNRECOGNIZED_HEADER = new ZipShort(0x5555);

    /**
     * Header-ID of a ZipExtraField used for the ArrayIndexOutOfBoundsTest.
     */
    private static final ZipShort AIOB_HEADER = new ZipShort(0x1000);

    private AsiExtraField asiExtraField;
    private UnrecognizedExtraField unrecognizedExtraField;
    private byte[] combinedData;
    private byte[] asiLocalData;

    @BeforeEach
    public void setUp() {
        // Initialize a standard AsiExtraField (Unix file attributes)
        asiExtraField = new AsiExtraField();
        asiExtraField.setMode(0755); // Permissions: rwxr-xr-x
        asiExtraField.setDirectory(true); // It's a directory

        // Initialize an UnrecognizedExtraField with a custom header ID
        unrecognizedExtraField = new UnrecognizedExtraField();
        unrecognizedExtraField.setHeaderId(UNRECOGNIZED_HEADER);
        unrecognizedExtraField.setLocalFileDataData(new byte[] { 0 });
        unrecognizedExtraField.setCentralDirectoryData(new byte[] { 0 });

        asiLocalData = asiExtraField.getLocalFileDataData();
        final byte[] unrecognizedLocalData = unrecognizedExtraField.getLocalFileDataData();

        // Combine the byte data of the two extra fields into a single byte array for parsing tests.
        // This simulates the structure of extra fields within a zip entry.
        combinedData = new byte[4 + asiLocalData.length + 4 + unrecognizedLocalData.length];

        // Copy AsiExtraField header ID and length
        System.arraycopy(asiExtraField.getHeaderId().getBytes(), 0, combinedData, 0, 2);
        System.arraycopy(asiExtraField.getLocalFileDataLength().getBytes(), 0, combinedData, 2, 2);
        System.arraycopy(asiLocalData, 0, combinedData, 4, asiLocalData.length);

        // Copy UnrecognizedExtraField header ID and length
        System.arraycopy(unrecognizedExtraField.getHeaderId().getBytes(), 0, combinedData, 4 + asiLocalData.length, 2);
        System.arraycopy(unrecognizedExtraField.getLocalFileDataLength().getBytes(), 0, combinedData, 4 + asiLocalData.length + 2, 2);
        System.arraycopy(unrecognizedLocalData, 0, combinedData, 4 + asiLocalData.length + 4, unrecognizedLocalData.length);
    }

    /**
     * Test the mergeLocalFileDataData and mergeCentralDirectoryData methods.
     * These methods combine the data from multiple ZipExtraField instances into a single byte array.
     */
    @Test
    void testMerge() {
        // Merge the local file data
        final byte[] local = ExtraFieldUtils.mergeLocalFileDataData(new ZipExtraField[] { asiExtraField, unrecognizedExtraField });
        assertEquals(combinedData.length, local.length, "local length");
        assertArrayEquals(combinedData, local, "local data should match");


        // Create expected central directory data
        final byte[] unrecognizedCentralData = unrecognizedExtraField.getCentralDirectoryData();
        final byte[] expectedCentralData = new byte[4 + asiLocalData.length + 4 + unrecognizedCentralData.length];
        System.arraycopy(combinedData, 0, expectedCentralData, 0, 4 + asiLocalData.length + 2);
        System.arraycopy(unrecognizedExtraField.getCentralDirectoryLength().getBytes(), 0, expectedCentralData, 4 + asiLocalData.length + 2, 2);
        System.arraycopy(unrecognizedCentralData, 0, expectedCentralData, 4 + asiLocalData.length + 4, unrecognizedCentralData.length);

        // Merge the central directory data
        final byte[] central = ExtraFieldUtils.mergeCentralDirectoryData(new ZipExtraField[] { asiExtraField, unrecognizedExtraField });
        assertEquals(expectedCentralData.length, central.length, "central length");
        assertArrayEquals(expectedCentralData, central, "central data should match");
    }

    /**
     * Tests the merge methods with an UnparseableExtraFieldData, which represents extra field data that could not be parsed.
     */
    @Test
    void testMergeWithUnparseableData() throws Exception {
        final ZipExtraField unparseableField = new UnparseableExtraFieldData();
        final byte[] headerBytes = UNRECOGNIZED_HEADER.getBytes();
        unparseableField.parseFromLocalFileData(new byte[] { headerBytes[0], headerBytes[1], 1, 0 }, 0, 4);

        // Merge Local File Data
        final byte[] local = ExtraFieldUtils.mergeLocalFileDataData(new ZipExtraField[] { asiExtraField, unparseableField });
        final byte[] expectedLocal = new byte[combinedData.length - 1];
        System.arraycopy(combinedData, 0, expectedLocal, 0, expectedLocal.length);
        assertEquals(expectedLocal.length, local.length, "local length");
        assertArrayEquals(expectedLocal, local, "local byte data");


        // Create Expected Central Directory Data
        final byte[] unparseableCentralData = unparseableField.getCentralDirectoryData();
        final byte[] expectedCentral = new byte[4 + asiLocalData.length + unparseableCentralData.length];
        System.arraycopy(combinedData, 0, expectedCentral, 0, 4 + asiLocalData.length + 2);
        System.arraycopy(unparseableCentralData, 0, expectedCentral, 4 + asiLocalData.length, unparseableCentralData.length);

        // Merge Central Directory Data
        final byte[] central = ExtraFieldUtils.mergeCentralDirectoryData(new ZipExtraField[] { asiExtraField, unparseableField });
        assertEquals(expectedCentral.length, central.length, "central length");
        assertArrayEquals(expectedCentral, central, "central byte data");
    }

    /**
     * Test the parse method, which converts a byte array into an array of ZipExtraField instances.
     */
    @Test
    void testParse() throws Exception {
        // Parse the combined data
        final ZipExtraField[] parsedFields = ExtraFieldUtils.parse(combinedData);
        assertEquals(2, parsedFields.length, "number of fields");
        assertTrue(parsedFields[0] instanceof AsiExtraField, "type field 1");
        assertEquals(040755, ((AsiExtraField) parsedFields[0]).getMode(), "mode field 1");
        assertTrue(parsedFields[1] instanceof UnrecognizedExtraField, "type field 2");
        assertEquals(1, parsedFields[1].getLocalFileDataLength().getValue(), "data length field 2");

        // Test parsing with invalid data (truncated byte array)
        final byte[] truncatedData = new byte[combinedData.length - 1];
        System.arraycopy(combinedData, 0, truncatedData, 0, truncatedData.length);
        final ZipException e = assertThrows(ZipException.class, () -> ExtraFieldUtils.parse(truncatedData), "data should be invalid");
        assertEquals("Bad extra field starting at " + (4 + asiLocalData.length) + ".  Block length of 1 bytes exceeds remaining data of 0 bytes.", e.getMessage(),
                "message");
    }

    /**
     * Tests parsing extra fields from central directory data.
     */
    @Test
    void testParseCentral() throws Exception {
        // Parse the combined data as central directory data
        final ZipExtraField[] parsedFields = ExtraFieldUtils.parse(combinedData, false);
        assertEquals(2, parsedFields.length, "number of fields");
        assertTrue(parsedFields[0] instanceof AsiExtraField, "type field 1");
        assertEquals(040755, ((AsiExtraField) parsedFields[0]).getMode(), "mode field 1");
        assertTrue(parsedFields[1] instanceof UnrecognizedExtraField, "type field 2");
        assertEquals(1, parsedFields[1].getCentralDirectoryLength().getValue(), "data length field 2");
    }

    /**
     * Tests that an ArrayIndexOutOfBoundsException during parsing is converted to a ZipException.
     */
    @Test
    void testParseTurnsArrayIndexOutOfBoundsIntoZipException() {
        // Register the custom AiobThrowingExtraField
        ExtraFieldUtils.register(AiobThrowingExtraField.class);
        final AiobThrowingExtraField throwingField = new AiobThrowingExtraField();
        final byte[] data = new byte[4 + AiobThrowingExtraField.LENGTH];

        // Create data for the throwing extra field
        System.arraycopy(throwingField.getHeaderId().getBytes(), 0, data, 0, 2);
        System.arraycopy(throwingField.getLocalFileDataLength().getBytes(), 0, data, 2, 2);
        System.arraycopy(throwingField.getLocalFileDataData(), 0, data, 4, AiobThrowingExtraField.LENGTH);

        // Verify that parsing throws a ZipException with the expected message
        final ZipException e = assertThrows(ZipException.class, () -> ExtraFieldUtils.parse(data), "data should be invalid");
        assertEquals("Failed to parse corrupt ZIP extra field of type 1000", e.getMessage(), "message");
    }

    /**
     * Tests parsing with the READ UnparseableExtraField option.
     */
    @Test
    void testParseWithRead() throws Exception {
        // Parse the combined data, reading unparseable fields
        ZipExtraField[] parsedFields = ExtraFieldUtils.parse(combinedData, true, ExtraFieldUtils.UnparseableExtraField.READ);
        assertEquals(2, parsedFields.length, "number of fields");
        assertTrue(parsedFields[0] instanceof AsiExtraField, "type field 1");
        assertEquals(040755, ((AsiExtraField) parsedFields[0]).getMode(), "mode field 1");
        assertTrue(parsedFields[1] instanceof UnrecognizedExtraField, "type field 2");
        assertEquals(1, parsedFields[1].getLocalFileDataLength().getValue(), "data length field 2");

        // Test parsing with invalid data, reading the unparseable part
        final byte[] truncatedData = new byte[combinedData.length - 1];
        System.arraycopy(combinedData, 0, truncatedData, 0, truncatedData.length);
        parsedFields = ExtraFieldUtils.parse(truncatedData, true, ExtraFieldUtils.UnparseableExtraField.READ);
        assertEquals(2, parsedFields.length, "number of fields");
        assertTrue(parsedFields[0] instanceof AsiExtraField, "type field 1");
        assertEquals(040755, ((AsiExtraField) parsedFields[0]).getMode(), "mode field 1");
        assertTrue(parsedFields[1] instanceof UnparseableExtraFieldData, "type field 2");
        assertEquals(4, parsedFields[1].getLocalFileDataLength().getValue(), "data length field 2");

        final byte[] unparseableData = parsedFields[1].getLocalFileDataData();
        for (int i = 0; i < 4; i++) {
            assertEquals(truncatedData[combinedData.length - 5 + i], unparseableData[i], "byte number " + i);
        }
    }

    /**
     * Tests parsing with the SKIP UnparseableExtraField option.
     */
    @Test
    void testParseWithSkip() throws Exception {
        // Parse the combined data, skipping unparseable fields
        ZipExtraField[] parsedFields = ExtraFieldUtils.parse(combinedData, true, ExtraFieldUtils.UnparseableExtraField.SKIP);
        assertEquals(2, parsedFields.length, "number of fields");
        assertTrue(parsedFields[0] instanceof AsiExtraField, "type field 1");
        assertEquals(040755, ((AsiExtraField) parsedFields[0]).getMode(), "mode field 1");
        assertTrue(parsedFields[1] instanceof UnrecognizedExtraField, "type field 2");
        assertEquals(1, parsedFields[1].getLocalFileDataLength().getValue(), "data length field 2");

        // Test parsing with invalid data, skipping the unparseable part
        final byte[] truncatedData = new byte[combinedData.length - 1];
        System.arraycopy(combinedData, 0, truncatedData, 0, truncatedData.length);
        parsedFields = ExtraFieldUtils.parse(truncatedData, true, ExtraFieldUtils.UnparseableExtraField.SKIP);
        assertEquals(1, parsedFields.length, "number of fields");
        assertTrue(parsedFields[0] instanceof AsiExtraField, "type field 1");
        assertEquals(040755, ((AsiExtraField) parsedFields[0]).getMode(), "mode field 1");
    }
}