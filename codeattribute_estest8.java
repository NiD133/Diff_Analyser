package org.apache.commons.compress.harmony.unpack200.bytecode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Collections;
import java.util.List;
import org.apache.commons.compress.harmony.pack200.Pack200Exception;
import org.apache.commons.compress.harmony.unpack200.Segment;
import org.junit.Test;

public class CodeAttribute_ESTestTest8 extends CodeAttribute_ESTest_scaffolding {

    /**
     * Tests that calling resolve() on a CodeAttribute with an unresolved
     * ClassConstantPool throws an IllegalStateException. A constant pool must be
     * resolved before attributes that depend on it can be resolved.
     */
    @Test
    public void resolveThrowsIllegalStateExceptionForUnresolvedConstantPool() throws Pack200Exception {
        // Arrange: Set up a CodeAttribute and a constant pool that has not been resolved.
        final byte[] codeBytes = new byte[0];
        final List<ExceptionTableEntry> exceptionTable = Collections.emptyList();
        final Segment segment = null; // The segment is not needed for this test.

        // The OperandManager constructor requires 21 integer arrays, but their
        // contents are not relevant for this test's logic.
        final int[] emptyIntArray = new int[0];
        final OperandManager operandManager = new OperandManager(
            emptyIntArray, emptyIntArray, emptyIntArray, emptyIntArray, emptyIntArray,
            emptyIntArray, emptyIntArray, emptyIntArray, emptyIntArray, emptyIntArray,
            emptyIntArray, emptyIntArray, emptyIntArray, emptyIntArray, emptyIntArray,
            emptyIntArray, emptyIntArray, emptyIntArray, emptyIntArray, emptyIntArray,
            emptyIntArray
        );

        final CodeAttribute codeAttribute = new CodeAttribute(
            0, /* maxStack */
            0, /* maxLocals */
            codeBytes,
            segment,
            operandManager,
            exceptionTable
        );

        // A newly created ClassConstantPool is in an unresolved state by default.
        final ClassConstantPool unresolvedConstantPool = new ClassConstantPool();

        // Act & Assert: Attempt to resolve and verify that the correct exception is thrown.
        try {
            codeAttribute.resolve(unresolvedConstantPool);
            fail("Expected an IllegalStateException because the constant pool has not been resolved.");
        } catch (final IllegalStateException e) {
            // This is the expected outcome. We can also verify the message for robustness.
            final String expectedMessage = "Constant pool is not yet resolved; this does not make any sense";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}