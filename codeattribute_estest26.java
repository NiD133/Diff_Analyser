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

public class CodeAttribute_ESTestTest26 extends CodeAttribute_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test25() throws Throwable {
        int[] intArray0 = new int[0];
        OperandManager operandManager0 = new OperandManager(intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0);
        byte[] byteArray0 = new byte[12];
        LinkedList<ExceptionTableEntry> linkedList0 = new LinkedList<ExceptionTableEntry>();
        CodeAttribute codeAttribute0 = new CodeAttribute((byte) 0, (byte) 0, byteArray0, (Segment) null, operandManager0, linkedList0);
        codeAttribute0.getStartPCs();
        assertEquals(12, codeAttribute0.codeLength);
        assertEquals(0, linkedList0.size());
        assertEquals(0, codeAttribute0.maxStack);
        assertEquals(0, codeAttribute0.maxLocals);
    }
}
