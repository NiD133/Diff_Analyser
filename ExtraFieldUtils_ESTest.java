package org.apache.commons.compress.archivers.zip;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.zip.ZipException;

import org.junit.jupiter.api.BeforeEach;
import org.apache.commons.compress.archivers.zip.AsiExtraField;
import org.apache.commons.compress.archivers.zip.ExtraFieldParsingBehavior;
import org.apache.commons.compress.archivers.zip.ExtraFieldUtils;
import org.apache.commons.compress.archivers.zip.UnparseableExtraFieldData;
import org.apache.commons.compress.archivers.zip.X000A_NTFS;
import org.apache.commons.compress.archivers.zip.X0016_CertificateIdForCentralDirectory;
import org.apache.commons.compress.archivers.zip.X5455_ExtendedTimestamp;
import org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipEightByteInteger;
import org.apache.commons.compress.archivers.zip.ZipExtraField;
import org.apache.commons.compress.archivers.zip.ZipShort;


/**
 * Test class for {@link ExtraFieldUtils}.  Focuses on testing the parsing and merging of ZipExtraFields.
 */
class ExtraFieldUtilsTest {

    private byte[] sampleByteArray;

    @BeforeEach
    void setup() {
        sampleByteArray = new byte[4];
    }

    @Test
    void testMergeLocalFileDataDataWithNullElement() {
        ZipExtraField[] extraFields = new ZipExtraField[6];
        extraFields[5] = new UnparseableExtraFieldData();

        assertThrows(NullPointerException.class, () -> ExtraFieldUtils.mergeLocalFileDataData(extraFields),
                "Should throw NullPointerException when array contains null elements before an UnparseableExtraFieldData instance.");
    }

    @Test
    void testParseAsiExtraFieldLocalData() throws ZipException {
        AsiExtraField asiExtraField = new AsiExtraField();
        byte[] localFileData = asiExtraField.getLocalFileDataData();

        ZipExtraField[] parsedFields = ExtraFieldUtils.parse(localFileData, false, ExtraFieldUtils.UnparseableExtraField.READ);

        assertEquals(1, parsedFields.length, "Should parse AsiExtraField data into a single ZipExtraField.");
        assertTrue(parsedFields[0] instanceof AsiExtraField, "Parsed field should be an instance of AsiExtraField.");
    }

    @Test
    void testUnparseableExtraFieldReadAction() {
        ExtraFieldUtils.UnparseableExtraField readAction = ExtraFieldUtils.UnparseableExtraField.READ;
        ZipExtraField result = readAction.onUnparseableExtraField(sampleByteArray, 4, 4, true, 4);

        assertNotNull(result, "Should return a ZipExtraField instance.");
        assertTrue(result instanceof UnparseableExtraFieldData, "Should return UnparseableExtraFieldData when read action is used.");
    }

    @Test
    void testUnparseableExtraFieldThrowActionThrowsException() {
        ExtraFieldUtils.UnparseableExtraField throwAction = ExtraFieldUtils.UnparseableExtraField.THROW;

        assertThrows(ZipException.class, () -> throwAction.onUnparseableExtraField(sampleByteArray, 565, -2168, false, (byte) 62),
                "Should throw ZipException when the data is unparseable and the action is THROW.");
    }

    @Test
    void testUnparseableExtraFieldGetKey() {
        ExtraFieldUtils.UnparseableExtraField throwAction = ExtraFieldUtils.UnparseableExtraField.THROW;
        assertEquals(0, throwAction.getKey(), "THROW key should be 0.");

        ExtraFieldUtils.UnparseableExtraField readAction = ExtraFieldUtils.UnparseableExtraField.READ;
        assertEquals(2, readAction.getKey(), "READ key should be 2.");

        ExtraFieldUtils.UnparseableExtraField skipAction = ExtraFieldUtils.UnparseableExtraField.SKIP;
        assertEquals(1, skipAction.getKey(), "SKIP key should be 1.");
    }

    @Test
    void testParseAsiExtraFieldSkipAction() throws ZipException {
        AsiExtraField asiExtraField = new AsiExtraField();
        byte[] localFileData = asiExtraField.getLocalFileDataData();

        ZipExtraField[] parsedFields = ExtraFieldUtils.parse(localFileData, true, ExtraFieldUtils.UnparseableExtraField.SKIP);

        assertEquals(0, parsedFields.length, "Should skip AsiExtraField data and return an empty array.");
    }

