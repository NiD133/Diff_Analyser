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
import org.apache.commons.compress.harmony.unpack200.bytecode.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the CodeAttribute class.
 */
class CodeAttributeTest {

    private Segment segment;
    private CpBands cpBands;

    private final byte[] mixedByteArray = {
        -47, // aload_0_getstatic_this 0, 1
        -46, // aload_0_putstatic_this 4, 5
        1,   // aconst_null 8
        -45, // aload_0_getfield_this 9, 10
        -44  // aload_0_putfield_this (int) 13, 14
    };

    private final byte[] singleByteArray = {
        42,  // aload_0 0
        1,   // aconst_null 1
        18,  // ldc 2
        -49  // return 4
    };

    @BeforeEach
    void setUp() {
        segment = new MockSegment();
        cpBands = new MockCpBands(segment);
    }

    @Test
    void testCodeAttributeLength() throws Pack200Exception {
        OperandManager operandManager = createMockOperandManager();
        MockCodeAttribute attribute = createMockCodeAttribute(mixedByteArray, operandManager);

        assertEquals(29, attribute.getLength());

        attribute.attributes.add(new LocalVariableTableAttribute(0, null, null, null, null, null));
        assertEquals(37, attribute.getLength());
    }

    @Test
    void testMixedByteCodes() throws Pack200Exception {
        OperandManager operandManager = createMockOperandManager();
        CodeAttribute attribute = new CodeAttribute(3, 2, mixedByteArray, segment, operandManager, new ArrayList<>());

        assertEquals(2, attribute.maxLocals);
        assertEquals(3, attribute.maxStack);
        assertEquals("aload_0_putfield_this", attribute.byteCodes.get(4).toString());

        int[] expectedOffsets = { 0, 1, 4, 5, 8, 9, 10, 13, 14 };
        assertByteCodeOffsets(expectedOffsets, attribute.byteCodeOffsets);
    }

    @Test
    void testSingleByteCodes() throws Pack200Exception {
        OperandManager operandManager = createMockOperandManager();
        CodeAttribute attribute = new CodeAttribute(4, 3, singleByteArray, segment, operandManager, new ArrayList<>());

        assertEquals(3, attribute.maxLocals);
        assertEquals(4, attribute.maxStack);
        assertEquals("invokespecial_this", attribute.byteCodes.get(3).toString());

        int[] expectedOffsets = { 0, 1, 2, 4 };
        assertByteCodeOffsets(expectedOffsets, attribute.byteCodeOffsets);
    }

    private OperandManager createMockOperandManager() {
        OperandManager operandManager = new MockOperandManager();
        operandManager.setSegment(segment);
        operandManager.setCurrentClass("java/lang/Foo");
        return operandManager;
    }

    private MockCodeAttribute createMockCodeAttribute(byte[] codePacked, OperandManager operandManager) throws Pack200Exception {
        return new MockCodeAttribute(3, 2, codePacked, segment, operandManager, new ArrayList<>());
    }

    private void assertByteCodeOffsets(int[] expectedOffsets, List<Integer> actualOffsets) {
        for (int i = 0; i < expectedOffsets.length; i++) {
            assertEquals(expectedOffsets[i], actualOffsets.get(i).intValue());
        }
    }

    // Mock classes
    public class MockCodeAttribute extends CodeAttribute {
        MockCodeAttribute(int maxStack, int maxLocals, byte[] codePacked, Segment segment, OperandManager operandManager, List<ExceptionTableEntry> exceptionTable) throws Pack200Exception {
            super(maxStack, maxLocals, codePacked, segment, operandManager, exceptionTable);
        }
    }

    public class MockCpBands extends CpBands {
        MockCpBands(Segment segment) {
            super(segment);
        }

        @Override
        public CPFieldRef cpFieldValue(int index) {
            return null;
        }

        @Override
        public CPMethodRef cpMethodValue(int index) {
            return null;
        }

        @Override
        public CPString cpStringValue(int index) {
            return new CPString(new CPUTF8("Hello"), -1);
        }
    }

    public class MockOperandManager extends OperandManager {
        MockOperandManager() {
            super(new int[] {}, new int[] {}, new int[] {}, new int[] {}, new int[] {}, new int[] {}, new int[] {}, new int[] {}, new int[] {}, new int[] {}, new int[] { 0, 1, 2, 3, 4 }, new int[] {}, new int[] {}, new int[] {}, new int[] {}, new int[] { 0, 0, 0, 0, 0, 0 }, new int[] {}, new int[] { 0 }, new int[] {}, new int[] {}, null);
        }
    }

    public class MockSegment extends Segment {
        @Override
        public SegmentConstantPool getConstantPool() {
            return new MockSegmentConstantPool(cpBands);
        }
    }

    public class MockSegmentConstantPool extends SegmentConstantPool {
        MockSegmentConstantPool(CpBands bands) {
            super(bands);
        }

        @Override
        protected int matchSpecificPoolEntryIndex(String[] nameArray, String compareString, int desiredIndex) {
            return 1;
        }
    }
}