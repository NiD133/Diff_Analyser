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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for JXPath extension functions.
 *
 * <p>This test suite is organized into nested classes to group related test cases,
 * improving readability and maintainability. Each nested class focuses on a specific
 * aspect of extension function behavior, such as constructor calls, static methods,
 * instance methods, and context-aware functions.</p>
 */
@DisplayName("Extension Function Tests")
class ExtensionFunctionTest extends AbstractJXPathTest {

    private JXPathContext context;
    private Functions classFunctions;
    private TypeConverter originalTypeConverter;

    /**
     * A mock ExpressionContext for direct function invocation tests.
     */
    private static final class TestExpressionContext implements ExpressionContext {
        private final Object object;

        public TestExpressionContext(final Object object) {
            this.object = object;
        }

        @Override
        public Pointer getContextNodePointer() {
            return NodePointer.newNodePointer(null, object, Locale.getDefault());
        }

        // Unused methods
        @Override public List<Pointer> getContextNodeList() { return null; }
        @Override public JXPathContext getJXPathContext() { return null; }
        @Override public int getPosition() { return 0; }
    }

    @BeforeEach
    @Override
    public void setUp() {
        // Arrange: Create a JXPathContext with a TestBean as the root object.
        final TestBean testBean = new TestBean();
        context = JXPathContext.newContext(testBean);

        // Arrange: Set up a rich function library for testing various scenarios.
        final FunctionLibrary lib = new FunctionLibrary();
        // Functions from TestFunctions and TestFunctions2 classes under the 'test' namespace.
        lib.addFunctions(new ClassFunctions(TestFunctions.class, "test"));
        lib.addFunctions(new ClassFunctions(TestFunctions2.class, "test"));
        // Functions from the current package, invokable with the 'call' prefix.
        lib.addFunctions(new PackageFunctions("", "call"));
        // Functions from a specific package, invokable with the 'jxpathtest' prefix.
        lib.addFunctions(new PackageFunctions("org.apache.commons.jxpath.ri.compiler.", "jxpathtest"));
        // Functions from a package with no prefix (e.g., for FQCN calls).
        lib.addFunctions(new PackageFunctions("", null));
        context.setFunctions(lib);

        // Arrange: Declare variables for use in XPath expressions.
        final Variables vars = context.getVariables();
        vars.declareVariable("test", new TestFunctions(4, "test"));
        vars.declareVariable("List.class", List.class);
        vars.declareVariable("NodeSet.class", NodeSet.class);

        // Arrange: Prepare for direct function lookup tests.
        classFunctions = new ClassFunctions(TestFunctions.class, "test");

        // Arrange: Save the original type converter to restore it after tests.
        originalTypeConverter = TypeUtils.getTypeConverter();
    }

    @AfterEach
    public void tearDown() {
        // Clean up: Restore the original type converter to avoid side effects.
        TypeUtils.setTypeConverter(originalTypeConverter);
    }

    @Nested
    @DisplayName("Constructor Calls")
    class ConstructorCalls {
        @Test
        void shouldCreateObjectWithDefaultConstructor() {
            // Act & Assert
            assertXPathValue(context, "string(test:new())", "foo=0; bar=null",
                    "Should call default constructor via ClassFunctions prefix.");
        }

        @Test
        void shouldCreateObjectWithDefaultConstructorUsingPackageFunctions() {
            // Act & Assert
            assertXPathValue(context, "string(jxpathtest:TestFunctions.new())", "foo=0; bar=null",
                    "Should call default constructor via PackageFunctions and class name.");
        }

        @Test
        void shouldCreateObjectWithDefaultConstructorUsingFqcn() {
            // Act & Assert
            final String xpath = "string(" + TestFunctions.class.getName() + ".new())";
            assertXPathValue(context, xpath, "foo=0; bar=null",
                    "Should call default constructor via a fully qualified class name.");
        }

