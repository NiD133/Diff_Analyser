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

public class CoreOperation_ESTestTest3 extends CoreOperation_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        Expression[] expressionArray0 = new Expression[0];
        CoreOperationUnion coreOperationUnion0 = new CoreOperationUnion(expressionArray0);
        JXPathContextReferenceImpl jXPathContextReferenceImpl0 = (JXPathContextReferenceImpl) JXPathContext.newContext((Object) coreOperationUnion0);
        VariablePointer variablePointer0 = new VariablePointer((QName) null);
        RootContext rootContext0 = new RootContext(jXPathContextReferenceImpl0, variablePointer0);
        InitialContext initialContext0 = (InitialContext) rootContext0.getConstantContext((Object) null);
        UnionContext unionContext0 = (UnionContext) coreOperationUnion0.computeValue(initialContext0);
        assertFalse(unionContext0.isChildOrderingRequired());
    }
}
