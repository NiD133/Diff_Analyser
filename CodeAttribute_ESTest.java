package org.apache.commons.compress.harmony.unpack200.bytecode;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.compress.harmony.unpack200.Segment;
import org.apache.commons.compress.harmony.unpack200.bytecode.*;

/**
 * Test suite for CodeAttribute class functionality.
 * Tests cover attribute creation, length calculation, serialization, and error handling.
 */
public class CodeAttributeTest {

    /**
     * Creates a basic OperandManager with null arrays for testing simple bytecode scenarios.
     */
    private OperandManager createBasicOperandManager() {
        return new OperandManager(
            null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null
        );
    }

    /**
     * Creates an OperandManager with provided integer arrays for more complex scenarios.
     */
    private OperandManager createOperandManagerWithArrays(int[] arrays) {
        return new OperandManager(
            arrays, arrays, arrays, arrays, arrays, arrays, arrays, arrays, arrays, arrays,
            arrays, arrays, arrays, arrays, arrays, arrays, arrays, arrays, arrays, arrays, arrays
        );
    }

    @Test
    public void testGetLengthWithExceptionTable() throws Exception {
        // Given: A code attribute with bytecode and exception table entry
        byte[] bytecode = new byte[4];
        OperandManager operandManager = createBasicOperandManager();
        List<ExceptionTableEntry> exceptionTable = new LinkedList<>();
        
        CodeAttribute codeAttribute = new CodeAttribute(-2553, 85, bytecode, null, operandManager, exceptionTable);
        
        // Add exception table entry
        CPUTF8 className = new CPUTF8("i2f", 38);
        CPClass exceptionClass = new CPClass(className, 85);
        ExceptionTableEntry entry = new ExceptionTableEntry(87, 38, 87, exceptionClass);
        exceptionTable.add(entry);
        
        // When: Getting the length
        int length = codeAttribute.getLength();
        
        // Then: Length should account for exception table
        assertEquals("Length should include exception table overhead", 24, length);
        assertEquals("Max locals should be preserved", 85, codeAttribute.maxLocals);
        assertEquals("Max stack should be preserved", -2553, codeAttribute.maxStack);
    }

    @Test
    public void testCodeAttributeCreationWithBytecode() throws Exception {
        // Given: Bytecode array with specific instruction
        byte[] bytecode = new byte[7];
        bytecode[4] = (byte) -60; // multianewarray instruction
        int[] operandArrays = new int[7];
        
        OperandManager operandManager = createOperandManagerWithArrays(operandArrays);
        List<ExceptionTableEntry> exceptionTable = new LinkedList<>();
        
        // When: Creating code attribute
        CodeAttribute codeAttribute = new CodeAttribute(28, 17, bytecode, null, operandManager, exceptionTable);
        
        // Then: Attribute should be properly initialized
        assertEquals("Max locals should match input", 17, codeAttribute.maxLocals);
        assertEquals("Max stack should match input", 28, codeAttribute.maxStack);
        assertEquals("Code length should account for instruction expansion", 9, codeAttribute.codeLength);
    }

    @Test
    public void testWriteBodyWithAttributes() throws Exception {
        // Given: Code attribute with nested attribute
        byte[] bytecode = new byte[4];
        OperandManager operandManager = createBasicOperandManager();
        List<ExceptionTableEntry> exceptionTable = new LinkedList<>();
        
        CodeAttribute codeAttribute = new CodeAttribute(85, 85, bytecode, null, operandManager, exceptionTable);
        
        // Resolve with constant pool
        ClassConstantPool constantPool = new ClassConstantPool();
        constantPool.resolve(null);
        codeAttribute.resolve(constantPool);
        
        // Add nested code attribute
        CodeAttribute nestedAttribute = new CodeAttribute(39, 100, bytecode, null, operandManager, exceptionTable);
        nestedAttribute.resolve(constantPool);
        codeAttribute.addAttribute(nestedAttribute);
        
        // When: Writing to output stream
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(byteStream);
        
        // Then: Should write without errors
        assertDoesNotThrow(() -> codeAttribute.writeBody(dataStream));
        assertEquals("Original max locals preserved", 85, codeAttribute.maxLocals);
        assertEquals("Code length should match bytecode", 4, codeAttribute.codeLength);
    }

    @Test
    public void testGetLengthWithLocalVariableTable() throws Exception {
        // Given: Code attribute with local variable table
        int[] operandArrays = new int[1];
        OperandManager operandManager = createOperandManagerWithArrays(operandArrays);
        byte[] bytecode = new byte[0];
        List<ExceptionTableEntry> exceptionTable = new LinkedList<>();
        
        CodeAttribute codeAttribute = new CodeAttribute(939, 1168, bytecode, null, operandManager, exceptionTable);
        
        // Add local variable table attribute
        CPUTF8[] names = new CPUTF8[1];
        LocalVariableTableAttribute localVarTable = new LocalVariableTableAttribute(
            -2518, operandArrays, operandArrays, names, names, operandArrays
        );
        codeAttribute.addAttribute(localVarTable);
        
        // When: Getting length
        int length = codeAttribute.getLength();
        
        // Then: Length should include local variable table
        assertEquals("Length should include local variable table", -25160, length);
        assertEquals("Max locals should be preserved", 1168, codeAttribute.maxLocals);
        assertEquals("Max stack should be preserved", 939, codeAttribute.maxStack);
    }

