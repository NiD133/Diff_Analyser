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

public class BasicNodeSet_ESTestTest6 extends BasicNodeSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        BasicNodeSet basicNodeSet0 = new BasicNodeSet();
        basicNodeSet0.add((Pointer) null);
        // Undeclared exception!
        try {
            basicNodeSet0.getNodes();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("java.util.stream.ReferencePipeline$3$1", e);
        }
    }
}
