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

public class CodeAttribute_ESTestTest22 extends CodeAttribute_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test21() throws Throwable {
        byte[] byteArray0 = new byte[2];
        int[] intArray0 = new int[6];
        OperandManager operandManager0 = new OperandManager(intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, intArray0, (int[]) null, intArray0, (int[]) null, (int[]) null);
        LinkedList<ExceptionTableEntry> linkedList0 = new LinkedList<ExceptionTableEntry>();
        CodeAttribute codeAttribute0 = new CodeAttribute(97, 97, byteArray0, (Segment) null, operandManager0, linkedList0);
        AnnotationsAttribute.Annotation[] annotationsAttribute_AnnotationArray0 = new AnnotationsAttribute.Annotation[4];
        RuntimeVisibleorInvisibleAnnotationsAttribute runtimeVisibleorInvisibleAnnotationsAttribute0 = new RuntimeVisibleorInvisibleAnnotationsAttribute((CPUTF8) null, annotationsAttribute_AnnotationArray0);
        codeAttribute0.addAttribute(runtimeVisibleorInvisibleAnnotationsAttribute0);
        // Undeclared exception!
        try {
            codeAttribute0.toString();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.commons.compress.harmony.unpack200.bytecode.RuntimeVisibleorInvisibleAnnotationsAttribute", e);
        }
    }
}