        @Test
        void shouldCreateObjectWithCustomConstructor() {
            // Act & Assert
            assertXPathValue(context, "string(test:new(3, 'baz'))", "foo=3; bar=baz",
                    "Should call constructor with matching arguments.");
        }

        @Test
        void shouldCreateObjectWithArgumentTypeConversion() {
            // Act & Assert
            assertXPathValue(context, "string(test:new('3', 4))", "foo=3; bar=4.0",
                    "Should perform type conversion for constructor arguments.");
        }

        @Test
        void shouldCreateObjectWithVariableArgument() {
            // Arrange
            context.getVariables().declareVariable("A", "baz");

            // Act & Assert
            assertXPathValue(context, "string(test:new(2, $A, false))", "foo=2; bar=baz",
                    "Should resolve variables used as constructor arguments.");
        }

        @Test
        void shouldFindMatchingConstructorDirectly() {
            // Arrange
            final Object[] args = { 1, "x" };

            // Act
            final Function func = classFunctions.getFunction("test", "new", args);
            final Object result = func.invoke(new TestExpressionContext(null), args);

            // Assert
            assertEquals("foo=1; bar=x", result.toString(),
                    "getFunction should find the constructor matching the argument types.");
        }

        @Test
        void shouldFindMatchingConstructorUsingExpressionContext() {
            // Arrange
            final Object[] args = { "baz" };
            final ExpressionContext expressionContext = new TestExpressionContext(1);

            // Act
            final Function func = classFunctions.getFunction("test", "new", args);
            final Object result = func.invoke(expressionContext, args);

            // Assert
            assertEquals("foo=1; bar=baz", result.toString(),
                    "getFunction should use the expression context to supply the first argument.");
        }
    }

    @Nested
    @DisplayName("Static Method Calls")
    class StaticMethodCalls {
        @Test
        void shouldCallStaticMethod() {
            // Act & Assert
            assertXPathValue(context, "string(test:build(8, 'goober'))", "foo=8; bar=goober",
                    "Should call a static method via ClassFunctions prefix.");
        }

        @Test
        void shouldCallStaticMethodUsingPackageFunctions() {
            // Act & Assert
            final String xpath = "string(jxpathtest:TestFunctions.build(8, 'goober'))";
            assertXPathValue(context, xpath, "foo=8; bar=goober",
                    "Should call a static method via PackageFunctions and class name.");
        }

        @Test
        void shouldCallStaticMethodUsingFqcn() {
            // Act & Assert
            final String xpath = "string(" + TestFunctions.class.getName() + ".build(8, 'goober'))";
            assertXPathValue(context, xpath, "foo=8; bar=goober",
                    "Should call a static method via a fully qualified class name.");
        }

        @Test
        void shouldCallStaticMethodFromDifferentClassWithSamePrefix() {
            // Act & Assert
            assertXPathValue(context, "string(test:increment(8))", "9",
                    "Should resolve to TestFunctions2.increment when two classes share a prefix.");
        }

        @Test
        void shouldConvertNodeSetArgumentToString() {
            // Act & Assert
            assertXPathValue(context, "test:string(/beans/name)", "Name 1",
                    "Should automatically convert a NodeSet argument to its string value.");
        }

        @Test
        void shouldFindMatchingStaticMethodDirectly() {
            // Arrange
            final Object[] args = { 1, "x" };

            // Act
            final Function func = classFunctions.getFunction("test", "build", args);
            final Object result = func.invoke(new TestExpressionContext(null), args);

            // Assert
            assertEquals("foo=1; bar=x", result.toString(),
                    "getFunction should find the static method matching the argument types.");
        }

        @Test
        void shouldFindMatchingStaticMethodWithArgumentTypeConversion() {
            // Arrange
            final Object[] args = { "7", 1 };

            // Act
            final Function func = classFunctions.getFunction("test", "build", args);
            final Object result = func.invoke(new TestExpressionContext(null), args);

            // Assert
            assertEquals("foo=7; bar=1", result.toString(),
                    "getFunction should find a static method that is applicable after type conversion.");
        }

