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

public class SimplePathInterpreter_ESTestTest6 extends SimplePathInterpreter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        Expression[] expressionArray0 = new Expression[0];
        CoreOperationUnion coreOperationUnion0 = new CoreOperationUnion(expressionArray0);
        CoreOperationMultiply coreOperationMultiply0 = new CoreOperationMultiply(coreOperationUnion0, coreOperationUnion0);
        PredicateContext predicateContext0 = new PredicateContext((EvalContext) null, coreOperationMultiply0);
        QName qName0 = new QName((String) null, "");
        BasicVariables basicVariables0 = new BasicVariables();
        VariablePointer variablePointer0 = new VariablePointer(basicVariables0, qName0);
        NodePointer nodePointer0 = NodePointer.newChildNodePointer(variablePointer0, qName0, qName0);
        NullPropertyPointer nullPropertyPointer0 = new NullPropertyPointer(nodePointer0);
        Locale locale0 = Locale.KOREA;
        NodePointer nodePointer1 = NodePointer.newNodePointer(qName0, nullPropertyPointer0, locale0);
        Step[] stepArray0 = new Step[4];
        NodeNameTest nodeNameTest0 = new NodeNameTest(qName0);
        Step step0 = mock(Step.class, new ViolatedAssumptionAnswer());
        doReturn(5, 0).when(step0).getAxis();
        doReturn(nodeNameTest0).when(step0).getNodeTest();
        doReturn((Object) expressionArray0, (Object) null).when(step0).getPredicates();
        stepArray0[0] = step0;
        stepArray0[1] = step0;
        stepArray0[2] = step0;
        stepArray0[3] = step0;
        // Undeclared exception!
        try {
            SimplePathInterpreter.interpretSimpleExpressionPath(predicateContext0, nodePointer1, expressionArray0, stepArray0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }
}
