package org.apache.commons.jxpath.ri.compiler;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.Locale;
import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.Function;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.JXPathContextReferenceImpl;
import org.apache.commons.jxpath.ri.NamespaceResolver;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.NodeSetContext;
import org.apache.commons.jxpath.ri.axes.RootContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.VariablePointer;
import org.apache.commons.jxpath.ri.model.beans.BeanPointer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class ExtensionFunction_ESTest extends ExtensionFunction_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testGetFunctionNameReturnsNullWhenConstructedWithNull() throws Throwable {
        // Create extension function with null name
        Expression[] args = new Expression[3];
        ExtensionFunction function = new ExtensionFunction(null, args);
        
        // Verify function name is null
        assertNull(function.getFunctionName());
    }

    @Test(timeout = 4000)
    public void testComputeContextDependentAlwaysReturnsTrue() throws Throwable {
        // Create any extension function
        Expression[] args = new Expression[1];
        ExtensionFunction function = new ExtensionFunction(null, args);
        
        // Verify context dependency
        assertTrue(function.computeContextDependent());
    }

    @Test(timeout = 4000)
    public void testToStringThrowsNullPointerExceptionWhenFunctionNameIsNull() throws Throwable {
        // Create extension function with recursive argument
        Expression[] args = new Expression[1];
        Expression[] recursiveArgs = new Expression[1];
        CoreOperationOr recursiveExpression = new CoreOperationOr(recursiveArgs);
        args[0] = recursiveExpression;
        
        ExtensionFunction function = new ExtensionFunction(null, args);
        
        try {
            function.toString();
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected due to null function name in complex expression
        }
    }

    @Test(timeout = 4000)
    public void testComputeValueThrowsRuntimeExceptionWhenFunctionNotFound() throws Throwable {
        // Setup context for function lookup
        QName functionName = new QName("org.apache.commons.jxpath.ri.Parser");
        ExtensionFunction function = new ExtensionFunction(functionName, null);
        
        BasicNodeSet nodeSet = new BasicNodeSet();
        NodeSetContext nodeContext = new NodeSetContext(null, nodeSet);
        JXPathContextReferenceImpl contextImpl = (JXPathContextReferenceImpl) 
            JXPathContext.newContext(nodeContext);
        
        BeanPointer beanPointer = (BeanPointer) NodePointer.newNodePointer(
            functionName, nodeContext, Locale.PRC
        );
        RootContext rootContext = new RootContext(contextImpl, beanPointer);

        try {
            function.computeValue(rootContext);
            fail("Expected RuntimeException for missing function");
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().contains("Cannot invoke extension function"));
        }
    }

    @Test(timeout = 4000)
    public void testComputeValuePropagatesArithmeticExceptionFromArguments() throws Throwable {
        // Create expression that will cause division by zero
        Expression[] args = new Expression[8];
        Constant constant = new Constant("$KX%u}cMaD3:");
        args[0] = constant;
        // Create mod operation that will divide by zero
        CoreOperationMod modOperation = new CoreOperationMod(args[0], args[0]);
        args[1] = modOperation;
        
        ExtensionFunction function = new ExtensionFunction(null, args);

        try {
            function.computeValue(null);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            assertEquals("/ by zero", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testComputeThrowsRuntimeExceptionForUndefinedFunction() throws Throwable {
        // Setup function with complex arguments
        QName functionName = new QName("", "");
        Expression[] args = new Expression[1];
        
        // Create argument expression: ( (-NaN * -NaN) <= -NaN )
        Double nan = Expression.NOT_A_NUMBER;
        Constant nanConstant = new Constant(nan);
        CoreOperationNegate negate = new CoreOperationNegate(nanConstant);
        CoreOperationMultiply multiply = new CoreOperationMultiply(negate, negate);
        CoreOperationLessThanOrEqual comparison = new CoreOperationLessThanOrEqual(multiply, negate);
        args[0] = comparison;
        
        ExtensionFunction function = new ExtensionFunction(functionName, args);
        
        // Setup evaluation context
        JXPathContextReferenceImpl contextImpl = (JXPathContextReferenceImpl)
            JXPathContext.newContext((JXPathContext) null, comparison);
        BeanPointer beanPointer = (BeanPointer) NodePointer.newChildNodePointer(
            null, functionName, nan
        );
        RootContext rootContext = new RootContext(contextImpl, beanPointer);
        InitialContext initialContext = (InitialContext) rootContext.getConstantContext(new Object());

        try {
            function.compute(initialContext);
            fail("Expected RuntimeException for undefined function");
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().contains("Undefined function"));
        }
    }

    @Test(timeout = 4000)
    public void testComputeThrowsStackOverflowErrorWithRecursiveArguments() throws Throwable {
        // Create recursive expression structure
        QName functionName = new QName("N(", "N(");
        Double nan = Expression.NOT_A_NUMBER;
        Constant constant = new Constant(nan);
        CoreOperationNegate recursiveExpression = new CoreOperationNegate(constant);
        
        Expression[] args = new Expression[1];
        // Create self-referential argument
        recursiveExpression.args = args;
        args[0] = recursiveExpression;
        
        ExtensionFunction function = new ExtensionFunction(functionName, args);

        try {
            function.compute(null);
            fail("Expected StackOverflowError");
        } catch (StackOverflowError e) {
            // Expected due to infinite recursion in argument evaluation
        }
    }

    @Test(timeout = 4000)
    public void testComputePropagatesArithmeticExceptionFromArgumentOperations() throws Throwable {
        // Create expression that causes division by zero
        QName functionName = new QName(",", "");
        Double nan = Expression.NOT_A_NUMBER;
        Constant constant = new Constant(nan);
        CoreOperationNegate negate = new CoreOperationNegate(constant);
        
        Expression[] args = new Expression[2];
        // Create mod operation: NaN % NaN (which becomes 0%0 -> division by zero)
        CoreOperationMod modOperation = new CoreOperationMod(constant, negate);
        CoreOperationNegate negatedMod = new CoreOperationNegate(modOperation);
        args[0] = negatedMod;
        
        ExtensionFunction function = new ExtensionFunction(functionName, args);

        try {
            function.compute(null);
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            assertEquals("/ by zero", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testToStringWithNullFunctionNameAndArguments() throws Throwable {
        // Create function with null name and two null arguments
        Expression[] args = new Expression[2]; // [null, null]
        ExtensionFunction function = new ExtensionFunction(null, args);
        
        assertEquals("null(null, null)", function.toString());
    }

    @Test(timeout = 4000)
    public void testToStringWithNoArguments() throws Throwable {
        // Create function with name and no arguments
        QName functionName = new QName("org.apache.commons.jxpath.ri.Parser");
        ExtensionFunction function = new ExtensionFunction(functionName, null);
        
        assertEquals("org.apache.commons.jxpath.ri.Parser()", function.toString());
    }

    @Test(timeout = 4000)
    public void testComputeValueThrowsNullPointerExceptionWithoutContext() throws Throwable {
        // Create function with null name and arguments
        Expression[] args = new Expression[8];
        args[0] = new Constant("$KX%u}cMaD3:");
        ExtensionFunction function = new ExtensionFunction(null, args);

        try {
            function.computeValue(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected due to null context
        }
    }

    @Test(timeout = 4000)
    public void testComputeValueThrowsRuntimeExceptionWhenFunctionNotFoundWithEmptyArgs() throws Throwable {
        // Setup function with empty arguments
        Expression[] emptyArgs = new Expression[0];
        ExtensionFunction function = new ExtensionFunction(null, emptyArgs);
        
        // Mock context that returns no function
        JXPathContextReferenceImpl mockContext = mock(JXPathContextReferenceImpl.class);
        when(mockContext.getFunction(any(), any())).thenReturn(null);
        when(mockContext.getNamespaceResolver()).thenReturn(null);
        
        RootContext rootContext = new RootContext(mockContext, new VariablePointer(null));

        try {
            function.computeValue(rootContext);
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().contains("No such function"));
        }
    }

    @Test(timeout = 4000)
    public void testGetFunctionNameReturnsConstructorParameter() throws Throwable {
        // Create function with specific name
        QName expectedName = new QName("org.apache.commons.jxpath.ri.compiler.ExtensionFunction");
        ExtensionFunction function = new ExtensionFunction(expectedName, null);
        
        assertSame(expectedName, function.getFunctionName());
    }

    @Test(timeout = 4000)
    public void testComputeThrowsNullPointerExceptionWithoutContext() throws Throwable {
        // Create function with arguments
        Expression[] args = new Expression[1];
        ExtensionFunction function = new ExtensionFunction(null, args);

        try {
            function.compute(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected due to null context
        }
    }
}