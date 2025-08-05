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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.zip.ZipException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * JUnit tests for {@link org.apache.commons.compress.archivers.zip.ExtraFieldUtils}.
 */
@DisplayName("ExtraFieldUtils Tests")
class ExtraFieldUtilsTest implements UnixStat {

    /**
     * A test-only ZipExtraField implementation that throws an exception during parsing,
     * simulating a corrupted or buggy field implementation.
     */
    public static class AiobThrowingExtraField implements ZipExtraField {
        static final int LENGTH = 4;
        static final ZipShort HEADER_ID = new ZipShort(0x1000); // AIOB = ArrayIndexOutOfBounds

        @Override
        public ZipShort getHeaderId() {
            return HEADER_ID;
        }

        @Override
        public ZipShort getLocalFileDataLength() {
            return new ZipShort(LENGTH);
        }

        @Override
        public ZipShort getCentralDirectoryLength() {
            return getLocalFileDataLength();
        }

        @Override
        public byte[] getLocalFileDataData() {
            return new byte[LENGTH];
        }

        @Override
        public byte[] getCentralDirectoryData() {
            return getLocalFileDataData();
        }

        @Override
        public void parseFromLocalFileData(final byte[] buffer, final int offset, final int length) {
            throw new ArrayIndexOutOfBoundsException("Simulating a parsing error");
        }

        @Override
        public void parseFromCentralDirectoryData(final byte[] buffer, final int offset, final int length) {
            parseFromLocalFileData(buffer, offset, length);
        }
    }

    /** Header-ID of a ZipExtraField not supported by Commons Compress. */
    private static final ZipShort UNRECOGNIZED_HEADER = new ZipShort(0x5555);

    private AsiExtraField asiField;
    private UnrecognizedExtraField unrecognizedField;

    @BeforeEach
    void setUp() {
        asiField = new AsiExtraField();
        asiField.setMode(0755);
        asiField.setDirectory(true);

        unrecognizedField = new UnrecognizedExtraField();
        unrecognizedField.setHeaderId(UNRECOGNIZED_HEADER);
        unrecognizedField.setLocalFileDataData(new byte[] { 0 });
        unrecognizedField.setCentralDirectoryData(new byte[] { 0 });
    }

    @Nested
    @DisplayName("merge methods")
    class MergeTests {

        @Test
        @DisplayName("should correctly merge local file data which can be parsed back")
        void shouldMergeLocalFileDataAndAllowRoundtrip() throws Exception {
            // Arrange
            final ZipExtraField[] originalFields = { asiField, unrecognizedField };

            // Act
            final byte[] mergedData = ExtraFieldUtils.mergeLocalFileDataData(originalFields);
            final ZipExtraField[] parsedFields = ExtraFieldUtils.parse(mergedData, true);

            // Assert
            assertAll("Parsed fields should match original fields",
                () -> assertEquals(2, parsedFields.length, "Should have 2 fields"),
                () -> assertInstanceOf(AsiExtraField.class, parsedFields[0], "First field should be AsiExtraField"),
                () -> assertEquals(040755, ((AsiExtraField) parsedFields[0]).getMode(), "Mode should match"),
                () -> assertInstanceOf(UnrecognizedExtraField.class, parsedFields[1], "Second field should be UnrecognizedExtraField"),
                () -> assertEquals(1, parsedFields[1].getLocalFileDataLength().getValue(), "Data length should match")
            );
        }

        @Test
        @DisplayName("should correctly merge central directory data which can be parsed back")
        void shouldMergeCentralDirectoryDataAndAllowRoundtrip() throws Exception {
            // Arrange
            final ZipExtraField[] originalFields = { asiField, unrecognizedField };

            // Act
            final byte[] mergedData = ExtraFieldUtils.mergeCentralDirectoryData(originalFields);
            final ZipExtraField[] parsedFields = ExtraFieldUtils.parse(mergedData, false);

            // Assert
            assertAll("Parsed fields should match original fields",
                () -> assertEquals(2, parsedFields.length, "Should have 2 fields"),
                () -> assertInstanceOf(AsiExtraField.class, parsedFields[0], "First field should be AsiExtraField"),
                () -> assertEquals(040755, ((AsiExtraField) parsedFields[0]).getMode(), "Mode should match"),
                () -> assertInstanceOf(UnrecognizedExtraField.class, parsedFields[1], "Second field should be UnrecognizedExtraField"),
                () -> assertEquals(1, parsedFields[1].getCentralDirectoryLength().getValue(), "Data length should match")
            );
        }

        @Test
        @DisplayName("should correctly merge unparseable extra field data")
        void shouldMergeUnparseableData() throws Exception {
            // Arrange
            final UnparseableExtraFieldData unparseableField = new UnparseableExtraFieldData();
            final byte[] unparseableBytes = { UNRECOGNIZED_HEADER.getByte1(), UNRECOGNIZED_HEADER.getByte2(), 1, 0, (byte) 0xFF };
            unparseableField.parseFromLocalFileData(unparseableBytes, 0, unparseableBytes.length);

            final ZipExtraField[] originalFields = { asiField, unparseableField };

            // Act
            final byte[] mergedData = ExtraFieldUtils.mergeLocalFileDataData(originalFields);
            // The unparseable data includes header and length, so we parse with READ to recover it.
            final ZipExtraField[] parsedFields = ExtraFieldUtils.parse(mergedData, true, ExtraFieldUtils.UnparseableExtraField.READ);

            // Assert
            assertAll("Parsed fields should include original AsiExtraField and recovered unparseable data",
                () -> assertEquals(2, parsedFields.length),
                () -> assertInstanceOf(AsiExtraField.class, parsedFields[0]),
                () -> assertInstanceOf(UnparseableExtraFieldData.class, parsedFields[1]),
                () -> assertArrayEquals(unparseableBytes, parsedFields[1].getLocalFileDataData())
            );
        }
    }

