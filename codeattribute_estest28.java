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

public class CodeAttribute_ESTestTest28 extends CodeAttribute_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test27() throws Throwable {
        byte[] byteArray0 = new byte[2];
        int[] intArray0 = new int[6];
        OperandManager operandManager0 = new OperandManager(intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, (int[]) null, intArray0, (int[]) null, (int[]) null);
        LinkedList<ExceptionTableEntry> linkedList0 = new LinkedList<ExceptionTableEntry>();
        CodeAttribute codeAttribute0 = new CodeAttribute(97, 97, byteArray0, (Segment) null, operandManager0, linkedList0);
        String string0 = codeAttribute0.toString();
        assertEquals(97, codeAttribute0.maxStack);
        assertEquals("Code: 14 bytes", string0);
        assertEquals(97, codeAttribute0.maxLocals);
    }
}