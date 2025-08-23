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

public class BasicNodeSet_ESTestTest7 extends BasicNodeSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        BasicNodeSet basicNodeSet0 = new BasicNodeSet();
        BasicVariables basicVariables0 = new BasicVariables();
        QName qName0 = new QName("", "");
        VariablePointer variablePointer0 = new VariablePointer(basicVariables0, qName0);
        basicNodeSet0.add((Pointer) variablePointer0);
        // Undeclared exception!
        try {
            basicNodeSet0.getNodes();
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // No such variable: ':'
            //
            verifyException("org.apache.commons.jxpath.BasicVariables", e);
        }
    }
}
