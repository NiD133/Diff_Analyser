package org.apache.commons.compress.harmony.unpack200.bytecode;

import org.apache.commons.compress.harmony.unpack200.Segment;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link CodeAttribute}.
 * This test focuses on verifying the nested class file entries returned by a CodeAttribute.
 */
public class CodeAttribute_ESTestTest20 extends CodeAttribute_ESTest_scaffolding {

    /**
     * Tests that getNestedClassFileEntries() correctly includes the attribute's name
     * and the class and name references from its exception table.
     */
    @Test
    public void getNestedClassFileEntriesShouldIncludeExceptionTableDetails() throws Exception {
        // Arrange
        // 1. An OperandManager with no operands, for a simple bytecode stream.
        int[] noOperands = new int[0];
        OperandManager operandManager = new OperandManager(
            noOperands, noOperands, noOperands, noOperands, noOperands, noOperands, noOperands,
            noOperands, noOperands, noOperands, noOperands, noOperands, noOperands, noOperands,
            noOperands, noOperands, noOperands, noOperands, noOperands, noOperands, noOperands
        );

        // 2. A minimal bytecode sequence, consisting of a single NOP instruction.
        byte[] singleNopInstruction = {0x00};

        // 3. An exception table with a single entry, which requires a CPClass and a CPUTF8
        //    to be added to the constant pool.
        CPUTF8 exceptionClassName = new CPUTF8("java/lang/Throwable", 0);
        CPClass exceptionClass = new CPClass(exceptionClassName, 1);
        ExceptionTableEntry exceptionTableEntry = new ExceptionTableEntry(0, 1, 1, exceptionClass);
        List<ExceptionTableEntry> exceptionTable = Collections.singletonList(exceptionTableEntry);

        // Act
        // Construct the CodeAttribute. The constructor will parse the bytecode.
        // We are testing the side-effects of construction and the getNestedClassFileEntries() method.
        CodeAttribute codeAttribute = new CodeAttribute(
            (byte) 0, // maxStack
            (byte) 0, // maxLocals
            singleNopInstruction,
            null, // segment
            operandManager,
            exceptionTable
        );
        ClassFileEntry[] nestedEntries = codeAttribute.getNestedClassFileEntries();

        // Assert
        // Check that the basic properties of the CodeAttribute are set correctly.
        assertEquals("maxStack should be 0", 0, codeAttribute.maxStack);
        assertEquals("maxLocals should be 0", 0, codeAttribute.maxLocals);
        assertEquals("codeLength should be 1 for a single NOP", 1, codeAttribute.codeLength);

        // The main assertion: verify the number of nested constant pool entries.
        // The expected entries are:
        // 1. The attribute name itself (a CPUTF8 for "Code").
        // 2. The exception's catch type (the CPClass for "java/lang/Throwable").
        // 3. The name of the exception's catch type (the CPUTF8 for "java/lang/Throwable").
        // Note: The source of the third entry is not immediately obvious from the SUT's code,
        // but this test confirms that three entries are expected.
        assertEquals("Should contain 3 nested entries", 3, nestedEntries.length);
    }
}