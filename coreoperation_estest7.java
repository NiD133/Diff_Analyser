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

public class CoreOperation_ESTestTest7 extends CoreOperation_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        Expression[] expressionArray0 = new Expression[9];
        CoreFunction coreFunction0 = new CoreFunction(15, expressionArray0);
        CoreOperationMultiply coreOperationMultiply0 = new CoreOperationMultiply(coreFunction0, coreFunction0);
        expressionArray0[0] = (Expression) coreOperationMultiply0;
        CoreOperationAdd coreOperationAdd0 = new CoreOperationAdd(expressionArray0);
        // Undeclared exception!
        try {
            coreOperationAdd0.compute((EvalContext) null);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            //
            // Incorrect number of arguments: string-length(org.apache.commons.jxpath.ri.compiler.CoreFunction@0000000001 * org.apache.commons.jxpath.ri.compiler.CoreFunction@0000000001, null, null, null, null, null, null, null, null)
            //
            verifyException("org.apache.commons.jxpath.ri.compiler.CoreFunction", e);
        }
    }
}
