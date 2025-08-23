package org.apache.commons.jxpath.ri.axes;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.Locale;
import org.apache.commons.jxpath.BasicVariables;
import org.apache.commons.jxpath.JXPathBasicBeanInfo;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.JXPathContextReferenceImpl;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.Constant;
import org.apache.commons.jxpath.ri.compiler.CoreFunction;
import org.apache.commons.jxpath.ri.compiler.CoreOperationAnd;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationMod;
import org.apache.commons.jxpath.ri.compiler.CoreOperationMultiply;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNegate;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationOr;
import org.apache.commons.jxpath.ri.compiler.CoreOperationSubtract;
import org.apache.commons.jxpath.ri.compiler.CoreOperationUnion;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.NameAttributeTest;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.NodeTest;
import org.apache.commons.jxpath.ri.compiler.NodeTypeTest;
import org.apache.commons.jxpath.ri.compiler.ProcessingInstructionTest;
import org.apache.commons.jxpath.ri.compiler.Step;
import org.apache.commons.jxpath.ri.compiler.VariableReference;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.VariablePointer;
import org.apache.commons.jxpath.ri.model.beans.BeanPointer;
import org.apache.commons.jxpath.ri.model.beans.BeanPropertyPointer;
import org.apache.commons.jxpath.ri.model.beans.NullPointer;
import org.apache.commons.jxpath.ri.model.beans.NullPropertyPointer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class SimplePathInterpreter_ESTestTest8 extends SimplePathInterpreter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        Locale locale0 = Locale.JAPANESE;
        NullPointer nullPointer0 = new NullPointer(locale0, "");
        JXPathContext jXPathContext0 = JXPathContext.newContext((Object) nullPointer0);
        JXPathContext jXPathContext1 = JXPathContext.newContext(jXPathContext0, (Object) jXPathContext0);
        JXPathContextReferenceImpl jXPathContextReferenceImpl0 = new JXPathContextReferenceImpl(jXPathContext1, jXPathContext1, nullPointer0);
        RootContext rootContext0 = new RootContext(jXPathContextReferenceImpl0, nullPointer0);
        rootContext0.getContextNodePointer();
        Constant constant0 = new Constant(Integer.MIN_VALUE);
        Step[] stepArray0 = new Step[2];
        Expression[] expressionArray0 = new Expression[8];
        expressionArray0[0] = (Expression) constant0;
        expressionArray0[1] = (Expression) constant0;
        expressionArray0[2] = (Expression) constant0;
        expressionArray0[3] = (Expression) constant0;
        expressionArray0[4] = (Expression) constant0;
        expressionArray0[5] = (Expression) constant0;
        expressionArray0[6] = (Expression) constant0;
        expressionArray0[7] = (Expression) constant0;
        Step step0 = mock(Step.class, new ViolatedAssumptionAnswer());
        doReturn(Integer.MIN_VALUE, 0).when(step0).getAxis();
        doReturn((Object) expressionArray0, (Object) null).when(step0).getPredicates();
        stepArray0[0] = step0;
        stepArray0[1] = step0;
        // Undeclared exception!
        try {
            SimplePathInterpreter.createNullPointer((EvalContext) null, nullPointer0, stepArray0, 0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }
}
