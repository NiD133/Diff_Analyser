package org.apache.commons.compress.harmony.unpack200.bytecode;

import org.apache.commons.compress.harmony.pack200.Pack200Exception;
import org.apache.commons.compress.harmony.unpack200.Segment;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link CodeAttribute}.
 */
public class CodeAttributeTest {

    /**
     * Tests that the CodeAttribute is correctly initialized by the constructor and that
     * a subsequent call to renumber() with the same offsets does not alter its state.
     */
    @Test
    public void renumberWithUnchangedOffsetsShouldNotAffectCodeAttributeState() throws Pack200Exception {
        // Arrange
        final int maxStack = 0;
        final int maxLocals = 0;

        // The bytecode array represents three 'nop' (0x00) instructions.
        // Each 'nop' is 1 byte long, so the total code length should be 3.
        final byte[] byteCodePacked = {0x00, 0x00, 0x00};
        final int expectedCodeLength = byteCodePacked.length;

        // The OperandManager is a required dependency, but its internal state is not
        // relevant for this test as 'nop' instructions don't have operands.
        // We can safely provide empty arrays.
        final int[] noOperands = new int[0];
        final OperandManager operandManager = new OperandManager(
            noOperands, noOperands, noOperands, noOperands, noOperands, noOperands,
            noOperands, noOperands, noOperands, noOperands, noOperands, noOperands,
            noOperands, noOperands, noOperands, noOperands, noOperands, noOperands,
            noOperands, noOperands, noOperands
        );

        final List<ExceptionTableEntry> exceptionTable = Collections.emptyList();
        final Segment segment = null; // The segment is not used for this test case.

        // Act
        // 1. Construct the CodeAttribute, which parses the bytecode.
        final CodeAttribute codeAttribute = new CodeAttribute(maxStack, maxLocals, byteCodePacked, segment, operandManager, exceptionTable);

        // 2. Call renumber with the attribute's own, unmodified list of offsets.
        // This action should not change the attribute's state.
        codeAttribute.renumber(codeAttribute.byteCodeOffsets);

        // Assert
        // Verify that the state set during construction remains unchanged.
        assertEquals("Max stack should be initialized correctly", maxStack, codeAttribute.maxStack);
        assertEquals("Max locals should be initialized correctly", maxLocals, codeAttribute.maxLocals);
        assertEquals("Code length should be calculated from bytecode", expectedCodeLength, codeAttribute.codeLength);
    }
}