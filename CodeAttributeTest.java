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
 * Tests for CodeAttribute functionality including bytecode parsing and length calculation.
 */
class CodeAttributeTest {

    // Test constants
    private static final int DEFAULT_MAX_STACK = 3;
    private static final int DEFAULT_MAX_LOCALS = 2;
    private static final String TEST_CLASS_NAME = "java/lang/Foo";
    private static final String HELLO_STRING = "Hello";
    
    // Expected bytecode instruction names
    private static final String ALOAD_0_PUTFIELD_THIS = "aload_0_putfield_this";
    private static final String INVOKESPECIAL_THIS = "invokespecial_this";
    
    // Expected attribute length constants
    private static final int BASE_ATTRIBUTE_LENGTH = 29;
    private static final int ATTRIBUTE_WITH_LOCAL_VAR_TABLE_LENGTH = 37;

    // Test data - bytecode arrays with comments explaining each instruction
    private byte[] mixedInstructionBytecode;
    private byte[] singleByteInstructionBytecode;
    
    // Test infrastructure
    private TestSegment testSegment;
    private TestCpBands testCpBands;

    @BeforeEach
    void setUp() {
        initializeTestBytecodeArrays();
        initializeTestInfrastructure();
    }

    private void initializeTestBytecodeArrays() {
        // Mixed bytecode with multi-byte instructions
        mixedInstructionBytecode = new byte[] {
            -47, // aload_0_getstatic_this (offset 0, 1)
            -46, // aload_0_putstatic_this (offset 4, 5)
            1,   // aconst_null (offset 8)
            -45, // aload_0_getfield_this (offset 9, 10)
            -44, // aload_0_putfield_this (offset 13, 14) - ends with multi-byte instruction
        };

        // Simple single-byte instructions
        singleByteInstructionBytecode = new byte[] {
            42,  // aload_0 (offset 0)
            1,   // aconst_null (offset 1)
            18,  // ldc (offset 2)
            -49, // return (offset 4)
        };
    }

    private void initializeTestInfrastructure() {
        testSegment = new TestSegment();
        testCpBands = new TestCpBands(testSegment);
    }

    @Test
    void shouldCalculateCorrectAttributeLength() throws Pack200Exception {
        // Given: A code attribute with mixed bytecode instructions
        TestOperandManager operandManager = createConfiguredOperandManager();
        TestableCodeAttribute codeAttribute = createCodeAttribute(
            DEFAULT_MAX_STACK, DEFAULT_MAX_LOCALS, mixedInstructionBytecode, operandManager);

        // When: Getting the attribute length
        int actualLength = codeAttribute.getLength();

        // Then: Should return the expected base length
        assertEquals(BASE_ATTRIBUTE_LENGTH, actualLength, 
            "Base code attribute should have expected length");
    }

    @Test
    void shouldCalculateCorrectLengthWithAdditionalAttributes() throws Pack200Exception {
        // Given: A code attribute with an additional local variable table
        TestOperandManager operandManager = createConfiguredOperandManager();
        TestableCodeAttribute codeAttribute = createCodeAttribute(
            DEFAULT_MAX_STACK, DEFAULT_MAX_LOCALS, mixedInstructionBytecode, operandManager);
        
        LocalVariableTableAttribute localVarTable = new LocalVariableTableAttribute(0, null, null, null, null, null);
        codeAttribute.attributes.add(localVarTable);

        // When: Getting the attribute length
        int actualLength = codeAttribute.getLength();

        // Then: Should include the additional attribute length
        assertEquals(ATTRIBUTE_WITH_LOCAL_VAR_TABLE_LENGTH, actualLength,
            "Code attribute with local variable table should have increased length");
    }

    @Test
    void shouldCorrectlyParseMixedBytecodeInstructions() throws Pack200Exception {
        // Given: Mixed bytecode with multi-byte instructions
        TestOperandManager operandManager = createConfiguredOperandManager();
        
        // When: Creating a code attribute from the bytecode
        CodeAttribute codeAttribute = createCodeAttribute(
            DEFAULT_MAX_STACK, DEFAULT_MAX_LOCALS, mixedInstructionBytecode, operandManager);

        // Then: Should preserve stack and locals configuration
        assertEquals(DEFAULT_MAX_LOCALS, codeAttribute.maxLocals, 
            "Max locals should be preserved");
        assertEquals(DEFAULT_MAX_STACK, codeAttribute.maxStack, 
            "Max stack should be preserved");
        
        // And: Should correctly parse the last instruction
        assertEquals(ALOAD_0_PUTFIELD_THIS, codeAttribute.byteCodes.get(4).toString(),
            "Last bytecode instruction should be correctly parsed");

        // And: Should generate correct bytecode offsets
        int[] expectedOffsets = {0, 1, 4, 5, 8, 9, 10, 13, 14};
        assertByteCodeOffsetsMatch(expectedOffsets, codeAttribute.byteCodeOffsets);
    }

