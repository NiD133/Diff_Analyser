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

public class CoreOperation_ESTestTest13 extends CoreOperation_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        Expression[] expressionArray0 = new Expression[8];
        CoreFunction coreFunction0 = new CoreFunction(16, expressionArray0);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual0 = new CoreOperationLessThanOrEqual(coreFunction0, coreFunction0);
        CoreOperationNegate coreOperationNegate0 = new CoreOperationNegate(coreOperationLessThanOrEqual0);
        Expression[] expressionArray1 = new Expression[6];
        expressionArray1[0] = (Expression) coreFunction0;
        CoreOperationUnion coreOperationUnion0 = new CoreOperationUnion(expressionArray1);
        QName qName0 = new QName("");
        VariableReference variableReference0 = new VariableReference(qName0);
        CoreOperationMultiply coreOperationMultiply0 = new CoreOperationMultiply(coreOperationNegate0, variableReference0);
        expressionArray0[6] = (Expression) coreOperationMultiply0;
        // Undeclared exception!
        try {
            coreOperationUnion0.computeValue((EvalContext) null);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            //
            // Incorrect number of arguments: normalize-space(null, null, null, null, null, null, org.apache.commons.jxpath.ri.compiler.CoreOperationNegate@0000000003 * org.apache.commons.jxpath.ri.compiler.VariableReference@0000000006, null)
            //
            verifyException("org.apache.commons.jxpath.ri.compiler.CoreFunction", e);
        }
    }
}