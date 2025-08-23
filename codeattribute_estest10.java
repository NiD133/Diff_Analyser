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

public class CodeAttribute_ESTestTest10 extends CodeAttribute_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        int[] intArray0 = new int[0];
        OperandManager operandManager0 = new OperandManager(intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0);
        byte[] byteArray0 = new byte[2];
        LinkedList<ExceptionTableEntry> linkedList0 = new LinkedList<ExceptionTableEntry>();
        ExceptionTableEntry exceptionTableEntry0 = new ExceptionTableEntry((-3254), 1477, 1477, (CPClass) null);
        linkedList0.add(exceptionTableEntry0);
        CodeAttribute codeAttribute0 = new CodeAttribute((byte) 0, (byte) 0, byteArray0, (Segment) null, operandManager0, linkedList0);
        LinkedList<Integer> linkedList1 = new LinkedList<Integer>();
        // Undeclared exception!
        try {
            codeAttribute0.renumber(linkedList1);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //
            // Index: -3254, Size: 0
            //
            verifyException("java.util.LinkedList", e);
        }
    }
}
