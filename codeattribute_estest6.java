package org.apache.commons.compress.harmony.unpack200.bytecode;

import org.apache.commons.compress.harmony.pack200.Pack200Exception;
import org.apache.commons.compress.harmony.unpack200.Segment;
import org.junit.Test;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PipedOutputStream;
import java.util.Collections;
import java.util.List;

/**
 * This test class contains tests for the {@link CodeAttribute} class.
 * This particular test focuses on I/O exception handling.
 */
public class CodeAttribute_ESTestTest6 extends CodeAttribute_ESTest_scaffolding {

    /**
     * Tests that {@link CodeAttribute#writeBody(DataOutputStream)} correctly propagates an
     * {@link IOException} when the underlying output stream fails.
     *
     * @throws Pack200Exception if the CodeAttribute cannot be constructed, which would indicate a
     *                          problem in the test setup rather than the tested method.
     */
    @Test(expected = IOException.class)
    public void writeBodyShouldPropagateIOExceptionOnStreamError() throws Pack200Exception {
        // ARRANGE: Set up a CodeAttribute instance and a stream designed to fail.

        // 1. Define minimal data needed to construct a CodeAttribute.
        // The specific values are not important as we only want to test I/O behavior.
        final int maxStack = 0;
        final int maxLocals = 0;
        final byte[] packedCode = new byte[1];
        final List<ExceptionTableEntry> exceptionTable = Collections.emptyList();

        // The OperandManager constructor requires 21 integer arrays. We can use a single
        // dummy array for all parameters since their content is irrelevant for this test.
        final int[] dummyOperandData = new int[1];
        final OperandManager operandManager = new OperandManager(
            dummyOperandData, dummyOperandData, dummyOperandData, dummyOperandData,
            dummyOperandData, dummyOperandData, dummyOperandData, dummyOperandData,
            dummyOperandData, dummyOperandData, dummyOperandData, dummyOperandData,
            dummyOperandData, dummyOperandData, dummyOperandData, dummyOperandData,
            dummyOperandData, dummyOperandData, dummyOperandData, dummyOperandData,
            dummyOperandData
        );

        final CodeAttribute codeAttribute = new CodeAttribute(
            maxStack, maxLocals, packedCode, null, operandManager, exceptionTable
        );

        // 2. Create a DataOutputStream that is guaranteed to throw an IOException on write.
        // An unconnected PipedOutputStream serves this purpose perfectly.
        final PipedOutputStream unconnectedPipe = new PipedOutputStream();
        final DataOutputStream failingStream = new DataOutputStream(unconnectedPipe);

        // ACT: Attempt to write the attribute's body to the failing stream.
        codeAttribute.writeBody(failingStream);

        // ASSERT: The test is expected to throw an IOException.
        // The @Test(expected = IOException.class) annotation handles this assertion.
        // If no exception is thrown, the test will fail.
    }
}