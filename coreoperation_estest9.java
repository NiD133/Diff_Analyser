package org.apache.commons.jxpath.ri.compiler;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.JXPathContextReferenceImpl;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.ParentContext;
import org.apache.commons.jxpath.ri.axes.PrecedingOrFollowingContext;
import org.apache.commons.jxpath.ri.axes.RootContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.axes.UnionContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.VariablePointer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class CoreOperation_ESTestTest9 extends CoreOperation_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        Expression[] expressionArray0 = new Expression[0];
        CoreOperationAnd coreOperationAnd0 = new CoreOperationAnd(expressionArray0);
        CoreOperationMod coreOperationMod0 = new CoreOperationMod(coreOperationAnd0, coreOperationAnd0);
        // Undeclared exception!
        try {
            coreOperationMod0.compute((EvalContext) null);
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }
}
