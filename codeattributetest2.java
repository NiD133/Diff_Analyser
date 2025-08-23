package org.apache.commons.compress.harmony.unpack200;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.compress.harmony.pack200.Pack200Exception;
import org.apache.commons.compress.harmony.unpack200.bytecode.CPFieldRef;
import org.apache.commons.compress.harmony.unpack200.bytecode.CPMethodRef;
import org.apache.commons.compress.harmony.unpack200.bytecode.CPString;
import org.apache.commons.compress.harmony.unpack200.bytecode.CPUTF8;
import org.apache.commons.compress.harmony.unpack200.bytecode.CodeAttribute;
import org.apache.commons.compress.harmony.unpack200.bytecode.ExceptionTableEntry;
import org.apache.commons.compress.harmony.unpack200.bytecode.OperandManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link CodeAttribute}.
 */
class CodeAttributeTest {

    // The CodeAttribute constructor processes a packed byte array and calculates the final bytecode offsets.
    // This test verifies the offset calculation for a mix of standard and multi-byte instructions.
    //
    // A "multi-byte" instruction in pack200 is a single byte in the packed stream that expands
    // into multiple bytecodes (e.g., aload_0 followed by getfield). The CodeAttribute constructor
    // has special logic to handle this expansion when calculating offsets.
    //
    // Bytecode | Mnemonic                 | Final Length | Multi-Byte |
    // ---------|--------------------------|--------------|------------|
    // 0xD1     | aload_0_getstatic_this   | 4 bytes      | true       |
    // 0xD2     | aload_0_putstatic_this   | 4 bytes      | true       |
    // 0x01     | aconst_null              | 1 byte       | false      |
    // 0xD3     | aload_0_getfield_this    | 4 bytes      | true       |
    // 0xD4     | aload_0_putfield_this    | 4 bytes      | true       |
    private static final byte[] PACKED_BYTE_CODES = {
        (byte) 0xD1, // aload_0_getstatic_this
        (byte) 0xD2, // aload_0_putstatic_this
        (byte) 0x01, // aconst_null
        (byte) 0xD3, // aload_0_getfield_this
        (byte) 0xD4  // aload_0_putfield_this
    };

    private Segment segment;
    private OperandManager operandManager;

    @BeforeEach
    void setUp() {
        // The test requires a complex set of mock objects.
        // Setting them up here makes the test case cleaner.
        segment = new MockSegment();
        CpBands cpBands = new MockCpBands(segment);
        // The mock segment needs access to cpBands, so we inject it.
        ((MockSegment) segment).setCpBands(cpBands);

        operandManager = new MockOperandManager();
        operandManager.setSegment(segment);
        operandManager.setCurrentClass("java/lang/Foo");
    }

    @Test
    @DisplayName("Should correctly calculate bytecode offsets for mixed single and multi-byte instructions")
    void shouldCorrectlyParseBytecodesAndCalculateOffsets() throws Pack200Exception {
        // Arrange
        final int maxStack = 3;
        final int maxLocals = 2;
        final List<ExceptionTableEntry> exceptionTable = new ArrayList<>();

        // Expected offsets are calculated based on the expansion of PACKED_BYTE_CODES.
        // Offset | Calculation
        // -------|-----------------------------------------------------------------
        // 0      | Initial offset
        // 1      | Start of aload_0_getstatic_this's second instruction (multi-byte)
        // 4      | 0 + length(aload_0_getstatic_this) = 0 + 4
        // 5      | Start of aload_0_putstatic_this's second instruction (multi-byte)
        // 8      | 4 + length(aload_0_putstatic_this) = 4 + 4
        // 9      | 8 + length(aconst_null) = 8 + 1
        // 10     | Start of aload_0_getfield_this's second instruction (multi-byte)
        // 13     | 9 + length(aload_0_getfield_this) = 9 + 4
        // 14     | Start of aload_0_putfield_this's second instruction (multi-byte)
        final List<Integer> expectedOffsets = Arrays.asList(0, 1, 4, 5, 8, 9, 10, 13, 14);

        // Act
        final CodeAttribute attribute = new CodeAttribute(maxStack, maxLocals, PACKED_BYTE_CODES, segment, operandManager,
            exceptionTable);

        // Assert
        assertAll("CodeAttribute properties",
            () -> assertEquals(maxLocals, attribute.maxLocals, "Max locals should be correctly set"),
            () -> assertEquals(maxStack, attribute.maxStack, "Max stack should be correctly set"),
            () -> assertEquals("aload_0_putfield_this", attribute.byteCodes.get(4).toString(),
                "The last bytecode instruction should be correctly identified"),
            () -> assertIterableEquals(expectedOffsets, attribute.byteCodeOffsets,
                "Bytecode offsets should be calculated correctly")
        );
    }

    // --- Mock implementations for dependencies ---

    private static class MockSegment extends Segment {
        private CpBands cpBands;

        public void setCpBands(final CpBands cpBands) {
            this.cpBands = cpBands;
        }

        @Override
        public SegmentConstantPool getConstantPool() {
            return new MockSegmentConstantPool(cpBands);
        }
    }

    private static class MockCpBands extends CpBands {
        public MockCpBands(final Segment segment) {
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

    private static class MockSegmentConstantPool extends SegmentConstantPool {
        public MockSegmentConstantPool(final CpBands bands) {
            super(bands);
        }

        @Override
        protected int matchSpecificPoolEntryIndex(final String[] nameArray, final String compareString,
            final int desiredIndex) {
            return 1;
        }
    }



    private static class MockOperandManager extends OperandManager {
        public MockOperandManager() {
            super(new int[]{}, new int[]{}, new int[]{}, new int[]{}, new int[]{}, new int[]{}, new int[]{},
                new int[]{}, new int[]{}, new int[]{}, new int[]{}, new int[]{0, 1, 2, 3, 4}, new int[]{},
                new int[]{}, new int[]{}, new int[]{}, new int[]{0, 0, 0, 0, 0, 0}, new int[]{}, new int[]{0},
                new int[]{}, new int[]{}, null);
        }
    }
}