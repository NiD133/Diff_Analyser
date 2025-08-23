package org.apache.commons.compress.harmony.unpack200.bytecode;

import org.apache.commons.compress.harmony.pack200.Pack200Exception;
import org.apache.commons.compress.harmony.unpack200.Segment;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

/**
 * Test cases for {@link CodeAttribute}.
 * This improved version focuses on clarity and maintainability.
 */
public class CodeAttributeTest {

    /**
     * Tests that {@link CodeAttribute#getNestedClassFileEntries()} throws a NullPointerException
     * when its exception table contains a null entry. The method is expected to iterate over
     * the table and access each entry, which fails when an entry is null.
     */
    @Test(expected = NullPointerException.class)
    public void getNestedClassFileEntriesShouldThrowNPEWhenExceptionTableContainsNull() throws Pack200Exception {
        // Arrange
        // Create an exception table list containing a single null entry.
        // This is the specific condition that should trigger the NullPointerException.
        final List<ExceptionTableEntry> exceptionTableWithNull = Collections.singletonList(null);

        // The CodeAttribute constructor requires an OperandManager and a code byte array.
        // Their specific contents are not relevant to this test, so we provide minimal, valid instances.
        final OperandManager operandManager = createEmptyOperandManager();
        final byte[] packedCode = new byte[1]; // A minimal, non-empty code array.

        final CodeAttribute codeAttribute = new CodeAttribute(
            0, // maxStack
            0, // maxLocals
            packedCode,
            null, // segment is not used in this path
            operandManager,
            exceptionTableWithNull
        );

        // Act & Assert
        // This call is expected to throw a NullPointerException because the implementation
        // will attempt to call a method on the null entry within the exception table.
        // The @Test(expected=...) annotation handles the assertion.
        codeAttribute.getNestedClassFileEntries();
    }

    /**
     * Creates a default OperandManager with empty operand arrays.
     * <p>
     * This helper method encapsulates the verbose construction of an OperandManager,
     * which is required by the CodeAttribute constructor but whose internal state
     * is not relevant for this test.
     * </p>
     * @return A new OperandManager instance initialized with empty arrays.
     */
    private OperandManager createEmptyOperandManager() {
        final int[] empty = new int[0];
        // The OperandManager constructor requires 21 integer arrays.
        return new OperandManager(
            empty, empty, empty, empty, empty, empty, empty, empty, empty, empty,
            empty, empty, empty, empty, empty, empty, empty, empty, empty, empty, empty
        );
    }
}