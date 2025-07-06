package org.apache.commons.compress.archivers.zip;

import org.junit.Test;
import static org.junit.Assert.*;
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
public class ExtraFieldUtilsTest extends ExtraFieldUtils_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testMergeLocalFileDataDataWithNullField() {
        UnparseableExtraFieldData unparseableExtraFieldData = new UnparseableExtraFieldData();
        ZipExtraField[] zipExtraFields = new ZipExtraField[6];
        zipExtraFields[5] = unparseableExtraFieldData;

        try {
            ExtraFieldUtils.mergeLocalFileDataData(zipExtraFields);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.archivers.zip.ExtraFieldUtils", e);
        }
    }

    @Test(timeout = 4000)
    public void testParseWithAsiExtraField() {
        AsiExtraField asiExtraField = new AsiExtraField();
        byte[] data = asiExtraField.getLocalFileDataData();
        ZipExtraField[] zipExtraFields = ExtraFieldUtils.parse(data, false, ExtraFieldUtils.UnparseableExtraField.READ);
        assertEquals(1, zipExtraFields.length);
    }

    @Test(timeout = 4000)
    public void testOnUnparseableExtraFieldWithReadBehavior() {
        byte[] data = new byte[9];
        ZipExtraField zipExtraField = ExtraFieldUtils.UnparseableExtraField.READ.onUnparseableExtraField(data, 4, 4, true, 4);
        assertNotNull(zipExtraField);
    }

    @Test(timeout = 4000)
    public void testOnUnparseableExtraFieldWithThrowBehavior() {
        byte[] data = new byte[4];
        try {
            ExtraFieldUtils.UnparseableExtraField.THROW.onUnparseableExtraField(data, 565, -2168, false, 62);
            fail("Expected ZipException");
        } catch (ZipException e) {
            verifyException("org.apache.commons.compress.archivers.zip.ExtraFieldUtils$UnparseableExtraField", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetKeyForThrowBehavior() {
        int key = ExtraFieldUtils.UnparseableExtraField.THROW.getKey();
        assertEquals(0, key);
    }

    @Test(timeout = 4000)
    public void testParseWithSkipBehavior() {
        AsiExtraField asiExtraField = new AsiExtraField();
        byte[] data = asiExtraField.getLocalFileDataData();
        ZipExtraField[] zipExtraFields = ExtraFieldUtils.parse(data, true, ExtraFieldUtils.UnparseableExtraField.SKIP);
        assertEquals(0, zipExtraFields.length);
    }

    @Test(timeout = 4000)
    public void testParseEmptyData() {
        byte[] data = new byte[2];
        ZipExtraField[] zipExtraFields = ExtraFieldUtils.parse(data, false);
        assertEquals(0, zipExtraFields.length);
    }

    @Test(timeout = 4000)
    public void testParseEmptyArray() {
        byte[] data = new byte[0];
        ZipExtraField[] zipExtraFields = ExtraFieldUtils.parse(data);
        assertEquals(0, zipExtraFields.length);
    }

    @Test(timeout = 4000)
    public void testFillExtraFieldWithEmptyData() {
        ZipExtraField[] zipExtraFields = new ZipExtraField[0];
        byte[] data = ExtraFieldUtils.mergeLocalFileDataData(zipExtraFields);
        X000A_NTFS x000A_NTFS = new X000A_NTFS();
        ExtraFieldUtils.fillExtraField(x000A_NTFS, data, 436, 0, true);
        assertEquals(0, data.length);
        assertArrayEquals(new byte[]{}, data);
    }

    @Test(timeout = 4000)
    public void testRegisterNullClass() {
        try {
            ExtraFieldUtils.register(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.archivers.zip.ExtraFieldUtils", e);
        }
    }

    @Test(timeout = 4000)
    public void testRegisterNonZipExtraFieldClass() {
        try {
            ExtraFieldUtils.register(Object.class);
            fail("Expected ClassCastException");
        } catch (ClassCastException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testParseWithThrowBehaviorAndInvalidData() {
        AsiExtraField asiExtraField = new AsiExtraField();
        byte[] data = asiExtraField.getCentralDirectoryData();
        try {
            ExtraFieldUtils.parse(data, false, ExtraFieldUtils.UnparseableExtraField.THROW);
            fail("Expected ZipException");
        } catch (ZipException e) {
            verifyException("org.apache.commons.compress.archivers.zip.ExtraFieldUtils$UnparseableExtraField", e);
        }
    }

    @Test(timeout = 4000)
    public void testParseNullDataWithReadBehavior() {
        try {
            ExtraFieldUtils.parse(null, false, ExtraFieldUtils.UnparseableExtraField.READ);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.archivers.zip.ExtraFieldUtils", e);
        }
    }

    @Test(timeout = 4000)
    public void testParseWithInvalidData() {
        AsiExtraField asiExtraField = new AsiExtraField();
        byte[] data = asiExtraField.getCentralDirectoryData();
        try {
            ExtraFieldUtils.parse(data, false);
            fail("Expected ZipException");
        } catch (ZipException e) {
            verifyException("org.apache.commons.compress.archivers.zip.ExtraFieldUtils$UnparseableExtraField", e);
        }
    }

    @Test(timeout = 4000)
    public void testParseNullData() {
        try {
            ExtraFieldUtils.parse(null, false);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.archivers.zip.ExtraFieldUtils", e);
        }
    }

    @Test(timeout = 4000)
    public void testOnUnparseableExtraFieldWithReadBehaviorAndParse() {
        byte[] data = new byte[4];
        ZipExtraField zipExtraField = ExtraFieldUtils.UnparseableExtraField.READ.onUnparseableExtraField(data, 2, 32768, false, 0);
        byte[] localFileData = zipExtraField.getLocalFileDataData();
        ExtraFieldUtils.parse(localFileData);
    }

    @Test(timeout = 4000)
    public void testParseNullDataWithoutBehavior() {
        try {
            ExtraFieldUtils.parse(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.archivers.zip.ExtraFieldUtils", e);
        }
    }

    @Test(timeout = 4000)
    public void testMergeLocalFileDataDataWithIncompleteZip64Field() {
        ZipExtraField[] zipExtraFields = new ZipExtraField[1];
        Zip64ExtendedInformationExtraField zip64Field = new Zip64ExtendedInformationExtraField();
        zip64Field.setSize(ZipEightByteInteger.ZERO);
        zipExtraFields[0] = zip64Field;

        try {
            ExtraFieldUtils.mergeLocalFileDataData(zipExtraFields);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField", e);
        }
    }

    @Test(timeout = 4000)
    public void testMergeCentralDirectoryDataWithNullField() {
        ZipExtraField[] zipExtraFields = new ZipExtraField[1];
        try {
            ExtraFieldUtils.mergeCentralDirectoryData(zipExtraFields);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.archivers.zip.ExtraFieldUtils", e);
        }
    }

    @Test(timeout = 4000)
    public void testMergeCentralDirectoryDataWithParse() {
        byte[] data = new byte[8];
        ZipExtraField[] zipExtraFields = ExtraFieldUtils.parse(data);
        try {
            ExtraFieldUtils.mergeCentralDirectoryData(zipExtraFields);
            fail("Expected NoClassDefFoundError");
        } catch (NoClassDefFoundError e) {
            verifyException("org.apache.commons.compress.archivers.zip.ZipShort", e);
        }
    }

    @Test(timeout = 4000)
    public void testFillExtraFieldWithInvalidData() {
        byte[] data = new byte[4];
        X0016_CertificateIdForCentralDirectory certificateIdField = new X0016_CertificateIdForCentralDirectory();
        try {
            ExtraFieldUtils.fillExtraField(certificateIdField, data, 9, 9, false);
            fail("Expected ZipException");
        } catch (ZipException e) {
            verifyException("org.apache.commons.compress.archivers.zip.ZipUtil", e);
        }
    }

    @Test(timeout = 4000)
    public void testFillExtraFieldWithNegativeArraySize() {
        byte[] data = new byte[26];
        Zip64ExtendedInformationExtraField zip64Field = new Zip64ExtendedInformationExtraField();
        try {
            ExtraFieldUtils.fillExtraField(zip64Field, data, -1188, -1188, false);
            fail("Expected NegativeArraySizeException");
        } catch (NegativeArraySizeException e) {
            verifyException("org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField", e);
        }
    }

    @Test(timeout = 4000)
    public void testCreateExtraFieldNoDefaultWithNull() {
        try {
            ExtraFieldUtils.createExtraFieldNoDefault(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testCreateExtraFieldWithNull() {
        try {
            ExtraFieldUtils.createExtraField(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testParseWithStrictMode() {
        byte[] data = new byte[4];
        data[2] = 4;
        ZipExtraField[] zipExtraFields = ExtraFieldUtils.parse(data, true, ZipArchiveEntry.ExtraFieldParsingMode.STRICT_FOR_KNOW_EXTRA_FIELDS);
        assertEquals(1, zipExtraFields.length);
    }

    @Test(timeout = 4000)
    public void testParseWithOnlyParseableStrictMode() {
        byte[] data = new byte[7];
        data[2] = -1;
        ZipExtraField[] zipExtraFields = ExtraFieldUtils.parse(data, true, ZipArchiveEntry.ExtraFieldParsingMode.ONLY_PARSEABLE_STRICT);
        assertEquals(0, zipExtraFields.length);
    }

    @Test(timeout = 4000)
    public void testParseWithDraconicMode() {
        AsiExtraField asiExtraField = new AsiExtraField();
        byte[] data = asiExtraField.getLocalFileDataData();
        try {
            ExtraFieldUtils.parse(data, true, ZipArchiveEntry.ExtraFieldParsingMode.DRACONIC);
            fail("Expected ZipException");
        } catch (ZipException e) {
            verifyException("org.apache.commons.compress.archivers.zip.ExtraFieldUtils$UnparseableExtraField", e);
        }
    }

    @Test(timeout = 4000)
    public void testParseWithOnlyParseableStrictModeAndEmptyData() {
        byte[] data = new byte[1];
        ZipExtraField[] zipExtraFields = ExtraFieldUtils.parse(data, false, ZipArchiveEntry.ExtraFieldParsingMode.ONLY_PARSEABLE_STRICT);
        assertEquals(0, zipExtraFields.length);
    }

    @Test(timeout = 4000)
    public void testParseWithNullParsingBehavior() {
        byte[] data = new byte[8];
        try {
            ExtraFieldUtils.parse(data, true, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.archivers.zip.ExtraFieldUtils", e);
        }
    }

    @Test(timeout = 4000)
    public void testFillExtraFieldWithNullData() {
        X5455_ExtendedTimestamp extendedTimestamp = new X5455_ExtendedTimestamp();
        try {
            ExtraFieldUtils.fillExtraField(extendedTimestamp, null, 4, 2, false);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.archivers.zip.X5455_ExtendedTimestamp", e);
        }
    }

    @Test(timeout = 4000)
    public void testFillExtraFieldWithIllegalArgument() {
        byte[] data = new byte[6];
        UnparseableExtraFieldData unparseableExtraFieldData = new UnparseableExtraFieldData();
        try {
            ExtraFieldUtils.fillExtraField(unparseableExtraFieldData, data, -3, -3, true);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("java.util.Arrays", e);
        }
    }

    @Test(timeout = 4000)
    public void testCreateExtraFieldNoDefaultWithUnparseableData() {
        UnparseableExtraFieldData unparseableExtraFieldData = new UnparseableExtraFieldData();
        ZipShort headerId = unparseableExtraFieldData.getHeaderId();
        ZipExtraField zipExtraField = ExtraFieldUtils.createExtraFieldNoDefault(headerId);
        assertNotSame(unparseableExtraFieldData, zipExtraField);
    }

    @Test(timeout = 4000)
    public void testCreateExtraFieldNoDefaultWithAsiExtraField() {
        AsiExtraField asiExtraField = new AsiExtraField();
        ZipShort headerId = asiExtraField.getLocalFileDataLength();
        ZipExtraField zipExtraField = ExtraFieldUtils.createExtraFieldNoDefault(headerId);
        assertNull(zipExtraField);
    }

    @Test(timeout = 4000)
    public void testCreateExtraFieldWithAsiExtraField() {
        AsiExtraField asiExtraField = new AsiExtraField();
        ZipShort headerId = asiExtraField.getLocalFileDataLength();
        ZipExtraField zipExtraField = ExtraFieldUtils.createExtraField(headerId);
        assertNotNull(zipExtraField);
    }

    @Test(timeout = 4000)
    public void testCreateExtraFieldWithUnparseableData() {
        UnparseableExtraFieldData unparseableExtraFieldData = new UnparseableExtraFieldData();
        ZipShort headerId = unparseableExtraFieldData.getHeaderId();
        ZipExtraField zipExtraField = ExtraFieldUtils.createExtraField(headerId);
        assertNotSame(unparseableExtraFieldData, zipExtraField);
    }

    @Test(timeout = 4000)
    public void testMergeLocalFileDataDataWithDuplicateFields() {
        AsiExtraField asiExtraField = new AsiExtraField();
        ZipExtraField[] zipExtraFields = new ZipExtraField[2];
        zipExtraFields[0] = asiExtraField;
        zipExtraFields[1] = asiExtraField;
        byte[] data = ExtraFieldUtils.mergeLocalFileDataData(zipExtraFields);
        assertEquals(36, data.length);
    }

    @Test(timeout = 4000)
    public void testMergeLocalFileDataDataWithUnparseableField() {
        ZipExtraField[] zipExtraFields = new ZipExtraField[1];
        UnparseableExtraFieldData unparseableExtraFieldData = new UnparseableExtraFieldData();
        zipExtraFields[0] = unparseableExtraFieldData;

        try {
            ExtraFieldUtils.mergeLocalFileDataData(zipExtraFields);
            fail("Expected NoClassDefFoundError");
        } catch (NoClassDefFoundError e) {
            verifyException("org.apache.commons.compress.archivers.zip.ZipShort", e);
        }
    }

    @Test(timeout = 4000)
    public void testMergeCentralDirectoryDataWithEmptyFields() {
        ZipExtraField[] zipExtraFields = new ZipExtraField[0];
        byte[] data = ExtraFieldUtils.mergeCentralDirectoryData(zipExtraFields);
        Zip64ExtendedInformationExtraField zip64Field = new Zip64ExtendedInformationExtraField();

        try {
            ExtraFieldUtils.fillExtraField(zip64Field, data, 32768, 32768, true);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verifyException("java.nio.Buffer", e);
        }
    }

    @Test(timeout = 4000)
    public void testParseWithReadBehavior() {
        AsiExtraField asiExtraField = new AsiExtraField();
        byte[] data = asiExtraField.getLocalFileDataData();
        ZipExtraField[] zipExtraFields = ExtraFieldUtils.parse(data, true, ExtraFieldUtils.UnparseableExtraField.READ);
        assertEquals(1, zipExtraFields.length);
    }

    @Test(timeout = 4000)
    public void testOnUnparseableExtraFieldWithReadBehaviorAndMerge() {
        byte[] data = new byte[4];
        ZipExtraField zipExtraField = ExtraFieldUtils.UnparseableExtraField.READ.onUnparseableExtraField(data, 2, 32768, false, 0);
        ZipExtraField[] zipExtraFields = new ZipExtraField[4];
        zipExtraFields[0] = zipExtraField;
        zipExtraFields[1] = zipExtraField;
