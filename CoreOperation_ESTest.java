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
import org.apache.commons.jxpath.ri.compiler.*;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.VariablePointer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

/**
 * Test suite for CoreOperation and its subclasses.
 * Tests various XPath operations like union, comparison, arithmetic, and logical operations.
 */
@RunWith(EvoRunner.class) 
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, 
                     resetStaticState = true, separateClassLoader = true) 
public class CoreOperation_ESTest extends CoreOperation_ESTest_scaffolding {

    // ========== Union Operation Tests ==========
    
    @Test(timeout = 4000)
    public void testEmptyUnionOperation_ReturnsEmptyString() throws Throwable {
        // Given: An empty union operation
        Expression[] emptyExpressions = new Expression[0];
        CoreOperationUnion unionOperation = new CoreOperationUnion(emptyExpressions);
        
        // When: Converting to string
        String result = unionOperation.toString();
        
        // Then: Should return empty string
        assertEquals("Empty union should return empty string", "", result);
    }

    @Test(timeout = 4000)
    public void testEmptyUnionOperation_ComputeValue_ReturnsUnionContext() throws Throwable {
        // Given: An empty union operation and evaluation context
        Expression[] emptyExpressions = new Expression[0];
        CoreOperationUnion unionOperation = new CoreOperationUnion(emptyExpressions);
        EvalContext evalContext = createBasicEvalContext(unionOperation);
        
        // When: Computing the value
        UnionContext result = (UnionContext) unionOperation.computeValue(evalContext);
        
        // Then: Should return UnionContext with no child ordering required
        assertFalse("Union context should not require child ordering", 
                   result.isChildOrderingRequired());
    }

    @Test(timeout = 4000)
    public void testEmptyUnionOperation_Compute_ReturnsUnionContext() throws Throwable {
        // Given: An empty union operation and complex evaluation context
        Expression[] emptyExpressions = new Expression[0];
        CoreOperationUnion unionOperation = new CoreOperationUnion(emptyExpressions);
        EvalContext evalContext = createComplexEvalContext();
        
        // When: Computing the result
        UnionContext result = (UnionContext) unionOperation.compute(evalContext);
        
        // Then: Should return UnionContext with no child ordering required
        assertFalse("Union context should not require child ordering", 
                   result.isChildOrderingRequired());
    }

    // ========== Comparison Operation Tests ==========
    
    @Test(timeout = 4000)
    public void testLessThanOrEqualOperation_GetSymbol_ReturnsCorrectSymbol() throws Throwable {
        // Given: A less-than-or-equal operation
        CoreOperationGreaterThan greaterThan = new CoreOperationGreaterThan(null, null);
        CoreOperationLessThanOrEqual lessThanOrEqual = 
            new CoreOperationLessThanOrEqual(greaterThan, null);
        
        // When: Getting the symbol
        String symbol = lessThanOrEqual.getSymbol();
        
        // Then: Should return the correct symbol
        assertEquals("Less than or equal symbol should be <=", "<=", symbol);
    }

    // ========== Error Handling Tests ==========
    
    @Test(timeout = 4000)
    public void testNameAttributeTest_ToString_ThrowsNullPointerException() throws Throwable {
        // Given: A NameAttributeTest with null expressions
        NameAttributeTest nameAttributeTest = new NameAttributeTest(null, null);
        
        // When & Then: Calling toString should throw NullPointerException
        try {
            nameAttributeTest.toString();
            fail("Expected NullPointerException when toString() called with null expressions");
        } catch (NullPointerException e) {
            // Expected behavior
            verifyException("org.evosuite.runtime.System", e);
        }
    }

    @Test(timeout = 4000)
    public void testModuloOperation_WithZeroDivisor_ThrowsArithmeticException() throws Throwable {
        // Given: A modulo operation that will result in division by zero
        Expression[] emptyExpressions = new Expression[0];
        CoreOperationAnd andOperation = new CoreOperationAnd(emptyExpressions);
        CoreOperationMod modOperation = new CoreOperationMod(andOperation, andOperation);
        
        // When & Then: Computing should throw ArithmeticException
        try {
            modOperation.compute(null);
            fail("Expected ArithmeticException for modulo by zero");
        } catch (ArithmeticException e) {
            // Expected behavior - division by zero
        }
    }

    @Test(timeout = 4000)
    public void testCoreFunction_WithIncorrectArguments_ThrowsRuntimeException() throws Throwable {
        // Given: A core function with incorrect number of arguments
        Expression[] expressions = new Expression[9];
        CoreFunction stringLengthFunction = new CoreFunction(15, expressions); // string-length function
        CoreOperationMultiply multiplyOperation = 
            new CoreOperationMultiply(stringLengthFunction, stringLengthFunction);
        expressions[0] = multiplyOperation;
        CoreOperationAdd addOperation = new CoreOperationAdd(expressions);
        
        // When & Then: Computing should throw RuntimeException
        try {
            addOperation.compute(null);
            fail("Expected RuntimeException for incorrect number of arguments");
        } catch (RuntimeException e) {
            assertTrue("Error message should mention incorrect number of arguments",
                      e.getMessage().contains("Incorrect number of arguments"));
            verifyException("org.apache.commons.jxpath.ri.compiler.CoreFunction", e);
        }
    }

