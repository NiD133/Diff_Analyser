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
 */
public class CodeAttributeTest {

    // Constants for Pack200 bytecode opcodes to avoid magic numbers.
    // These are custom packed opcodes that expand into multiple JVM bytecodes.
    private static final byte ALOAD_0_GETSTATIC_THIS = -47; // 0xd1
    private static final byte ALOAD_0_PUTSTATIC_THIS = -46; // 0xd2
    private static final byte ACONST_NULL = 1;              // 0x01
    private static final byte ALOAD_0_GETFIELD_THIS = -45;  // 0xd3
    private static final byte ALOAD_0_PUTFIELD_THIS = -44;  // 0xd4

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
    void testLengthIsCalculatedFromCodeAndAttributes() throws Pack200Exception {
        // Arrange
        final byte[] packedCode = {
            ALOAD_0_GETSTATIC_THIS,
            ALOAD_0_PUTSTATIC_THIS,
            ACONST_NULL,
            ALOAD_0_GETFIELD_THIS,
            ALOAD_0_PUTFIELD_THIS
        };

        // The total length of a CodeAttribute in a class file is the sum of its components.
        // The getLength() method returns the value of the 'attribute_length' field.
        // attribute_length = (
        //      2 bytes for max_stack
        //    + 2 bytes for max_locals
        //    + 4 bytes for code_length (field)
        //    + N bytes for the actual code
        //    + 2 bytes for exception_table_length
        //    + M bytes for the exception table
        //    + 2 bytes for attributes_count
        //    + P bytes for the attributes
        // )
        // The fixed-size header part is 12 bytes (for an empty exception table and no sub-attributes).
        final int CODE_ATTRIBUTE_FIXED_HEADER_SIZE = 12;

        // The packed bytecodes are expected to unpack to the following lengths:
        // - ALOAD_0_GETSTATIC_THIS: 4 bytes (expands to aload_0 + getstatic)
        // - ALOAD_0_PUTSTATIC_THIS: 4 bytes (expands to aload_0 + putstatic)
        // - ACONST_NULL:            1 byte
        // - ALOAD_0_GETFIELD_THIS:  4 bytes (expands to aload_0 + getfield)
        // - ALOAD_0_PUTFIELD_THIS:  4 bytes (expands to aload_0 + putfield)
        // Total unpacked code length = 4 + 4 + 1 + 4 + 4 = 17 bytes.
        final int unpackedCodeLength = 17;

        final int expectedInitialLength = CODE_ATTRIBUTE_FIXED_HEADER_SIZE + unpackedCodeLength; // 12 + 17 = 29

        // Act
        final CodeAttribute codeAttribute = new CodeAttribute(3, 2, packedCode, segment, operandManager, new ArrayList<>());

        // Assert
        assertEquals(expectedInitialLength, codeAttribute.getLength(),
            "Initial attribute length should be the sum of the header and the unpacked code size.");

        // Arrange for the next step: adding a sub-attribute
        // A sub-attribute adds its own total size to the CodeAttribute's length.
        // For a LocalVariableTableAttribute, this is:
        //   6 bytes for the attribute_info header (name_index + length)
        // + 2 bytes for the local_variable_table_length (which is 0 here)
        // = 8 bytes total.
        final int LOCAL_VARIABLE_TABLE_ATTRIBUTE_SIZE = 8;
        final LocalVariableTableAttribute localVariableTableAttribute =
            new LocalVariableTableAttribute(0, null, null, null, null, null);
        final int expectedLengthAfterAdd = expectedInitialLength + LOCAL_VARIABLE_TABLE_ATTRIBUTE_SIZE; // 29 + 8 = 37

        // Act
        codeAttribute.addAttribute(localVariableTableAttribute);

        // Assert
        assertEquals(expectedLengthAfterAdd, codeAttribute.getLength(),
            "Attribute length should increase by the size of the added sub-attribute.");
    }

    // --- Mock implementations for testing dependencies ---

    private static class MockSegment extends Segment {
        private final CpBands cpBands = new MockCpBands(this);

        @Override
        public SegmentConstantPool getConstantPool() {
            return new MockSegmentConstantPool(cpBands);
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

    private static class MockSegmentConstantPool extends SegmentConstantPool {
        MockSegmentConstantPool(final CpBands bands) {
            super(bands);
        }

        @Override
        protected int matchSpecificPoolEntryIndex(final String[] nameArray, final String compareString, final int desiredIndex) {
            return 1;
        }
    }

    private static class MockOperandManager extends OperandManager {
        MockOperandManager() {
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
                new int[]{}, // bcStringRef
                new int[]{0, 1, 2, 3, 4}, // bcClassRef (needed for field/method lookups)
                new int[]{}, // bcFieldRef
                new int[]{}, // bcMethodRef
                new int[]{}, // bcIMethodRef
                new int[]{}, // bcThisField
                new int[]{0, 0, 0, 0, 0, 0}, // bcSuperField
                new int[]{}, // bcThisMethod
                new int[]{0}, // bcSuperMethod
                new int[]{}, // bcInitRef
                new int[]{}, null);
        }
    }
}