    @Test(expected = NullPointerException.class)
    public void testWriteBodyWithNullAttribute() throws Exception {
        // Given: Code attribute with null attribute added
        byte[] bytecode = new byte[1];
        int[] operandArrays = new int[1];
        OperandManager operandManager = createOperandManagerWithArrays(operandArrays);
        List<ExceptionTableEntry> exceptionTable = new LinkedList<>();
        
        CodeAttribute codeAttribute = new CodeAttribute(3668, 3534, bytecode, null, operandManager, exceptionTable);
        
        ClassConstantPool constantPool = new ClassConstantPool();
        constantPool.resolve(null);
        codeAttribute.resolve(constantPool);
        codeAttribute.addAttribute(null); // Add null attribute
        
        // When: Writing body with null attribute
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(byteStream);
        
        // Then: Should throw NullPointerException
        codeAttribute.writeBody(dataStream);
    }

    @Test(expected = IOException.class)
    public void testWriteBodyWithClosedStream() throws Exception {
        // Given: Code attribute and closed output stream
        int[] operandArrays = new int[1];
        OperandManager operandManager = createOperandManagerWithArrays(operandArrays);
        byte[] bytecode = new byte[3];
        List<ExceptionTableEntry> exceptionTable = new LinkedList<>();
        
        CodeAttribute codeAttribute = new CodeAttribute(0, 0, bytecode, null, operandManager, exceptionTable);
        
        // Create a stream that will throw IOException
        DataOutputStream closedStream = new DataOutputStream(new ByteArrayOutputStream()) {
            @Override
            public void writeShort(int v) throws IOException {
                throw new IOException("Stream closed");
            }
        };
        
        // When: Writing to closed stream
        // Then: Should throw IOException
        codeAttribute.writeBody(closedStream);
    }

    @Test(expected = IllegalStateException.class)
    public void testResolveWithUnresolvedConstantPool() throws Exception {
        // Given: Code attribute and unresolved constant pool
        int[] operandArrays = new int[3];
        OperandManager operandManager = createOperandManagerWithArrays(operandArrays);
        byte[] bytecode = new byte[8];
        List<ExceptionTableEntry> exceptionTable = new LinkedList<>();
        
        CodeAttribute codeAttribute = new CodeAttribute(0, 0, bytecode, null, operandManager, exceptionTable);
        ClassConstantPool unresolvedPool = new ClassConstantPool();
        
        // When: Resolving with unresolved constant pool
        // Then: Should throw IllegalStateException
        codeAttribute.resolve(unresolvedPool);
    }

    @Test(expected = NullPointerException.class)
    public void testRenumberWithNullByteCodeOffsets() throws Exception {
        // Given: Code attribute with null exception table
        int[] operandArrays = new int[8];
        OperandManager operandManager = createOperandManagerWithArrays(operandArrays);
        byte[] bytecode = new byte[7];
        
        CodeAttribute codeAttribute = new CodeAttribute(-1, 66, bytecode, null, operandManager, null);
        
        // When: Renumbering with null offsets
        // Then: Should throw NullPointerException
        codeAttribute.renumber(null);
    }

    @Test
    public void testGetNestedClassFileEntriesWithExceptions() throws Exception {
        // Given: Code attribute with exception table entries
        byte[] bytecode = new byte[10];
        OperandManager operandManager = createBasicOperandManager();
        List<ExceptionTableEntry> exceptionTable = new LinkedList<>();
        
        // Add exception with null class (catch-all)
        ExceptionTableEntry catchAllEntry = new ExceptionTableEntry(25, 25, 25, null);
        exceptionTable.add(catchAllEntry);
        
        CodeAttribute codeAttribute = new CodeAttribute(25, 25, bytecode, null, operandManager, exceptionTable);
        
        // When: Getting nested entries
        ClassFileEntry[] entries = codeAttribute.getNestedClassFileEntries();
        
        // Then: Should return appropriate number of entries
        assertEquals("Should return correct number of nested entries", 11, entries.length);
        assertEquals("Max stack should be preserved", 25, codeAttribute.maxStack);
        assertEquals("Max locals should be preserved", 25, codeAttribute.maxLocals);
        assertEquals("Code length should match bytecode", 10, codeAttribute.codeLength);
    }

