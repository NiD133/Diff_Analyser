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

public class CodeAttribute_ESTestTest19 extends CodeAttribute_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test18() throws Throwable {
        byte[] byteArray0 = new byte[10];
        OperandManager operandManager0 = new OperandManager((int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null);
        LinkedList<ExceptionTableEntry> linkedList0 = new LinkedList<ExceptionTableEntry>();
        ExceptionTableEntry exceptionTableEntry0 = new ExceptionTableEntry(25, 25, 25, (CPClass) null);
        linkedList0.addFirst(exceptionTableEntry0);
        CodeAttribute codeAttribute0 = new CodeAttribute(25, 25, byteArray0, (Segment) null, operandManager0, linkedList0);
        ClassFileEntry[] classFileEntryArray0 = codeAttribute0.getNestedClassFileEntries();
        assertEquals(25, codeAttribute0.maxStack);
        assertEquals(11, classFileEntryArray0.length);
        assertEquals(25, codeAttribute0.maxLocals);
        assertEquals(10, codeAttribute0.codeLength);
    }
}