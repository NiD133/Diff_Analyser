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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.apache.commons.jxpath.AbstractJXPathTest;
import org.apache.commons.jxpath.ClassFunctions;
import org.apache.commons.jxpath.ExpressionContext;
import org.apache.commons.jxpath.Function;
import org.apache.commons.jxpath.FunctionLibrary;
import org.apache.commons.jxpath.Functions;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.NodeSet;
import org.apache.commons.jxpath.PackageFunctions;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.TestBean;
import org.apache.commons.jxpath.Variables;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.util.JXPath11CompatibleTypeConverter;
import org.apache.commons.jxpath.util.TypeConverter;
import org.apache.commons.jxpath.util.TypeUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for JXPath extension functions including:
 * - Object allocation via constructors
 * - Static and instance method calls
 * - Collection handling and return types
 * - NodeSet operations
 * - Type conversion and compatibility
 */
class ExtensionFunctionTest extends AbstractJXPathTest {

    // Test constants
    private static final String EXPECTED_DEFAULT_CONSTRUCTOR_RESULT = "foo=0; bar=null";
    private static final String EXPECTED_CUSTOM_CONSTRUCTOR_RESULT = "foo=3; bar=baz";
    private static final String EXPECTED_TYPE_CONVERTED_CONSTRUCTOR_RESULT = "foo=3; bar=4.0";
    private static final String EXPECTED_VARIABLE_CONSTRUCTOR_RESULT = "foo=2; bar=baz";
    private static final String EXPECTED_STATIC_METHOD_RESULT = "foo=8; bar=goober";
    private static final String EXPECTED_INCREMENT_RESULT = "9";

    /**
     * Mock ExpressionContext implementation for testing function invocation.
     * Provides a simple context with a single object.
     */
    private static final class TestExpressionContext implements ExpressionContext {
        private final Object contextObject;

        public TestExpressionContext(final Object contextObject) {
            this.contextObject = contextObject;
        }

        @Override
        public List<Pointer> getContextNodeList() {
            return null;
        }

        @Override
        public Pointer getContextNodePointer() {
            return NodePointer.newNodePointer(null, contextObject, Locale.getDefault());
        }

        @Override
        public JXPathContext getJXPathContext() {
            return null;
        }

        @Override
        public int getPosition() {
            return 0;
        }
    }

    // Test fixtures
    private Functions testFunctions;
    private JXPathContext jxpathContext;
    private TestBean testBean;
    private TypeConverter originalTypeConverter;

    @Override
    @BeforeEach
    public void setUp() {
        setupJXPathContext();
        setupTestFunctions();
        preserveOriginalTypeConverter();
    }

    @AfterEach
    public void tearDown() {
        restoreOriginalTypeConverter();
    }

    // === Object Allocation Tests ===

    @Test
    void shouldAllocateObjectUsingDefaultConstructor() {
        String actualResult = evaluateXPathAsString("test:new()");
        
        assertEquals(EXPECTED_DEFAULT_CONSTRUCTOR_RESULT, actualResult,
            "Default constructor should create object with initial values");
    }

    @Test
    void shouldAllocateObjectUsingPackageFunctionsWithClassName() {
        String actualResult = evaluateXPathAsString("jxpathtest:TestFunctions.new()");
        
        assertEquals(EXPECTED_DEFAULT_CONSTRUCTOR_RESULT, actualResult,
            "PackageFunctions with class name should create object with default constructor");
    }

    @Test
    void shouldAllocateObjectUsingFullyQualifiedClassName() {
        String fullyQualifiedExpression = "string(" + TestFunctions.class.getName() + ".new())";
        String actualResult = (String) jxpathContext.getValue(fullyQualifiedExpression);
        
        assertEquals(EXPECTED_DEFAULT_CONSTRUCTOR_RESULT, actualResult,
            "Fully qualified class name should create object with default constructor");
    }

    @Test
    void shouldAllocateObjectUsingCustomConstructor() {
        String actualResult = evaluateXPathAsString("test:new(3, 'baz')");
        
        assertEquals(EXPECTED_CUSTOM_CONSTRUCTOR_RESULT, actualResult,
            "Custom constructor should create object with provided values");
    }

    @Test
    void shouldAllocateObjectWithTypeConversion() {
        String actualResult = evaluateXPathAsString("test:new('3', 4)");
        
        assertEquals(EXPECTED_TYPE_CONVERTED_CONSTRUCTOR_RESULT, actualResult,
            "Constructor should handle type conversion from string to int and int to double");
    }

