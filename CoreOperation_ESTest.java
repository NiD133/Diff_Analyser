package org.apache.commons.jxpath.ri.compiler;

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
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class CoreOperation_ESTest extends CoreOperation_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testCoreOperationUnionToStringReturnsEmptyString() throws Throwable {
        Expression[] expressionArray = new Expression[0];
        CoreOperationUnion coreOperationUnion = new CoreOperationUnion(expressionArray);
        String result = coreOperationUnion.toString();
        assertEquals("", result);
    }

    @Test(timeout = 4000)
    public void testCoreOperationLessThanOrEqualSymbol() throws Throwable {
        CoreOperationGreaterThan greaterThan = new CoreOperationGreaterThan(null, null);
        CoreOperationLessThanOrEqual lessThanOrEqual = new CoreOperationLessThanOrEqual(greaterThan, null);
        String symbol = lessThanOrEqual.getSymbol();
        assertEquals("<=", symbol);
    }

    @Test(timeout = 4000)
    public void testUnionContextChildOrdering() throws Throwable {
        Expression[] expressionArray = new Expression[0];
        CoreOperationUnion coreOperationUnion = new CoreOperationUnion(expressionArray);
        JXPathContextReferenceImpl contextReference = (JXPathContextReferenceImpl) JXPathContext.newContext(coreOperationUnion);
        VariablePointer variablePointer = new VariablePointer(null);
        RootContext rootContext = new RootContext(contextReference, variablePointer);
        InitialContext initialContext = (InitialContext) rootContext.getConstantContext(null);
        UnionContext unionContext = (UnionContext) coreOperationUnion.computeValue(initialContext);
        assertFalse(unionContext.isChildOrderingRequired());
    }

    @Test(timeout = 4000)
    public void testComputeUnionContext() throws Throwable {
        Expression[] expressionArray = new Expression[0];
        CoreOperationUnion coreOperationUnion = new CoreOperationUnion(expressionArray);
        RootContext rootContext = new RootContext(null, null);
        NodeTypeTest nodeTypeTest = new NodeTypeTest(22);
        PrecedingOrFollowingContext precedingContext = new PrecedingOrFollowingContext(rootContext, nodeTypeTest, false);
        SelfContext selfContext = new SelfContext(precedingContext, nodeTypeTest);
        QName qName = new QName("");
        NodeNameTest nodeNameTest = new NodeNameTest(qName);
        ParentContext parentContext = new ParentContext(selfContext, nodeNameTest);
        UnionContext unionContext = (UnionContext) coreOperationUnion.compute(parentContext);
        assertFalse(unionContext.isChildOrderingRequired());
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testNameAttributeTestToStringThrowsNullPointerException() throws Throwable {
        NameAttributeTest nameAttributeTest = new NameAttributeTest(null, null);
        nameAttributeTest.toString();
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testCoreOperationSubtractComputeValueThrowsException() throws Throwable {
        Expression[] expressionArray = new Expression[2];
        CoreOperationAnd coreOperationAnd = new CoreOperationAnd(expressionArray);
        expressionArray[0] = coreOperationAnd;
        CoreOperationSubtract coreOperationSubtract = new CoreOperationSubtract(coreOperationAnd, expressionArray[1]);
        coreOperationSubtract.computeValue(null);
    }

    @Test(timeout = 4000, expected = RuntimeException.class)
    public void testCoreOperationAddComputeThrowsRuntimeException() throws Throwable {
        Expression[] expressionArray = new Expression[9];
        CoreFunction coreFunction = new CoreFunction(15, expressionArray);
        CoreOperationMultiply coreOperationMultiply = new CoreOperationMultiply(coreFunction, coreFunction);
        expressionArray[0] = coreOperationMultiply;
        CoreOperationAdd coreOperationAdd = new CoreOperationAdd(expressionArray);
        coreOperationAdd.compute(null);
    }

    @Test(timeout = 4000, expected = ArrayIndexOutOfBoundsException.class)
    public void testNameAttributeTestComputeThrowsArrayIndexOutOfBoundsException() throws Throwable {
        NameAttributeTest nameAttributeTest = new NameAttributeTest(null, null);
        NameAttributeTest nameAttributeTest1 = new NameAttributeTest(nameAttributeTest, nameAttributeTest);
        Expression[] expressionArray = new Expression[0];
        nameAttributeTest1.args = expressionArray;
        nameAttributeTest1.compute(null);
    }

    @Test(timeout = 4000, expected = ArithmeticException.class)
    public void testCoreOperationModComputeThrowsArithmeticException() throws Throwable {
        Expression[] expressionArray = new Expression[0];
        CoreOperationAnd coreOperationAnd = new CoreOperationAnd(expressionArray);
        CoreOperationMod coreOperationMod = new CoreOperationMod(coreOperationAnd, coreOperationAnd);
        coreOperationMod.compute(null);
    }

    @Test(timeout = 4000)
    public void testCoreOperationGreaterThanOrEqualToString() throws Throwable {
        CoreFunction coreFunction = new CoreFunction(-716, null);
        CoreOperationGreaterThan greaterThan = new CoreOperationGreaterThan(coreFunction, coreFunction);
        CoreOperationLessThan lessThan = new CoreOperationLessThan(greaterThan, coreFunction);
        CoreOperationGreaterThanOrEqual greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(lessThan, lessThan);
        String result = greaterThanOrEqual.toString();
        assertNotNull(result);
    }

    @Test(timeout = 4000)
    public void testNameAttributeTestToString() throws Throwable {
        NameAttributeTest nameAttributeTest = new NameAttributeTest(null, null);
        NameAttributeTest nameAttributeTest1 = new NameAttributeTest(nameAttributeTest, nameAttributeTest);
        String result = nameAttributeTest1.toString();
        assertNotNull(result);
    }

    @Test(timeout = 4000, expected = RuntimeException.class)
    public void testCoreOperationUnionComputeValueThrowsRuntimeException() throws Throwable {
        Expression[] expressionArray = new Expression[8];
        CoreFunction coreFunction = new CoreFunction(16, expressionArray);
        CoreOperationLessThanOrEqual lessThanOrEqual = new CoreOperationLessThanOrEqual(coreFunction, coreFunction);
        CoreOperationNegate negate = new CoreOperationNegate(lessThanOrEqual);
        Expression[] expressionArray1 = new Expression[6];
        expressionArray1[0] = coreFunction;
        CoreOperationUnion coreOperationUnion = new CoreOperationUnion(expressionArray1);
        QName qName = new QName("");
        VariableReference variableReference = new VariableReference(qName);
        CoreOperationMultiply multiply = new CoreOperationMultiply(negate, variableReference);
        expressionArray[6] = multiply;
        coreOperationUnion.computeValue(null);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testCoreOperationUnionComputeValueThrowsNullPointerException() throws Throwable {
        Expression[] expressionArray = new Expression[8];
        CoreFunction coreFunction = new CoreFunction(16, expressionArray);
        CoreOperationLessThanOrEqual lessThanOrEqual = new CoreOperationLessThanOrEqual(coreFunction, coreFunction);
        CoreOperationNegate negate = new CoreOperationNegate(lessThanOrEqual);
        Expression[] expressionArray1 = new Expression[6];
        expressionArray1[0] = coreFunction;
        expressionArray1[1] = negate;
        CoreOperationUnion coreOperationUnion = new CoreOperationUnion(expressionArray1);
        expressionArray[1] = coreOperationUnion;
        coreOperationUnion.computeValue(null);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testNameAttributeTestComputeThrowsNullPointerException() throws Throwable {
        NameAttributeTest nameAttributeTest = new NameAttributeTest(null, null);
        nameAttributeTest.compute(null);
    }
}