    @Test
    void testParseEmptyByteArrayFalse() throws ZipException {
        byte[] emptyByteArray = new byte[2];

        ZipExtraField[] parsedFields = ExtraFieldUtils.parse(emptyByteArray, false);
        assertNotNull(parsedFields, "Result should not be null.");
        assertEquals(0, parsedFields.length, "Should return an empty array when parsing an empty byte array.");
    }

    @Test
    void testParseEmptyByteArray() throws ZipException {
        byte[] emptyByteArray = new byte[0];
        ZipExtraField[] parsedFields = ExtraFieldUtils.parse(emptyByteArray);

        assertNotNull(parsedFields, "Result should not be null.");
        assertEquals(0, parsedFields.length, "Should return an empty array when parsing an empty byte array.");
    }

    @Test
    void testFillExtraFieldWithEmptyByteArray() throws ZipException {
        ZipExtraField[] emptyExtraFieldArray = new ZipExtraField[0];
        byte[] emptyByteArray = ExtraFieldUtils.mergeLocalFileDataData(emptyExtraFieldArray);
        X000A_NTFS ntfsExtraField = new X000A_NTFS();

        ExtraFieldUtils.fillExtraField(ntfsExtraField, emptyByteArray, 436, 0, true);

        assertEquals(0, emptyByteArray.length, "Byte array should still be empty after filling.");
        assertArrayEquals(new byte[] {}, emptyByteArray, "Byte array should not have been modified.");
    }

    @Test
    void testRegisterNullClassThrowsException() {
        assertThrows(NullPointerException.class, () -> ExtraFieldUtils.register(null), "Should throw NullPointerException when registering a null class.");
    }

    @Test
    void testRegisterInvalidClassThrowsException() {
        Class<Object> objectClass = Object.class;

        assertThrows(ClassCastException.class, () -> ExtraFieldUtils.register(objectClass), "Should throw ClassCastException when registering a class that does not implement ZipExtraField.");
    }

    @Test
    void testParseCentralDirectoryDataThrowsZipException() {
        AsiExtraField asiExtraField = new AsiExtraField();
        byte[] centralDirectoryData = asiExtraField.getCentralDirectoryData();
        ExtraFieldUtils.UnparseableExtraField throwAction = ExtraFieldUtils.UnparseableExtraField.THROW;

        assertThrows(ZipException.class, () -> ExtraFieldUtils.parse(centralDirectoryData, false, throwAction),
                "Should throw ZipException when central directory data is corrupt and the action is THROW.");
    }

    @Test
    void testParseNullByteArrayThrowsExceptionWithParsingBehavior() {
        ExtraFieldUtils.UnparseableExtraField readAction = ExtraFieldUtils.UnparseableExtraField.READ;

        assertThrows(NullPointerException.class, () -> ExtraFieldUtils.parse(null, false, readAction),
                "Should throw NullPointerException when parsing a null byte array.");
    }

    @Test
    void testParseCentralDirectoryDataThrowsZipExceptionWithoutParsingBehavior() {
        AsiExtraField asiExtraField = new AsiExtraField();
        byte[] centralDirectoryData = asiExtraField.getCentralDirectoryData();

        assertThrows(ZipException.class, () -> ExtraFieldUtils.parse(centralDirectoryData, false),
                "Should throw ZipException when central directory data is corrupt.");
    }

    @Test
    void testParseNullByteArrayThrowsException() {
        assertThrows(NullPointerException.class, () -> ExtraFieldUtils.parse(null, false),
                "Should throw NullPointerException when parsing a null byte array.");
    }

    @Test
    void testParseNullByteArrayThrowsExceptionNoArgs() {
        assertThrows(NullPointerException.class, () -> ExtraFieldUtils.parse(null),
                "Should throw NullPointerException when parsing a null byte array.");
    }

