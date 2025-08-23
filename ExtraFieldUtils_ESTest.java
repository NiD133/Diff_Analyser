package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.*;

import java.util.zip.ZipException;

import org.junit.Test;

/**
 * Readable tests for ExtraFieldUtils.
 *
 * Goals:
 * - Cover common, developer-facing behavior.
 * - Use descriptive test names.
 * - Avoid brittle assertions (e.g., on exact exception messages).
 * - Prefer small, self-contained helpers where needed.
 */
public class ExtraFieldUtilsReadableTest {

    // ------------------------------------------------------------
    // Helpers
    // ------------------------------------------------------------

    /**
     * Builds a single extra field block with the given header id and declared length,
     * but truncates the payload to a smaller size to force "unparseable" behavior.
     *
     * Format: [2 bytes header id][2 bytes length][payload (truncated)]
     */
    private static byte[] buildTruncatedExtraField(final int headerId, final int declaredLength, final int actualPayloadBytes) {
        final byte[] data = new byte[4 + Math.max(actualPayloadBytes, 0)];
        // header id (little endian)
        data[0] = (byte) (headerId & 0xFF);
        data[1] = (byte) ((headerId >>> 8) & 0xFF);
        // length (little endian)
        data[2] = (byte) (declaredLength & 0xFF);
        data[3] = (byte) ((declaredLength >>> 8) & 0xFF);
        // payload: leave zeros; size is intentionally less than declaredLength
        return data;
    }

    // ------------------------------------------------------------
    // createExtraField / createExtraFieldNoDefault
    // ------------------------------------------------------------

    @Test
    public void createExtraField_returnsKnownImplementation() {
        ZipExtraField field = ExtraFieldUtils.createExtraField(AsiExtraField.HEADER_ID);
        assertNotNull(field);
        assertTrue(field instanceof AsiExtraField);
    }

    @Test
    public void createExtraField_unknownId_returnsUnrecognizedExtraField() {
        ZipShort unknown = new ZipShort(0xCAFE);
        ZipExtraField field = ExtraFieldUtils.createExtraField(unknown);
        assertNotNull(field);
        assertTrue("Unknown headers should use UnrecognizedExtraField",
                field instanceof UnrecognizedExtraField);
    }

    @Test
    public void createExtraFieldNoDefault_knownId_returnsImplementation() {
        ZipExtraField field = ExtraFieldUtils.createExtraFieldNoDefault(AsiExtraField.HEADER_ID);
        assertNotNull(field);
        assertTrue(field instanceof AsiExtraField);
    }

    @Test
    public void createExtraFieldNoDefault_unknownId_returnsNull() {
        ZipShort unknown = new ZipShort(0xBEEF);
        assertNull(ExtraFieldUtils.createExtraFieldNoDefault(unknown));
    }

    @Test(expected = NullPointerException.class)
    public void createExtraField_nullId_throwsNPE() {
        ExtraFieldUtils.createExtraField(null);
    }

    @Test(expected = NullPointerException.class)
    public void createExtraFieldNoDefault_nullId_throwsNPE() {
        ExtraFieldUtils.createExtraFieldNoDefault(null);
    }

    // ------------------------------------------------------------
    // parse (empty / null)
    // ------------------------------------------------------------

    @Test
    public void parse_emptyData_returnsEmptyArray() throws Exception {
        ZipExtraField[] fields = ExtraFieldUtils.parse(new byte[0]);
        assertNotNull(fields);
        assertEquals(0, fields.length);
    }

    @Test(expected = NullPointerException.class)
    public void parse_null_throwsNPE() throws Exception {
        ExtraFieldUtils.parse((byte[]) null);
    }

    // ------------------------------------------------------------
    // parse (unparseable behavior: THROW / SKIP / READ)
    // ------------------------------------------------------------

