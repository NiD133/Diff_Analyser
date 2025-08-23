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

public class SimplePathInterpreter_ESTestTest11 extends SimplePathInterpreter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        Locale locale0 = Locale.JAPANESE;
        QName qName0 = new QName("[nme)SR=1'");
        NodePointer nodePointer0 = NodePointer.newNodePointer(qName0, locale0, locale0);
        Step[] stepArray0 = new Step[2];
        Expression[] expressionArray0 = new Expression[1];
        CoreFunction coreFunction0 = new CoreFunction(13, expressionArray0);
        CoreOperationSubtract coreOperationSubtract0 = new CoreOperationSubtract(coreFunction0, coreFunction0);
        expressionArray0[0] = (Expression) coreOperationSubtract0;
        Step step0 = mock(Step.class, new ViolatedAssumptionAnswer());
        doReturn(2013265920).when(step0).getAxis();
        doReturn((Object) expressionArray0, (Object) expressionArray0).when(step0).getPredicates();
        stepArray0[0] = step0;
        // Undeclared exception!
        try {
            SimplePathInterpreter.interpretSimpleLocationPath((EvalContext) null, nodePointer0, stepArray0);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            //
            // Incorrect number of arguments: substring-after(org.apache.commons.jxpath.ri.compiler.CoreFunction@0000000003 - org.apache.commons.jxpath.ri.compiler.CoreFunction@0000000003)
            //
            verifyException("org.apache.commons.jxpath.ri.compiler.CoreFunction", e);
        }
    }
}
