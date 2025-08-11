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

    // Test constants
    private static final int UNIX_MODE_755 = 0755;
    private static final int OCTAL_MODE_755 = 040755;
    private static final ZipShort UNRECOGNIZED_HEADER = new ZipShort(0x5555);
    private static final ZipShort ARRAY_INDEX_OUT_OF_BOUNDS_HEADER = new ZipShort(0x1000);
    
    // Test fixtures
    private AsiExtraField asiField;
    private UnrecognizedExtraField unrecognizedField;
    private byte[] combinedExtraFieldData;
    private byte[] asiFieldLocalData;

    /**
     * Test helper class that throws ArrayIndexOutOfBoundsException during parsing.
     * Used to verify proper exception handling in ExtraFieldUtils.
     */
    public static class ArrayIndexOutOfBoundsThrowingExtraField implements ZipExtraField {
        static final int DATA_LENGTH = 4;

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
            return ARRAY_INDEX_OUT_OF_BOUNDS_HEADER;
        }

        @Override
        public byte[] getLocalFileDataData() {
            return new byte[DATA_LENGTH];
        }

        @Override
        public ZipShort getLocalFileDataLength() {
            return new ZipShort(DATA_LENGTH);
        }

        @Override
        public void parseFromCentralDirectoryData(final byte[] buffer, final int offset, final int length) {
            parseFromLocalFileData(buffer, offset, length);
        }

        @Override
        public void parseFromLocalFileData(final byte[] buffer, final int offset, final int length) {
            throw new ArrayIndexOutOfBoundsException("Simulated parsing error");
        }
    }

    @BeforeEach
    public void setUp() {
        setupAsiExtraField();
        setupUnrecognizedExtraField();
        combinedExtraFieldData = createCombinedExtraFieldData();
    }

    private void setupAsiExtraField() {
        asiField = new AsiExtraField();
        asiField.setMode(UNIX_MODE_755);
        asiField.setDirectory(true);
        asiFieldLocalData = asiField.getLocalFileDataData();
    }

    private void setupUnrecognizedExtraField() {
        unrecognizedField = new UnrecognizedExtraField();
        unrecognizedField.setHeaderId(UNRECOGNIZED_HEADER);
        unrecognizedField.setLocalFileDataData(new byte[]{0});
        unrecognizedField.setCentralDirectoryData(new byte[]{0});
    }

    /**
     * Creates test data containing two extra fields: ASI field and unrecognized field.
     * Format: [header1][length1][data1][header2][length2][data2]
     */
    private byte[] createCombinedExtraFieldData() {
        byte[] unrecognizedFieldLocalData = unrecognizedField.getLocalFileDataData();
        
        // Calculate total size: 2 fields × (2 bytes header + 2 bytes length) + data
        int totalSize = 4 + asiFieldLocalData.length + 4 + unrecognizedFieldLocalData.length;
        byte[] result = new byte[totalSize];
        
        int offset = 0;
        
        // Add ASI field
        offset = addExtraFieldToByteArray(result, offset, asiField, asiFieldLocalData);
        
        // Add unrecognized field
        addExtraFieldToByteArray(result, offset, unrecognizedField, unrecognizedFieldLocalData);
        
        return result;
    }

    private int addExtraFieldToByteArray(byte[] target, int offset, ZipExtraField field, byte[] fieldData) {
        // Add header ID (2 bytes)
        System.arraycopy(field.getHeaderId().getBytes(), 0, target, offset, 2);
        offset += 2;
        
        // Add data length (2 bytes)
        System.arraycopy(field.getLocalFileDataLength().getBytes(), 0, target, offset, 2);
        offset += 2;
        
        // Add field data
        System.arraycopy(fieldData, 0, target, offset, fieldData.length);
        offset += fieldData.length;
        
        return offset;
    }

    @Test
    void testMergeLocalFileData() {
        byte[] mergedData = ExtraFieldUtils.mergeLocalFileDataData(new ZipExtraField[]{asiField, unrecognizedField});
        
        assertEquals(combinedExtraFieldData.length, mergedData.length, "Merged local data should have correct length");
        assertByteArraysEqual(combinedExtraFieldData, mergedData, "local");
    }

    @Test
    void testMergeCentralDirectoryData() {
        byte[] expectedCentralData = createExpectedCentralDirectoryData();
        byte[] actualCentralData = ExtraFieldUtils.mergeCentralDirectoryData(new ZipExtraField[]{asiField, unrecognizedField});
        
        assertEquals(expectedCentralData.length, actualCentralData.length, "Merged central data should have correct length");
        assertByteArraysEqual(expectedCentralData, actualCentralData, "central");
    }

    private byte[] createExpectedCentralDirectoryData() {
        byte[] unrecognizedCentralData = unrecognizedField.getCentralDirectoryData();
        int totalSize = 4 + asiFieldLocalData.length + 4 + unrecognizedCentralData.length;
        byte[] result = new byte[totalSize];
        
        // Copy ASI field data (same as local)
        System.arraycopy(combinedExtraFieldData, 0, result, 0, 4 + asiFieldLocalData.length + 2);
        
        // Add unrecognized field central directory length
        System.arraycopy(unrecognizedField.getCentralDirectoryLength().getBytes(), 0, 
                        result, 4 + asiFieldLocalData.length + 2, 2);
        
        // Add unrecognized field central directory data
        System.arraycopy(unrecognizedCentralData, 0, result, 4 + asiFieldLocalData.length + 4, 
                        unrecognizedCentralData.length);
        
        return result;
    }

    @Test
    void testMergeWithUnparseableExtraFieldData() throws Exception {
        ZipExtraField unparseableField = createUnparseableExtraFieldData();
        
        byte[] mergedLocalData = ExtraFieldUtils.mergeLocalFileDataData(new ZipExtraField[]{asiField, unparseableField});
        byte[] mergedCentralData = ExtraFieldUtils.mergeCentralDirectoryData(new ZipExtraField[]{asiField, unparseableField});
        
        // Unparseable field has 1 byte less data than the original unrecognized field
        assertEquals(combinedExtraFieldData.length - 1, mergedLocalData.length, "Local data length should account for unparseable field");
        assertByteArraysEqual(combinedExtraFieldData, mergedLocalData, "local", mergedLocalData.length);
        
        verifyUnparseableCentralDirectoryMerge(unparseableField, mergedCentralData);
    }

    private ZipExtraField createUnparseableExtraFieldData() throws Exception {
        ZipExtraField unparseableField = new UnparseableExtraFieldData();
        byte[] headerBytes = UNRECOGNIZED_HEADER.getBytes();
        unparseableField.parseFromLocalFileData(new byte[]{headerBytes[0], headerBytes[1], 1, 0}, 0, 4);
        return unparseableField;
    }

    private void verifyUnparseableCentralDirectoryMerge(ZipExtraField unparseableField, byte[] mergedCentralData) {
        byte[] unparseableCentralData = unparseableField.getCentralDirectoryData();
        byte[] expectedCentralData = new byte[4 + asiFieldLocalData.length + unparseableCentralData.length];
        
        System.arraycopy(combinedExtraFieldData, 0, expectedCentralData, 0, 4 + asiFieldLocalData.length + 2);
        System.arraycopy(unparseableCentralData, 0, expectedCentralData, 4 + asiFieldLocalData.length, 
                        unparseableCentralData.length);
        
        assertEquals(expectedCentralData.length, mergedCentralData.length, "Central data length should be correct");
        assertByteArraysEqual(expectedCentralData, mergedCentralData, "central");
    }

    @Test
    void testParseValidExtraFieldData() throws Exception {
        ZipExtraField[] parsedFields = ExtraFieldUtils.parse(combinedExtraFieldData);
        
        assertEquals(2, parsedFields.length, "Should parse exactly 2 extra fields");
        verifyParsedAsiField(parsedFields[0]);
        verifyParsedUnrecognizedField(parsedFields[1]);
    }

    @Test
    void testParseInvalidExtraFieldData() {
        byte[] invalidData = createInvalidExtraFieldData();
        
        Exception exception = assertThrows(Exception.class, 
            () -> ExtraFieldUtils.parse(invalidData), 
            "Parsing invalid data should throw an exception");
        
        String expectedMessage = "Bad extra field starting at " + (4 + asiFieldLocalData.length) + 
                               ".  Block length of 1 bytes exceeds remaining data of 0 bytes.";
        assertEquals(expectedMessage, exception.getMessage(), "Exception message should describe the parsing error");
    }

    private byte[] createInvalidExtraFieldData() {
        // Create data that's 1 byte shorter than expected, causing a parsing error
        byte[] invalidData = new byte[combinedExtraFieldData.length - 1];
        System.arraycopy(combinedExtraFieldData, 0, invalidData, 0, invalidData.length);
        return invalidData;
    }

    @Test
    void testParseCentralDirectoryData() throws Exception {
        ZipExtraField[] parsedFields = ExtraFieldUtils.parse(combinedExtraFieldData, false);
        
        assertEquals(2, parsedFields.length, "Should parse exactly 2 extra fields from central directory");
        verifyParsedAsiField(parsedFields[0]);
        
        assertTrue(parsedFields[1] instanceof UnrecognizedExtraField, "Second field should be UnrecognizedExtraField");
        assertEquals(1, parsedFields[1].getCentralDirectoryLength().getValue(), 
                    "Central directory data length should be 1");
    }

    @Test
    void testParseWithArrayIndexOutOfBoundsExceptionHandling() {
        ExtraFieldUtils.register(ArrayIndexOutOfBoundsThrowingExtraField.class);
        byte[] testData = createArrayIndexOutOfBoundsTestData();
        
        ZipException exception = assertThrows(ZipException.class, 
            () -> ExtraFieldUtils.parse(testData), 
            "Should convert ArrayIndexOutOfBoundsException to ZipException");
        
        assertEquals("Failed to parse corrupt ZIP extra field of type 1000", exception.getMessage(), 
                    "Exception message should indicate parsing failure");
    }

    private byte[] createArrayIndexOutOfBoundsTestData() {
        ArrayIndexOutOfBoundsThrowingExtraField field = new ArrayIndexOutOfBoundsThrowingExtraField();
        byte[] data = new byte[4 + ArrayIndexOutOfBoundsThrowingExtraField.DATA_LENGTH];
        
        System.arraycopy(field.getHeaderId().getBytes(), 0, data, 0, 2);
        System.arraycopy(field.getLocalFileDataLength().getBytes(), 0, data, 2, 2);
        System.arraycopy(field.getLocalFileDataData(), 0, data, 4, ArrayIndexOutOfBoundsThrowingExtraField.DATA_LENGTH);
        
        return data;
    }

    @Test
    void testParseWithReadBehaviorOnValidData() throws Exception {
        ZipExtraField[] parsedFields = ExtraFieldUtils.parse(combinedExtraFieldData, true, 
                                                            ExtraFieldUtils.UnparseableExtraField.READ);
        
        assertEquals(2, parsedFields.length, "Should parse 2 fields with READ behavior");
        verifyParsedAsiField(parsedFields[0]);
        verifyParsedUnrecognizedField(parsedFields[1]);
    }

    @Test
    void testParseWithReadBehaviorOnInvalidData() throws Exception {
        byte[] invalidData = createInvalidExtraFieldData();
        
        ZipExtraField[] parsedFields = ExtraFieldUtils.parse(invalidData, true, 
                                                            ExtraFieldUtils.UnparseableExtraField.READ);
        
        assertEquals(2, parsedFields.length, "Should still parse 2 fields with READ behavior");
        verifyParsedAsiField(parsedFields[0]);
        verifyUnparseableExtraFieldData(parsedFields[1], invalidData);
    }

    @Test
    void testParseWithSkipBehaviorOnValidData() throws Exception {
        ZipExtraField[] parsedFields = ExtraFieldUtils.parse(combinedExtraFieldData, true, 
                                                            ExtraFieldUtils.UnparseableExtraField.SKIP);
        
        assertEquals(2, parsedFields.length, "Should parse 2 fields with SKIP behavior");
        verifyParsedAsiField(parsedFields[0]);
        verifyParsedUnrecognizedField(parsedFields[1]);
    }

    @Test
    void testParseWithSkipBehaviorOnInvalidData() throws Exception {
        byte[] invalidData = createInvalidExtraFieldData();
        
        ZipExtraField[] parsedFields = ExtraFieldUtils.parse(invalidData, true, 
                                                            ExtraFieldUtils.UnparseableExtraField.SKIP);
        
        assertEquals(1, parsedFields.length, "Should skip unparseable field and return only 1 field");
        verifyParsedAsiField(parsedFields[0]);
    }

    // Helper methods for verification
    
    private void verifyParsedAsiField(ZipExtraField field) {
        assertTrue(field instanceof AsiExtraField, "First field should be AsiExtraField");
        assertEquals(OCTAL_MODE_755, ((AsiExtraField) field).getMode(), "ASI field should have correct mode");
    }

    private void verifyParsedUnrecognizedField(ZipExtraField field) {
        assertTrue(field instanceof UnrecognizedExtraField, "Second field should be UnrecognizedExtraField");
        assertEquals(1, field.getLocalFileDataLength().getValue(), "Unrecognized field should have data length 1");
    }

    private void verifyUnparseableExtraFieldData(ZipExtraField field, byte[] originalInvalidData) {
        assertTrue(field instanceof UnparseableExtraFieldData, "Should be UnparseableExtraFieldData");
        assertEquals(4, field.getLocalFileDataLength().getValue(), "Should have length 4");
        
        byte[] fieldData = field.getLocalFileDataData();
        int startOffset = originalInvalidData.length - 4;
        for (int i = 0; i < 4; i++) {
            assertEquals(originalInvalidData[startOffset + i], fieldData[i], 
                        "Byte " + i + " should match original data");
        }
    }

    private void assertByteArraysEqual(byte[] expected, byte[] actual, String context) {
        assertByteArraysEqual(expected, actual, context, Math.min(expected.length, actual.length));
    }

    private void assertByteArraysEqual(byte[] expected, byte[] actual, String context, int length) {
        for (int i = 0; i < length; i++) {
            assertEquals(expected[i], actual[i], context + " byte " + i + " should match");
        }
    }
}