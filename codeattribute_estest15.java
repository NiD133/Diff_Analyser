package org.apache.commons.compress.harmony.unpack200.bytecode;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PipedOutputStream;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.compress.harmony.unpack200.Segment;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.io.MockFileOutputStream;
import org.junit.runner.RunWith;

public class CodeAttribute_ESTestTest15 extends CodeAttribute_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        int[] intArray0 = new int[16];
        OperandManager operandManager0 = new OperandManager(intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0);
        LinkedList<ExceptionTableEntry> linkedList0 = new LinkedList<ExceptionTableEntry>();
        byte[] byteArray0 = new byte[2];
        byteArray0[0] = (byte) (-93);
        intArray0[0] = (int) (byte) 106;
        CodeAttribute codeAttribute0 = null;
        try {
            codeAttribute0 = new CodeAttribute(1717, 1717, byteArray0, (Segment) null, operandManager0, linkedList0);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //
            // Index: 106, Size: 2
            //
            verifyException("java.util.ArrayList", e);
        }
    }
}
