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

public class CodeAttribute_ESTestTest21 extends CodeAttribute_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test20() throws Throwable {
        byte[] byteArray0 = new byte[7];
        OperandManager operandManager0 = new OperandManager((int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null);
        LinkedList<ExceptionTableEntry> linkedList0 = new LinkedList<ExceptionTableEntry>();
        CodeAttribute codeAttribute0 = new CodeAttribute(1, 1, byteArray0, (Segment) null, operandManager0, linkedList0);
        LocalVariableTypeTableAttribute localVariableTypeTableAttribute0 = new LocalVariableTypeTableAttribute(1, (int[]) null, (int[]) null, (CPUTF8[]) null, (CPUTF8[]) null, (int[]) null);
        codeAttribute0.addAttribute(localVariableTypeTableAttribute0);
        assertEquals(1, codeAttribute0.maxLocals);
        assertEquals(0, linkedList0.size());
        assertEquals(1, codeAttribute0.maxStack);
        assertEquals(7, codeAttribute0.codeLength);
    }
}