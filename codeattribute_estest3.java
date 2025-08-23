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

public class CodeAttribute_ESTestTest3 extends CodeAttribute_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        byte[] byteArray0 = new byte[4];
        OperandManager operandManager0 = new OperandManager((int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null);
        LinkedList<ExceptionTableEntry> linkedList0 = new LinkedList<ExceptionTableEntry>();
        CodeAttribute codeAttribute0 = new CodeAttribute(85, 85, byteArray0, (Segment) null, operandManager0, linkedList0);
        ClassConstantPool classConstantPool0 = new ClassConstantPool();
        classConstantPool0.resolve((Segment) null);
        codeAttribute0.resolve(classConstantPool0);
        CodeAttribute codeAttribute1 = new CodeAttribute(39, 100, byteArray0, (Segment) null, operandManager0, codeAttribute0.exceptionTable);
        codeAttribute1.resolve(classConstantPool0);
        codeAttribute0.addAttribute(codeAttribute1);
        assertEquals(0, linkedList0.size());
        assertEquals(39, codeAttribute1.maxStack);
        MockFileOutputStream mockFileOutputStream0 = new MockFileOutputStream("!", true);
        DataOutputStream dataOutputStream0 = new DataOutputStream(mockFileOutputStream0);
        codeAttribute0.writeBody(dataOutputStream0);
        assertEquals(85, codeAttribute0.maxLocals);
        assertEquals(4, codeAttribute0.codeLength);
    }
}