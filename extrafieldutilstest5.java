package org.apache.commons.compress.archivers.zip;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.zip.ZipException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests error handling scenarios for {@link ExtraFieldUtils}.
 */
class ExtraFieldUtilsTest {

    /**
     * A mock {@link ZipExtraField} that always throws an {@link ArrayIndexOutOfBoundsException}
     * from its parse methods. This is used to verify that {@link ExtraFieldUtils} correctly
     * wraps such low-level parsing errors into a {@link ZipException}.
     */
    private static class ExceptionThrowingExtraField implements ZipExtraField {

        static final ZipShort HEADER_ID = new ZipShort(0x1000);
        private static final int DATA_LENGTH = 4;

        @Override
        public ZipShort getHeaderId() {
            return HEADER_ID;
        }

        @Override
        public ZipShort getLocalFileDataLength() {
            return new ZipShort(DATA_LENGTH);
        }

        @Override
        public byte[] getLocalFileDataData() {
            return new byte[DATA_LENGTH];
        }

        @Override
        public ZipShort getCentralDirectoryLength() {
            return getLocalFileDataLength();
        }

        @Override
        public byte[] getCentralDirectoryData() {
            return getLocalFileDataData();
        }

        @Override
        public void parseFromLocalFileData(final byte[] buffer, final int offset, final int length) {
            throw new ArrayIndexOutOfBoundsException("Simulating a parsing error.");
        }

        @Override
        public void parseFromCentralDirectoryData(final byte[] buffer, final int offset, final int length) {
            parseFromLocalFileData(buffer, offset, length);
        }
    }

    @Test
    @DisplayName("parse() should wrap an ArrayIndexOutOfBoundsException from a field's parser in a ZipException")
    void parseShouldWrapUnderlyingParsingException() {
        // This test relies on the deprecated static registration method to inject
        // a custom ZipExtraField implementation for testing purposes.
        ExtraFieldUtils.register(ExceptionThrowingExtraField.class);

        // Arrange
        final ExceptionThrowingExtraField faultyField = new ExceptionThrowingExtraField();
        final byte[] extraFieldData = buildExtraFieldData(faultyField);

        // The SUT is expected to format the header ID as a hex string in the exception message.
        final String expectedMessage = "Failed to parse corrupt ZIP extra field of type "
            + Integer.toHexString(ExceptionThrowingExtraField.HEADER_ID.getValue());

        // Act & Assert
        final ZipException thrown = assertThrows(ZipException.class,
            () -> ExtraFieldUtils.parse(extraFieldData),
            "Parsing data from a field that throws an exception should result in a ZipException.");

        assertEquals(expectedMessage, thrown.getMessage(), "The exception message should identify the faulty field type.");
    }

    /**
     * Creates a raw byte array representing the data for a single extra field.
     * The format is: [Header ID (2 bytes)][Data Length (2 bytes)][Data].
     *
     * @param field The extra field to serialize.
     * @return A byte array with the serialized extra field data.
     */
    private byte[] buildExtraFieldData(final ZipExtraField field) {
        final byte[] headerId = field.getHeaderId().getBytes();
        final byte[] dataLength = field.getLocalFileDataLength().getBytes();
        final byte[] data = field.getLocalFileDataData();

        final byte[] rawData = new byte[headerId.length + dataLength.length + data.length];

        System.arraycopy(headerId, 0, rawData, 0, headerId.length);
        System.arraycopy(dataLength, 0, rawData, headerId.length, dataLength.length);
        System.arraycopy(data, 0, rawData, headerId.length + dataLength.length, data.length);

        return rawData;
    }
}