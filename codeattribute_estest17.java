package org.apache.commons.compress.harmony.unpack200.bytecode;

import org.apache.commons.compress.harmony.unpack200.Segment;
import org.junit.Test;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * This test verifies that the {@link CodeAttribute#writeBody(DataOutputStream)} method throws an
 * {@link IllegalStateException} if it contains a nested attribute that has not been resolved
 * against the class's constant pool.
 */
public class CodeAttributeTest {

    @Test
    public void writeBodyShouldThrowIllegalStateExceptionIfNestedAttributeIsNotResolved() throws IOException {
        // --- Arrange ---

        // 1. Create a CodeAttribute with minimal valid dependencies.
        final byte[] packedCode = new byte[1]; // The actual byte code is not relevant for this test.
        final OperandManager operandManager = new OperandManager(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        final List<ExceptionTableEntry> exceptionTable = Collections.emptyList();
        final CodeAttribute codeAttribute = new CodeAttribute(10, 10, packedCode, null, operandManager, exceptionTable);

        // 2. Create a constant pool and resolve the main CodeAttribute against it.
        final ClassConstantPool constantPool = new ClassConstantPool();
        codeAttribute.resolve(constantPool);

        // 3. Create a nested attribute whose constant pool entry is *not* added to the constant pool.
        // This makes the entry "unresolved".
        final CPUTF8 unresolvedUtf8Entry = new CPUTF8("SourceFile.java");
        final SourceFileAttribute attributeWithUnresolvedEntry = new SourceFileAttribute(unresolvedUtf8Entry);
        codeAttribute.addAttribute(attributeWithUnresolvedEntry);

        // 4. Prepare a dummy output stream.
        final DataOutputStream dataOutputStream = new DataOutputStream(OutputStream.nullOutputStream());

        // --- Act & Assert ---

        // The call to writeBody should fail because the nested SourceFileAttribute
        // refers to a CPUTF8 entry that was never indexed in the constant pool.
        final IllegalStateException thrown = assertThrows(
            IllegalStateException.class,
            () -> codeAttribute.writeBody(dataOutputStream)
        );

        assertEquals("Entry has not been resolved", thrown.getMessage());
    }
}