    @Test
    public void testGetNestedClassFileEntriesWithTypedException() throws Exception {
        // Given: Code attribute with typed exception
        int[] emptyArrays = new int[0];
        OperandManager operandManager = createOperandManagerWithArrays(emptyArrays);
        byte[] bytecode = new byte[1];
        List<ExceptionTableEntry> exceptionTable = new LinkedList<>();
        
        CPUTF8 exceptionName = new CPUTF8("ZPf8xB8qNlg", 1526);
        CPClass exceptionClass = new CPClass(exceptionName, (byte) 8);
        ExceptionTableEntry typedException = new ExceptionTableEntry(1526, 1526, 1526, exceptionClass);
        exceptionTable.add(typedException);
        
        CodeAttribute codeAttribute = new CodeAttribute(0, 0, bytecode, null, operandManager, exceptionTable);
        
        // When: Getting nested entries
        ClassFileEntry[] entries = codeAttribute.getNestedClassFileEntries();
        
        // Then: Should include exception class entries
        assertEquals("Should return entries for typed exception", 3, entries.length);
        assertEquals("Code length should match", 1, codeAttribute.codeLength);
    }

    @Test
    public void testAddLocalVariableTypeTableAttribute() throws Exception {
        // Given: Code attribute
        byte[] bytecode = new byte[7];
        OperandManager operandManager = createBasicOperandManager();
        List<ExceptionTableEntry> exceptionTable = new LinkedList<>();
        
        CodeAttribute codeAttribute = new CodeAttribute(1, 1, bytecode, null, operandManager, exceptionTable);
        
        // When: Adding local variable type table
        LocalVariableTypeTableAttribute typeTable = new LocalVariableTypeTableAttribute(
            1, null, null, null, null, null
        );
        codeAttribute.addAttribute(typeTable);
        
        // Then: Attribute should be added successfully
        assertEquals("Max locals should be preserved", 1, codeAttribute.maxLocals);
        assertEquals("Max stack should be preserved", 1, codeAttribute.maxStack);
        assertEquals("Code length should match", 7, codeAttribute.codeLength);
        assertTrue("Exception table should be empty", exceptionTable.isEmpty());
    }

    @Test
    public void testToStringBasic() throws Exception {
        // Given: Simple code attribute
        byte[] bytecode = new byte[2];
        int[] operandArrays = new int[6];
        OperandManager operandManager = createOperandManagerWithArrays(operandArrays);
        List<ExceptionTableEntry> exceptionTable = new LinkedList<>();
        
        CodeAttribute codeAttribute = new CodeAttribute(97, 97, bytecode, null, operandManager, exceptionTable);
        
        // When: Converting to string
        String result = codeAttribute.toString();
        
        // Then: Should return descriptive string
        assertEquals("Should return descriptive string", "Code: 14 bytes", result);
        assertEquals("Max stack should be preserved", 97, codeAttribute.maxStack);
        assertEquals("Max locals should be preserved", 97, codeAttribute.maxLocals);
    }

    @Test
    public void testSetAttributeName() {
        // Given: CPUTF8 attribute name
        CPUTF8 attributeName = null;
        
        // When: Setting attribute name
        // Then: Should not throw exception
        assertDoesNotThrow(() -> CodeAttribute.setAttributeName(attributeName));
    }

    @Test
    public void testRenumberWithValidOffsets() throws Exception {
        // Given: Code attribute with valid byte code offsets
        int[] operandArrays = new int[3];
        OperandManager operandManager = createOperandManagerWithArrays(operandArrays);
        byte[] bytecode = new byte[3];
        List<ExceptionTableEntry> exceptionTable = new LinkedList<>();
        
        CodeAttribute codeAttribute = new CodeAttribute(0, 0, bytecode, null, operandManager, exceptionTable);
        
        // When: Renumbering with valid offsets
        // Then: Should complete without error
        assertDoesNotThrow(() -> codeAttribute.renumber(codeAttribute.byteCodeOffsets));
        assertEquals("Max stack should be preserved", 0, codeAttribute.maxStack);
        assertEquals("Max locals should be preserved", 0, codeAttribute.maxLocals);
        assertEquals("Code length should match", 3, codeAttribute.codeLength);
    }

    @Test
    public void testGetStartPCs() throws Exception {
        // Given: Code attribute
        int[] emptyArrays = new int[0];
        OperandManager operandManager = createOperandManagerWithArrays(emptyArrays);
        byte[] bytecode = new byte[12];
        List<ExceptionTableEntry> exceptionTable = new LinkedList<>();
        
        CodeAttribute codeAttribute = new CodeAttribute(0, 0, bytecode, null, operandManager, exceptionTable);
        
        // When: Getting start PCs
        int[] startPCs = codeAttribute.getStartPCs();
        
        // Then: Should return valid array
        assertNotNull("Start PCs should not be null", startPCs);
        assertEquals("Code length should match", 12, codeAttribute.codeLength);
        assertTrue("Exception table should be empty", exceptionTable.isEmpty());
    }

    /**
     * Helper method to assert that a lambda does not throw an exception.
     */
    private void assertDoesNotThrow(Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            fail("Expected no exception, but got: " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }
}