    @Test
    void testMergeLocalFileDataDataThrowsIllegalArgumentException() {
        ZipExtraField[] extraFields = new ZipExtraField[1];
        Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField();
        zip64ExtraField.setSize(ZipEightByteInteger.ZERO);
        extraFields[0] = zip64ExtraField;

        assertThrows(IllegalArgumentException.class, () -> ExtraFieldUtils.mergeLocalFileDataData(extraFields),
                "Should throw IllegalArgumentException when Zip64 extended information is incomplete.");
    }

    @Test
    void testMergeCentralDirectoryDataThrowsNullPointerException() {
        ZipExtraField[] extraFields = new ZipExtraField[1];

        assertThrows(NullPointerException.class, () -> ExtraFieldUtils.mergeCentralDirectoryData(extraFields),
                "Should throw NullPointerException when an element of the array is null.");
    }

    @Test
    void testFillExtraFieldThrowsZipException() {
        byte[] byteArray = new byte[4];
        X0016_CertificateIdForCentralDirectory certId = new X0016_CertificateIdForCentralDirectory();

        assertThrows(ZipException.class, () -> ExtraFieldUtils.fillExtraField(certId, byteArray, 9, 9, false),
                "Should throw ZipException when failing to parse corrupt ZIP extra field.");
    }

    @Test
    void testFillExtraFieldThrowsNegativeArraySizeException() {
        byte[] byteArray = new byte[26];
        Zip64ExtendedInformationExtraField zip64ExtraField = new Zip64ExtendedInformationExtraField();

        assertThrows(NegativeArraySizeException.class, () -> ExtraFieldUtils.fillExtraField(zip64ExtraField, byteArray, -1188, -1188, false),
                "Should throw NegativeArraySizeException due to negative size values.");
    }

    @Test
    void testCreateExtraFieldNoDefaultNullZipShort() {
        assertThrows(NullPointerException.class, () -> ExtraFieldUtils.createExtraFieldNoDefault(null), "Should throw NullPointerException when creating extra field with a null ZipShort.");
    }

    @Test
    void testCreateExtraFieldNullZipShort() {
        assertThrows(NullPointerException.class, () -> ExtraFieldUtils.createExtraField(null), "Should throw NullPointerException when creating extra field with a null ZipShort.");
    }

    @Test
    void testParseWithStrictParsingMode() throws ZipException {
        byte[] byteArray = new byte[4];
        byteArray[2] = (byte) 4;
        ZipArchiveEntry.ExtraFieldParsingMode strictMode = ZipArchiveEntry.ExtraFieldParsingMode.STRICT_FOR_KNOW_EXTRA_FIELDS;

        ZipExtraField[] parsedFields = ExtraFieldUtils.parse(byteArray, true, (ExtraFieldParsingBehavior) strictMode);

        assertEquals(1, parsedFields.length, "Should parse the extra field in STRICT mode.");
    }

    @Test
    void testParseWithOnlyParseableStrictParsingMode() throws ZipException {
        byte[] byteArray = new byte[7];
        byteArray[2] = (byte) (-1);
        ZipArchiveEntry.ExtraFieldParsingMode onlyParseableStrict = ZipArchiveEntry.ExtraFieldParsingMode.ONLY_PARSEABLE_STRICT;

        ZipExtraField[] parsedFields = ExtraFieldUtils.parse(byteArray, true, (ExtraFieldParsingBehavior) onlyParseableStrict);

        assertEquals(0, parsedFields.length, "Should not parse when in ONLY_PARSEABLE_STRICT mode and data is unparseable.");
    }

    @Test
    void testParseDraconianModeThrowsZipException() {
        AsiExtraField asiExtraField = new AsiExtraField();
        byte[] localFileData = asiExtraField.getLocalFileDataData();
        ZipArchiveEntry.ExtraFieldParsingMode draconianMode = ZipArchiveEntry.ExtraFieldParsingMode.DRACONIC;

        assertThrows(ZipException.class, () -> ExtraFieldUtils.parse(localFileData, true, (ExtraFieldParsingBehavior) draconianMode),
                "Should throw ZipException when parsing in DRACONIC mode and encountering bad data.");
    }

