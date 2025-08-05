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
 * JUnit tests for org.apache.commons.compress.archivers.zip.ExtraFieldUtils.
 */
class ExtraFieldUtilsTest implements UnixStat {

    private static final int ASI_EXTRA_FIELD_MODE = 040755;
    private static final int DUMMY_EXTRA_FIELD_LENGTH = 1;
    private static final int AIOB_EXTRA_FIELD_LENGTH = 4;
    private static final ZipShort UNRECOGNIZED_HEADER = new ZipShort(0x5555);
    private static final ZipShort AIOB_HEADER = new ZipShort(0x1000);

    public static class AiobThrowingExtraField implements ZipExtraField {
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
            return new byte[AIOB_EXTRA_FIELD_LENGTH];
        }

        @Override
        public ZipShort getLocalFileDataLength() {
            return new ZipShort(AIOB_EXTRA_FIELD_LENGTH);
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

    private AsiExtraField asiExtraField;
    private UnrecognizedExtraField dummyExtraField;
    private byte[] validExtraFieldData;

    @BeforeEach
    public void setUp() {
        // Setup ASI extra field
        asiExtraField = new AsiExtraField();
        asiExtraField.setMode(0755);
        asiExtraField.setDirectory(true);
        byte[] asiLocalData = asiExtraField.getLocalFileDataData();

        // Setup dummy unrecognized extra field
        dummyExtraField = new UnrecognizedExtraField();
        dummyExtraField.setHeaderId(UNRECOGNIZED_HEADER);
        dummyExtraField.setLocalFileDataData(new byte[] {0});
        dummyExtraField.setCentralDirectoryData(new byte[] {0});
        byte[] dummyLocalData = dummyExtraField.getLocalFileDataData();

        // Create valid extra field data combining both fields
        validExtraFieldData = new byte[4 + asiLocalData.length + 4 + dummyLocalData.length];
        int pos = 0;
        
        // Add ASI field header and data
        pos = appendFieldData(validExtraFieldData, pos, asiExtraField.getHeaderId(), 
                             asiExtraField.getLocalFileDataLength(), asiLocalData);
        
        // Add dummy field header and data
        appendFieldData(validExtraFieldData, pos, dummyExtraField.getHeaderId(), 
                        dummyExtraField.getLocalFileDataLength(), dummyLocalData);
    }

    // Helper to append field data to byte array
    private int appendFieldData(byte[] target, int offset, ZipShort header, 
                               ZipShort length, byte[] data) {
        System.arraycopy(header.getBytes(), 0, target, offset, 2);
        System.arraycopy(length.getBytes(), 0, target, offset + 2, 2);
        System.arraycopy(data, 0, target, offset + 4, data.length);
        return offset + 4 + data.length;
    }

    // Helper to create corrupted data by truncating last byte
    private byte[] createTruncatedData(byte[] original) {
        byte[] truncated = new byte[original.length - 1];
        System.arraycopy(original, 0, truncated, 0, truncated.length);
        return truncated;
    }

    // Helper to verify ASI extra field
    private void assertAsiExtraField(ZipExtraField field) {
        assertTrue(field instanceof AsiExtraField, "Should be AsiExtraField");
        assertEquals(ASI_EXTRA_FIELD_MODE, ((AsiExtraField) field).getMode());
    }

    // Helper to verify dummy extra field
    private void assertDummyExtraField(ZipExtraField field) {
        assertTrue(field instanceof UnrecognizedExtraField, "Should be UnrecognizedExtraField");
        assertEquals(DUMMY_EXTRA_FIELD_LENGTH, field.getLocalFileDataLength().getValue());
    }

    /* ========== MERGE TESTS ========== */

    @Test
    void mergeLocalFileDataData_CombinesFieldsCorrectly() {
        // When
        byte[] merged = ExtraFieldUtils.mergeLocalFileDataData(
            new ZipExtraField[]{asiExtraField, dummyExtraField});

        // Then
        assertArrayEquals(validExtraFieldData, merged, 
            "Merged local data should match expected structure");
    }

    @Test
    void mergeCentralDirectoryData_CombinesFieldsCorrectly() {
        // Setup
        byte[] expected = new byte[validExtraFieldData.length];
        System.arraycopy(validExtraFieldData, 0, expected, 0, 
                         validExtraFieldData.length - 1);
        System.arraycopy(dummyExtraField.getCentralDirectoryLength().getBytes(), 
                         0, expected, validExtraFieldData.length - 3, 2);

        // When
        byte[] merged = ExtraFieldUtils.mergeCentralDirectoryData(
            new ZipExtraField[]{asiExtraField, dummyExtraField});

        // Then
        assertArrayEquals(expected, merged, 
            "Merged central data should match expected structure");
    }

    @Test
    void mergeLocalFileDataData_WithUnparseableField_CombinesFieldsCorrectly() throws Exception {
        // Setup unparseable field
        UnparseableExtraFieldData unparseable = new UnparseableExtraFieldData();
        byte[] header = UNRECOGNIZED_HEADER.getBytes();
        unparseable.parseFromLocalFileData(new byte[]{header[0], header[1], 1, 0}, 0, 4);

        // When
        byte[] merged = ExtraFieldUtils.mergeLocalFileDataData(
            new ZipExtraField[]{asiExtraField, unparseable});

        // Then (expect dummy field to be replaced by unparseable)
        byte[] expected = new byte[validExtraFieldData.length - 1];
        System.arraycopy(validExtraFieldData, 0, expected, 0, expected.length);
        assertArrayEquals(expected, merged);
    }

    /* ========== PARSE TESTS ========== */

    @Test
    void parse_WithValidData_ReturnsAllFields() throws Exception {
        // When
        ZipExtraField[] fields = ExtraFieldUtils.parse(validExtraFieldData);

        // Then
        assertEquals(2, fields.length, "Should parse two fields");
        assertAsiExtraField(fields[0]);
        assertDummyExtraField(fields[1]);
    }

    @Test
    void parse_WithTruncatedData_ThrowsException() {
        // Setup
        byte[] invalidData = createTruncatedData(validExtraFieldData);
        int truncationPosition = validExtraFieldData.length - 4;

        // When/Then
        Exception ex = assertThrows(Exception.class, 
            () -> ExtraFieldUtils.parse(invalidData),
            "Should throw on truncated data");
        
        assertEquals("Bad extra field starting at " + truncationPosition + 
                    ".  Block length of 1 bytes exceeds remaining data of 0 bytes.", 
                    ex.getMessage());
    }

    @Test
    void parseCentralDirectoryData_WithValidData_ReturnsAllFields() throws Exception {
        // When
        ZipExtraField[] fields = ExtraFieldUtils.parse(validExtraFieldData, false);

        // Then
        assertEquals(2, fields.length, "Should parse two fields");
        assertAsiExtraField(fields[0]);
        assertDummyExtraField(fields[1]);
    }

    @Test
    void parse_WhenFieldParserThrowsArrayIndexOutOfBounds_ThrowsZipException() {
        // Setup
        ExtraFieldUtils.register(AiobThrowingExtraField.class);
        AiobThrowingExtraField field = new AiobThrowingExtraField();
        byte[] data = new byte[4 + AIOB_EXTRA_FIELD_LENGTH];
        appendFieldData(data, 0, field.getHeaderId(), 
                      field.getLocalFileDataLength(), field.getLocalFileDataData());

        // When/Then
        ZipException ex = assertThrows(ZipException.class, 
            () -> ExtraFieldUtils.parse(data),
            "Should convert ArrayIndexOutOfBounds to ZipException");
        
        assertEquals("Failed to parse corrupt ZIP extra field of type 1000", ex.getMessage());
    }

    @Test
    void parse_WithReadBehaviorAndValidData_ReturnsAllFields() throws Exception {
        // When
        ZipExtraField[] fields = ExtraFieldUtils.parse(validExtraFieldData, true, 
            ExtraFieldUtils.UnparseableExtraField.READ);

        // Then
        assertEquals(2, fields.length, "Should parse two fields");
        assertAsiExtraField(fields[0]);
        assertDummyExtraField(fields[1]);
    }

    @Test
    void parse_WithReadBehaviorAndTruncatedData_ReturnsUnparseableField() throws Exception {
        // Setup
        byte[] invalidData = createTruncatedData(validExtraFieldData);
        int expectedUnparseableLength = 4;

        // When
        ZipExtraField[] fields = ExtraFieldUtils.parse(invalidData, true, 
            ExtraFieldUtils.UnparseableExtraField.READ);

        // Then
        assertEquals(2, fields.length, "Should return two fields");
        assertAsiExtraField(fields[0]);
        
        assertTrue(fields[1] instanceof UnparseableExtraFieldData, 
            "Second field should be unparseable");
        assertEquals(expectedUnparseableLength, 
            fields[1].getLocalFileDataLength().getValue());
    }

    @Test
    void parse_WithSkipBehaviorAndValidData_ReturnsAllFields() throws Exception {
        // When
        ZipExtraField[] fields = ExtraFieldUtils.parse(validExtraFieldData, true, 
            ExtraFieldUtils.UnparseableExtraField.SKIP);

        // Then
        assertEquals(2, fields.length, "Should parse two fields");
        assertAsiExtraField(fields[0]);
        assertDummyExtraField(fields[1]);
    }

    @Test
    void parse_WithSkipBehaviorAndTruncatedData_OmitsUnparseableField() throws Exception {
        // Setup
        byte[] invalidData = createTruncatedData(validExtraFieldData);

        // When
        ZipExtraField[] fields = ExtraFieldUtils.parse(invalidData, true, 
            ExtraFieldUtils.UnparseableExtraField.SKIP);

        // Then
        assertEquals(1, fields.length, "Should skip unparseable field");
        assertAsiExtraField(fields[0]);
    }
}