    @Test
    void shouldCorrectlyParseSingleByteInstructions() throws Pack200Exception {
        // Given: Simple single-byte instructions
        TestOperandManager operandManager = createConfiguredOperandManager();
        int maxStack = 4;
        int maxLocals = 3;
        
        // When: Creating a code attribute from the bytecode
        CodeAttribute codeAttribute = createCodeAttribute(
            maxStack, maxLocals, singleByteInstructionBytecode, operandManager);

        // Then: Should preserve stack and locals configuration
        assertEquals(maxLocals, codeAttribute.maxLocals, 
            "Max locals should be preserved");
        assertEquals(maxStack, codeAttribute.maxStack, 
            "Max stack should be preserved");
        
        // And: Should correctly parse the last instruction
        assertEquals(INVOKESPECIAL_THIS, codeAttribute.byteCodes.get(3).toString(),
            "Last bytecode instruction should be correctly parsed");

        // And: Should generate correct bytecode offsets
        int[] expectedOffsets = {0, 1, 2, 4};
        assertByteCodeOffsetsMatch(expectedOffsets, codeAttribute.byteCodeOffsets);
    }

    // Helper methods for test setup and assertions

    private TestOperandManager createConfiguredOperandManager() {
        TestOperandManager operandManager = new TestOperandManager();
        operandManager.setSegment(testSegment);
        operandManager.setCurrentClass(TEST_CLASS_NAME);
        return operandManager;
    }

    private TestableCodeAttribute createCodeAttribute(int maxStack, int maxLocals, 
            byte[] bytecode, OperandManager operandManager) throws Pack200Exception {
        return new TestableCodeAttribute(maxStack, maxLocals, bytecode, 
            testSegment, operandManager, new ArrayList<>());
    }

    private void assertByteCodeOffsetsMatch(int[] expectedOffsets, List<Integer> actualOffsets) {
        assertEquals(expectedOffsets.length, actualOffsets.size(), 
            "Number of bytecode offsets should match expected");
        
        for (int i = 0; i < expectedOffsets.length; i++) {
            assertEquals(expectedOffsets[i], actualOffsets.get(i).intValue(),
                String.format("Bytecode offset at index %d should match expected value", i));
        }
    }

    // Test infrastructure classes with descriptive names and clear purposes

    /**
     * Testable version of CodeAttribute that exposes the protected getLength() method.
     */
    private static class TestableCodeAttribute extends CodeAttribute {
        TestableCodeAttribute(int maxStack, int maxLocals, byte[] codePacked, 
                Segment segment, OperandManager operandManager,
                List<ExceptionTableEntry> exceptionTable) throws Pack200Exception {
            super(maxStack, maxLocals, codePacked, segment, operandManager, exceptionTable);
        }

        @Override
        public int getLength() {
            return super.getLength();
        }
    }

    /**
     * Test implementation of CpBands that provides mock constant pool values.
     */
    private static class TestCpBands extends CpBands {
        TestCpBands(Segment segment) {
            super(segment);
        }

        @Override
        public CPFieldRef cpFieldValue(int index) {
            return null; // Not needed for current tests
        }

        @Override
        public CPMethodRef cpMethodValue(int index) {
            return null; // Not needed for current tests
        }

        @Override
        public CPString cpStringValue(int index) {
            return new CPString(new CPUTF8(HELLO_STRING), -1);
        }
    }

    /**
     * Test implementation of OperandManager with predefined operand arrays.
     */
    private static class TestOperandManager extends OperandManager {
        TestOperandManager() {
            super(
                new int[] {}, // bcCaseCount
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
                null
            );
        }
    }

    /**
     * Test implementation of Segment that provides a mock constant pool.
     */
    private class TestSegment extends Segment {
        @Override
        public SegmentConstantPool getConstantPool() {
            return new TestSegmentConstantPool(testCpBands);
        }
    }

    /**
     * Test implementation of SegmentConstantPool with mock pool entry matching.
     */
    private static class TestSegmentConstantPool extends SegmentConstantPool {
        TestSegmentConstantPool(CpBands bands) {
            super(bands);
        }

        @Override
        protected int matchSpecificPoolEntryIndex(String[] nameArray, String compareString, int desiredIndex) {
            return 1; // Return a fixed index for testing
        }
    }
}