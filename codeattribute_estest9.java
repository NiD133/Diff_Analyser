package org.apache.commons.compress.harmony.unpack200.bytecode;

import org.apache.commons.compress.harmony.pack200.Pack200Exception;
import org.apache.commons.compress.harmony.unpack200.Segment;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

/**
 * This class contains tests for the {@link CodeAttribute} class.
 * This specific test was improved for understandability from an auto-generated version.
 */
public class CodeAttribute_ESTestTest9 extends CodeAttribute_ESTest_scaffolding {

    /**
     * Tests that calling renumber() on a CodeAttribute instance that was constructed
     * with a null exception table throws a NullPointerException. The renumber() method
     * attempts to iterate over this table without a null check.
     */
    @Test(expected = NullPointerException.class)
    public void renumberThrowsNPEWhenConstructedWithNullExceptionTable() throws Pack200Exception {
        // Arrange: Set up the minimal objects required to construct a CodeAttribute.

        // The OperandManager constructor requires 21 non-null int arrays.
        // Their contents are irrelevant for this test, so empty arrays are sufficient.
        final int[] emptyOperands = new int[0];
        final OperandManager operandManager = new OperandManager(
            emptyOperands, emptyOperands, emptyOperands, emptyOperands, emptyOperands,
            emptyOperands, emptyOperands, emptyOperands, emptyOperands, emptyOperands,
            emptyOperands, emptyOperands, emptyOperands, emptyOperands, emptyOperands,
            emptyOperands, emptyOperands, emptyOperands, emptyOperands, emptyOperands,
            emptyOperands
        );

        // The constructor also requires a non-null byte array for the packed code.
        // An empty array prevents the constructor from doing any complex bytecode processing.
        final byte[] packedCode = new byte[0];

        // Create a CodeAttribute with a null exception table, which is the specific condition under test.
        final CodeAttribute codeAttribute = new CodeAttribute(
            0, // maxStack (value doesn't matter for this test)
            0, // maxLocals (value doesn't matter for this test)
            packedCode,
            null, // segment
            operandManager,
            null  // exceptionTable -> This is the cause of the expected NPE.
        );

        // Act: Call the method under test.
        // The renumber() method will attempt to iterate over the null exception table field.
        // The list passed as an argument here is not the cause of the exception.
        codeAttribute.renumber(Collections.emptyList());

        // Assert: The test will pass if a NullPointerException is thrown,
        // as specified by the @Test(expected = ...) annotation.
    }
}