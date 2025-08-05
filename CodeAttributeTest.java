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
package org.apache.commons.compress.harmony.unpack200;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.compress.harmony.pack200.Pack200Exception;
import org.apache.commons.compress.harmony.unpack200.bytecode.CPFieldRef;
import org.apache.commons.compress.harmony.unpack200.bytecode.CPMethodRef;
import org.apache.commons.compress.harmony.unpack200.bytecode.CPString;
import org.apache.commons.compress.harmony.unpack200.bytecode.CPUTF8;
import org.apache.commons.compress.harmony.unpack200.bytecode.CodeAttribute;
import org.apache.commons.compress.harmony.unpack200.bytecode.ExceptionTableEntry;
import org.apache.commons.compress.harmony.unpack200.bytecode.LocalVariableTableAttribute;
import org.apache.commons.compress.harmony.unpack200.bytecode.OperandManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the CodeAttribute class, focusing on its constructor logic for parsing
 * packed bytecodes and calculating offsets and attribute length.
 *
 * <p>This test class uses a set of private static mock classes to provide the necessary
 * dependencies (Segment, CpBands, OperandManager) for instantiating a CodeAttribute.</p>
 */
class CodeAttributeTest {

    // Bytecodes with single-byte and multi-byte instructions.
    private static final byte[] MIXED_WIDTH_BYTE_CODES = {
        -47, // aload_0_getstatic_this (multi-byte)
        -46, // aload_0_putstatic_this (multi-byte)
        1,   // aconst_null (single-byte)
        -45, // aload_0_getfield_this (multi-byte)
        -44, // aload_0_putfield_this (multi-byte)
    };

    // Bytecodes with only single-byte instructions.
    private static final byte[] SINGLE_BYTE_CODES = {
        42,  // aload_0
        1,   // aconst_null
        18,  // ldc
        -49, // return (custom invokespecial_this)
    };

    private Segment segment;
    private OperandManager operandManager;

    @BeforeEach
    void setUp() {
        segment = new MockSegment();
        operandManager = new MockOperandManager();
        operandManager.setSegment(segment);
        operandManager.setCurrentClass("java/lang/Foo");
    }

    @Test
    @DisplayName("Length should be calculated correctly and updated when nested attributes are added")
    void length_whenAttributeAdded_isRecalculatedCorrectly() throws Pack200Exception {
        // Arrange
        final ExposedLengthCodeAttribute attribute = new ExposedLengthCodeAttribute(3, 2, MIXED_WIDTH_BYTE_CODES,
            segment, operandManager, new ArrayList<>());

        // Assert initial length
        // The length is derived from the size of the parsed bytecodes, header, and other components.
        assertEquals(29, attribute.getLength(), "Initial attribute length should be calculated correctly.");

        // Act: Add a nested attribute
        attribute.attributes.add(new LocalVariableTableAttribute(0, null, null, null, null, null));

        // Assert new length
        assertEquals(37, attribute.getLength(), "Attribute length should increase after adding a nested attribute.");
    }

    @Test
    @DisplayName("Constructor should correctly parse bytecodes with mixed widths")
    void constructor_withMixedByteCodes_parsesCorrectly() throws Pack200Exception {
        // Arrange
        final int maxStack = 3;
        final int maxLocals = 2;
        final int[] expectedOffsets = {0, 1, 4, 5, 8, 9, 10, 13, 14};

        // Act
        final CodeAttribute attribute = new CodeAttribute(maxStack, maxLocals, MIXED_WIDTH_BYTE_CODES, segment,
            operandManager, new ArrayList<>());
        final int[] actualOffsets = attribute.byteCodeOffsets.stream().mapToInt(Integer::intValue).toArray();

        // Assert
        assertAll("Attribute properties for mixed-width bytecodes",
            () -> assertEquals(maxLocals, attribute.maxLocals, "maxLocals should be set correctly"),
            () -> assertEquals(maxStack, attribute.maxStack, "maxStack should be set correctly"),
            // This assertion checks the string representation of the last parsed bytecode instruction.
            () -> assertEquals("aload_0_putfield_this", attribute.byteCodes.get(4).toString()),
            () -> assertArrayEquals(expectedOffsets, actualOffsets, "Bytecode offsets should be calculated correctly")
        );
    }