    @Test
    void shouldAllocateObjectUsingVariables() {
        jxpathContext.getVariables().declareVariable("A", "baz");
        
        String actualResult = evaluateXPathAsString("test:new(2, $A, false)");
        
        assertEquals(EXPECTED_VARIABLE_CONSTRUCTOR_RESULT, actualResult,
            "Constructor should work with XPath variables");
    }

    // === Static Method Call Tests ===

    @Test
    void shouldCallStaticMethodWithClassFunctions() {
        String actualResult = evaluateXPathAsString("test:build(8, 'goober')");
        
        assertEquals(EXPECTED_STATIC_METHOD_RESULT, actualResult,
            "Static method should be callable via ClassFunctions");
    }

    @Test
    void shouldCallStaticMethodWithPackageFunctions() {
        String actualResult = evaluateXPathAsString("jxpathtest:TestFunctions.build(8, 'goober')");
        
        assertEquals(EXPECTED_STATIC_METHOD_RESULT, actualResult,
            "Static method should be callable via PackageFunctions");
    }

    @Test
    void shouldCallStaticMethodWithFullyQualifiedName() {
        String fullyQualifiedExpression = "string(" + TestFunctions.class.getName() + ".build(8, 'goober'))";
        String actualResult = (String) jxpathContext.getValue(fullyQualifiedExpression);
        
        assertEquals(EXPECTED_STATIC_METHOD_RESULT, actualResult,
            "Static method should be callable with fully qualified class name");
    }

    @Test
    void shouldCallStaticMethodFromDifferentClassWithSamePrefix() {
        String actualResult = evaluateXPathAsString("test:increment(8)");
        
        assertEquals(EXPECTED_INCREMENT_RESULT, actualResult,
            "Should be able to call methods from different classes sharing the same prefix");
    }

    @Test
    void shouldConvertNodeSetToStringInStaticMethod() {
        String actualResult = (String) jxpathContext.getValue("test:string(/beans/name)");
        
        assertEquals("Name 1", actualResult,
            "NodeSet should be properly converted to string in static method calls");
    }

    // === Instance Method Call Tests ===

    @Test
    void shouldCallBuiltInStringMethod() {
        Integer actualLength = (Integer) jxpathContext.getValue("length('foo')");
        
        assertEquals(Integer.valueOf(3), actualLength,
            "Built-in string length method should work");
    }

    @Test
    void shouldCallMethodIgnoringPrefix() {
        String actualResult = (String) jxpathContext.getValue("call:substring('foo', 1, 2)");
        
        assertEquals("o", actualResult,
            "Method calls should work regardless of prefix");
    }

    @Test
    void shouldCallInstanceMethodWithVariable() {
        String actualResult = evaluateXPathAsString("test:getFoo($test)");
        
        assertEquals("4", actualResult,
            "Instance method should work with variable arguments");
    }

    @Test
    void shouldCallInstanceMethodWithMultipleArguments() {
        String actualResult = evaluateXPathAsString("test:setFooAndBar($test, 7, 'biz')");
        
        assertEquals("foo=7; bar=biz", actualResult,
            "Instance method should handle multiple arguments");
    }

    // === Collection Handling Tests ===

    @Test
    void shouldHandleCollectionMethodCalls() {
        List<String> testList = new ArrayList<>();
        testList.add("foo");
        jxpathContext.getVariables().declareVariable("myList", testList);
        
        // Test size method on variable
        assertEquals(Integer.valueOf(1), jxpathContext.getValue("size($myList)"),
            "Size method should work on collection variables");
        
        // Test size method on context property
        assertEquals(Integer.valueOf(2), jxpathContext.getValue("size(beans)"),
            "Size method should work on context properties");
        
        // Test mutating method call
        jxpathContext.getValue("add($myList, 'hello')");
        assertEquals(2, testList.size(),
            "Collection should be modified after method call");
    }

    @Test
    void shouldHandleCollectionMethodOnRootContext() {
        JXPathContext rootCollectionContext = JXPathContext.newContext(new ArrayList<>());
        
        String actualSize = String.valueOf(rootCollectionContext.getValue("size(/)"));
        
        assertEquals("0", actualSize,
            "Extension function should work on root collection context");
    }

