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

public class CodeAttribute_ESTestTest17 extends CodeAttribute_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test16() throws Throwable {
        byte[] byteArray0 = new byte[10];
        OperandManager operandManager0 = new OperandManager((int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null);
        ClassConstantPool classConstantPool0 = new ClassConstantPool();
        classConstantPool0.resolve((Segment) null);
        LinkedList<ExceptionTableEntry> linkedList0 = new LinkedList<ExceptionTableEntry>();
        CodeAttribute codeAttribute0 = new CodeAttribute(158, 158, byteArray0, (Segment) null, operandManager0, linkedList0);
        codeAttribute0.resolve(classConstantPool0);
        CPUTF8 cPUTF8_0 = new CPUTF8("gp");
        SourceFileAttribute sourceFileAttribute0 = new SourceFileAttribute(cPUTF8_0);
        codeAttribute0.addAttribute(sourceFileAttribute0);
        MockFileOutputStream mockFileOutputStream0 = new MockFileOutputStream("gp", false);
        DataOutputStream dataOutputStream0 = new DataOutputStream(mockFileOutputStream0);
        // Undeclared exception!
        try {
            codeAttribute0.writeBody(dataOutputStream0);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            //
            // Entry has not been resolved
            //
            verifyException("org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry", e);
        }
    }
}
