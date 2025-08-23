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
 * Unit tests for CodeAttribute.
 *
 * These tests verify:
 * - Length calculation with and without nested attributes
 * - Decoding of packed bytecode (including “multiple bytecode” entries)
 * - Computation of bytecode offsets
 *
 * Notes on “packed” byte arrays:
 * Pack200 uses a compact representation. Some packed opcodes expand into
 * multiple logical bytecodes (e.g., “aload_0_putfield_this”). CodeAttribute
 * accounts for these by inserting extra offsets.
 */
class CodeAttributeTest {

    // Common test data
    private static final String TEST_CLASS_INTERNAL_NAME = "java/lang/Foo";

    /**
     * Packed opcodes containing a mix of single-byte and multi-byte entries.
     * Negative numbers are signed byte literals; the CodeAttribute will
     * interpret them as unsigned opcodes.
     *
     * Each entry is annotated for readability:
     * -47  => aload_0_getstatic_this
     * -46  => aload_0_putstatic_this
     * 1    => aconst_null
     * -45  => aload_0_getfield_this
     * -44  => aload_0_putfield_this (int)
     *
     * The test assumes the final instruction is a multi-byte entry to exercise
     * offset handling at the end of the sequence.
     */
    private final byte[] PACKED_MIXED_OPCODES = {
        -47, // aload_0_getstatic_this
        -46, // aload_0_putstatic_this
        1,   // aconst_null
        -45, // aload_0_getfield_this
        -44  // aload_0_putfield_this (int)
    };

    /**
     * Packed opcodes consisting only of single-byte or single-entry expansions
     * except the last which expands to invokespecial_this.
     *
     * 42  => aload_0
     * 1   => aconst_null
     * 18  => ldc
     * -49 => expands to invokespecial_this in this test context
     */
    private final byte[] PACKED_SINGLE_OPCODES = {
        42,  // aload_0
        1,   // aconst_null
        18,  // ldc
        -49  // invokespecial_this (in this packed form)
    };

    // Test scaffolding (stubs)
    private static class StubCodeAttribute extends CodeAttribute {
        StubCodeAttribute(final int maxStack, final int maxLocals, final byte[] codePacked,
                          final Segment segment, final OperandManager operandManager,
                          final List<ExceptionTableEntry> exceptionTable) throws Pack200Exception {
            super(maxStack, maxLocals, codePacked, segment, operandManager, exceptionTable);
        }

        // Expose getLength() publicly for assertions
        @Override
        public int getLength() {
            return super.getLength();
        }
    }

    private static class StubCpBands extends CpBands {
        StubCpBands(final Segment segment) {
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
            // Minimal value to satisfy string lookups in bytecode extraction
            return new CPString(new CPUTF8("Hello"), -1);
        }
    }

    /**
     * OperandManager stub that feeds deterministic operand streams to the
     * ByteCode extractor. Only the arrays needed by these tests are populated.
     */
    private static class StubOperandManager extends OperandManager {
        StubOperandManager() {
            super(
                new int[] {},                 // bcCaseCount
                new int[] {},                 // bcCaseValues
                new int[] {},                 // bcByte
                new int[] {},                 // bcShort
                new int[] {},                 // bcLocal
                new int[] {},                 // bcLabel
                new int[] {},                 // bcIntRef
                new int[] {},                 // bcFloatRef
                new int[] {},                 // bcLongRef
                new int[] {},                 // bcDoubleRef
                new int[] {0, 1, 2, 3, 4},    // bcStringRef
                new int[] {},                 // bcClassRef
                new int[] {},                 // bcFieldRef
                new int[] {},                 // bcMethodRef
                new int[] {},                 // bcIMethodRef
                new int[] {0, 0, 0, 0, 0, 0}, // bcThisField
                new int[] {},                 // bcSuperField
                new int[] {0},                // bcThisMethod
                new int[] {},                 // bcSuperMethod
                new int[] {},                 // bcInitRef
                null
            );
        }
    }

    private static class StubSegmentConstantPool extends SegmentConstantPool {
        StubSegmentConstantPool(final CpBands bands) {
            super(bands);
        }

