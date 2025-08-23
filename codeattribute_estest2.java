package org.apache.commons.compress.harmony.unpack200.bytecode;

import org.apache.commons.compress.harmony.pack200.Pack200Exception;
import org.apache.commons.compress.harmony.unpack200.OperandManager;
import org.apache.commons.compress.harmony.unpack200.Segment;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link CodeAttribute}.
 * This class replaces the original EvoSuite-generated test class.
 */
public class CodeAttributeTest {

    /**
     * Tests that the CodeAttribute constructor correctly calculates the total code length
     * when the bytecode sequence includes a 'wide' instruction. The 'wide' instruction
     * has a different length than standard opcodes and affects how the input bytes are processed.
     */
    @Test
    public void constructorShouldCalculateCorrectCodeLengthWithWideOpcode() throws Pack200Exception {
        // Arrange
        final int maxStack = 28;
        final int maxLocals = 17;
        final byte OPCODE_WIDE = (byte) 0xC4;

        // This represents a packed sequence of bytecodes. The 'wide' opcode (0xC4)
        // is special: it has a length of 4 and causes the constructor to skip
        // processing the immediately following byte in the input array.
        final byte[] packedCode = {
            0x00,        // nop instruction (length 1)
            0x00,        // nop instruction (length 1)
            0x00,        // nop instruction (length 1)
            0x00,        // nop instruction (length 1)
            OPCODE_WIDE, // wide instruction (length 4)
            0x00,        // This byte is skipped due to the preceding 'wide' opcode
            0x00         // nop instruction (length 1)
        };

        // The OperandManager provides operands for bytecodes. For this specific test,
        // the actual operand values are not important, so we can use empty arrays.
        final int[] emptyOperands = new int[0];
        final OperandManager operandManager = new OperandManager(
            emptyOperands, emptyOperands, emptyOperands, emptyOperands, emptyOperands,
            emptyOperands, emptyOperands, emptyOperands, emptyOperands, emptyOperands,
            emptyOperands, emptyOperands, emptyOperands, emptyOperands, emptyOperands,
            emptyOperands, emptyOperands, emptyOperands, emptyOperands, emptyOperands,
            emptyOperands
        );

        final List<ExceptionTableEntry> exceptionTable = new ArrayList<>();
        final Segment segment = null; // Not used for this specific calculation.

        // Act
        final CodeAttribute codeAttribute = new CodeAttribute(maxStack, maxLocals, packedCode, segment, operandManager, exceptionTable);

        // Assert
        // The total code length is the sum of the lengths of the processed bytecodes:
        // 1 (nop) + 1 (nop) + 1 (nop) + 1 (nop) + 4 (wide) + 1 (nop) = 9.
        final int expectedCodeLength = 9;
        assertEquals("The calculated code length should be correct.", expectedCodeLength, codeAttribute.codeLength);
        assertEquals("Max locals should be set correctly.", maxLocals, codeAttribute.maxLocals);
        assertEquals("Max stack should be set correctly.", maxStack, codeAttribute.maxStack);
    }
}