        @Test
        void shouldFindStaticMethodUsingExpressionContextDirectly() {
            // Arrange
            final Object[] args = {};
            final ExpressionContext expressionContext = new TestExpressionContext(1);

            // Act
            final Function func = classFunctions.getFunction("test", "path", args);
            final Object result = func.invoke(expressionContext, args);

            // Assert
            assertEquals("1", result,
                    "getFunction should find a static method that uses the expression context.");
        }
    }

    @Nested
    @DisplayName("Instance Method Calls")
    class InstanceMethodCalls {
        @Test
        void shouldCallBuiltInStringMethod() {
            // Act & Assert
            assertXPathValue(context, "length('foo')", 3,
                    "Should call standard Java methods on objects like String.");
        }

        @Test
        void shouldCallMethodOnObjectFromVariable() {
            // Act & Assert
            assertXPathValue(context, "string(getFoo($test))", "4",
                    "Should call an instance method on an object supplied by a variable.");
        }

        @Test
        void shouldCallMethodWithMultipleArguments() {
            // Act & Assert
            assertXPathValue(context, "string(test:setFooAndBar($test, 7, 'biz'))", "foo=7; bar=biz",
                    "Should call an instance method with multiple arguments.");
        }

        @Test
        void shouldFindMatchingInstanceMethodDirectly() {
            // Arrange
            final Object[] args = { new TestFunctions() };

            // Act
            final Function func = classFunctions.getFunction("test", "getFoo", args);
            final Object result = func.invoke(new TestExpressionContext(null), args);

            // Assert
            assertEquals("0", result.toString(),
                    "getFunction should find an instance method matching the argument types.");
        }

        @Test
        void shouldFindInstanceMethodUsingExpressionContextDirectly() {
            // Arrange
            final Object[] args = { new TestFunctions() };
            final ExpressionContext expressionContext = new TestExpressionContext(1);

            // Act
            final Function func = classFunctions.getFunction("test", "instancePath", args);
            final Object result = func.invoke(expressionContext, args);

            // Assert
            assertEquals("1", result,
                    "getFunction should find an instance method that uses the expression context.");
        }
    }

    @Nested
    @DisplayName("Functions Using ExpressionContext")
    class FunctionsUsingExpressionContext {
        @Test
        void shouldAccessCurrentNodeInPredicate() {
            // Act & Assert
            assertXPathValue(context, "//.[test:isMap()]/Key1", "Value 1",
                    "Function in predicate should use ExpressionContext to evaluate the current node.");
        }

        @Test
        void shouldAccessContextNodesToPerformCount() {
            // Act & Assert
            assertXPathValue(context, "count(//.[test:count(strings) = 3])", 7.0,
                    "Function should receive all context nodes matching its argument expression.");
        }

        @Test
        void shouldPassNodeValuesToFunction() {
            // Act & Assert
            assertXPathValue(context, "test:count(//strings)", 21,
                    "Function should receive a collection of node values (Strings).");
        }

        @Test
        void shouldPassNodePointersToFunction() {
            // Act & Assert
            assertXPathValue(context, "test:countPointers(//strings)", 21,
                    "Function should receive a collection of Pointers.");
        }

        @Test
        void shouldAccessCurrentPointerPath() {
            // Act & Assert
            assertXPathValue(context, "/beans[contains(test:path(), '[2]')]/name", "Name 2",
                    "Function should use ExpressionContext to get the path of the current pointer.");
        }
    }

    @Nested
    @DisplayName("Functions Returning Collections")
    class FunctionsReturningCollections {
        @Test
        void shouldHandleFunctionReturningCollectionOfBeans() {
            // Act & Assert
            assertXPathValueIterator(context, "test:collection()/name", list("foo", "bar"));
            assertXPathPointerIterator(context, "test:collection()/name", list("/.[1]/name", "/.[2]/name"));
            assertXPathValue(context, "test:collection()/name", "foo");
            assertXPathValue(context, "test:collection()/@name", "foo");
        }

