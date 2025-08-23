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

public class BasicNodeSet_ESTestTest13 extends BasicNodeSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        BasicNodeSet basicNodeSet0 = new BasicNodeSet();
        QName qName0 = new QName("D@-&EQH");
        BasicVariables basicVariables0 = new BasicVariables();
        VariablePointer variablePointer0 = new VariablePointer(basicVariables0, qName0);
        basicNodeSet0.add((Pointer) variablePointer0);
        basicNodeSet0.add((NodeSet) basicNodeSet0);
    }
}
