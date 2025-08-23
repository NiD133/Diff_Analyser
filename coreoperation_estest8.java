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

public class CoreOperation_ESTestTest8 extends CoreOperation_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        NameAttributeTest nameAttributeTest0 = new NameAttributeTest((Expression) null, (Expression) null);
        NameAttributeTest nameAttributeTest1 = new NameAttributeTest(nameAttributeTest0, nameAttributeTest0);
        Expression[] expressionArray0 = new Expression[0];
        nameAttributeTest1.args = expressionArray0;
        // Undeclared exception!
        try {
            nameAttributeTest1.compute((EvalContext) null);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            //
            // 0
            //
            verifyException("org.apache.commons.jxpath.ri.compiler.CoreOperationCompare", e);
        }
    }
}
