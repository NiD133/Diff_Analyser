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

public class CodeAttribute_ESTestTest4 extends CodeAttribute_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        int[] intArray0 = new int[1];
        OperandManager operandManager0 = new OperandManager(intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0);
        byte[] byteArray0 = new byte[0];
        LinkedList<ExceptionTableEntry> linkedList0 = new LinkedList<ExceptionTableEntry>();
        CodeAttribute codeAttribute0 = new CodeAttribute(939, 1168, byteArray0, (Segment) null, operandManager0, linkedList0);
        CPUTF8[] cPUTF8Array0 = new CPUTF8[1];
        LocalVariableTableAttribute localVariableTableAttribute0 = new LocalVariableTableAttribute((-2518), intArray0, intArray0, cPUTF8Array0, cPUTF8Array0, intArray0);
        codeAttribute0.addAttribute(localVariableTableAttribute0);
        int int0 = codeAttribute0.getLength();
        assertEquals(1168, codeAttribute0.maxLocals);
        assertEquals(939, codeAttribute0.maxStack);
        assertEquals((-25160), int0);
    }
}