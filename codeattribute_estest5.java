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

public class CodeAttribute_ESTestTest5 extends CodeAttribute_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        byte[] byteArray0 = new byte[1];
        int[] intArray0 = new int[1];
        OperandManager operandManager0 = new OperandManager(intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, (int[]) null, (int[]) null, (int[]) null, (int[]) null, intArray0, (int[]) null, (int[]) null, (int[]) null, (int[]) null, (int[]) null, intArray0, intArray0);
        LinkedList<ExceptionTableEntry> linkedList0 = new LinkedList<ExceptionTableEntry>();
        CodeAttribute codeAttribute0 = new CodeAttribute(3668, 3534, byteArray0, (Segment) null, operandManager0, linkedList0);
        ClassConstantPool classConstantPool0 = new ClassConstantPool();
        classConstantPool0.resolve((Segment) null);
        codeAttribute0.resolve(classConstantPool0);
        codeAttribute0.addAttribute((Attribute) null);
        MockFileOutputStream mockFileOutputStream0 = new MockFileOutputStream("fconst_1", true);
        DataOutputStream dataOutputStream0 = new DataOutputStream(mockFileOutputStream0);
        // Undeclared exception!
        try {
            codeAttribute0.writeBody(dataOutputStream0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.commons.compress.harmony.unpack200.bytecode.CodeAttribute", e);
        }
    }
}