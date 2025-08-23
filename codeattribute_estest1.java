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

public class CodeAttribute_ESTestTest1 extends CodeAttribute_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        byte[] byteArray0 = new byte[4];
        OperandManager operandManager0 = new OperandManager((int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null);
        LinkedList<ExceptionTableEntry> linkedList0 = new LinkedList<ExceptionTableEntry>();
        CodeAttribute codeAttribute0 = new CodeAttribute((-2553), 85, byteArray0, (Segment) null, operandManager0, linkedList0);
        CPUTF8 cPUTF8_0 = new CPUTF8("i2f", 38);
        CPClass cPClass0 = new CPClass(cPUTF8_0, 85);
        ExceptionTableEntry exceptionTableEntry0 = new ExceptionTableEntry(87, 38, 87, cPClass0);
        linkedList0.add(exceptionTableEntry0);
        int int0 = codeAttribute0.getLength();
        assertEquals(85, codeAttribute0.maxLocals);
        assertEquals((-2553), codeAttribute0.maxStack);
        assertEquals(24, int0);
    }
}