    @Test
    void shouldReturnCollectionFromExtensionFunction() {
        // Test iteration over returned collection
        assertXPathValueIterator(jxpathContext, "test:collection()/name", list("foo", "bar"));
        assertXPathPointerIterator(jxpathContext, "test:collection()/name", list("/.[1]/name", "/.[2]/name"));
        
        // Test single value access
        assertXPathValue(jxpathContext, "test:collection()/name", "foo");
        assertXPathValue(jxpathContext, "test:collection()/@name", "foo");
        
        // Test collection conversion
        List<String> inputList = list("foo", "bar");
        jxpathContext.getVariables().declareVariable("list", inputList);
        
        Object result = jxpathContext.getValue("test:items($list)");
        assertInstanceOf(Collection.class, result, "Return type should be Collection");
        assertEquals(inputList, new ArrayList<>((Collection<?>) result), "Return values should match input");
    }

    // === NodeSet Handling Tests ===

    @Test
    void shouldReturnNodeSetFromExtensionFunction() {
        assertXPathValueIterator(jxpathContext, "test:nodeSet()/name", list("Name 1", "Name 2"));
        assertXPathValueIterator(jxpathContext, "test:nodeSet()", list(testBean.getBeans()[0], testBean.getBeans()[1]));
        assertXPathPointerIterator(jxpathContext, "test:nodeSet()/name", list("/beans[1]/name", "/beans[2]/name"));
        assertXPathValueAndPointer(jxpathContext, "test:nodeSet()/name", "Name 1", "/beans[1]/name");
        assertXPathValueAndPointer(jxpathContext, "test:nodeSet()/@name", "Name 1", "/beans[1]/@name");
        
        assertEquals(2, ((Number) jxpathContext.getValue("count(test:nodeSet())")).intValue(),
            "NodeSet should contain correct number of elements");
        
        assertEquals(testBean.getBeans()[0], jxpathContext.getValue("test:nodeSet()"),
            "NodeSet should return first element when accessed as single value");
    }

    // === Type Conversion Tests ===

    @Test
    void shouldHandleNodeSetTypeChecking() {
        jxpathContext.getVariables().declareVariable("List.class", List.class);
        jxpathContext.getVariables().declareVariable("NodeSet.class", NodeSet.class);
        
        assertEquals(Boolean.TRUE, jxpathContext.getValue("test:isInstance(//strings, $List.class)"),
            "NodeSet should be recognized as List by default");
        assertEquals(Boolean.FALSE, jxpathContext.getValue("test:isInstance(//strings, $NodeSet.class)"),
            "NodeSet should not be recognized as NodeSet by default");
    }

    @Test
    void shouldHandleBackwardCompatibilityTypeConversion() {
        TypeUtils.setTypeConverter(new JXPath11CompatibleTypeConverter());
        jxpathContext.getVariables().declareVariable("List.class", List.class);
        jxpathContext.getVariables().declareVariable("NodeSet.class", NodeSet.class);
        
        assertEquals(Boolean.FALSE, jxpathContext.getValue("test:isInstance(//strings, $List.class)"),
            "With JXPath 1.1 compatibility, NodeSet should not be recognized as List");
        assertEquals(Boolean.TRUE, jxpathContext.getValue("test:isInstance(//strings, $NodeSet.class)"),
            "With JXPath 1.1 compatibility, NodeSet should be recognized as NodeSet");
    }

    // === Expression Context Tests ===

    @Test
    void shouldUseExpressionContextInExtensionFunctions() {
        // Function uses ExpressionContext to access current node
        assertXPathValue(jxpathContext, "//.[test:isMap()]/Key1", "Value 1");
        
        // Function gets all nodes matching pattern
        assertXPathValue(jxpathContext, "count(//.[test:count(strings) = 3])", Double.valueOf(7));
        
        // Function receives collection and processes it
        assertXPathValue(jxpathContext, "test:count(//strings)", Integer.valueOf(21));
        
        // Function receives pointers and processes them
        assertXPathValue(jxpathContext, "test:countPointers(//strings)", Integer.valueOf(21));
        
        // Function uses ExpressionContext to get current pointer path
        assertXPathValue(jxpathContext, "/beans[contains(test:path(), '[2]')]/name", "Name 2");
    }

    // === Function Lookup Tests ===

    @Test
    void shouldLookupConstructorFunction() {
        Object[] arguments = { Integer.valueOf(1), "x" };
        
        Function constructorFunction = testFunctions.getFunction("test", "new", arguments);
        Object result = constructorFunction.invoke(new TestExpressionContext(null), arguments);
        
        assertEquals("foo=1; bar=x", result.toString(),
            "Constructor function lookup should find appropriate constructor");
    }

