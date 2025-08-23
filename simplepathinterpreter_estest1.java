package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.JXPathContextReferenceImpl;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.Constant;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.Step;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.BeanPointer;
import org.junit.Test;

import java.util.Locale;

/**
 * Contains tests for the {@link SimplePathInterpreter} class.
 */
public class SimplePathInterpreterTest {

    /**
     * Verifies that interpretSimpleExpressionPath throws a NullPointerException
     * when the provided array of steps contains a null element. This ensures
     * the method correctly handles invalid input and prevents unexpected runtime failures.
     */
    @Test(expected = NullPointerException.class)
    public void interpretSimpleExpressionPathShouldThrowNPEForNullStep() {
        // ARRANGE: Set up the necessary context, pointers, and arguments for the method call.
        JXPathContextReferenceImpl jxpathContext =
                (JXPathContextReferenceImpl) JXPathContext.newContext(new Object());

        QName nodeName = new QName("root");
        BeanPointer rootPointer = (BeanPointer) NodePointer.newNodePointer(nodeName, jxpathContext, Locale.getDefault());
        RootContext rootContext = new RootContext(jxpathContext, rootPointer);

        // Predicates are required by the method signature, but their content is not
        // relevant for triggering the exception in this test.
        Expression[] predicates = new Expression[]{
                new Constant("any-predicate")
        };

        // Create an array of Step objects containing a null element. This is the
        // specific condition that should cause the NullPointerException.
        Step[] stepsWithNull = new Step[1];

        // ACT & ASSERT: Call the method and expect a NullPointerException.
        // The @Test(expected) annotation handles the assertion.
        SimplePathInterpreter.interpretSimpleExpressionPath(rootContext, rootPointer, predicates, stepsWithNull);
    }
}