    @Nested
    @DisplayName("parse method")
    class ParseTests {

        @Test
        @DisplayName("should parse valid extra field data correctly")
        void shouldParseValidData() throws Exception {
            // Arrange
            final byte[] localData = ExtraFieldUtils.mergeLocalFileDataData(new ZipExtraField[] { asiField, unrecognizedField });

            // Act
            final ZipExtraField[] parsedFields = ExtraFieldUtils.parse(localData);

            // Assert
            assertAll("Parsed fields should match original structure",
                () -> assertEquals(2, parsedFields.length, "Should have 2 fields"),
                () -> assertInstanceOf(AsiExtraField.class, parsedFields[0], "First field should be AsiExtraField"),
                () -> assertEquals(040755, ((AsiExtraField) parsedFields[0]).getMode(), "Mode should match"),
                () -> assertInstanceOf(UnrecognizedExtraField.class, parsedFields[1], "Second field should be UnrecognizedExtraField"),
                () -> assertEquals(1, parsedFields[1].getLocalFileDataLength().getValue(), "Data length should match")
            );
        }

        @Test
        @DisplayName("should throw ZipException for truncated extra field data")
        void shouldThrowExceptionForTruncatedData() {
            // Arrange
            final byte[] validData = ExtraFieldUtils.mergeLocalFileDataData(new ZipExtraField[] { asiField, unrecognizedField });
            final byte[] truncatedData = Arrays.copyOf(validData, validData.length - 1); // Truncate last byte

            // Act & Assert
            final int expectedOffset = 4 + asiField.getLocalFileDataLength().getValue();
            final Exception e = assertThrows(ZipException.class, () -> ExtraFieldUtils.parse(truncatedData));
            assertEquals("Bad extra field starting at " + expectedOffset + ".  Block length of 1 bytes exceeds remaining data of 0 bytes.", e.getMessage());
        }

        @Test
        @DisplayName("should wrap underlying parsing exceptions in ZipException")
        void shouldWrapUnderlyingParsingException() {
            // Arrange
            ExtraFieldUtils.register(AiobThrowingExtraField.class);
            final AiobThrowingExtraField faultyField = new AiobThrowingExtraField();
            final byte[] data = ExtraFieldUtils.mergeLocalFileDataData(new ZipExtraField[] { faultyField });

            // Act & Assert
            final ZipException e = assertThrows(ZipException.class, () -> ExtraFieldUtils.parse(data));
            assertEquals("Failed to parse corrupt ZIP extra field of type " + AiobThrowingExtraField.HEADER_ID.getValue(), e.getMessage());
            assertInstanceOf(ArrayIndexOutOfBoundsException.class, e.getCause());
        }

        @Test
        @DisplayName("should read truncated field as unparseable data when in READ mode")
        void shouldReadTruncatedFieldInReadMode() throws Exception {
            // Arrange
            final byte[] validData = ExtraFieldUtils.mergeLocalFileDataData(new ZipExtraField[] { asiField, unrecognizedField });
            final byte[] truncatedData = Arrays.copyOf(validData, validData.length - 1);

            // Act
            final ZipExtraField[] parsedFields = ExtraFieldUtils.parse(truncatedData, true, ExtraFieldUtils.UnparseableExtraField.READ);

            // Assert
            assertAll("Should parse valid field and recover truncated field as unparseable",
                () -> assertEquals(2, parsedFields.length),
                () -> assertInstanceOf(AsiExtraField.class, parsedFields[0]),
                () -> assertEquals(040755, ((AsiExtraField) parsedFields[0]).getMode()),
                () -> assertInstanceOf(UnparseableExtraFieldData.class, parsedFields[1]),
                () -> assertEquals(4, parsedFields[1].getLocalFileDataLength().getValue(), "Length of unparseable data should be header + length field")
            );
        }

        @Test
        @DisplayName("should skip truncated field when in SKIP mode")
        void shouldSkipTruncatedFieldInSkipMode() throws Exception {
            // Arrange
            final byte[] validData = ExtraFieldUtils.mergeLocalFileDataData(new ZipExtraField[] { asiField, unrecognizedField });
            final byte[] truncatedData = Arrays.copyOf(validData, validData.length - 1);

            // Act
            final ZipExtraField[] parsedFields = ExtraFieldUtils.parse(truncatedData, true, ExtraFieldUtils.UnparseableExtraField.SKIP);

            // Assert
            assertAll("Should parse only the valid field and skip the truncated one",
                () -> assertEquals(1, parsedFields.length),
                () -> assertInstanceOf(AsiExtraField.class, parsedFields[0]),
                () -> assertEquals(040755, ((AsiExtraField) parsedFields[0]).getMode())
            );
        }
    }
}