        @Test
        void shouldHandleFunctionReturningCollectionOfValues() {
            // Arrange
            final List<String> list = new ArrayList<>();
            list.add("foo");
            list.add("bar");
            context.getVariables().declareVariable("list", list);

            // Act
            final Object values = context.getValue("test:items($list)");

            // Assert
            assertInstanceOf(Collection.class, values, "Return type should be a Collection.");
            assertEquals(list, new ArrayList<>((Collection<?>) values), "Returned collection should match the original.");
        }

        @Test
        void shouldHandleFunctionReturningNodeSet() {
            // Arrange
            final TestBean testBean = (TestBean) context.getContextBean();

            // Act & Assert
            assertXPathValueIterator(context, "test:nodeSet()/name", list("Name 1", "Name 2"));
            assertXPathValueIterator(context, "test:nodeSet()", list(testBean.getBeans()[0], testBean.getBeans()[1]));
            assertXPathPointerIterator(context, "test:nodeSet()/name", list("/beans[1]/name", "/beans[2]/name"));
            assertXPathValueAndPointer(context, "test:nodeSet()/name", "Name 1", "/beans[1]/name");
            assertXPathValueAndPointer(context, "test:nodeSet()/@name", "Name 1", "/beans[1]/@name");
            assertEquals(2, ((Number) context.getValue("count(test:nodeSet())")).intValue());
            assertXPathValue(context, "test:nodeSet()", testBean.getBeans()[0]);
        }
    }

    @Nested
    @DisplayName("Method Calls on Collections")
    class MethodCallsOnCollections {
        @Test
        void shouldCallSizeMethodOnCollectionVariable() {
            // Arrange
            final List<String> list = new ArrayList<>();
            list.add("foo");
            context.getVariables().declareVariable("myList", list);

            // Act & Assert
            assertXPathValue(context, "size($myList)", 1);
        }

        @Test
        void shouldCallSizeMethodOnNodeSetResult() {
            // Act & Assert
            assertXPathValue(context, "size(beans)", 2);
        }

        @Test
        void shouldCallAddMethodOnCollectionVariable() {
            // Arrange
            final List<String> list = new ArrayList<>();
            list.add("foo");
            context.getVariables().declareVariable("myList", list);

            // Act
            context.getValue("add($myList, 'hello')");

            // Assert
            assertEquals(2, list.size(), "List size should be 2 after calling add().");
        }

        @Test
        void shouldCallSizeMethodOnRootCollection() {
            // Arrange
            final JXPathContext collectionContext = JXPathContext.newContext(new ArrayList<>());

            // Act & Assert
            assertEquals("0", String.valueOf(collectionContext.getValue("size(/)")),
                    "size() extension function should work on a root collection.");
        }
    }

    @Nested
    @DisplayName("NodeSet Type Conversion")
    class NodeSetTypeConversion {
        @Test
        void isInstance_shouldIdentifyNodeSetAsList_byDefault() {
            // Act & Assert
            assertXPathValue(context, "test:isInstance(//strings, $List.class)", Boolean.TRUE,
                    "By default, a NodeSet should be convertible to a List.");
            assertXPathValue(context, "test:isInstance(//strings, $NodeSet.class)", Boolean.FALSE,
                    "By default, a NodeSet is not considered an instance of the NodeSet interface itself.");
        }

        @Test
        void isInstance_shouldIdentifyNodeSetAsNodeSet_withJXPath11Compatibility() {
            // Arrange
            TypeUtils.setTypeConverter(new JXPath11CompatibleTypeConverter());

            // Act & Assert
            assertXPathValue(context, "test:isInstance(//strings, $List.class)", Boolean.FALSE,
                    "With JXPath 1.1 compatibility, a NodeSet is not a List.");
            assertXPathValue(context, "test:isInstance(//strings, $NodeSet.class)", Boolean.TRUE,
                    "With JXPath 1.1 compatibility, a NodeSet is an instance of the NodeSet interface.");
        }
    }
}