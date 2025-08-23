package org.apache.commons.jxpath;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.List;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.VariablePointer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class BasicNodeSet_ESTestTest1 extends BasicNodeSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        BasicVariables basicVariables0 = new BasicVariables();
        QName qName0 = new QName("5>?Fr~\"n B, <j");
        VariablePointer variablePointer0 = new VariablePointer(basicVariables0, qName0);
        variablePointer0.setIndex(1805);
        BasicNodeSet basicNodeSet0 = new BasicNodeSet();
        basicNodeSet0.add((Pointer) variablePointer0);
        // Undeclared exception!
        try {
            basicNodeSet0.toString();
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // No such variable: '5>?Fr~\"n B, <j'
            //
            verifyException("org.apache.commons.jxpath.BasicVariables", e);
        }
    }
}