    @Test(timeout = 4000)
    public void testNameAttributeTest_WithEmptyArgs_ThrowsArrayIndexOutOfBoundsException() throws Throwable {
        // Given: A NameAttributeTest with empty arguments array
        NameAttributeTest nameAttributeTest1 = new NameAttributeTest(null, null);
        NameAttributeTest nameAttributeTest2 = new NameAttributeTest(nameAttributeTest1, nameAttributeTest1);
        nameAttributeTest2.args = new Expression[0]; // Force empty args
        
        // When & Then: Computing should throw ArrayIndexOutOfBoundsException
        try {
            nameAttributeTest2.compute(null);
            fail("Expected ArrayIndexOutOfBoundsException for empty arguments");
        } catch (ArrayIndexOutOfBoundsException e) {
            assertEquals("Should be index 0", "0", e.getMessage());
            verifyException("org.apache.commons.jxpath.ri.compiler.CoreOperationCompare", e);
        }
    }

    // ========== String Representation Tests ==========
    
    @Test(timeout = 4000)
    public void testComplexOperation_ToString_ReturnsValidString() throws Throwable {
        // Given: A complex nested operation
        CoreFunction coreFunction = new CoreFunction(-716, null);
        CoreOperationGreaterThan greaterThan = new CoreOperationGreaterThan(coreFunction, coreFunction);
        CoreOperationLessThan lessThan = new CoreOperationLessThan(greaterThan, coreFunction);
        CoreOperationGreaterThanOrEqual greaterThanOrEqual = 
            new CoreOperationGreaterThanOrEqual(lessThan, lessThan);
        
        // When: Converting to string
        String result = greaterThanOrEqual.toString();
        
        // Then: Should return a valid string representation
        assertNotNull("String representation should not be null", result);
    }

    @Test(timeout = 4000)
    public void testNestedNameAttributeTest_ToString_ReturnsValidString() throws Throwable {
        // Given: Nested NameAttributeTest operations
        NameAttributeTest innerTest = new NameAttributeTest(null, null);
        NameAttributeTest outerTest = new NameAttributeTest(innerTest, innerTest);
        
        // When: Converting to string
        String result = outerTest.toString();
        
        // Then: Should return a valid string representation
        assertNotNull("String representation should not be null", result);
    }

    // ========== Complex Scenario Tests ==========
    
    @Test(timeout = 4000)
    public void testComplexUnionWithNormalizeSpace_ThrowsRuntimeException() throws Throwable {
        // Given: A complex union operation with normalize-space function
        Expression[] functionArgs = new Expression[8];
        CoreFunction normalizeSpaceFunction = new CoreFunction(16, functionArgs); // normalize-space
        CoreOperationLessThanOrEqual lessThanOrEqual = 
            new CoreOperationLessThanOrEqual(normalizeSpaceFunction, normalizeSpaceFunction);
        CoreOperationNegate negateOperation = new CoreOperationNegate(lessThanOrEqual);
        
        Expression[] unionArgs = new Expression[6];
        unionArgs[0] = normalizeSpaceFunction;
        CoreOperationUnion unionOperation = new CoreOperationUnion(unionArgs);
        functionArgs[5] = negateOperation;
        
        // When & Then: Computing should throw RuntimeException
        try {
            unionOperation.computeValue(null);
            fail("Expected RuntimeException for incorrect normalize-space arguments");
        } catch (RuntimeException e) {
            assertTrue("Error should mention normalize-space", 
                      e.getMessage().contains("normalize-space"));
            verifyException("org.apache.commons.jxpath.ri.compiler.CoreFunction", e);
        }
    }

    // ========== Helper Methods ==========
    
    /**
     * Creates a basic evaluation context for testing.
     */
    private EvalContext createBasicEvalContext(Object contextObject) {
        JXPathContextReferenceImpl jxPathContext = 
            (JXPathContextReferenceImpl) JXPathContext.newContext(contextObject);
        VariablePointer variablePointer = new VariablePointer((QName) null);
        RootContext rootContext = new RootContext(jxPathContext, variablePointer);
        return (InitialContext) rootContext.getConstantContext(null);
    }
    
    /**
     * Creates a complex evaluation context with nested contexts for testing.
     */
    private EvalContext createComplexEvalContext() {
        RootContext rootContext = new RootContext(null, null);
        NodeTypeTest nodeTypeTest = new NodeTypeTest(22);
        PrecedingOrFollowingContext precedingContext = 
            new PrecedingOrFollowingContext(rootContext, nodeTypeTest, false);
        SelfContext selfContext = new SelfContext(precedingContext, nodeTypeTest);
        QName qName = new QName("");
        NodeNameTest nodeNameTest = new NodeNameTest(qName);
        return new ParentContext(selfContext, nodeNameTest);
    }
}