        @Override
        protected int matchSpecificPoolEntryIndex(final String[] nameArray, final String compareString,
                                                  final int desiredIndex) {
            // Minimal deterministic behavior for tests
            return 1;
        }
    }

    /**
     * Segment stub whose getConstantPool() delegates to a constant pool
     * built from the CpBands of this test. The stub captures the outer
     * test's cpBands instance to break the real-world circular dependency:
     * CpBands needs a Segment and Segment needs a constant pool.
     */
    private class StubSegment extends Segment {
        @Override
        public SegmentConstantPool getConstantPool() {
            return new StubSegmentConstantPool(cpBands);
        }
    }

    // Per-test state
    private Segment segment;
    private CpBands cpBands;

    @BeforeEach
    void setUp() {
        // The segment refers back to cpBands via getConstantPool(). We create
        // them in this order and let the segment capture the later cpBands field.
        segment = new StubSegment();
        cpBands = new StubCpBands(segment);
    }

    @Test
    void testLengthCalculation_withAndWithoutNestedAttribute() throws Pack200Exception {
        final OperandManager opManager = newStubOperandManager();
        final StubCodeAttribute attribute = new StubCodeAttribute(
            3,                 // maxStack
            2,                 // maxLocals
            PACKED_MIXED_OPCODES,
            segment,
            opManager,
            new ArrayList<>()
        );

        // Base length
        assertEquals(29, attribute.getLength(), "Unexpected Code attribute length");

        // Adding a nested attribute should increase length accordingly
        attribute.attributes.add(new LocalVariableTableAttribute(0, null, null, null, null, null));
        assertEquals(37, attribute.getLength(), "Length should include nested LocalVariableTableAttribute");
    }

    @Test
    void testDecoding_mixedPackedOpcodesAndOffsets() throws Pack200Exception {
        final OperandManager opManager = newStubOperandManager();

        final CodeAttribute attribute = new CodeAttribute(
            3,                 // maxStack
            2,                 // maxLocals
            PACKED_MIXED_OPCODES,
            segment,
            opManager,
            new ArrayList<>()
        );

        assertEquals(2, attribute.maxLocals, "maxLocals mismatch");
        assertEquals(3, attribute.maxStack, "maxStack mismatch");

        // Last packed opcode expands to "aload_0_putfield_this"
        assertEquals("aload_0_putfield_this",
            attribute.byteCodes.get(4).toString(),
            "Unexpected mnemonic at index 4");

        // Expected offsets after expansion; see comments in PACKED_MIXED_OPCODES
        final int[] expectedOffsets = {0, 1, 4, 5, 8, 9, 10, 13, 14};
        assertOffsets(attribute.byteCodeOffsets, expectedOffsets);
    }

    @Test
    void testDecoding_singlePackedOpcodesAndOffsets() throws Pack200Exception {
        final OperandManager opManager = newStubOperandManager();

        final CodeAttribute attribute = new CodeAttribute(
            4,                 // maxStack
            3,                 // maxLocals
            PACKED_SINGLE_OPCODES,
            segment,
            opManager,
            new ArrayList<>()
        );

        assertEquals(3, attribute.maxLocals, "maxLocals mismatch");
        assertEquals(4, attribute.maxStack, "maxStack mismatch");

        // The last entry expands to invokespecial_this in this context
        assertEquals("invokespecial_this",
            attribute.byteCodes.get(3).toString(),
            "Unexpected mnemonic at index 3");

        final int[] expectedOffsets = {0, 1, 2, 4};
        assertOffsets(attribute.byteCodeOffsets, expectedOffsets);
    }

    // Helpers

    private OperandManager newStubOperandManager() {
        final OperandManager operandManager = new StubOperandManager();
        operandManager.setSegment(segment);
        operandManager.setCurrentClass(TEST_CLASS_INTERNAL_NAME);
        return operandManager;
    }

    private static void assertOffsets(final List<Integer> actualOffsets, final int[] expectedOffsets) {
        for (int i = 0; i < expectedOffsets.length; i++) {
            assertEquals(
                expectedOffsets[i],
                actualOffsets.get(i).intValue(),
                "Offset mismatch at index " + i
            );
        }
    }
}