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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.util.zip.ZipException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;

/**
 * Unit tests for the {@link ExtraFieldUtils} class.
 */
// The EvoSuite runner is kept to ensure static state isolation between tests,
// which is important for testing classes with static registries like ExtraFieldUtils.
@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class ExtraFieldUtils_ESTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    // --- Test Registration ---

    @Test(timeout = 4000)
    public void registerShouldAcceptZipExtraFieldImplementation() {
        // This test ensures the register method works with a valid ZipExtraField class.
        ExtraFieldUtils.register(UnparseableExtraFieldData.class);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void registerWithNullClassShouldThrowNpe() {
        ExtraFieldUtils.register(null);
    }

    @Test(timeout = 4000, expected = ClassCastException.class)
    public void registerWithNonZipExtraFieldClassShouldThrowClassCastException() {
        // Registering a class that does not implement ZipExtraField should fail.
        ExtraFieldUtils.register(Object.class);
    }

    // --- Test Field Creation ---

    @Test(timeout = 4000)
    public void createExtraFieldShouldReturnNewInstanceForRegisteredType() {
        UnparseableExtraFieldData originalField = new UnparseableExtraFieldData();
        ZipShort headerId = originalField.getHeaderId();

        ZipExtraField createdField = ExtraFieldUtils.createExtraField(headerId);

        assertNotNull(createdField);
        assertTrue(createdField instanceof UnparseableExtraFieldData);
        assertNotSame(originalField, createdField);
    }

    @Test(timeout = 4000)
    public void createExtraFieldShouldReturnUnrecognizedFieldForUnknownHeaderId() {
        // An unknown header ID should result in an UnrecognizedExtraField instance.
        ZipShort unknownHeaderId = new ZipShort(0xDEAD);
        ZipExtraField createdField = ExtraFieldUtils.createExtraField(unknownHeaderId);

        assertNotNull(createdField);
        assertTrue(createdField instanceof UnrecognizedExtraField);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void createExtraFieldWithNullHeaderIdShouldThrowNpe() {
        ExtraFieldUtils.createExtraField(null);
    }

    @Test(timeout = 4000)
    public void createExtraFieldNoDefaultShouldReturnKnownField() {
        // Using a known header ID should return the corresponding field implementation.
        ZipExtraField createdField = ExtraFieldUtils.createExtraFieldNoDefault(X000A_NTFS.HEADER_ID);
        assertNotNull(createdField);
        assertTrue(createdField instanceof X000A_NTFS);
    }

    @Test(timeout = 4000)
    public void createExtraFieldNoDefaultShouldReturnNullForUnknownHeaderId() {
        // An unknown header ID should return null when using the "NoDefault" variant.
        ZipShort unknownHeaderId = new ZipShort(0xDEAD);
        ZipExtraField createdField = ExtraFieldUtils.createExtraFieldNoDefault(unknownHeaderId);
        assertNull(createdField);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void createExtraFieldNoDefaultWithNullHeaderIdShouldThrowNpe() {
        ExtraFieldUtils.createExtraFieldNoDefault(null);
    }

    // --- Test Parsing ---

    @Test(timeout = 4000)
    public void parseEmptyByteArrayShouldReturnEmptyFieldArray() {
        byte[] emptyData = new byte[0];
        ZipExtraField[] fields = ExtraFieldUtils.parse(emptyData);
        assertEquals(0, fields.length);
    }

    @Test(timeout = 4000)
    public void parseShouldHandleDataWithPartialLastField() {
        // Data is 5 bytes long. A full extra field block requires at least 4 bytes (header + length).
        // The parser should read one 0-length field and then stop as there isn't enough data for another.
        byte[] data = new byte[5];
        ZipExtraField[] fields = ExtraFieldUtils.parse(data, false);
        assertEquals(1, fields.length);
    }

    @Test(timeout = 4000)
    public void parseWithLenientModeShouldParseMultipleFields() {
        // A 9-byte zeroed array can be parsed as two 0-length fields.
        byte[] data = new byte[9];
        ZipExtraField[] fields = ExtraFieldUtils.parse(data, true, ZipArchiveEntry.ExtraFieldParsingMode.ONLY_PARSEABLE_LENIENT);
        assertEquals(2, fields.length);
    }

    @Test(timeout = 4000)
    public void parseWithLenientModeShouldSkipUnparseableField() {
        byte[] data = new byte[9];
        // This creates a field with a length that exceeds the remaining data, making it unparseable.
        data[3] = (byte) -47; // Corrupts the length of the first field.
        ZipExtraField[] fields = ExtraFieldUtils.parse(data, true, ZipArchiveEntry.ExtraFieldParsingMode.ONLY_PARSEABLE_LENIENT);
        assertEquals(0, fields.length);
    }

    @Test(timeout = 4000)
    public void parseShouldThrowZipExceptionForLengthExceedingData() throws ZipException {
        byte[] data = new byte[9];
        // After the first field (header 0, len 0), the parser is at offset 4.
        // The next field's length is read from bytes 6 and 7.
        // Setting byte 7 to 4 makes the length 1024 (4 * 256).
        data[7] = (byte) 4;

        thrown.expect(ZipException.class);
        thrown.expectMessage("Bad extra field starting at 4.  Block length of 1024 bytes exceeds remaining data of 1 bytes.");

        ExtraFieldUtils.parse(data, true);
    }

    @Test(timeout = 4000)
    public void parseWithThrowOptionShouldThrowZipExceptionOnCorruptData() throws ZipException {
        // The default AsiExtraField produces corrupt data from the perspective of parse(),
        // as getLocalFileDataData() returns only the payload, not the full extra field block.
        AsiExtraField asiExtraField = new AsiExtraField();
        byte[] corruptData = asiExtraField.getLocalFileDataData();

        thrown.expect(ZipException.class);
        thrown.expectMessage("Bad extra field starting at 0.  Block length of 58250 bytes exceeds remaining data of 10 bytes.");

        ExtraFieldUtils.parse(corruptData, false, ExtraFieldUtils.UnparseableExtraField.THROW);
    }

    @Test(timeout = 4000)
    public void parseWithSkipOptionShouldReturnEmptyArrayForCorruptData() throws ZipException {
        AsiExtraField asiExtraField = new AsiExtraField();
        byte[] corruptData = asiExtraField.getCentralDirectoryData();
        // The data is intentionally made corrupt by using the wrong parsing flag (local=false for local data).
        ZipExtraField[] fields = ExtraFieldUtils.parse(asiExtraField.getLocalFileDataData(), false, ExtraFieldUtils.UnparseableExtraField.SKIP);
        assertEquals(0, fields.length);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void parseWithNullDataShouldThrowException() throws ZipException {
        ExtraFieldUtils.parse(null);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void parseWithNullDataAndLocalFlagShouldThrowException() throws ZipException {
        ExtraFieldUtils.parse(null, true);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void parseWithNullBehaviorShouldThrowException() throws ZipException {
        ExtraFieldUtils.parse(new byte[9], false, (ExtraFieldParsingBehavior) null);
    }

    // --- Test Merging ---

    @Test(timeout = 4000)
    public void mergeLocalFileDataWithEmptyFieldsShouldReturnEmptyByteArray() {
        byte[] mergedData = ExtraFieldUtils.mergeLocalFileDataData(new ZipExtraField[0]);
        assertEquals(0, mergedData.length);
    }

    @Test(timeout = 4000)
    public void mergeCentralDirectoryDataWithEmptyFieldsShouldReturnEmptyByteArray() {
        byte[] mergedData = ExtraFieldUtils.mergeCentralDirectoryData(new ZipExtraField[0]);
        assertEquals(0, mergedData.length);
    }

    @Test(timeout = 4000)
    public void mergeAndParseMultipleIdenticalLocalFields() throws ZipException {
        X000A_NTFS ntfsField = new X000A_NTFS();
        ZipExtraField[] fields = {ntfsField, ntfsField, ntfsField};

        byte[] mergedData = ExtraFieldUtils.mergeLocalFileDataData(fields);
        // Each NTFS field has a local length of 32 bytes + 4 bytes for header/length.
        assertEquals(3 * (32 + 4), mergedData.length);

        ZipExtraField[] parsedFields = ExtraFieldUtils.parse(mergedData, true);
        assertEquals(3, parsedFields.length);
    }

    @Test(timeout = 4000)
    public void mergeAndParseMultipleIdenticalCentralDirectoryFields() throws ZipException {
        X000A_NTFS ntfsField = new X000A_NTFS();
        ZipExtraField[] fields = {ntfsField, ntfsField, ntfsField};

        byte[] mergedData = ExtraFieldUtils.mergeCentralDirectoryData(fields);
        // Each NTFS field has a central directory length of 24 bytes + 4 bytes for header/length.
        assertEquals(3 * (24 + 4), mergedData.length);

        ZipExtraField[] parsedFields = ExtraFieldUtils.parse(mergedData);
        assertEquals(3, parsedFields.length);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void mergeLocalFileDataDataShouldThrowNpeIfArrayContainsNull() {
        ZipExtraField[] fields = {new AsiExtraField(), null};
        ExtraFieldUtils.mergeLocalFileDataData(fields);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void mergeCentralDirectoryDataShouldThrowNpeIfArrayContainsNull() {
        ZipExtraField[] fields = {new AsiExtraField(), null};
        ExtraFieldUtils.mergeCentralDirectoryData(fields);
    }

    @Test(timeout = 4000, expected = IllegalArgumentException.class)
    public void mergeLocalFileDataWithIncompleteZip64FieldShouldThrowException() {
        // A Zip64 field in local file data must have both size and compressed size.
        // Here, only size is set, which is invalid.
        Zip64ExtendedInformationExtraField zip64Field = new Zip64ExtendedInformationExtraField();
        zip64Field.setSize(ZipEightByteInteger.ZERO);
        ZipExtraField[] fields = {zip64Field};

        ExtraFieldUtils.mergeLocalFileDataData(fields);
    }

    // --- Test Filling ---

    @Test(timeout = 4000)
    public void fillExtraFieldWithEmptyDataShouldNotThrowException() throws ZipException {
        UnparseableExtraFieldData field = new UnparseableExtraFieldData();
        byte[] emptyData = new byte[0];
        ExtraFieldUtils.fillExtraField(field, emptyData, 0, 0, true);
    }

    @Test(timeout = 4000, expected = ZipException.class)
    public void fillExtraFieldWithInsufficientDataShouldThrowZipException() throws ZipException {
        ResourceAlignmentExtraField field = new ResourceAlignmentExtraField();
        byte[] emptyData = new byte[0];
        // Attempting to fill from an out-of-bounds offset and length will cause a parsing failure.
        ExtraFieldUtils.fillExtraField(field, emptyData, 2, 2, false);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void fillExtraFieldWithNullDataShouldThrowNpe() throws ZipException {
        ResourceAlignmentExtraField field = new ResourceAlignmentExtraField();
        ExtraFieldUtils.fillExtraField(field, null, 2, 2, true);
    }

    // --- Test UnparseableExtraField Inner Class ---

    @Test(timeout = 4000)
    public void unparseableExtraFieldConstantsShouldHaveCorrectKeys() {
        assertEquals(0, ExtraFieldUtils.UnparseableExtraField.THROW.getKey());
        assertEquals(1, ExtraFieldUtils.UnparseableExtraField.SKIP.getKey());
        assertEquals(2, ExtraFieldUtils.UnparseableExtraField.READ.getKey());
    }

    @Test(timeout = 4000, expected = ArrayIndexOutOfBoundsException.class)
    public void onUnparseableExtraFieldShouldThrowAioobeForInvalidOffset() throws ZipException {
        ExtraFieldUtils.UnparseableExtraField readBehavior = ExtraFieldUtils.UnparseableExtraField.READ;
        readBehavior.onUnparseableExtraField(new byte[0], 100, 0, true, 0);
    }
}