    @Test
    public void parse_unparseable_withThrow_throwsZipException() {
        byte[] data = buildTruncatedExtraField(0xCAFE, /*declared*/ 4, /*actual*/ 1);
        assertThrows(ZipException.class, () ->
                ExtraFieldUtils.parse(data, /*local*/ true, ExtraFieldUtils.UnparseableExtraField.THROW));
    }

    @Test
    public void parse_unparseable_withSkip_returnsEmpty() throws Exception {
        byte[] data = buildTruncatedExtraField(0xCAFE, /*declared*/ 8, /*actual*/ 0);
        ZipExtraField[] result = ExtraFieldUtils.parse(data, /*local*/ true, ExtraFieldUtils.UnparseableExtraField.SKIP);
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    public void parse_unparseable_withRead_returnsUnparseableExtraFieldData() throws Exception {
        byte[] data = buildTruncatedExtraField(0xCAFE, /*declared*/ 16, /*actual*/ 1);
        ZipExtraField[] result = ExtraFieldUtils.parse(data, /*local*/ true, ExtraFieldUtils.UnparseableExtraField.READ);
        assertNotNull(result);
        assertEquals(1, result.length);
        assertTrue(result[0] instanceof UnparseableExtraFieldData);
    }

    // ------------------------------------------------------------
    // merge (empty)
    // ------------------------------------------------------------

    @Test
    public void mergeLocalFileData_empty_returnsEmptyByteArray() {
        byte[] merged = ExtraFieldUtils.mergeLocalFileDataData(ExtraFieldUtils.EMPTY_ZIP_EXTRA_FIELD_ARRAY);
        assertNotNull(merged);
        assertEquals(0, merged.length);
    }

    @Test
    public void mergeCentralDirectory_empty_returnsEmptyByteArray() {
        byte[] merged = ExtraFieldUtils.mergeCentralDirectoryData(ExtraFieldUtils.EMPTY_ZIP_EXTRA_FIELD_ARRAY);
        assertNotNull(merged);
        assertEquals(0, merged.length);
    }

    // ------------------------------------------------------------
    // fillExtraField (wraps AIOOBE into ZipException)
    // ------------------------------------------------------------

    @Test
    public void fillExtraField_wrapsArrayIndexOutOfBounds_asZipException() {
        ResourceAlignmentExtraField field = new ResourceAlignmentExtraField();
        byte[] tooShort = new byte[0]; // intentionally too short for requested len
        assertThrows(ZipException.class, () ->
                ExtraFieldUtils.fillExtraField(field, tooShort, /*off*/ 0, /*len*/ 2, /*local*/ false));
    }

    // ------------------------------------------------------------
    // merge (Zip64 constraints)
    // ------------------------------------------------------------

    @Test
    public void mergeLocalFileData_withZip64MissingSizes_throwsIAE() {
        Zip64ExtendedInformationExtraField zip64 = new Zip64ExtendedInformationExtraField();
        zip64.setSize(ZipEightByteInteger.ZERO); // missing compressed size in local header
        ZipExtraField[] fields = new ZipExtraField[] { zip64 };
        assertThrows(IllegalArgumentException.class, () ->
                ExtraFieldUtils.mergeLocalFileDataData(fields));
    }

    // ------------------------------------------------------------
    // register
    // ------------------------------------------------------------

    @Test
    public void register_roundTrip_allowsCreationViaCreateExtraField() {
        // UnparseableExtraFieldData implements ZipExtraField and has a no-arg constructor.
        ExtraFieldUtils.register(UnparseableExtraFieldData.class);

        ZipShort id = new UnparseableExtraFieldData().getHeaderId();
        ZipExtraField created = ExtraFieldUtils.createExtraField(id);
        assertNotNull(created);
        assertEquals(UnparseableExtraFieldData.class, created.getClass());
    }

    @Test(expected = NullPointerException.class)
    public void register_null_throwsNPE() {
        ExtraFieldUtils.register(null);
    }

    @Test(expected = ClassCastException.class)
    public void register_classNotImplementingZipExtraField_throwsCCE() {
        ExtraFieldUtils.register(Object.class);
    }
}