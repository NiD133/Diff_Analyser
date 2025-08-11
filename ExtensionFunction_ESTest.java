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

import org.apache.commons.jxpath.Function;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.JXPathContextReferenceImpl;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.axes.RootContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.VariablePointer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link ExtensionFunction}.
 */
public class ExtensionFunctionTest {

    /**
     * Helper to create a minimal, non-null evaluation context.
     */
    private EvalContext createMinimalContext() {
        JXPathContextReferenceImpl context = new JXPathContextReferenceImpl(null, new Object());
        return context.getAbsoluteRootContext();
    }

    @Test
    public void getFunctionName_shouldReturnProvidedName() {
        QName expectedName = new QName("ns:test");
        Expression[] noArgs = new Expression[0];
        ExtensionFunction func = new ExtensionFunction(expectedName, noArgs);

        assertSame("The returned QName should be the same instance provided to the constructor.",
                expectedName, func.getFunctionName());
    }

    @Test
    public void getFunctionName_shouldReturnNullWhenConstructedWithNull() {
        Expression[] noArgs = new Expression[0];
        ExtensionFunction func = new ExtensionFunction(null, noArgs);

        assertNull("getFunctionName should return null if the function was constructed with a null name.",
                func.getFunctionName());
    }

    @Test
    public void computeContextDependent_shouldAlwaysReturnTrue() {
        ExtensionFunction func = new ExtensionFunction(new QName("any"), null);
        assertTrue("Extension functions are always considered context-dependent.",
                func.computeContextDependent());
    }

    // -----------------------------------------------------------------------
    // toString() Tests
    // -----------------------------------------------------------------------

    @Test
    public void toString_shouldFormatCorrectlyWithFunctionNameAndNoArgs() {
        QName functionName = new QName("my-func");
        ExtensionFunction func = new ExtensionFunction(functionName, new Expression[0]);
        assertEquals("my-func()", func.toString());
    }

    @Test
    public void toString_shouldFormatCorrectlyWithFunctionNameAndNullArgs() {
        QName functionName = new QName("my-func");
        ExtensionFunction func = new ExtensionFunction(functionName, null);
        assertEquals("my-func()", func.toString());
    }

    @Test
    public void toString_shouldFormatCorrectlyWithFunctionNameAndArgs() {
        QName functionName = new QName("ns:my-func");
        Expression[] arguments = {new Constant("arg1"), new Constant(123)};
        ExtensionFunction func = new ExtensionFunction(functionName, arguments);

        assertEquals("ns:my-func('arg1', 123)", func.toString());
    }

    @Test
    public void toString_shouldUseNullStringForNullFunctionNameAndArgs() {
        Expression[] arguments = {null, new Constant(42)};
        ExtensionFunction func = new ExtensionFunction(null, arguments);

        assertEquals("null(null, 42)", func.toString());
    }

    // -----------------------------------------------------------------------
    // compute() and computeValue() Tests
    // -----------------------------------------------------------------------

    @Test
    public void computeValue_shouldThrowNPE_whenFunctionNameIsNull() {
        ExtensionFunction func = new ExtensionFunction(null, new Expression[0]);
        EvalContext context = createMinimalContext();

        // A NullPointerException is expected because the underlying context cannot look up a null function name.
        assertThrows(NullPointerException.class, () -> func.computeValue(context));
    }

    @Test
    public void computeValue_shouldThrowRuntimeException_whenFunctionIsUndefined() {
        QName undefinedFuncName = new QName("undefined:function");
        ExtensionFunction func = new ExtensionFunction(undefinedFuncName, new Expression[0]);
        JXPathContext context = JXPathContext.newContext(new Object());
        EvalContext evalContext = (EvalContext) context.getContextPointer();

        RuntimeException ex = assertThrows(RuntimeException.class, () -> func.computeValue(evalContext));
        assertTrue("Exception message should indicate an undefined function.",
                ex.getMessage().contains("Undefined function: " + undefinedFuncName));
    }

    @Test
    public void computeValue_shouldThrowRuntimeException_whenFunctionIsNotFoundByContext() {
        QName functionName = new QName("ns:my-func");
        ExtensionFunction func = new ExtensionFunction(functionName, new Expression[0]);

        // Mock the context to simulate a function lookup failure.
        JXPathContextReferenceImpl mockContext = mock(JXPathContextReferenceImpl.class);
        when(mockContext.getFunction(any(QName.class), any(Object[].class))).thenReturn(null);
        RootContext evalContext = new RootContext(mockContext, new VariablePointer(new QName("var")));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> func.computeValue(evalContext));
        assertEquals("No such function: ns:my-func[]", ex.getMessage());
    }

    @Test
    public void computeValue_shouldThrowRuntimeException_whenInvokingInvalidPackageFunction() {
        // A QName with no prefix is treated as a call to a class-based 'package' function.
        // Here, we provide an invalid method signature.
        QName invalidPackageFunc = new QName(null, "java.lang.String.invalidMethod");
        ExtensionFunction func = new ExtensionFunction(invalidPackageFunc, new Expression[0]);
        JXPathContext context = JXPathContext.newContext(new Object());
        EvalContext evalContext = (EvalContext) context.getContextPointer();

        RuntimeException ex = assertThrows(RuntimeException.class, () -> func.computeValue(evalContext));
        assertTrue("Exception message should indicate failure to invoke the function.",
                ex.getMessage().startsWith("Cannot invoke extension function"));
    }

    @Test
    public void computeValue_shouldPropagateExceptionFromArgumentEvaluation() {
        // Create an argument expression that will fail during evaluation.
        Expression badArgument = new CoreOperationMod(new Constant(1), new Constant(0)); // Division by zero
        ExtensionFunction func = new ExtensionFunction(new QName("any-func"), new Expression[]{badArgument});

        // The exception from the argument evaluation should propagate up.
        // The context can be null as it's not reached.
        assertThrows(ArithmeticException.class, () -> func.computeValue(null));
    }

    @Test
    public void compute_shouldThrowStackOverflowError_forRecursiveArgument() {
        // This test ensures that a deeply recursive argument structure leads to a
        // StackOverflowError, as expected, rather than another exception or infinite loop.
        QName functionName = new QName("recursive-func");
        CoreOperationNegate recursiveNegate = new CoreOperationNegate(new Constant(1));
        Expression[] args = {recursiveNegate};
        recursiveNegate.args = args; // Create a recursive cycle in the expression tree.

        ExtensionFunction func = new ExtensionFunction(functionName, args);

        // The compute method will endlessly evaluate the argument, causing a stack overflow.
        // The context can be null as it's not reached.
        assertThrows(StackOverflowError.class, () -> func.compute(null));
    }
}