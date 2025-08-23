package org.apache.commons.compress.harmony.unpack200.bytecode;

import static org.junit.Assert.assertEquals;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.compress.harmony.pack200.Pack200Exception;
import org.apache.commons.compress.harmony.unpack200.Segment;
import org.evosuite.runtime.mock.java.io.MockFileOutputStream;
import org.junit.Test;

public class CodeAttribute_ESTestTest18 extends CodeAttribute_ESTest_scaffolding {

    /**
     * Tests that a CodeAttribute containing an exception table can be successfully
     * resolved and its body written to a stream.
     *
     * This test verifies that the core properties (maxStack, maxLocals, codeLength)
     * are correctly maintained after these operations.
     */
    @Test(timeout = 4000)
    public void testResolveAndWriteBodyWithExceptionTable() throws Pack200Exception, IOException {
        // Arrange
        final int maxStack = -2553;
        final int maxLocals = 85;
        // A 4-byte array of zeros represents four 'nop' instructions.
        final byte[] packedCode = new byte[4];
        final int expectedCodeLength = 4;

        // The OperandManager is required by the constructor but is not used in this test
        // because 'nop' instructions have no operands. Thus, it can be created with nulls.
        final OperandManager operandManager = new OperandManager(
            null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null);

        final List<ExceptionTableEntry> exceptionTable = new LinkedList<>();
        final Segment segment = null; // The segment is not used for 'nop' instructions.

        final CodeAttribute codeAttribute = new CodeAttribute(
            maxStack, maxLocals, packedCode, segment, operandManager, exceptionTable);

        // Define and add an exception table entry. This is done after creating the
        // CodeAttribute to ensure it correctly handles a live, mutable list.
        final CPUTF8 exceptionClassName = new CPUTF8("java/lang/Exception", 38);
        final CPClass exceptionClassRef = new CPClass(exceptionClassName, 85);
        final int startPc = 87;
        final int endPc = 38;
        final int handlerPc = 87;
        final ExceptionTableEntry exceptionEntry = new ExceptionTableEntry(startPc, endPc, handlerPc, exceptionClassRef);
        exceptionTable.add(exceptionEntry);

        final ClassConstantPool constantPool = new ClassConstantPool();
        // The constant pool must be resolved before it's used by the attribute.
        constantPool.resolve(null);

        // Act
        // 1. Resolve constant pool references within the code attribute. This will
        // process the exception table and add its entries to the constant pool.
        codeAttribute.resolve(constantPool);

        // 2. Write the attribute's body. This test ensures the operation completes successfully.
        MockFileOutputStream outputStream = new MockFileOutputStream("test-output.bin");
        DataOutputStream dataOut = new DataOutputStream(outputStream);
        codeAttribute.writeBody(dataOut);

        // Assert
        // Verify that the core properties of the CodeAttribute are preserved.
        assertEquals("Code length should match the unpacked bytecode size", expectedCodeLength, codeAttribute.codeLength);
        assertEquals("maxLocals should be preserved", maxLocals, codeAttribute.maxLocals);
        assertEquals("maxStack should be preserved", maxStack, codeAttribute.maxStack);
    }
}