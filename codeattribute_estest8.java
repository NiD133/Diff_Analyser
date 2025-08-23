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

public class CodeAttribute_ESTestTest8 extends CodeAttribute_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        int[] intArray0 = new int[3];
        OperandManager operandManager0 = new OperandManager(intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0);
        byte[] byteArray0 = new byte[8];
        LinkedList<ExceptionTableEntry> linkedList0 = new LinkedList<ExceptionTableEntry>();
        CodeAttribute codeAttribute0 = new CodeAttribute((byte) 0, (byte) 0, byteArray0, (Segment) null, operandManager0, linkedList0);
        ClassConstantPool classConstantPool0 = new ClassConstantPool();
        // Undeclared exception!
        try {
            codeAttribute0.resolve(classConstantPool0);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            //
            // Constant pool is not yet resolved; this does not make any sense
            //
            verifyException("org.apache.commons.compress.harmony.unpack200.bytecode.ClassConstantPool", e);
        }
    }
}