    @Test
    void testParseOnlyParseableStrictReturnsEmptyArray() throws ZipException {
        byte[] byteArray = new byte[1];
        ZipArchiveEntry.ExtraFieldParsingMode onlyParseableStrict = ZipArchiveEntry.ExtraFieldParsingMode.ONLY_PARSEABLE_STRICT;

        ZipExtraField[] parsedFields = ExtraFieldUtils.parse(byteArray, false, (ExtraFieldParsingBehavior) onlyParseableStrict);

        assertEquals(0, parsedFields.length, "Should return empty array in ONLY_PARSEABLE_STRICT if unparseable.");
    }

    @Test
    void testParseWithNullParsingBehavior() {
        byte[] byteArray = new byte[8];

        assertThrows(NullPointerException.class, () -> ExtraFieldUtils.parse(byteArray, true, (ExtraFieldParsingBehavior) null),
                "Should throw NullPointerException when parsing with a null ExtraFieldParsingBehavior.");
    }

    @Test
    void testFillExtraFieldWithNullByteArrayInExtendedTimestampThrowsException() {
        X5455_ExtendedTimestamp extendedTimestamp = new X5455_ExtendedTimestamp();

        assertThrows(NullPointerException.class, () -> ExtraFieldUtils.fillExtraField(extendedTimestamp, null, (byte) 4, (byte) 2, false),
                "Should throw NullPointerException when the byte array is null.");
    }

    @Test
    void testFillExtraFieldThrowsIllegalArgumentExceptionDueToLength() {
        byte[] byteArray = new byte[6];
        UnparseableExtraFieldData unparseableExtraFieldData = new UnparseableExtraFieldData();

        assertThrows(IllegalArgumentException.class, () -> ExtraFieldUtils.fillExtraField(unparseableExtraFieldData, byteArray, -3, -3, true),
                "Should throw IllegalArgumentException due to length issues.");
    }

    @Test
    void testCreateExtraFieldNoDefault() {
        UnparseableExtraFieldData unparseableExtraFieldData = new UnparseableExtraFieldData();
        ZipShort headerId = unparseableExtraFieldData.getHeaderId();

        ZipExtraField extraField = ExtraFieldUtils.createExtraFieldNoDefault(headerId);

        assertNotSame(unparseableExtraFieldData, extraField, "Created field should not be the same instance.");
    }

    @Test
    void testCreateExtraFieldNoDefaultReturnsNull() {
        AsiExtraField asiExtraField = new AsiExtraField();
        ZipShort localFileDataLength = asiExtraField.getLocalFileDataLength();

        ZipExtraField extraField = ExtraFieldUtils.createExtraFieldNoDefault(localFileDataLength);

        assertNull(extraField, "Should return null if the header ID is not known.");
    }

    @Test
    void testCreateExtraFieldAsiExtraField() {
        AsiExtraField asiExtraField = new AsiExtraField();
        ZipShort localFileDataLength = asiExtraField.getLocalFileDataLength();

        ZipExtraField extraField = ExtraFieldUtils.createExtraField(localFileDataLength);

        assertNotNull(extraField, "Should not return null if the header ID is known.");
    }

    @Test
    void testCreateExtraFieldUnparseableExtraField() {
        UnparseableExtraFieldData unparseableExtraFieldData = new UnparseableExtraFieldData();
        ZipShort headerId = unparseableExtraFieldData.getHeaderId();

        ZipExtraField extraField = ExtraFieldUtils.createExtraField(headerId);

        assertNotSame(unparseableExtraFieldData, extraField, "Created field should not be the same instance.");
    }

    @Test
    void testMergeLocalFileDataDataWithMultipleEntries() {
        AsiExtraField asiExtraField = new AsiExtraField();
        ZipExtraField[] extraFields = new ZipExtraField[2];
        extraFields[0] = asiExtraField;
        extraFields[1] = asiExtraField;

        byte[] mergedData = ExtraFieldUtils.mergeLocalFileDataData(extraFields);

        assertEquals(36, mergedData.length, "Should have correct length when merging two AsiExtraFields.");
    }

    @Test
    void testRegisterValidClass() {
        ExtraFieldUtils.register(UnparseableExtraFieldData.class);
    }

    @Test
    void testUnparseableExtraFieldDataMergeLocalFileDataData() {
        ZipExtraField[] zipExtraFieldArray0 = new ZipExtraField[1];
        UnparseableExtraFieldData unparseableExtraFieldData0 = new UnparseableExtraFieldData();
        zipExtraFieldArray0[0] = (ZipExtraField) unparseableExtraFieldData0;
        assertThrows(NoClassDefFoundError.class, () -> ExtraFieldUtils.mergeLocalFileDataData(zipExtraFieldArray0));

    }

