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
 * Tests for {@link CodeAttribute}.
 * <p>
 * These tests use mock objects to isolate the {@link CodeAttribute} from its dependencies.
 */
class CodeAttributeTest {

    private Segment segment;
    private CpBands cpBands;
    private OperandManager operandManager;

    // Sample bytecode sequences for testing.  Descriptive names improve readability.
    private static final byte[] MIXED_BYTE_ARRAY = {
        -47, // aload_0_getstatic_this 0, 1
        -46, // aload_0_putstatic_this 4, 5
        1,   // aconst_null 8
        -45, // aload_0_getfield_this 9, 10
        // Should always end with a multibyte instruction
        -44, // aload_0_putfield_this (int) 13, 14
    };

    private static final byte[] SINGLE_BYTE_ARRAY = {
        42,  // aload_0 0
        1,   // aconst_null 1
        18,  // ldc 2
        -49, // return 4
    };

    @BeforeEach
    void setup() {
        segment = new MockSegment();
        cpBands = new MockCpBands(segment);
        operandManager = new MockOperandManager();
        operandManager.setSegment(segment);
        operandManager.setCurrentClass("java/lang/Foo");
    }

    /**
     * Tests the {@link CodeAttribute#getLength()} method.
     */
    @Test
    void testLength() throws Pack200Exception {
        // Given
        final MockCodeAttribute attribute = new MockCodeAttribute(
            3,                // maxStack
            2,                // maxLocals
            MIXED_BYTE_ARRAY, // codePacked
            segment,          // segment
            operandManager,   // operandManager
            new ArrayList<>());

        // When
        int length = attribute.getLength();

        // Then
        assertEquals(29, length, "Initial length should be 29");

        // When
        attribute.attributes.add(new LocalVariableTableAttribute(0, null, null, null, null, null));
        length = attribute.getLength();

        // Then
        assertEquals(37, length, "Length after adding attribute should be 37");
    }

    /**
     * Tests the {@link CodeAttribute} with mixed single and multi-byte opcodes.
     * Verifies the correct parsing of bytecodes and their offsets.
     */
    @Test
    void testMixedByteCodes() throws Pack200Exception {
        // Given
        final CodeAttribute attribute = new CodeAttribute(
            3,                // maxStack
            2,                // maxLocals
            MIXED_BYTE_ARRAY, // codePacked
            segment,          // segment
            operandManager,   // operandManager
            new ArrayList<>());

        // When/Then
        assertEquals(2, attribute.maxLocals, "maxLocals should be 2");
        assertEquals(3, attribute.maxStack, "maxStack should be 3");
        assertEquals("aload_0_putfield_this", attribute.byteCodes.get(4).toString(), "Bytecode at index 4 should be aload_0_putfield_this");

        final int[] expectedLabels = {0, 1, 4, 5, 8, 9, 10, 13, 14};
        for (int index = 0; index < expectedLabels.length; index++) {
            assertEquals(expectedLabels[index], attribute.byteCodeOffsets.get(index).intValue(), "Bytecode offset at index " + index + " is incorrect");
        }
    }

    /**
     * Tests the {@link CodeAttribute} with only single-byte opcodes.
     * Verifies the correct parsing of bytecodes and their offsets.
     */
    @Test
    void testSingleByteCodes() throws Pack200Exception {
        // Given
        final CodeAttribute attribute = new CodeAttribute(
            4,                // maxStack
            3,                // maxLocals
            SINGLE_BYTE_ARRAY, // codePacked
            segment,          // segment
            operandManager,   // operandManager
            new ArrayList<>());

        // When/Then
        assertEquals(3, attribute.maxLocals, "maxLocals should be 3");
        assertEquals(4, attribute.maxStack, "maxStack should be 4");
        assertEquals("invokespecial_this", attribute.byteCodes.get(3).toString(), "Bytecode at index 3 should be invokespecial_this");

        final int[] expectedLabels = {0, 1, 2, 4};
        for (int index = 0; index < expectedLabels.length; index++) {
            assertEquals(expectedLabels[index], attribute.byteCodeOffsets.get(index).intValue(), "Bytecode offset at index " + index + " is incorrect");
        }
    }

    // Mock classes to isolate the CodeAttribute
    public class MockCodeAttribute extends CodeAttribute {

        MockCodeAttribute(final int maxStack, final int maxLocals, final byte[] codePacked, final Segment segment, final OperandManager operandManager,
            final List<ExceptionTableEntry> exceptionTable) throws Pack200Exception {
            super(maxStack, maxLocals, codePacked, segment, operandManager, exceptionTable);
        }

        @Override
        public int getLength() {
            return super.getLength();
        }
    }

    public class MockCpBands extends CpBands {

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

    public class MockOperandManager extends OperandManager {

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
                new int[] {0, 1, 2, 3, 4}, // bcStringRef
                new int[] {}, // bcClassRef
                new int[] {}, // bcFieldRef
                new int[] {}, // bcMethodRef
                new int[] {}, // bcIMethodRef
                new int[] {0, 0, 0, 0, 0, 0}, // bcThisField
                new int[] {}, // bcSuperField
                new int[] {0}, // bcThisMethod
                new int[] {}, // bcSuperMethod
                new int[] {}, // bcInitRef
                null);
        }
    }

    public class MockSegment extends Segment {

        @Override
        public SegmentConstantPool getConstantPool() {
            return new MockSegmentConstantPool(cpBands);
        }
    }

    public class MockSegmentConstantPool extends SegmentConstantPool {

        MockSegmentConstantPool(final CpBands bands) {
            super(bands);
        }

        @Override
        protected int matchSpecificPoolEntryIndex(final String[] nameArray, final String compareString, final int desiredIndex) {
            return 1;
        }
    }
}