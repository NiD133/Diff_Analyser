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

public class CodeAttribute_ESTestTest20 extends CodeAttribute_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test19() throws Throwable {
        int[] intArray0 = new int[0];
        OperandManager operandManager0 = new OperandManager(intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0);
        byte[] byteArray0 = new byte[1];
        LinkedList<ExceptionTableEntry> linkedList0 = new LinkedList<ExceptionTableEntry>();
        CPUTF8 cPUTF8_0 = new CPUTF8("ZPf8xB8qNlg", 1526);
        CPClass cPClass0 = new CPClass(cPUTF8_0, (byte) 8);
        ExceptionTableEntry exceptionTableEntry0 = new ExceptionTableEntry(1526, 1526, 1526, cPClass0);
        linkedList0.add(exceptionTableEntry0);
        CodeAttribute codeAttribute0 = new CodeAttribute((byte) 0, (byte) 0, byteArray0, (Segment) null, operandManager0, linkedList0);
        ClassFileEntry[] classFileEntryArray0 = codeAttribute0.getNestedClassFileEntries();
        assertEquals(0, codeAttribute0.maxStack);
        assertEquals(1, codeAttribute0.codeLength);
        assertEquals(3, classFileEntryArray0.length);
        assertEquals(0, codeAttribute0.maxLocals);
    }
}
