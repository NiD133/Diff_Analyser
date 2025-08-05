/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.UnionContext;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the abstract {@link CoreOperation} class, focusing on shared behavior
 * like string representation, exception handling, and computation logic.
 */
public class CoreOperationTest {

    @Test
    public void getSymbolShouldReturnCorrectOperator() {
        // Arrange
        Expression[] noArgs = new Expression[0];
        CoreOperation add = new CoreOperationAdd(noArgs);
        CoreOperation lessThan = new CoreOperationLessThan(null, null);
        CoreOperation and = new CoreOperationAnd(noArgs);

        // Act & Assert
        assertEquals("The symbol for Add should be '+'", "+", add.getSymbol());
        assertEquals("The symbol for LessThan should be '<'", "<", lessThan.getSymbol());
        assertEquals("The symbol for And should be 'and'", "and", and.getSymbol());
    }

    @Test
    public void toStringOnNestedExpressionShouldUseParenthesesForPrecedence() {
        // Arrange: Create an expression like (1 + 2) * 3
        // Add has lower precedence (4) than Multiply (5), so it should be parenthesized.
        Expression one = new Constant(1);
        Expression two = new Constant(2);
        Expression three = new Constant(3);

        CoreOperationAdd addOperation = new CoreOperationAdd(new Expression[]{one, two});
        CoreOperationMultiply multiplyOperation = new CoreOperationMultiply(addOperation, three);

        // Act
        String result = multiplyOperation.toString();

        // Assert
        assertEquals("Lower precedence operations should be parenthesized", "(1 + 2) * 3", result);
    }

    @Test
    public void toStringWithNoArgumentsShouldReturnEmptyStringForUnion() {
        // Arrange
        CoreOperationUnion union = new CoreOperationUnion(new Expression[0]);

        // Act
        String result = union.toString();

        // Assert
        assertEquals("toString on a union with no arguments should be empty", "", result);
    }

    @Test(expected = NullPointerException.class)
    public void toStringWithNullArgumentsShouldThrowNPE() {
        // Arrange
        // NameAttributeTest is a CoreOperationCompare that takes two arguments.
        CoreOperation operationWithNullArgs = new NameAttributeTest(null, null);

        // Act
        operationWithNullArgs.toString(); // Should throw NullPointerException
    }

    @Test(expected = NullPointerException.class)
    public void computeWithNullArgumentsShouldThrowNPE() {
        // Arrange
        CoreOperation operationWithNullArgs = new NameAttributeTest(null, null);

        // Act
        operationWithNullArgs.compute(null); // Should throw NullPointerException
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void computeWithInsufficientArgumentsShouldThrowAIOOBE() {
        // Arrange
        // CoreOperationCompare requires two arguments, but we provide zero.
        CoreOperation operationWithNoArgs = new NameAttributeTest(new Expression[0]);

        // Act
        operationWithNoArgs.compute(null); // Should throw ArrayIndexOutOfBoundsException
    }

    @Test(expected = ArithmeticException.class)
    public void computeModByZeroShouldThrowArithmeticException() {
        // Arrange
        Expression one = new Constant(1);
        Expression zero = new Constant(0);
        CoreOperationMod modByZero = new CoreOperationMod(one, zero);

        // Act
        modByZero.compute(null); // Should throw ArithmeticException
    }

    @Test(expected = StackOverflowError.class)
    public void computeWithCircularArgumentDependencyShouldThrowStackOverflow() {
        // Arrange
        // Create an operation that has itself as an argument.
        CoreOperationAdd selfReferentialOp = new CoreOperationAdd(new Expression[1]);
        selfReferentialOp.args[0] = selfReferentialOp;

        // Act
        selfReferentialOp.computeValue(null); // Should cause infinite recursion
    }

    @Test(expected = RuntimeException.class)
    public void computeShouldPropagateExceptionsFromArgumentEvaluation() {
        // Arrange
        // CoreFunction "string-length" (code 15) expects one argument.
        // We provide zero to guarantee it throws a RuntimeException during evaluation.
        Expression invalidFunctionCall = new CoreFunction(15, new Expression[0]);
        CoreOperationAdd operation = new CoreOperationAdd(new Expression[]{invalidFunctionCall});

        // Act
        operation.compute(null); // Should evaluate the invalid function and propagate the exception.
    }

    @Test
    public void unionOperationShouldComputeToAUnionContext() {
        // Arrange
        CoreOperationUnion union = new CoreOperationUnion(new Expression[0]);
        // A simple, valid EvalContext is sufficient for this test.
        JXPathContext context = JXPathContext.newContext(null);
        EvalContext evalContext = context.getAbsoluteRootContext();

        // Act
        Object result = union.computeValue(evalContext);

        // Assert
        assertTrue("Union operation should compute to a UnionContext", result instanceof UnionContext);
        assertFalse("Union of an empty set should not require child ordering", ((UnionContext) result).isChildOrderingRequired());
    }
}