package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.zip.ZipException;
import org.apache.commons.compress.archivers.zip.*;

/**
 * Test suite for ExtraFieldUtils class functionality.
 * Tests parsing, merging, and creation of ZIP extra fields.
 */
public class ExtraFieldUtilsTest {

    // ========== Extra Field Creation Tests ==========
    
    @Test
    public void testCreateExtraFieldWithUnparseableData() {
        // Given: An unparseable extra field with a header ID
        UnparseableExtraFieldData unparseableField = new UnparseableExtraFieldData();
        ZipShort headerId = unparseableField.getHeaderId();
        
        // When: Creating a new extra field with the same header ID
        ZipExtraField createdField = ExtraFieldUtils.createExtraField(headerId);
        
        // Then: The created field should be different from the original
        assertFalse(createdField.equals(unparseableField));
    }

    @Test
    public void testCreateExtraFieldWithNTFSField() {
        // Given: An NTFS extra field
        X000A_NTFS ntfsField = new X000A_NTFS();
        
        // When: Creating a field with the same header ID
        ZipExtraField createdField = ExtraFieldUtils.createExtraFieldNoDefault(ntfsField.HEADER_ID);
        
        // Then: A new instance should be created
        assertNotSame(ntfsField, createdField);
    }

    @Test
    public void testCreateExtraFieldWithUnknownHeaderReturnsNull() {
        // Given: An NTFS field with unknown central directory length
        X000A_NTFS ntfsField = new X000A_NTFS();
        ZipShort unknownHeaderId = ntfsField.getCentralDirectoryLength();
        
        // When: Trying to create field with unknown header
        ZipExtraField result = ExtraFieldUtils.createExtraFieldNoDefault(unknownHeaderId);
        
        // Then: Should return null for unknown headers
        assertNull(result);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateExtraFieldWithNullHeaderThrowsException() {
        ExtraFieldUtils.createExtraField(null);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateExtraFieldNoDefaultWithNullHeaderThrowsException() {
        ExtraFieldUtils.createExtraFieldNoDefault(null);
    }

    // ========== Extra Field Parsing Tests ==========

    @Test
    public void testParseEmptyByteArray() {
        // Given: Empty byte array
        byte[] emptyData = new byte[0];
        
        // When: Parsing the empty array
        ZipExtraField[] fields = ExtraFieldUtils.parse(emptyData);
        
        // Then: Should return empty array
        assertEquals(0, fields.length);
    }

    @Test
    public void testParseValidExtraFieldData() {
        // Given: Valid extra field data from multiple NTFS fields
        X000A_NTFS ntfsField = new X000A_NTFS();
        ZipExtraField[] inputFields = createNTFSFieldArray(6);
        byte[] mergedData = ExtraFieldUtils.mergeLocalFileDataData(inputFields);
        
        // When: Parsing the merged data
        ZipExtraField[] parsedFields = ExtraFieldUtils.parse(mergedData, true);
        
        // Then: Should parse all fields correctly
        assertEquals(6, parsedFields.length);
        assertEquals(216, mergedData.length);
    }

    @Test
    public void testParseWithLenientMode() {
        // Given: Byte array with some invalid data
        byte[] dataWithInvalidLength = new byte[9];
        dataWithInvalidLength[3] = (byte) (-47); // Invalid length marker
        
        // When: Parsing with lenient mode
        ZipArchiveEntry.ExtraFieldParsingMode lenientMode = 
            ZipArchiveEntry.ExtraFieldParsingMode.ONLY_PARSEABLE_LENIENT;
        ZipExtraField[] fields = ExtraFieldUtils.parse(dataWithInvalidLength, true, lenientMode);
        
        // Then: Should skip invalid fields and return empty array
        assertEquals(0, fields.length);
    }

    @Test(expected = ZipException.class)
    public void testParseInvalidDataThrowsException() throws ZipException {
        // Given: Invalid extra field data with incorrect length
        AsiExtraField asiField = new AsiExtraField();
        byte[] invalidData = asiField.getLocalFileDataData();
        
        // When/Then: Parsing should throw ZipException due to invalid length
        ExtraFieldUtils.parse(invalidData);
    }

    @Test(expected = ZipException.class)
    public void testParseWithThrowBehaviorOnInvalidData() throws ZipException {
        // Given: Invalid data and THROW behavior
        AsiExtraField asiField = new AsiExtraField();
        byte[] invalidData = asiField.getLocalFileDataData();
        ExtraFieldUtils.UnparseableExtraField throwBehavior = 
            ExtraFieldUtils.UnparseableExtraField.THROW;
        
        // When/Then: Should throw exception
        ExtraFieldUtils.parse(invalidData, false, throwBehavior);
    }

    @Test
    public void testParseWithSkipBehaviorOnInvalidData() throws ZipException {
        // Given: Invalid data and SKIP behavior
        AsiExtraField asiField = new AsiExtraField();
        byte[] invalidData = asiField.getCentralDirectoryData();
        ExtraFieldUtils.UnparseableExtraField skipBehavior = 
            ExtraFieldUtils.UnparseableExtraField.SKIP;
        
        // When: Parsing with skip behavior
        ZipExtraField[] fields = ExtraFieldUtils.parse(invalidData, false, skipBehavior);
        
        // Then: Should return empty array (skipped invalid data)
        assertEquals(0, fields.length);
    }

    @Test
    public void testParseWithReadBehaviorOnInvalidData() throws ZipException {
        // Given: Invalid data and READ behavior
        AsiExtraField asiField = new AsiExtraField();
        byte[] invalidData = asiField.getCentralDirectoryData();
        ExtraFieldUtils.UnparseableExtraField readBehavior = 
            ExtraFieldUtils.UnparseableExtraField.READ;
        
        // When: Parsing with read behavior
        ZipExtraField[] fields = ExtraFieldUtils.parse(invalidData, false, readBehavior);
        
        // Then: Should create unparseable field data
        byte[] mergedData = ExtraFieldUtils.mergeCentralDirectoryData(fields);
        assertEquals(14, mergedData.length);
    }

    @Test(expected = NullPointerException.class)
    public void testParseWithNullDataThrowsException() {
        ExtraFieldUtils.parse(null);
    }

    // ========== Data Merging Tests ==========

    @Test
    public void testMergeEmptyLocalFileData() {
        // Given: Empty array of extra fields
        ZipExtraField[] emptyFields = new ZipExtraField[0];
        
        // When: Merging local file data
        byte[] mergedData = ExtraFieldUtils.mergeLocalFileDataData(emptyFields);
        
        // Then: Should return empty byte array
        assertEquals(0, mergedData.length);
        assertArrayEquals(new byte[]{}, mergedData);
    }

    @Test
    public void testMergeEmptyCentralDirectoryData() {
        // Given: Empty array of extra fields
        ZipExtraField[] emptyFields = new ZipExtraField[0];
        
        // When: Merging central directory data
        byte[] mergedData = ExtraFieldUtils.mergeCentralDirectoryData(emptyFields);
        
        // Then: Should return empty byte array
        assertEquals(0, mergedData.length);
    }

    @Test
    public void testMergeMultipleNTFSFields() {
        // Given: Multiple NTFS extra fields
        ZipExtraField[] ntfsFields = createNTFSFieldArray(6);
        
        // When: Merging central directory data
        byte[] mergedData = ExtraFieldUtils.mergeCentralDirectoryData(ntfsFields);
        ZipExtraField[] parsedFields = ExtraFieldUtils.parse(mergedData);
        
        // Then: All fields should be preserved
        assertEquals(6, parsedFields.length);
    }

    @Test(expected = NullPointerException.class)
    public void testMergeLocalFileDataWithNullElementThrowsException() {
        // Given: Array with null element
        ZipExtraField[] fieldsWithNull = new ZipExtraField[2];
        fieldsWithNull[1] = createUnparseableField();
        // fieldsWithNull[0] remains null
        
        // When/Then: Should throw NullPointerException
        ExtraFieldUtils.mergeLocalFileDataData(fieldsWithNull);
    }

    @Test(expected = NullPointerException.class)
    public void testMergeCentralDirectoryDataWithNullElementThrowsException() {
        // Given: Array with null elements
        ZipExtraField[] fieldsWithNull = new ZipExtraField[2];
        
        // When/Then: Should throw NullPointerException
        ExtraFieldUtils.mergeCentralDirectoryData(fieldsWithNull);
    }

    // ========== Field Filling Tests ==========

    @Test
    public void testFillExtraFieldWithEmptyData() {
        // Given: Empty data and unparseable field
        ZipExtraField[] emptyFields = new ZipExtraField[0];
        byte[] emptyData = ExtraFieldUtils.mergeLocalFileDataData(emptyFields);
        ZipExtraField unparseableField = createUnparseableField();
        
        // When: Filling the field with empty data
        ExtraFieldUtils.fillExtraField(unparseableField, emptyData, 0, 0, true);
        
        // Then: Should complete without error
        assertEquals(0, emptyData.length);
    }

    @Test(expected = ZipException.class)
    public void testFillResourceAlignmentFieldWithInvalidDataThrowsException() throws ZipException {
        // Given: Resource alignment field and invalid data
        ResourceAlignmentExtraField resourceField = new ResourceAlignmentExtraField();
        byte[] invalidData = new byte[0];
        
        // When/Then: Should throw ZipException for corrupt data
        ExtraFieldUtils.fillExtraField(resourceField, invalidData, 2, 2, false);
    }

    @Test(expected = NullPointerException.class)
    public void testFillExtraFieldWithNullDataThrowsException() {
        ResourceAlignmentExtraField resourceField = new ResourceAlignmentExtraField();
        ExtraFieldUtils.fillExtraField(resourceField, null, 2, 2, true);
    }

    // ========== Unparseable Field Behavior Tests ==========

    @Test
    public void testUnparseableFieldBehaviorKeys() {
        // Test the key values for different behaviors
        assertEquals(0, ExtraFieldUtils.UnparseableExtraField.THROW.getKey());
        assertEquals(1, ExtraFieldUtils.UnparseableExtraField.SKIP.getKey());
        assertEquals(2, ExtraFieldUtils.UnparseableExtraField.READ.getKey());
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testUnparseableFieldWithInvalidOffsetThrowsException() {
        // Given: Empty data and invalid offset
        byte[] emptyData = new byte[0];
        ExtraFieldUtils.UnparseableExtraField readBehavior = 
            ExtraFieldUtils.UnparseableExtraField.READ;
        
        // When/Then: Should throw ArrayIndexOutOfBoundsException
        readBehavior.onUnparseableExtraField(emptyData, 3722, 0, true, 0);
    }

    // ========== Registration Tests ==========

    @Test
    public void testRegisterValidExtraFieldClass() {
        // Given: Valid extra field class
        Class<UnparseableExtraFieldData> validClass = UnparseableExtraFieldData.class;
        
        // When: Registering the class
        ExtraFieldUtils.register(validClass);
        
        // Then: Should complete without error (deprecated but functional)
    }

    @Test(expected = NullPointerException.class)
    public void testRegisterNullClassThrowsException() {
        ExtraFieldUtils.register(null);
    }

    @Test(expected = ClassCastException.class)
    public void testRegisterInvalidClassThrowsException() {
        // Given: Invalid class that doesn't implement ZipExtraField
        Class<Object> invalidClass = Object.class;
        
        // When/Then: Should throw ClassCastException
        ExtraFieldUtils.register(invalidClass);
    }

    // ========== Constructor Test ==========

    @Test
    public void testConstructorExists() {
        // Verify that the utility class can be instantiated (though not recommended)
        ExtraFieldUtils utils = new ExtraFieldUtils();
        assertNotNull(utils);
    }

    // ========== Helper Methods ==========

    private ZipExtraField[] createNTFSFieldArray(int count) {
        X000A_NTFS ntfsField = new X000A_NTFS();
        ZipExtraField[] fields = new ZipExtraField[count];
        for (int i = 0; i < count; i++) {
            fields[i] = ntfsField;
        }
        return fields;
    }

    private ZipExtraField createUnparseableField() {
        ExtraFieldUtils.UnparseableExtraField readBehavior = 
            ExtraFieldUtils.UnparseableExtraField.READ;
        byte[] testData = new byte[8];
        return readBehavior.onUnparseableExtraField(testData, 0, 7, false, 7);
    }
}