    @Test
    void shouldLookupConstructorFunctionWithExpressionContext() {
        Object[] arguments = { "baz" };
        
        Function constructorFunction = testFunctions.getFunction("test", "new", arguments);
        Object result = constructorFunction.invoke(new TestExpressionContext(Integer.valueOf(1)), arguments);
        
        assertEquals("foo=1; bar=baz", result.toString(),
            "Constructor function should use ExpressionContext when available");
    }

    @Test
    void shouldLookupStaticMethodFunction() {
        Object[] arguments = { Integer.valueOf(1), "x" };
        
        Function staticMethod = testFunctions.getFunction("test", "build", arguments);
        Object result = staticMethod.invoke(new TestExpressionContext(null), arguments);
        
        assertEquals("foo=1; bar=x", result.toString(),
            "Static method function lookup should work correctly");
    }

    @Test
    void shouldLookupStaticMethodWithTypeConversion() {
        Object[] arguments = { "7", Integer.valueOf(1) };
        
        Function staticMethod = testFunctions.getFunction("test", "build", arguments);
        Object result = staticMethod.invoke(new TestExpressionContext(null), arguments);
        
        assertEquals("foo=7; bar=1", result.toString(),
            "Static method should handle type conversion during lookup");
    }

    @Test
    void shouldLookupStaticMethodWithExpressionContext() {
        Object[] arguments = {};
        
        Function staticMethod = testFunctions.getFunction("test", "path", arguments);
        Object result = staticMethod.invoke(new TestExpressionContext(Integer.valueOf(1)), arguments);
        
        assertEquals("1", result,
            "Static method should use ExpressionContext when needed");
    }

    @Test
    void shouldLookupInstanceMethodFunction() {
        Object[] arguments = { new TestFunctions() };
        
        Function instanceMethod = testFunctions.getFunction("test", "getFoo", arguments);
        Object result = instanceMethod.invoke(new TestExpressionContext(null), arguments);
        
        assertEquals("0", result.toString(),
            "Instance method function lookup should work correctly");
    }

    @Test
    void shouldLookupInstanceMethodWithExpressionContext() {
        Object[] arguments = { new TestFunctions() };
        
        Function instanceMethod = testFunctions.getFunction("test", "instancePath", arguments);
        Object result = instanceMethod.invoke(new TestExpressionContext(Integer.valueOf(1)), arguments);
        
        assertEquals("1", result,
            "Instance method should use ExpressionContext when available");
    }

    @Test
    void shouldLookupInstanceMethodWithExpressionContextAndArguments() {
        Object[] arguments = { new TestFunctions(), "*" };
        
        Function instanceMethod = testFunctions.getFunction("test", "pathWithSuffix", arguments);
        Object result = instanceMethod.invoke(new TestExpressionContext(Integer.valueOf(1)), arguments);
        
        assertEquals("1*", result,
            "Instance method should handle both ExpressionContext and regular arguments");
    }

    // === Helper Methods ===

    private void setupJXPathContext() {
        if (jxpathContext == null) {
            testBean = new TestBean();
            jxpathContext = JXPathContext.newContext(testBean);
            
            setupVariables();
            setupFunctionLibrary();
        }
    }

    private void setupVariables() {
        Variables variables = jxpathContext.getVariables();
        variables.declareVariable("test", new TestFunctions(4, "test"));
        variables.declareVariable("List.class", List.class);
        variables.declareVariable("NodeSet.class", NodeSet.class);
    }

    private void setupFunctionLibrary() {
        FunctionLibrary functionLibrary = new FunctionLibrary();
        functionLibrary.addFunctions(new ClassFunctions(TestFunctions.class, "test"));
        functionLibrary.addFunctions(new ClassFunctions(TestFunctions2.class, "test"));
        functionLibrary.addFunctions(new PackageFunctions("", "call"));
        functionLibrary.addFunctions(new PackageFunctions("org.apache.commons.jxpath.ri.compiler.", "jxpathtest"));
        functionLibrary.addFunctions(new PackageFunctions("", null));
        
        jxpathContext.setFunctions(functionLibrary);
    }

    private void setupTestFunctions() {
        testFunctions = new ClassFunctions(TestFunctions.class, "test");
    }

    private void preserveOriginalTypeConverter() {
        originalTypeConverter = TypeUtils.getTypeConverter();
    }

    private void restoreOriginalTypeConverter() {
        TypeUtils.setTypeConverter(originalTypeConverter);
    }

    private String evaluateXPathAsString(String expression) {
        return (String) jxpathContext.getValue("string(" + expression + ")");
    }
}