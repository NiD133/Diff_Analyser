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

public class SimplePathInterpreter_ESTestTest28 extends SimplePathInterpreter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test27() throws Throwable {
        Locale locale0 = Locale.JAPANESE;
        QName qName0 = new QName("<<unknown namespace>>");
        NodePointer nodePointer0 = NodePointer.newNodePointer(qName0, qName0, locale0);
        JXPathContextReferenceImpl jXPathContextReferenceImpl0 = new JXPathContextReferenceImpl((JXPathContext) null, nodePointer0, nodePointer0);
        Constant constant0 = new Constant("string");
        NameAttributeTest nameAttributeTest0 = new NameAttributeTest(constant0, constant0);
        RootContext rootContext0 = new RootContext(jXPathContextReferenceImpl0, nodePointer0);
        Expression[] expressionArray0 = new Expression[2];
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual0 = new CoreOperationGreaterThanOrEqual(nameAttributeTest0, constant0);
        expressionArray0[0] = (Expression) coreOperationGreaterThanOrEqual0;
        VariableReference variableReference0 = new VariableReference(qName0);
        expressionArray0[1] = (Expression) variableReference0;
        // Undeclared exception!
        try {
            SimplePathInterpreter.interpretSimpleExpressionPath(rootContext0, nodePointer0, expressionArray0, (Step[]) null);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            //
            // Undefined variable: <<unknown namespace>>
            //
            verifyException("org.apache.commons.jxpath.ri.model.VariablePointer$1", e);
        }
    }
}
