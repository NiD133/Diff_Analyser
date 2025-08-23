package org.apache.commons.compress.harmony.unpack200.bytecode;

import org.apache.commons.compress.harmony.unpack200.Segment;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * This test class has been refactored to focus on a single, clear, and understandable test case.
 */
public class CodeAttributeTest {

    private static final byte INVOKESTATIC_OPCODE = (byte) 0xD2;

    /**
     * Tests that the CodeAttribute constructor throws a NullPointerException
     * when it needs to process a bytecode that requires a Segment to resolve
     * its operands (e.g., an 'invokestatic' instruction), but the provided
     * Segment is null.
     */
    @Test
    public void constructorShouldThrowNPEWhenByteCodeRequiresNullSegment() {
        // Arrange
        // The 'invokestatic' bytecode requires resolving a method reference from the
        // constant pool, which is accessed via the Segment.
        byte[] byteCodes = {INVOKESTATIC_OPCODE};

        // The OperandManager provides operand values. For this test, we only need to
        // provide a single value for the constant pool index that 'invokestatic' will use.
        // We use a single, non-empty array for all operand types for simplicity.
        int[] operandValues = {0};
        OperandManager operandManager = new OperandManager(
            operandValues, operandValues, operandValues, operandValues, operandValues,
            operandValues, operandValues, operandValues, operandValues, operandValues,
            operandValues, operandValues, operandValues, operandValues, operandValues,
            operandValues, operandValues, operandValues, operandValues, operandValues,
            operandValues
        );

        List<ExceptionTableEntry> exceptionTable = Collections.emptyList();
        Segment segment = null; // This null segment is the condition under test.

        // These values are required by the constructor but are not relevant to this test.
        int maxStack = 1;
        int maxLocals = 1;

        // Act & Assert
        try {
            new CodeAttribute(maxStack, maxLocals, byteCodes, segment, operandManager, exceptionTable);
            fail("Expected a NullPointerException because the segment is null and is required by the 'invokestatic' bytecode.");
        } catch (NullPointerException e) {
            // This is the expected outcome. The test passes.
            // The original test also verified that the exception message was null.
            assertNull("The NullPointerException should not have a message.", e.getMessage());
        } catch (Exception e) {
            fail("An unexpected exception was thrown: " + e);
        }
    }
}