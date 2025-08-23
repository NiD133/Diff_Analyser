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

public class BasicNodeSet_ESTestTest2 extends BasicNodeSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        BasicNodeSet basicNodeSet0 = new BasicNodeSet();
        VariablePointer variablePointer0 = new VariablePointer((QName) null);
        basicNodeSet0.add((Pointer) variablePointer0);
        // Undeclared exception!
        try {
            basicNodeSet0.getValues();
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            //
            // Undefined variable: null
            //
            verifyException("org.apache.commons.jxpath.ri.model.VariablePointer$1", e);
        }
    }
}