package org.apache.commons.compress.harmony.unpack200.bytecode;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.compress.harmony.pack200.Pack200Exception;
import org.apache.commons.compress.harmony.unpack200.Segment;
import org.junit.Before;
import org.junit.Test;

public class CodeAttributeTest {

    // The byte value 0x00 is NOP in JVM bytecode.
    // Using only NOPs keeps operand extraction trivial and tests stable.
    private static byte[] nops(int count) {
        return new byte[count]; // all zeros
    }

    // Builds an OperandManager with all sources null, which is fine for NOPs.
    private static OperandManager emptyOperandManager() {
        return new OperandManager(
                null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null,
                null, null, null, null, null, null
        );
    }

    private static CodeAttribute newCodeWithNops(int maxStack, int maxLocals, int nopCount) throws Pack200Exception {
        return new CodeAttribute(
                maxStack,
                maxLocals,
                nops(nopCount),
                (Segment) null,
                emptyOperandManager(),
                new ArrayList<ExceptionTableEntry>()
        );
    }

    @Before
    public void setUp() {
        // Ensure CodeAttribute has a name for the attribute header.
        CodeAttribute.setAttributeName(new CPUTF8("Code"));
    }

    @Test
    public void constructor_withOnlyNops_setsMaxesAndComputesCodeLengthAndOffsets() throws Exception {
        // Arrange
        int maxStack = 2;
        int maxLocals = 3;
        int nopCount = 3;

        // Act
        CodeAttribute code = newCodeWithNops(maxStack, maxLocals, nopCount);

        // Assert
        assertEquals(maxStack, code.maxStack);
        assertEquals(maxLocals, code.maxLocals);

        // With only NOPs, each instruction is 1 byte.
        assertEquals("Code length should match number of NOPs", nopCount, code.codeLength);

        // Offsets should start at 0 and increment by 1 per NOP.
        assertEquals("There should be one offset per bytecode", nopCount, code.byteCodeOffsets.size());
        for (int i = 0; i < nopCount; i++) {
            assertEquals("Unexpected bytecode offset at index " + i, Integer.valueOf(i), code.byteCodeOffsets.get(i));
        }

        // We should have as many decoded bytecodes as source bytes for simple NOPs.
        assertEquals(nopCount, code.byteCodes.size());
    }

    @Test
    public void writeBody_withNoAttributesOrExceptions_writesExpectedLayout() throws Exception {
        // Arrange
        CodeAttribute code = newCodeWithNops(1, 1, 5);

        // Expected body structure (not including the attribute header and name index):
        // u2 max_stack + u2 max_locals + u4 code_length + code + u2 exception_table_length + exception_table + u2 attributes_count + attributes
        // With no exceptions and no nested attributes: 2 + 2 + 4 + code_length + 2 + 0 + 2 + 0 = 12 + code_length
        // Note: different implementations count it as 10 + code_length or 12 + code_length; using 12 here accounts for the two u2 fields for tables.
        // To avoid fragility across implementations, we verify structure indirectly:
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(out);

        // Act
        code.writeBody(dos);
        dos.flush();
        byte[] body = out.toByteArray();

        // Assert
        // The body must contain at least the fixed header plus the code bytes, i.e. "header + 5"
        assertTrue("Body should be larger than the code array", body.length > code.codeLength);

        // A minimal structural check: it should be header + code_length + empty exception table + empty attributes.
        // For a typical Code attribute body, this is 2 + 2 + 4 + N + 2 + 0 + 2 + 0 = 12 + N.
        int expectedMin = 12 + code.codeLength;
        assertEquals("Unexpected body length", expectedMin, body.length);
    }

    @Test
    public void exceptionTable_isStoredAndStartPCsCanBeQueried() throws Exception {
        // Arrange
        CPUTF8 className = new CPUTF8("java/lang/Object");
        CPClass catchType = new CPClass(className, 0);
        ExceptionTableEntry entry = new ExceptionTableEntry(0, 4, 8, catchType);

        List<ExceptionTableEntry> table = new ArrayList<>();
        table.add(entry);

        CodeAttribute code = new CodeAttribute(
                1,
                1,
                nops(3),
                (Segment) null,
                emptyOperandManager(),
                table
        );

        // Act
        int[] startPCs = code.getStartPCs();

        // Assert
        assertArrayEquals(new int[] { 0 }, startPCs);
        assertEquals(1, code.exceptionTable.size());
        assertSame("The same exception table instance should be retained", table, code.exceptionTable);
    }

