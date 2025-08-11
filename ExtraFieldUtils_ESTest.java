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
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class ExtraFieldUtilsTest extends ExtraFieldUtils_ESTest_scaffolding {

    private static final int TIMEOUT = 4000;

    @Test(timeout = TIMEOUT)
    public void testCreateExtraFieldWithUnparseableData() throws Throwable {
        UnparseableExtraFieldData unparseableData = new UnparseableExtraFieldData();
        ZipShort headerId = unparseableData.getHeaderId();
        ZipExtraField extraField = ExtraFieldUtils.createExtraField(headerId);
        assertFalse(extraField.equals(unparseableData));
    }

    @Test(timeout = TIMEOUT)
    public void testMergeLocalFileDataWithNullField() {
        ExtraFieldUtils.UnparseableExtraField unparseableField = ExtraFieldUtils.UnparseableExtraField.READ;
        byte[] byteArray = new byte[8];
        ZipExtraField extraField = unparseableField.onUnparseableExtraField(byteArray, (byte) 0, (byte) 7, false, (byte) 7);
        ZipExtraField[] extraFields = {null, extraField};

        assertThrows(NullPointerException.class, () -> ExtraFieldUtils.mergeLocalFileDataData(extraFields));
    }

    @Test(timeout = TIMEOUT)
    public void testMergeCentralDirectoryDataWithNullField() {
        ExtraFieldUtils.UnparseableExtraField unparseableField = ExtraFieldUtils.UnparseableExtraField.READ;
        byte[] byteArray = new byte[8];
        ZipExtraField extraField = unparseableField.onUnparseableExtraField(byteArray, 6, (byte) 7, false, 60436);
        ZipExtraField[] extraFields = {null, null, null, null, extraField};

        assertThrows(NullPointerException.class, () -> ExtraFieldUtils.mergeCentralDirectoryData(extraFields));
    }

    @Test(timeout = TIMEOUT)
    public void testOnUnparseableExtraFieldWithInvalidOffset() {
        byte[] byteArray = new byte[0];
        ExtraFieldUtils.UnparseableExtraField unparseableField = ExtraFieldUtils.UnparseableExtraField.READ;

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> 
            unparseableField.onUnparseableExtraField(byteArray, 3722, 0, true, 0)
        );
    }

    @Test(timeout = TIMEOUT)
    public void testUnparseableExtraFieldSkipKey() {
        ExtraFieldUtils.UnparseableExtraField unparseableField = ExtraFieldUtils.UnparseableExtraField.SKIP;
        assertEquals(1, unparseableField.getKey());
    }

    @Test(timeout = TIMEOUT)
    public void testMergeLocalFileDataWithEmptyFields() throws Throwable {
        ZipExtraField[] extraFields = new ZipExtraField[0];
        byte[] byteArray = ExtraFieldUtils.mergeLocalFileDataData(extraFields);
        ExtraFieldUtils.parse(byteArray, true);
        assertEquals(0, byteArray.length);
        assertArrayEquals(new byte[]{}, byteArray);
    }

    @Test(timeout = TIMEOUT)
    public void testParseWithEmptyByteArray() throws Throwable {
        byte[] byteArray = new byte[0];
        ZipExtraField[] extraFields = ExtraFieldUtils.parse(byteArray);
        assertEquals(0, extraFields.length);
    }

    @Test(timeout = TIMEOUT)
    public void testFillExtraFieldWithEmptyData() throws Throwable {
        ZipExtraField[] extraFields = new ZipExtraField[0];
        byte[] byteArray = ExtraFieldUtils.mergeLocalFileDataData(extraFields);
        ExtraFieldUtils.UnparseableExtraField unparseableField = ExtraFieldUtils.UnparseableExtraField.READ;
        ZipExtraField extraField = unparseableField.onUnparseableExtraField(byteArray, 0, 0, false, 0);
        assertNotNull(extraField);

        ExtraFieldUtils.fillExtraField(extraField, byteArray, 0, 0, true);
        assertEquals(0, byteArray.length);
        assertArrayEquals(new byte[]{}, byteArray);
    }

    @Test(timeout = TIMEOUT)
    public void testRegisterWithNullClass() {
        assertThrows(NullPointerException.class, () -> ExtraFieldUtils.register(null));
    }

    @Test(timeout = TIMEOUT)
    public void testRegisterWithInvalidClass() {
        Class<Object> clazz = Object.class;
        assertThrows(ClassCastException.class, () -> ExtraFieldUtils.register(clazz));
    }

    @Test(timeout = TIMEOUT)
    public void testParseWithInvalidExtraFieldData() {
        AsiExtraField asiExtraField = new AsiExtraField();
        byte[] byteArray = asiExtraField.getLocalFileDataData();
        ExtraFieldUtils.UnparseableExtraField unparseableField = ExtraFieldUtils.UnparseableExtraField.THROW;

        assertThrows(ZipException.class, () -> 
            ExtraFieldUtils.parse(byteArray, false, unparseableField)
        );
    }

    @Test(timeout = TIMEOUT)
    public void testParseWithNullByteArray() {
        ExtraFieldUtils.UnparseableExtraField unparseableField = ExtraFieldUtils.UnparseableExtraField.SKIP;

        assertThrows(NullPointerException.class, () -> 
            ExtraFieldUtils.parse(null, true, unparseableField)
        );
    }

    @Test(timeout = TIMEOUT)
    public void testMergeLocalFileDataWithIncompleteZip64Field() {
        ZipExtraField[] extraFields = new ZipExtraField[1];
        Zip64ExtendedInformationExtraField zip64Field = new Zip64ExtendedInformationExtraField();
        ZipEightByteInteger zero = ZipEightByteInteger.ZERO;
        zip64Field.setSize(zero);
        extraFields[0] = zip64Field;

        assertThrows(IllegalArgumentException.class, () -> 
            ExtraFieldUtils.mergeLocalFileDataData(extraFields)
        );
    }

    @Test(timeout = TIMEOUT)
    public void testMergeCentralDirectoryDataWithNullFields() {
        ZipExtraField[] extraFields = new ZipExtraField[2];

        assertThrows(NullPointerException.class, () -> 
            ExtraFieldUtils.mergeCentralDirectoryData(extraFields)
        );
    }

    @Test(timeout = TIMEOUT)
    public void testParseWithInvalidByteArray() {
        byte[] byteArray = new byte[12];
        ZipExtraField[] extraFields = ExtraFieldUtils.parse(byteArray);

        assertThrows(NoClassDefFoundError.class, () -> 
            ExtraFieldUtils.mergeCentralDirectoryData(extraFields)
        );
    }

    @Test(timeout = TIMEOUT)
    public void testFillExtraFieldWithInvalidData() {
        ResourceAlignmentExtraField resourceField = new ResourceAlignmentExtraField();
        byte[] byteArray = new byte[0];

        assertThrows(ZipException.class, () -> 
            ExtraFieldUtils.fillExtraField(resourceField, byteArray, 2, 2, false)
        );
    }

    @Test(timeout = TIMEOUT)
    public void testFillExtraFieldWithNegativeIndices() {
        X0019_EncryptionRecipientCertificateList encryptionField = new X0019_EncryptionRecipientCertificateList();
        byte[] byteArray = new byte[0];

        assertThrows(IllegalArgumentException.class, () -> 
            ExtraFieldUtils.fillExtraField(encryptionField, byteArray, -1100, -1100, false)
        );
    }

    @Test(timeout = TIMEOUT)
    public void testCreateExtraFieldNoDefaultWithNull() {
        assertThrows(NullPointerException.class, () -> 
            ExtraFieldUtils.createExtraFieldNoDefault(null)
        );
    }

    @Test(timeout = TIMEOUT)
    public void testCreateExtraFieldWithNull() {
        assertThrows(NullPointerException.class, () -> 
            ExtraFieldUtils.createExtraField(null)
        );
    }

    @Test(timeout = TIMEOUT)
    public void testParseWithOnlyParseableLenientMode() throws Throwable {
        byte[] byteArray = new byte[9];
        byteArray[3] = (byte) (-47);
        ZipArchiveEntry.ExtraFieldParsingMode parsingMode = ZipArchiveEntry.ExtraFieldParsingMode.ONLY_PARSEABLE_LENIENT;
        ZipExtraField[] extraFields = ExtraFieldUtils.parse(byteArray, true, parsingMode);
        assertEquals(0, extraFields.length);
    }

    @Test(timeout = TIMEOUT)
    public void testParseWithStrictForKnownFieldsMode() {
        byte[] byteArray = new byte[4];
        byteArray[2] = (byte) 118;
        ZipArchiveEntry.ExtraFieldParsingMode parsingMode = ZipArchiveEntry.ExtraFieldParsingMode.STRICT_FOR_KNOW_EXTRA_FIELDS;
        ZipExtraField[] extraFields = ExtraFieldUtils.parse(byteArray, true, parsingMode);

        assertThrows(NoClassDefFoundError.class, () -> 
            ExtraFieldUtils.mergeLocalFileDataData(extraFields)
        );
    }

    @Test(timeout = TIMEOUT)
    public void testParseWithSkipUnparseableData() throws Throwable {
        AsiExtraField asiExtraField = new AsiExtraField();
        byte[] byteArray = asiExtraField.getCentralDirectoryData();
        ExtraFieldUtils.UnparseableExtraField unparseableField = ExtraFieldUtils.UnparseableExtraField.SKIP;
        ZipExtraField[] extraFields = ExtraFieldUtils.parse(byteArray, false, unparseableField);
        assertEquals(0, extraFields.length);
    }

    @Test(timeout = TIMEOUT)
    public void testParseWithReadUnparseableData() throws Throwable {
        AsiExtraField asiExtraField = new AsiExtraField();
        byte[] byteArray = asiExtraField.getCentralDirectoryData();
        ExtraFieldUtils.UnparseableExtraField unparseableField = ExtraFieldUtils.UnparseableExtraField.READ;
        ZipExtraField[] extraFields = ExtraFieldUtils.parse(byteArray, false, unparseableField);
        byte[] mergedData = ExtraFieldUtils.mergeCentralDirectoryData(extraFields);
        assertEquals(14, mergedData.length);
    }

    @Test(timeout = TIMEOUT)
    public void testUnparseableExtraFieldThrowKey() {
        ExtraFieldUtils.UnparseableExtraField unparseableField = ExtraFieldUtils.UnparseableExtraField.THROW;
        assertEquals(0, unparseableField.getKey());
    }

    @Test(timeout = TIMEOUT)
    public void testMergeLocalFileDataWithMultipleNTFSFields() throws Throwable {
        X000A_NTFS ntfsField = new X000A_NTFS();
        ZipExtraField[] extraFields = {ntfsField, ntfsField, ntfsField, ntfsField, ntfsField, ntfsField};
        byte[] byteArray = ExtraFieldUtils.mergeLocalFileDataData(extraFields);
        ZipExtraField[] parsedFields = ExtraFieldUtils.parse(byteArray, true);
        assertEquals(216, byteArray.length);
        assertEquals(6, parsedFields.length);
    }

    @Test(timeout = TIMEOUT)
    public void testRegisterUnparseableExtraFieldData() {
        Class<UnparseableExtraFieldData> clazz = UnparseableExtraFieldData.class;
        ExtraFieldUtils.register(clazz);
    }

    @Test(timeout = TIMEOUT)
    public void testMergeCentralDirectoryDataWithMultipleNTFSFields() throws Throwable {
        X000A_NTFS ntfsField = new X000A_NTFS();
        ZipExtraField[] extraFields = {ntfsField, ntfsField, ntfsField, ntfsField, ntfsField, ntfsField};
        byte[] byteArray = ExtraFieldUtils.mergeCentralDirectoryData(extraFields);
        ZipExtraField[] parsedFields = ExtraFieldUtils.parse(byteArray);
        assertEquals(6, parsedFields.length);
    }
}