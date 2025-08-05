/*
 * Refactored test suite for ExtraFieldUtils
 * Focus: Improved readability and maintainability
 */
package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.zip.ZipException;
import org.apache.commons.compress.archivers.zip.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class ExtraFieldUtils_ESTest extends ExtraFieldUtils_ESTest_scaffolding {

    // Test handling of UnparseableExtraFieldData
    @Test(timeout = 4000)
    public void testCreateExtraFieldFromUnparseableDataHeader() throws Throwable {
        UnparseableExtraFieldData unparseableData = new UnparseableExtraFieldData();
        ZipShort headerId = unparseableData.getHeaderId();
        ZipExtraField createdField = ExtraFieldUtils.createExtraField(headerId);
        assertNotSame("Should create new instance for unparseable header", 
                     unparseableData, createdField);
    }

    // Test exception handling during local data merging
    @Test(timeout = 4000)
    public void testMergeLocalFileDataWithNullElementThrowsException() throws Throwable {
        byte[] dummyData = new byte[8];
        ZipExtraField unparseableField = ExtraFieldUtils.UnparseableExtraField.READ
            .onUnparseableExtraField(dummyData, 0, 7, false, 7);
        
        ZipExtraField[] fields = new ZipExtraField[2];
        fields[1] = unparseableField; // First element remains null
        
        try {
            ExtraFieldUtils.mergeLocalFileDataData(fields);
            fail("Should throw NullPointerException when array contains null");
        } catch(NullPointerException e) {
            // Expected behavior
        }
    }

    // Test invalid offset handling in unparseable fields
    @Test(timeout = 4000)
    public void testUnparseableFieldHandlingWithInvalidOffsetThrowsException() throws Throwable {
        byte[] emptyData = new byte[0];
        ExtraFieldUtils.UnparseableExtraField handler = ExtraFieldUtils.UnparseableExtraField.READ;
        
        try {
            handler.onUnparseableExtraField(emptyData, 3722, 0, true, 0);
            fail("Should throw ArrayIndexOutOfBoundsException for invalid offset");
        } catch(ArrayIndexOutOfBoundsException e) {
            // Expected behavior
        }
    }

    // Test unparseable field policy constants
    @Test(timeout = 4000)
    public void testUnparseableExtraFieldPolicyConstants() throws Throwable {
        assertEquals("THROW policy key should be 0", 
                     0, ExtraFieldUtils.UnparseableExtraField.THROW.getKey());
        assertEquals("SKIP policy key should be 1", 
                     1, ExtraFieldUtils.UnparseableExtraField.SKIP.getKey());
    }

    // Test empty field handling
    @Test(timeout = 4000)
    public void testParseEmptyFieldData() throws Throwable {
        ZipExtraField[] emptyFields = new ZipExtraField[0];
        byte[] mergedData = ExtraFieldUtils.mergeLocalFileDataData(emptyFields);
        assertEquals("Merged empty fields should be empty", 0, mergedData.length);
        
        ZipExtraField[] parsedFields = ExtraFieldUtils.parse(mergedData, true);
        assertEquals("Parsed empty data should return empty array", 0, parsedFields.length);
    }

    // Test field registration edge cases
    @Test(timeout = 4000)
    public void testFieldRegistrationEdgeCases() {
        try {
            ExtraFieldUtils.register(null);
            fail("Should throw NullPointerException for null class");
        } catch(NullPointerException e) {
            // Expected behavior
        }
        
        try {
            ExtraFieldUtils.register(Object.class);
            fail("Should throw ClassCastException for non-ZipExtraField class");
        } catch(ClassCastException e) {
            // Expected behavior
        }
    }

    // Test field parsing with various policies
    @Test(timeout = 4000)
    public void testFieldParsingWithDifferentPolicies() throws Throwable {
        AsiExtraField asiField = new AsiExtraField();
        byte[] asiData = asiField.getLocalFileDataData();
        
        // Test THROW policy
        try {
            ExtraFieldUtils.parse(asiData, false, ExtraFieldUtils.UnparseableExtraField.THROW);
            fail("Should throw ZipException for malformed data with THROW policy");
        } catch(ZipException e) {
            assertTrue("Exception should mention data length issue",
                      e.getMessage().contains("exceeds remaining data"));
        }
        
        // Test SKIP policy with null input
        try {
            ExtraFieldUtils.parse(null, true, ExtraFieldUtils.UnparseableExtraField.SKIP);
            fail("Should throw NullPointerException for null data");
        } catch(NullPointerException e) {
            // Expected behavior
        }
    }

    // Test field creation with known headers
    @Test(timeout = 4000)
    public void testFieldCreationWithKnownHeaders() {
        X000A_NTFS ntfsField = new X000A_NTFS();
        
        // Test registered header
        ZipExtraField createdField = ExtraFieldUtils.createExtraFieldNoDefault(ntfsField.HEADER_ID);
        assertNotNull("Should create field for registered header", createdField);
        assertNotSame("Should create new instance", ntfsField, createdField);
        
        // Test unregistered header
        ZipShort unregisteredHeader = ntfsField.getCentralDirectoryLength();
        assertNull("Should return null for unregistered header", 
                  ExtraFieldUtils.createExtraFieldNoDefault(unregisteredHeader));
    }

    // Test field merging edge cases
    @Test(timeout = 4000)
    public void testFieldMergingEdgeCases() throws Throwable {
        // Test incomplete Zip64 field
        Zip64ExtendedInformationExtraField incompleteField = new Zip64ExtendedInformationExtraField();
        incompleteField.setSize(ZipEightByteInteger.ZERO);
        ZipExtraField[] fields = {incompleteField};
        
        try {
            ExtraFieldUtils.mergeLocalFileDataData(fields);
            fail("Should throw IllegalArgumentException for incomplete Zip64 field");
        } catch(IllegalArgumentException e) {
            assertTrue("Exception should mention missing size values",
                      e.getMessage().contains("must contain both size values"));
        }
        
        // Test null element handling
        try {
            ExtraFieldUtils.mergeCentralDirectoryData(new ZipExtraField[2]);
            fail("Should throw NullPointerException for array with null elements");
        } catch(NullPointerException e) {
            // Expected behavior
        }
    }

    // Test NTFS field handling
    @Test(timeout = 4000)
    public void testNTFSExtraFieldHandling() throws Throwable {
        X000A_NTFS ntfsField = new X000A_NTFS();
        ZipExtraField[] fields = new ZipExtraField[6];
        for (int i = 0; i < fields.length; i++) {
            fields[i] = ntfsField;
        }
        
        byte[] localData = ExtraFieldUtils.mergeLocalFileDataData(fields);
        ZipExtraField[] parsedLocal = ExtraFieldUtils.parse(localData, true);
        assertEquals("Should parse all NTFS fields", 6, parsedLocal.length);
        
        byte[] centralData = ExtraFieldUtils.mergeCentralDirectoryData(fields);
        ZipExtraField[] parsedCentral = ExtraFieldUtils.parse(centralData);
        assertEquals("Should parse central directory fields", 6, parsedCentral.length);
    }

    // Test utility class contract
    @Test(timeout = 4000)
    public void testUtilityClassContract() {
        new ExtraFieldUtils(); // Just for coverage
    }
}