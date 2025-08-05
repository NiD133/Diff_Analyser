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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
import org.junit.jupiter.api.Test;

/**
 * Tests for CodeAttribute
 */
class CodeAttributeTest {

    private static final byte[] MIXED_BYTE_INSTRUCTIONS = { 
        -47, // aload_0_getstatic_this (multi-byte instruction)
        -46, // aload_0_putstatic_this (multi-byte instruction)
        1,   // aconst_null (single-byte instruction)
        -45, // aload_0_getfield_this (multi-byte instruction)
        -44  // aload_0_putfield_this (multi-byte instruction)
    };

    private static final byte[] SINGLE_BYTE_INSTRUCTIONS = { 
        42,  // aload_0
        1,   // aconst_null
        18,  // ldc
        -49  // return
    };

    private static final String TEST_CLASS = "java/lang/Foo";
    private Segment segment;
    private CpBands cpBands;

    @BeforeEach
    void setUp() {
        segment = new MockSegment();
        cpBands = new MockCpBands(segment);
    }

    private static class MockCodeAttribute extends CodeAttribute {
        MockCodeAttribute(final int maxStack, final int maxLocals, final byte[] codePacked, 
                          final Segment segment, final OperandManager operandManager,
                          final List<ExceptionTableEntry> exceptionTable) throws Pack200Exception {
            super(maxStack, maxLocals, codePacked, segment, operandManager, exceptionTable);
        }

        @Override
        public int getLength() {
            return super.getLength();
        }
    }

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

    private static class MockOperandManager extends OperandManager {
        MockOperandManager() {
            super(new int[] {}, // bcCaseCount
                    new int[] {}, // bcCaseValues
                    new int[] {}, // bcByte
                    new int[] {}, // bcShort
                    new int[] {}, // bcLocal
                    new int[] {}, // bcLabel
                    new int[] {}, // bcIntRef
                    new int[] {}, // bcFloatRef
                    new int[] {}, // bcLongRef
                    new int[] {}, // bcDoubleRef
                    new int[] { 0, 1, 2, 3, 4 }, // bcStringRef
                    new int[] {}, // bcClassRef
                    new int[] {}, // bcFieldRef
                    new int[] {}, // bcMethodRef
                    new int[] {}, // bcIMethodRef
                    new int[] { 0, 0, 0, 0, 0, 0 }, // bcThisField
                    new int[] {}, // bcSuperField
                    new int[] { 0 }, // bcThisMethod
                    new int[] {}, // bcSuperMethod
                    new int[] {} // bcInitRef
                    , null);
        }
    }

    private static class MockSegment extends Segment {
        @Override
        public SegmentConstantPool getConstantPool() {
            return new MockSegmentConstantPool(((CodeAttributeTest.MockCpBands) cpBands));
        }
    }

    private static class MockSegmentConstantPool extends SegmentConstantPool {
        MockSegmentConstantPool(final CpBands bands) {
            super(bands);
        }

        @Override
        protected int matchSpecificPoolEntryIndex(final String[] nameArray, 
                                                 final String compareString, final int desiredIndex) {
            return 1;
        }
    }

    private OperandManager createOperandManager() {
        final OperandManager operandManager = new MockOperandManager();
        operandManager.setSegment(segment);
        operandManager.setCurrentClass(TEST_CLASS);
        return operandManager;
    }

    @Test
    void testGetLengthWithAttributes() throws Pack200Exception {
        // Setup
        final OperandManager operandManager = createOperandManager();
        final int expectedBaseLength = 29;
        final int expectedLengthWithAttribute = 37;

        // Create code attribute with mixed byte instructions
        final MockCodeAttribute attribute = new MockCodeAttribute(
            3, // maxStack
            2, // maxLocals
            MIXED_BYTE_INSTRUCTIONS,
            segment,
            operandManager,
            new ArrayList<>()
        );

        // Verify base length without attributes
        assertEquals(expectedBaseLength, attribute.getLength());

        // Add attribute and verify updated length
        attribute.attributes.add(new LocalVariableTableAttribute(0, null, null, null, null, null));
        assertEquals(expectedLengthWithAttribute, attribute.getLength());
    }

    @Test
    void testMixedByteCodeDecoding() throws Pack200Exception {
        // Setup
        final OperandManager operandManager = createOperandManager();
        final int expectedMaxLocals = 2;
        final int expectedMaxStack = 3;
        final int[] expectedByteCodeOffsets = { 0, 1, 4, 5, 8, 9, 10, 13, 14 };

        // Create code attribute
        final CodeAttribute attribute = new CodeAttribute(
            expectedMaxStack,
            expectedMaxLocals,
            MIXED_BYTE_INSTRUCTIONS,
            segment,
            operandManager,
            new ArrayList<>()
        );

        // Verify basic properties
        assertEquals(expectedMaxLocals, attribute.maxLocals);
        assertEquals(expectedMaxStack, attribute.maxStack);

        // Verify last instruction was decoded correctly
        assertEquals("aload_0_putfield_this", attribute.byteCodes.get(4).toString());

        // Verify all bytecode offsets
        for (int i = 0; i < expectedByteCodeOffsets.length; i++) {
            assertEquals(expectedByteCodeOffsets[i], attribute.byteCodeOffsets.get(i));
        }
    }

    @Test
    void testSingleByteCodeDecoding() throws Pack200Exception {
        // Setup
        final OperandManager operandManager = createOperandManager();
        final int expectedMaxLocals = 3;
        final int expectedMaxStack = 4;
        final int[] expectedByteCodeOffsets = { 0, 1, 2, 4 };

        // Create code attribute
        final CodeAttribute attribute = new CodeAttribute(
            expectedMaxStack,
            expectedMaxLocals,
            SINGLE_BYTE_INSTRUCTIONS,
            segment,
            operandManager,
            new ArrayList<>()
        );

        // Verify basic properties
        assertEquals(expectedMaxLocals, attribute.maxLocals);
        assertEquals(expectedMaxStack, attribute.maxStack);

        // Verify last instruction was decoded correctly
        assertEquals("invokespecial_this", attribute.byteCodes.get(3).toString());

        // Verify all bytecode offsets
        for (int i = 0; i < expectedByteCodeOffsets.length; i++) {
            assertEquals(expectedByteCodeOffsets[i], attribute.byteCodeOffsets.get(i));
        }
    }
}