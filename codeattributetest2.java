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
import org.junit.jupiter.api.Test;

public class CodeAttributeTestTest2 {

    Segment segment = new MockSegment();

    CpBands cpBands = new MockCpBands(segment);

    public byte[] mixedByteArray = { // aload_0_getstatic_this 0, 1
    -47, // aload_0_putstatic_this 4, 5
    -46, // aconst_null 8
    1, // aload_0_getfield_this 9, 10
    -45, // Should always end with a multibyte
    // instruction
    // aload_0_putfield_this (int) 13, 14
    -44 };

    public byte[] singleByteArray = { // aload_0 0
    42, // aconst_null 1
    1, // ldc 2
    18, // return 4
    -49 };

    public class MockCodeAttribute extends CodeAttribute {

        MockCodeAttribute(final int maxStack, final int maxLocals, final byte[] codePacked, final Segment segment, final OperandManager operandManager, final List<ExceptionTableEntry> exceptionTable) throws Pack200Exception {
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
            super(// bcCaseCount
            new int[] {}, // bcCaseValues
            new int[] {}, // bcByte
            new int[] {}, // bcShort
            new int[] {}, // bcLocal
            new int[] {}, // bcLabel
            new int[] {}, // bcIntRef
            new int[] {}, // bcFloatRef
            new int[] {}, // bcLongRef
            new int[] {}, // bcDoubleRef
            new int[] {}, // bcStringRef
            new int[] { 0, 1, 2, 3, 4 }, // bcClassRef
            new int[] {}, // bcFieldRef
            new int[] {}, // bcMethodRef
            new int[] {}, // bcIMethodRef
            new int[] {}, // bcThisField
            new int[] { 0, 0, 0, 0, 0, 0 }, // bcSuperField
            new int[] {}, // bcThisMethod
            new int[] { 0 }, // bcSuperMethod
            new int[] {}, // bcInitRef
            new int[] {}, null);
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

    @Test
    void testMixedByteCodes() throws Pack200Exception {
        final OperandManager operandManager = new MockOperandManager();
        operandManager.setSegment(segment);
        operandManager.setCurrentClass("java/lang/Foo");
        final CodeAttribute attribute = new // maxStack
        CodeAttribute(// maxStack
        3, // maxLocals
        2, // codePacked
        mixedByteArray, // segment
        segment, // operandManager
        operandManager, new ArrayList<>());
        assertEquals(2, attribute.maxLocals);
        assertEquals(3, attribute.maxStack);
        assertEquals("aload_0_putfield_this", attribute.byteCodes.get(4).toString());
        final int[] expectedLabels = { 0, 1, 4, 5, 8, 9, 10, 13, 14 };
        for (int index = 0; index < expectedLabels.length; index++) {
            assertEquals(expectedLabels[index], attribute.byteCodeOffsets.get(index).intValue());
        }
    }
}
