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

public class CoreOperation_ESTestTest4 extends CoreOperation_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        Expression[] expressionArray0 = new Expression[0];
        CoreOperationUnion coreOperationUnion0 = new CoreOperationUnion(expressionArray0);
        RootContext rootContext0 = new RootContext((JXPathContextReferenceImpl) null, (NodePointer) null);
        NodeTypeTest nodeTypeTest0 = new NodeTypeTest(22);
        PrecedingOrFollowingContext precedingOrFollowingContext0 = new PrecedingOrFollowingContext(rootContext0, nodeTypeTest0, false);
        SelfContext selfContext0 = new SelfContext(precedingOrFollowingContext0, nodeTypeTest0);
        QName qName0 = new QName("");
        NodeNameTest nodeNameTest0 = new NodeNameTest(qName0);
        ParentContext parentContext0 = new ParentContext(selfContext0, nodeNameTest0);
        UnionContext unionContext0 = (UnionContext) coreOperationUnion0.compute(parentContext0);
        assertFalse(unionContext0.isChildOrderingRequired());
    }
}
