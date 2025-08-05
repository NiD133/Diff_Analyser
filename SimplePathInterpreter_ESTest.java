package org.apache.commons.jxpath.ri.axes;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.Locale;
import org.apache.commons.jxpath.*;
import org.apache.commons.jxpath.ri.*;
import org.apache.commons.jxpath.ri.compiler.*;
import org.apache.commons.jxpath.ri.model.*;
import org.apache.commons.jxpath.ri.model.beans.*;
import org.evosuite.runtime.*;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class SimplePathInterpreterTest extends SimplePathInterpreter_ESTest_scaffolding {

    private static final Locale LOCALE_KOREA = Locale.KOREA;
    private static final Locale LOCALE_JAPANESE = Locale.JAPANESE;
    private static final String UNKNOWN_NAMESPACE = "<<unknown namespace>>";
    private static final QName QNAME_SAMPLE = new QName("#{WI2%=9byI>t{^<");

    @Test(timeout = 4000)
    public void testInterpretSimpleExpressionPathWithNullPointerException() {
        Object object = new Object();
        JXPathContextReferenceImpl context = (JXPathContextReferenceImpl) JXPathContext.newContext(object);
        QName qName = new QName("SampleQName");
        BeanPointer beanPointer = (BeanPointer) NodePointer.newNodePointer(qName, context, LOCALE_KOREA);
        RootContext rootContext = new RootContext(context, beanPointer);
        Step[] steps = new Step[4];
        Expression[] expressions = {new Constant("SampleConstant"), new Constant("SampleConstant")};

        try {
            SimplePathInterpreter.interpretSimpleExpressionPath(rootContext, beanPointer, expressions, steps);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.jxpath.ri.axes.SimplePathInterpreter", e);
        }
    }

    @Test(timeout = 4000)
    public void testInterpretSimpleExpressionPathWithNullPointerExceptionOnNullEvalContext() {
        NullPointer nullPointer = new NullPointer(LOCALE_JAPANESE, "");
        NullPropertyPointer nullPropertyPointer = new NullPropertyPointer(nullPointer);
        Step[] steps = new Step[3];
        Expression[] expressions = {
            new Constant(UNKNOWN_NAMESPACE),
            new CoreOperationSubtract(new Constant(UNKNOWN_NAMESPACE), new Constant(UNKNOWN_NAMESPACE))
        };

        try {
            SimplePathInterpreter.interpretSimpleExpressionPath(null, nullPropertyPointer, expressions, steps);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.jxpath.ri.axes.SimplePathInterpreter", e);
        }
    }

    @Test(timeout = 4000)
    public void testInterpretSimpleLocationPathWithValidNodePointer() {
        NullPointer nullPointer = new NullPointer(LOCALE_JAPANESE, "");
        NodePointer nodePointer = NodePointer.newNodePointer(QNAME_SAMPLE, LOCALE_KOREA, LOCALE_KOREA);
        Expression[] expressions = {new Constant(UNKNOWN_NAMESPACE)};
        Step[] steps = createMockSteps(expressions);

        SimplePathInterpreter.interpretSimpleExpressionPath(null, nodePointer, expressions, steps);
        NodePointer resultNodePointer = SimplePathInterpreter.interpretSimpleLocationPath(null, nullPointer, steps);

        assertEquals(0, resultNodePointer.getIndex());
    }

    @Test(timeout = 4000)
    public void testCreateNullPointerWithNullPointerException() {
        NullPointer nullPointer = new NullPointer(LOCALE_JAPANESE, "");
        Step[] steps = new Step[2];
        Expression[] expressions = {new CoreFunction(13, new Expression[1])};

        try {
            SimplePathInterpreter.createNullPointer(null, nullPointer, steps, 0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.jxpath.ri.axes.SimplePathInterpreter", e);
        }
    }

    @Test(timeout = 4000)
    public void testInterpretSimpleLocationPathWithRuntimeException() {
        NodePointer nodePointer = NodePointer.newNodePointer(QNAME_SAMPLE, LOCALE_JAPAN, LOCALE_JAPAN);
        Step[] steps = new Step[1];
        Expression[] expressions = {new CoreFunction(13, new Expression[1])};

        try {
            SimplePathInterpreter.interpretSimpleLocationPath(null, nodePointer, steps);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.apache.commons.jxpath.ri.compiler.CoreFunction", e);
        }
    }

    private Step[] createMockSteps(Expression[] expressions) {
        Step step = mock(Step.class, new ViolatedAssumptionAnswer());
        doReturn(Integer.MIN_VALUE).when(step).getAxis();
        doReturn(expressions).when(step).getPredicates();
        return new Step[]{step, step};
    }
}