    @Test
    public void addAttribute_addsToAttributeList() throws Exception {
        // Arrange
        CodeAttribute code = newCodeWithNops(0, 0, 1);
        assertTrue(code.attributes.isEmpty());

        SourceFileAttribute source = new SourceFileAttribute(new CPUTF8("Test.java"));

        // Act
        code.addAttribute(source);

        // Assert
        assertEquals(1, code.attributes.size());
        assertSame(source, code.attributes.get(0));
    }

    @Test
    public void toString_containsCodeAndByteCount() throws Exception {
        // Arrange
        CodeAttribute code = newCodeWithNops(0, 0, 2);

        // Act
        String s = code.toString();

        // Assert
        assertNotNull(s);
        assertTrue("toString should mention 'Code'", s.contains("Code"));
        assertTrue("toString should mention 'bytes'", s.toLowerCase().contains("byte"));
    }

    @Test
    public void renumber_withSameOffsets_keepsExceptionTableIndicesStable() throws Exception {
        // Arrange
        CPUTF8 className = new CPUTF8("java/lang/Throwable");
        CPClass catchType = new CPClass(className, 0);
        ExceptionTableEntry entry = new ExceptionTableEntry(0, 1, 1, catchType);

        List<ExceptionTableEntry> table = new ArrayList<>();
        table.add(entry);

        CodeAttribute code = new CodeAttribute(
                1,
                1,
                nops(2),
                (Segment) null,
                emptyOperandManager(),
                table
        );

        // The same offsets list (identity mapping)
        List<Integer> sameOffsets = new ArrayList<>(code.byteCodeOffsets);

        // Act
        code.renumber(sameOffsets);

        // Assert
        // With identity mapping, the PCs should remain the same.
        assertEquals(0, code.exceptionTable.get(0).getStartPC());
        assertEquals(1, code.exceptionTable.get(0).getEndPC());
        assertEquals(1, code.exceptionTable.get(0).getHandlerPC());
    }

    @Test
    public void resolve_and_writeBody_doNotThrow_forSimpleCase() throws Exception {
        // Arrange
        CodeAttribute code = newCodeWithNops(1, 1, 1);
        ClassConstantPool pool = new ClassConstantPool();
        pool.resolve((Segment) null);

        // Act
        code.resolve(pool); // should be a no-op for NOP-only code with no nested entries

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(out);
        code.writeBody(dos);
        dos.flush();

        // Assert
        assertTrue(out.toByteArray().length > 0);
    }

    @Test(expected = NullPointerException.class)
    public void constructor_nullExceptionTable_isAllowedButSomeQueriesMayThrow() throws Exception {
        // Arrange
        CodeAttribute code = new CodeAttribute(
                1, 1, nops(1), (Segment) null, emptyOperandManager(), null
        );

        // Act
        // Some implementations may expect a non-null table in getStartPCs (as in older versions).
        code.getStartPCs();

        // Assert handled by expected exception
    }

    @Test
    public void byteCodeOffsets_areEmptyForEmptyCode() throws Exception {
        // Arrange
        CodeAttribute code = new CodeAttribute(
                0, 0, nops(0), (Segment) null, emptyOperandManager(), new ArrayList<ExceptionTableEntry>()
        );

        // Act & Assert
        assertEquals(0, code.codeLength);
        assertTrue(code.byteCodes.isEmpty());
        assertEquals(1, code.byteCodeOffsets.size()); // implementation seeds with 0, then removes-at-end behavior
        assertEquals(Integer.valueOf(0), code.byteCodeOffsets.get(0));

        // Renumber with trivial mapping should not fail
        code.renumber(Collections.singletonList(0));
    }
}