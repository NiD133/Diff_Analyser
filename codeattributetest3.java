package org.apache.commons.compress.harmony.unpack200;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.compress.harmony.pack200.Pack200Exception;
import org.apache.commons.compress.harmony.unpack200.bytecode.ByteCode;
import org.apache.commons.compress.harmony.unpack200.bytecode.CPFieldRef;
import org.apache.commons.compress.harmony.unpack200.bytecode.CPMethodRef;
import org.apache.commons.compress.harmony.unpack200.bytecode.CPString;
import org.apache.commons.compress.harmony.unpack200.bytecode.CPUTF8;
import org.apache.commons.compress.harmony.unpack200.bytecode.CodeAttribute;
import org.apache.commons.compress.harmony.unpack200.bytecode.ExceptionTableEntry;
import org.apache.commons.compress.harmony.unpack200.bytecode.OperandManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link CodeAttribute} focusing on bytecode parsing and offset calculation.
 */
@DisplayName("CodeAttribute")
class CodeAttributeTest {

    // Opcodes used in the test data. Using named constants improves readability.
    private static final byte ALOAD_0 = 42;
    private static final byte ACONST_NULL = 1;
    private static final byte LDC = 18;
    private static final byte INVOKESPECIAL_THIS = -49; // Opcode 207

    /**
     * Packed bytecode stream. The CodeAttribute constructor is responsible for
     * unpacking this into a list of ByteCode objects.
     * Note: The 'ldc' instruction is variable length, but its packed representation is a single byte.
     * The constructor logic correctly determines its full length (2 bytes) during parsing.
     */
    private static final byte[] PACKED_BYTECODE_STREAM = {
        ALOAD_0,           // aload_0 (length 1)
        ACONST_NULL,       // aconst_null (length 1)
        LDC,               // ldc (length 2, operand from OperandManager)
        INVOKESPECIAL_THIS // invokespecial_this (length 1)
    };

    @Test
    @DisplayName("should correctly parse bytecodes and calculate their offsets")
    void shouldCorrectlyParseBytecodesAndCalculateOffsets() throws Pack200Exception {
        // Arrange
        final Segment segment = new MockSegment();
        final OperandManager operandManager = new MockOperandManager();
        operandManager.setSegment(segment);
        operandManager.setCurrentClass("java/lang/Foo");

        final int maxStack = 4;
        final int maxLocals = 3;

        // Act: The CodeAttribute constructor performs the parsing and offset calculation.
        final CodeAttribute codeAttribute = new CodeAttribute(
            maxStack,
            maxLocals,
            PACKED_BYTECODE_STREAM,
            segment,
            operandManager,
            new ArrayList<>() // exceptionTable
        );

        // Assert
        assertEquals(maxLocals, codeAttribute.maxLocals, "maxLocals should be correctly set.");
        assertEquals(maxStack, codeAttribute.maxStack, "maxStack should be correctly set.");

        // Verify that the bytecodes were parsed correctly
        final List<String> expectedBytecodeNames = List.of("aload_0", "aconst_null", "ldc", "invokespecial_this");
        final List<String> actualBytecodeNames = codeAttribute.byteCodes.stream()
            .map(ByteCode::toString)
            .collect(Collectors.toList());
        assertIterableEquals(expectedBytecodeNames, actualBytecodeNames, "Parsed bytecodes should match the expected instructions.");

        // Verify that the offsets for each bytecode were calculated correctly
        // Offsets:
        // 0: aload_0 (length 1)
        // 1: aconst_null (length 1)
        // 2: ldc (length 2)
        // 4: invokespecial_this (length 1)
        final List<Integer> expectedOffsets = List.of(0, 1, 2, 4);
        assertIterableEquals(expectedOffsets, codeAttribute.byteCodeOffsets, "Bytecode offsets should be calculated correctly.");
    }

    // --- Mock Helper Classes ---

    /**
     * Mock Segment to provide a constant pool for the CodeAttribute.
     */
    private static class MockSegment extends Segment {
        @Override
        public SegmentConstantPool getConstantPool() {
            return new MockSegmentConstantPool(new MockCpBands(this));
        }
    }

    private static class MockSegmentConstantPool extends SegmentConstantPool {
        public MockSegmentConstantPool(final CpBands bands) {
            super(bands);
        }

        @Override
        protected int matchSpecificPoolEntryIndex(final String[] nameArray, final String compareString, final int desiredIndex) {
            // Return a dummy index for simplicity.
            return 1;
        }
    }

    /**
     * Mock CpBands to provide constant pool values required by some bytecodes (e.g., ldc).
     */
    private static class MockCpBands extends CpBands {
        public MockCpBands(final Segment segment) {
            super(segment);
        }

        @Override
        public CPFieldRef cpFieldValue(final int index) {
            return null; // Not needed for this test
        }

        @Override
        public CPMethodRef cpMethodValue(final int index) {
            return null; // Not needed for this test
        }

        @Override
        public CPString cpStringValue(final int index) {
            // Required for the 'ldc' instruction
            return new CPString(new CPUTF8("MockString"), -1);
        }
    }

    /**
     * Mock OperandManager to provide operands for bytecodes that require them.
     */
    private static class MockOperandManager extends OperandManager {
        public MockOperandManager() {
            super(
                new int[]{}, // bcCaseCount
                new int[]{}, // bcCaseValues
                new int[]{}, // bcByte
                new int[]{}, // bcShort
                new int[]{}, // bcLocal
                new int[]{}, // bcLabel
                new int[]{}, // bcIntRef
                new int[]{}, // bcFloatRef
                new int[]{}, // bcLongRef
                new int[]{}, // bcDoubleRef
                new int[]{0}, // bcStringRef: provides operand for 'ldc'
                new int[]{0, 1, 2, 3, 4}, // bcClassRef
                new int[]{}, // bcFieldRef
                new int[]{}, // bcMethodRef
                new int[]{}, // bcIMethodRef
                new int[]{}, // bcThisField
                new int[]{0, 0, 0, 0, 0, 0}, // bcSuperField
                new int[]{}, // bcThisMethod
                new int[]{0}, // bcSuperMethod
                new int[]{}, // bcInitRef
                null // bcDoubleRef (duplicate name in constructor)
            );
        }
    }
}