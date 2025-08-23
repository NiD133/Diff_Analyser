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

public class CoreOperation_ESTestTest6 extends CoreOperation_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        Expression[] expressionArray0 = new Expression[2];
        CoreOperationAnd coreOperationAnd0 = new CoreOperationAnd(expressionArray0);
        expressionArray0[0] = (Expression) coreOperationAnd0;
        CoreOperationSubtract coreOperationSubtract0 = new CoreOperationSubtract(coreOperationAnd0, expressionArray0[1]);
        // Undeclared exception!
        coreOperationSubtract0.computeValue((EvalContext) null);
    }
}
