package org.apache.commons.compress.harmony.unpack200.bytecode;

import org.junit.Before;
import org.junit.Test;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PipedOutputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.apache.commons.compress.harmony.unpack200.Segment;
import org.evosuite.runtime.mock.java.io.MockFileOutputStream;

/**
 * This test suite is a refactored version of an EvoSuite-generated test class for {@link CodeAttribute}.
 * The goal of the refactoring is to improve readability and maintainability.
 */
public class CodeAttributeTest {

    private static final CPUTF8 ATTRIBUTE_NAME = new CPUTF8("Code", 0);

    @Before
    public void setUp() {
        // Reset the static attribute name before each test to ensure isolation.
        CodeAttribute.setAttributeName(ATTRIBUTE_NAME);
    }

    /**
     * Creates a default, empty OperandManager for tests that don't need specific operand values.
     */
    private OperandManager createEmptyOperandManager() {
        return new OperandManager(new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0],
                new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0], new int[0],
                new int[0], new int[0], new int[0], new int[0], new int[0], new int[0]);
    }

    @Test
    public void constructor_shouldCorrectlyInitializeProperties() {
        // Arrange
        byte[] packedCode = {(byte) 0x00 /* nop */};
        List<ExceptionTableEntry> exceptionTable = Collections.emptyList();
        OperandManager operandManager = createEmptyOperandManager();

        // Act
        CodeAttribute codeAttribute = new CodeAttribute(10, 5, packedCode, null, operandManager, exceptionTable);

        // Assert
        assertEquals(10, codeAttribute.maxStack);
        assertEquals(5, codeAttribute.maxLocals);
        assertEquals(1, codeAttribute.codeLength);
        assertNotNull(codeAttribute.byteCodes);
        assertEquals(1, codeAttribute.byteCodes.size());
    }

    @Test
    public void constructor_shouldCorrectlyParseWideInstruction() {
        // Arrange
        // 0xC4 is the 'wide' instruction modifier, which affects the next instruction.
        byte[] packedCode = {0, 0, 0, 0, (byte) 0xC4, 0, 0};
        int[] intOperands = new int[7];
        OperandManager operandManager = new OperandManager(intOperands, intOperands, intOperands, intOperands, intOperands,
                intOperands, intOperands, intOperands, intOperands, intOperands, intOperands, intOperands, intOperands,
                intOperands, intOperands, intOperands, intOperands, intOperands, intOperands, intOperands, intOperands);
        List<ExceptionTableEntry> exceptionTable = Collections.emptyList();

        // Act
        CodeAttribute codeAttribute = new CodeAttribute(28, 17, packedCode, null, operandManager, exceptionTable);

        // Assert
        // The 'wide' instruction format can alter the final code length.
        assertEquals(9, codeAttribute.codeLength);
        assertEquals(28, codeAttribute.maxStack);
        assertEquals(17, codeAttribute.maxLocals);
    }

    @Test
    public void getLength_shouldIncludeExceptionTableSize() {
        // Arrange
        byte[] packedCode = new byte[4];
        OperandManager operandManager = createEmptyOperandManager();
        List<ExceptionTableEntry> exceptionTable = new LinkedList<>();
        CPUTF8 exceptionClassName = new CPUTF8("java/lang/Exception", 1);
        CPClass exceptionClass = new CPClass(exceptionClassName, 2);
        exceptionTable.add(new ExceptionTableEntry(1, 2, 3, exceptionClass));

        CodeAttribute codeAttribute = new CodeAttribute(10, 5, packedCode, null, operandManager, exceptionTable);

        // Act
        // The total length is the sum of its components:
        // max_stack (2) + max_locals (2) + code_length (4) + code (4)
        // + exception_table_length (2) + exception_table (1 entry * 8 bytes)
        // + attributes_count (2) = 24 bytes.
        int length = codeAttribute.getLength();

        // Assert
        assertEquals(24, length);
    }

    @Test
    public void getLength_shouldIncludeLengthOfAddedAttributes() {
        // Arrange
        byte[] packedCode = new byte[0];
        OperandManager operandManager = createEmptyOperandManager();
        CodeAttribute codeAttribute = new CodeAttribute(939, 1168, packedCode, null, operandManager, Collections.emptyList());

        // This test uses a negative number for the local variable count, which is invalid but tests calculation robustness.
        int[] emptyInts = new int[1];
        CPUTF8[] emptyUTF8s = new CPUTF8[1];
        LocalVariableTableAttribute localVariableTable = new LocalVariableTableAttribute(-2518, emptyInts, emptyInts, emptyUTF8s, emptyUTF8s, emptyInts);
        codeAttribute.addAttribute(localVariableTable);

        // Act
        // Calculation:
        // Code attribute body: max_stack(2) + max_locals(2) + code_length(4) + code(0) + exception_table_length(2) + attributes_count(2) = 12
        // Nested attribute: name_index(2) + length(4) + body
        // LocalVarTable body length: 2 + num_vars * 10 = 2 + (-2518 * 10) = -25178
        // Total length = 12 + (6 + (-25178)) = 18 - 25178 = -25160
        int length = codeAttribute.getLength();

        // Assert
        assertEquals(-25160, length);
    }

    @Test
    public void getNestedClassFileEntries_shouldIncludeEntriesFromExceptionTable() {
        // Arrange
        byte[] packedCode = new byte[1];
        OperandManager operandManager = createEmptyOperandManager();
        List<ExceptionTableEntry> exceptionTable = new LinkedList<>();
        CPUTF8 exceptionClassName = new CPUTF8("MyException", 1);
        CPClass exceptionClass = new CPClass(exceptionClassName, 2);
        exceptionTable.add(new ExceptionTableEntry(1, 2, 3, exceptionClass));

        CodeAttribute codeAttribute = new CodeAttribute(0, 0, packedCode, null, operandManager, exceptionTable);

        // Act
        ClassFileEntry[] nestedEntries = codeAttribute.getNestedClassFileEntries();

        // Assert
        // Expecting 3 entries: "Code" attribute name (static), the CPClass for the exception, and the CPUTF8 for its name.
        assertEquals(3, nestedEntries.length);
    }

    @Test
    public void writeBody_shouldSucceedWithResolvedAttributes() throws IOException {
        // Arrange
        byte[] packedCode = new byte[4];
        OperandManager operandManager = createEmptyOperandManager();
        CodeAttribute codeAttribute = new CodeAttribute(85, 85, packedCode, null, operandManager, Collections.emptyList());

        // Attributes must be resolved against a constant pool before writing.
        ClassConstantPool constantPool = new ClassConstantPool();
        constantPool.resolve(null); // Mark pool as resolved
        codeAttribute.resolve(constantPool);

        MockFileOutputStream outputStream = new MockFileOutputStream("test.bin", true);
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        // Act & Assert (no exception should be thrown)
        codeAttribute.writeBody(dataOutputStream);
    }

    @Test(expected = IllegalStateException.class)
    public void writeBody_shouldThrowIllegalStateException_whenNestedAttributeIsNotResolved() throws IOException {
        // Arrange
        byte[] packedCode = new byte[10];
        OperandManager operandManager = createEmptyOperandManager();
        CodeAttribute codeAttribute = new CodeAttribute(158, 158, packedCode, null, operandManager, Collections.emptyList());

        // Add an attribute that requires resolution (SourceFileAttribute needs its CPUTF8 entry resolved).
        CPUTF8 sourceFileName = new CPUTF8("MySource.java");
        codeAttribute.addAttribute(new SourceFileAttribute(sourceFileName));

        // Resolve the CodeAttribute, but the constant pool doesn't contain the entry for the nested attribute.
        ClassConstantPool constantPool = new ClassConstantPool();
        constantPool.resolve(null);
        codeAttribute.resolve(constantPool);

        MockFileOutputStream outputStream = new MockFileOutputStream("test.bin", false);
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        // Act
        codeAttribute.writeBody(dataOutputStream); // Should fail here.
    }

    @Test(expected = IOException.class)
    public void writeBody_shouldPropagateIOException_whenOutputStreamThrows() throws IOException {
        // Arrange
        byte[] packedCode = new byte[3];
        OperandManager operandManager = createEmptyOperandManager();
        CodeAttribute codeAttribute = new CodeAttribute(0, 0, packedCode, null, operandManager, Collections.emptyList());

        // A disconnected PipedOutputStream will throw an IOException on write.
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(pipedOutputStream);

        // Act
        codeAttribute.writeBody(dataOutputStream);
    }

    @Test(expected = IllegalStateException.class)
    public void resolve_shouldThrowIllegalStateException_whenConstantPoolIsNotResolved() {
        // Arrange
        byte[] packedCode = new byte[8];
        OperandManager operandManager = createEmptyOperandManager();
        CodeAttribute codeAttribute = new CodeAttribute(0, 0, packedCode, null, operandManager, Collections.emptyList());
        ClassConstantPool unresolvedConstantPool = new ClassConstantPool();

        // Act
        codeAttribute.resolve(unresolvedConstantPool);
    }

    @Test(expected = NullPointerException.class)
    public void resolve_shouldThrowNullPointerException_whenGivenNullConstantPool() {
        // Arrange
        byte[] packedCode = new byte[3];
        OperandManager operandManager = createEmptyOperandManager();
        CodeAttribute codeAttribute = new CodeAttribute(0, 0, packedCode, null, operandManager, Collections.emptyList());

        // Act
        codeAttribute.resolve(null);
    }

    @Test(expected = NullPointerException.class)
    public void renumber_shouldThrowNullPointerException_whenGivenNullOffsets() {
        // Arrange
        byte[] packedCode = new byte[7];
        CodeAttribute codeAttribute = new CodeAttribute(-1, 66, packedCode, null, createEmptyOperandManager(), null);

        // Act
        codeAttribute.renumber(null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void renumber_shouldThrowIndexOutOfBoundsException_whenExceptionTableHasInvalidOffset() {
        // Arrange
        byte[] packedCode = new byte[2];
        List<ExceptionTableEntry> exceptionTable = new LinkedList<>();
        // Add an entry with a negative start_pc, which is invalid.
        exceptionTable.add(new ExceptionTableEntry(-3254, 1477, 1477, null));
        CodeAttribute codeAttribute = new CodeAttribute(0, 0, packedCode, null, createEmptyOperandManager(), exceptionTable);

        // Act
        codeAttribute.renumber(new LinkedList<>());
    }

    @Test
    public void toString_shouldReturnExpectedFormat() {
        // Arrange
        byte[] packedCode = new byte[2];
        OperandManager operandManager = createEmptyOperandManager();
        CodeAttribute codeAttribute = new CodeAttribute(97, 97, packedCode, null, operandManager, Collections.emptyList());

        // Act
        String result = codeAttribute.toString();

        // Assert
        // Expected length: 12 (header) + 2 (code) = 14 bytes
        assertEquals("Code: 14 bytes", result);
    }
}