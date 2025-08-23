package org.apache.commons.compress.harmony.unpack200.bytecode;

import org.apache.commons.compress.harmony.pack200.Pack200Exception;
import org.apache.commons.compress.harmony.unpack200.Segment;
import org.junit.Test;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link CodeAttribute}.
 */
public class CodeAttributeTest {

    /**
     * Tests that the getLength() method correctly calculates the total length of the
     * CodeAttribute, including the header, the bytecode, and a non-empty exception table.
     */
    @Test
    public void getLengthShouldCalculateCorrectlyForCodeWithExceptionTable() throws Pack200Exception {
        // Arrange
        final int maxStack = -2553; // An arbitrary value for testing.
        final int maxLocals = 85;
        // A simple bytecode sequence (4 NOP instructions).
        final byte[] byteCodeBytes = {0x00, 0x00, 0x00, 0x00};

        // The OperandManager is required by the constructor. For simple bytecodes like NOP,
        // it is not used, so providing nulls is sufficient for this test.
        final OperandManager operandManager = new OperandManager(
            null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null);

        // Create an exception table with a single entry.
        final List<ExceptionTableEntry> exceptionTable = new LinkedList<>();
        final CPUTF8 exceptionName = new CPUTF8("java/lang/Exception", 0);
        final CPClass exceptionClass = new CPClass(exceptionName, 0);
        exceptionTable.add(new ExceptionTableEntry(87, 38, 87, exceptionClass));

        final CodeAttribute codeAttribute = new CodeAttribute(
            maxStack,
            maxLocals,
            byteCodeBytes,
            null, // Segment is not needed for this test.
            operandManager,
            exceptionTable
        );

        /*
         * The expected length of a Code attribute is calculated based on the JVM specification:
         *   - max_stack (u2)                  :  2 bytes
         *   - max_locals (u2)                 :  2 bytes
         *   - code_length (u4)                :  4 bytes
         *   - code (byte[code_length])        :  4 bytes (for our 4 NOPs)
         *   - exception_table_length (u2)     :  2 bytes
         *   - exception_table (1 entry * 8B)  :  8 bytes
         *   - attributes_count (u2)           :  2 bytes (for sub-attributes, none in this case)
         *   --------------------------------------------------
         *   Total                             : 24 bytes
         */
        final int expectedLength = 24;

        // Act
        final int actualLength = codeAttribute.getLength();

        // Assert
        assertEquals("The calculated attribute length should be correct.", expectedLength, actualLength);

        // Also verify that constructor parameters were stored correctly.
        assertEquals("Max stack should be set correctly.", maxStack, codeAttribute.maxStack);
        assertEquals("Max locals should be set correctly.", maxLocals, codeAttribute.maxLocals);
    }
}