    @Test
    void testMergeCentralDirectoryDataThrowsIndexOutOfBoundsException() {
        ZipExtraField[] emptyExtraFieldArray = new ZipExtraField[0];
        byte[] byteArray0 = ExtraFieldUtils.mergeCentralDirectoryData(emptyExtraFieldArray);
        Zip64ExtendedInformationExtraField zip64ExtendedInformationExtraField0 = new Zip64ExtendedInformationExtraField();
        assertThrows(IndexOutOfBoundsException.class, () -> ExtraFieldUtils.fillExtraField(zip64ExtendedInformationExtraField0, byteArray0, 32768, 32768, true));
    }

    @Test
    void testParseByteArrayWithUnparseableExtraFieldData() throws ZipException {
        AsiExtraField asiExtraField = new AsiExtraField();
        byte[] byteArray0 = asiExtraField.getLocalFileDataData();
        ExtraFieldUtils.UnparseableExtraField extraFieldUtils_UnparseableExtraField0 = ExtraFieldUtils.UnparseableExtraField.READ;
        ZipExtraField[] zipExtraFieldArray0 = ExtraFieldUtils.parse(byteArray0, true, extraFieldUtils_UnparseableExtraField0);
        assertEquals(1, zipExtraFieldArray0.length);
    }

    @Test
    void testMergeCentralDirectoryDataParseByteArrayWithUnparseableExtraFieldData() throws ZipException {
        byte[] byteArray0 = new byte[4];
        ExtraFieldUtils.UnparseableExtraField extraFieldUtils_UnparseableExtraField0 = ExtraFieldUtils.UnparseableExtraField.READ;
        ZipExtraField zipExtraField0 = extraFieldUtils_UnparseableExtraField0.onUnparseableExtraField(byteArray0, 2, 32768, false, (byte)0);
        ZipExtraField[] zipExtraFieldArray0 = new ZipExtraField[4];
        zipExtraFieldArray0[0] = zipExtraField0;
        zipExtraFieldArray0[1] = zipExtraField0;
        zipExtraFieldArray0[2] = zipExtraField0;
        zipExtraFieldArray0[3] = zipExtraField0;
        byte[] byteArray1 = ExtraFieldUtils.mergeCentralDirectoryData(zipExtraFieldArray0);
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> ExtraFieldUtils.parse(byteArray1, false));
    }

    @Test
    void testParseCentralDirectoryDataThrowsZipExceptionForAsiExtraField() {
        AsiExtraField asiExtraField = new AsiExtraField();
        byte[] byteArray0 = asiExtraField.getCentralDirectoryData();
        assertThrows(ZipException.class, () -> ExtraFieldUtils.parse(byteArray0));
    }

    @Test
    void testParseSkipActionAddsUnparseableExtraField() throws ZipException {
        byte[] byteArray0 = new byte[9];
        byteArray0[6] = (byte)42;
        ExtraFieldUtils.UnparseableExtraField extraFieldUtils_UnparseableExtraField0 = ExtraFieldUtils.UnparseableExtraField.SKIP;
        ZipExtraField[] zipExtraFieldArray0 = ExtraFieldUtils.parse(byteArray0, false, extraFieldUtils_UnparseableExtraField0);
        assertEquals(1, zipExtraFieldArray0.length);
    }

    @Test
    void testUnparseableExtraFieldReadKeyIsTwo() {
        ExtraFieldUtils.UnparseableExtraField extraFieldUtils_UnparseableExtraField0 = ExtraFieldUtils.UnparseableExtraField.READ;
        int int0 = extraFieldUtils_UnparseableExtraField0.getKey();
        assertEquals(2, int0);
    }

    @Test
    void testParseWithByteArrayReturnsOneExtraFieldData() throws ZipException {
        byte[] byteArray0 = new byte[4];
        ZipExtraField[] zipExtraFieldArray0 = ExtraFieldUtils.parse(byteArray0, false);
        assertEquals(1, zipExtraFieldArray0.length);
    }
}