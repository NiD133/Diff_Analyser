package org.apache.commons.compress.harmony.unpack200.bytecode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.LinkedList;
import java.util.List;
import org.apache.commons.compress.harmony.unpack200.Pack200Exception;
import org.apache.commons.compress.harmony.unpack200.Segment;
import org.junit.Test;

// The class name and inheritance are preserved from the original generated test.
// A more descriptive name would be CodeAttributeTest.
public class CodeAttribute_ESTestTest10 extends CodeAttribute_ESTest_scaffolding {

    /**
     * Tests that the renumber() method throws an IndexOutOfBoundsException
     * when an exception table entry has a negative start_pc, which is an invalid index.
     */
    @Test(timeout = 4000)
    public void renumberShouldThrowIndexOutOfBoundsForNegativeStartIndexInExceptionTable() throws Pack200Exception {
        // Arrange
        // 1. Create an exception table with an entry containing a negative start_pc.
        // This negative value will be used as an index, which should fail.
        final int invalidStartIndex = -3254;
        ExceptionTableEntry entryWithNegativeStart = new ExceptionTableEntry(
            invalidStartIndex, 1477, 1477, null // end and handler PCs are not relevant
        );
        List<ExceptionTableEntry> exceptionTable = new LinkedList<>();
        exceptionTable.add(entryWithNegativeStart);

        // 2. Set up minimal dependencies for the CodeAttribute constructor.
        // The contents of these objects are not important for this specific test.
        byte[] packedCode = new byte[2];
        int[] emptyInts = new int[0];
        OperandManager operandManager = new OperandManager(
            emptyInts, emptyInts, emptyInts, emptyInts, emptyInts, emptyInts, emptyInts,
            emptyInts, emptyInts, emptyInts, emptyInts, emptyInts, emptyInts, emptyInts,
            emptyInts, emptyInts, emptyInts, emptyInts, emptyInts, emptyInts, emptyInts
        );

        CodeAttribute codeAttribute = new CodeAttribute(
            (byte) 0, // maxStack
            (byte) 0, // maxLocals
            packedCode,
            null,     // Segment
            operandManager,
            exceptionTable
        );

        // The list of new offsets is empty. The renumber method will attempt to access
        // this list at the invalid negative index, triggering the exception.
        List<Integer> newByteCodeOffsets = new LinkedList<>();

        // Act & Assert
        try {
            codeAttribute.renumber(newByteCodeOffsets);
            fail("Expected an IndexOutOfBoundsException due to negative start_pc in exception table.");
        } catch (IndexOutOfBoundsException e) {
            // Verify that the exception is thrown because of the invalid index access.
            String expectedMessage = "Index: " + invalidStartIndex + ", Size: 0";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}