    @Test
    @DisplayName("Constructor should correctly parse single-byte bytecodes")
    void constructor_withSingleByteCodes_parsesCorrectly() throws Pack200Exception {
        // Arrange
        final int maxStack = 4;
        final int maxLocals = 3;
        final int[] expectedOffsets = {0, 1, 2, 4};

        // Act
        final CodeAttribute attribute = new CodeAttribute(maxStack, maxLocals, SINGLE_BYTE_CODES, segment,
            operandManager, new ArrayList<>());
        final int[] actualOffsets = attribute.byteCodeOffsets.stream().mapToInt(Integer::intValue).toArray();

        // Assert
        assertAll("Attribute properties for single-byte bytecodes",
            () -> assertEquals(maxLocals, attribute.maxLocals, "maxLocals should be set correctly"),
            () -> assertEquals(maxStack, attribute.maxStack, "maxStack should be set correctly"),
            // This assertion checks the string representation of the last parsed bytecode instruction.
            () -> assertEquals("invokespecial_this", attribute.byteCodes.get(3).toString()),
            () -> assertArrayEquals(expectedOffsets, actualOffsets, "Bytecode offsets should be calculated correctly")
        );
    }

    // --- Helper Mocks ---

    /**
     * A mock CodeAttribute that exposes the protected getLength() method for testing.
     */
    private static class ExposedLengthCodeAttribute extends CodeAttribute {
        ExposedLengthCodeAttribute(final int maxStack, final int maxLocals, final byte[] codePacked,
            final Segment segment, final OperandManager operandManager, final List<ExceptionTableEntry> exceptionTable)
            throws Pack200Exception {
            super(maxStack, maxLocals, codePacked, segment, operandManager, exceptionTable);
        }

        @Override
        public int getLength() {
            return super.getLength();
        }
    }

    /**
     * Mock OperandManager that provides the necessary operand streams for the test bytecodes.
     * The non-empty arrays are consumed by the bytecode parsing logic in CodeAttribute.
     */
    private static class MockOperandManager extends OperandManager {
        MockOperandManager() {
            super(new int[]{}, new int[]{}, new int[]{}, new int[]{}, new int[]{}, new int[]{}, new int[]{},
                new int[]{}, new int[]{}, new int[]{}, new int[]{0, 1, 2, 3, 4}, // bcStringRef
                new int[]{}, new int[]{}, new int[]{}, new int[]{}, new int[]{0, 0, 0, 0, 0, 0}, // bcThisField
                new int[]{}, new int[]{0}, // bcThisMethod
                new int[]{}, new int[]{}, null);
        }
    }

    /**
     * Mock Segment that provides a mock Constant Pool.
     */
    private static class MockSegment extends Segment {
        private final CpBands cpBands = new MockCpBands(this);

        @Override
        public SegmentConstantPool getConstantPool() {
            return new MockSegmentConstantPool(cpBands);
        }
    }

    /**
     * Mock CpBands that returns a dummy CPString, needed for bytecodes like 'ldc'.
     */
    private static class MockCpBands extends CpBands {
        MockCpBands(final Segment segment) {
            super(segment);
        }

        @Override
        public CPFieldRef cpFieldValue(final int index) {
            return null;
        }

        @Override
        public CPMethodRef cpMethodValue(final int index) {
            return null;
        }

        @Override
        public CPString cpStringValue(final int index) {
            return new CPString(new CPUTF8("Hello"), -1);
        }
    }

    /**
     * Mock SegmentConstantPool that returns a fixed index for pool entry lookups.
     */
    private static class MockSegmentConstantPool extends SegmentConstantPool {
        MockSegmentConstantPool(final CpBands bands) {
            super(bands);
        }

        @Override
        protected int matchSpecificPoolEntryIndex(final String[] nameArray, final String compareString,
            final int desiredIndex) {
            return 1;
        }
    }
}