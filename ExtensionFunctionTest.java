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
 * Higher-level tests for invoking extension functions from XPath.
 * These tests cover:
 * - Object allocation via constructors
 * - Static and instance method calls
 * - Function lookup with and without ExpressionContext
 * - NodeSet and Collection conversions
 */
class ExtensionFunctionTest extends AbstractJXPathTest {

    private static final String PREFIX_TEST = "test";
    private static final String PREFIX_JXPathTest = "jxpathtest";
    private static final String VAR_LIST_CLASS = "List.class";
    private static final String VAR_NODESET_CLASS = "NodeSet.class";

    /**
     * Simple ExpressionContext stub used to drive functions that need access
     * to the current context node or its pointer.
     */
    private static final class TestExpressionContext implements ExpressionContext {

        private final Object contextObject;

        TestExpressionContext(final Object contextObject) {
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

    private Functions functions;
    private JXPathContext context;
    private TestBean testBean;
    private TypeConverter originalTypeConverter;

    @Override
    @BeforeEach
    public void setUp() {
        // Always create a fresh context per test to avoid test inter-dependencies.
        testBean = new TestBean();
        context = JXPathContext.newContext(testBean);

        final Variables vars = context.getVariables();
        // Variable exposed to XPath functions
        vars.declareVariable("test", new TestFunctions(4, "test"));

        // Register functions available to the context
        context.setFunctions(buildFunctionLibrary());

        // Variables used by tests that need to check type-conversion behavior
        vars.declareVariable(VAR_LIST_CLASS, List.class);
        vars.declareVariable(VAR_NODESET_CLASS, NodeSet.class);

        // Convenience handle to the "test" function namespace for lookup tests
        functions = new ClassFunctions(TestFunctions.class, PREFIX_TEST);

        // Remember and restore the converter around tests that change it
        originalTypeConverter = TypeUtils.getTypeConverter();
    }

    private static FunctionLibrary buildFunctionLibrary() {
        final FunctionLibrary lib = new FunctionLibrary();
        // Two classes share the same prefix "test"
        lib.addFunctions(new ClassFunctions(TestFunctions.class, PREFIX_TEST));
        lib.addFunctions(new ClassFunctions(TestFunctions2.class, PREFIX_TEST));

        // Invoke methods by their Java names (prefix is ignored for call:)
        lib.addFunctions(new PackageFunctions("", "call"));

        // Access by simple class name using the jxpathtest: prefix
        lib.addFunctions(new PackageFunctions("org.apache.commons.jxpath.ri.compiler.", PREFIX_JXPathTest));

        // Allow calling methods with no prefix (plain Java methods)
        lib.addFunctions(new PackageFunctions("", null));
        return lib;
    }

    @AfterEach
    public void tearDown() {
        TypeUtils.setTypeConverter(originalTypeConverter);
    }

    // ------------------------------------------------------------
    // Allocation via constructors
    // ------------------------------------------------------------

    @Test
    void testAllocation() {
        // Default constructor
        assertXPathValue(context, "string(test:new())", "foo=0; bar=null");

        // Using PackageFunctions and simple class name
        assertXPathValue(context, "string(jxpathtest:TestFunctions.new())", "foo=0; bar=null");

        // Fully qualified class name
        assertXPathValue(context, "string(" + TestFunctions.class.getName() + ".new())", "foo=0; bar=null");

        // Custom constructor
        assertXPathValue(context, "string(test:new(3, 'baz'))", "foo=3; bar=baz");

        // Custom constructor with type conversion
        assertXPathValue(context, "string(test:new('3', 4))", "foo=3; bar=4.0");

        // Custom constructor with an extra boolean argument (ignored)
        context.getVariables().declareVariable("A", "baz");
        assertXPathValue(context, "string(test:new(2, $A, false))", "foo=2; bar=baz");
    }

    // ------------------------------------------------------------
    // Type conversion: NodeSet vs Collection behavior
    // ------------------------------------------------------------

    @Test
    void testEstablishNodeSetBaseline() {
        // Default behavior: NodeSet is presented as a List to extension methods
        assertXPathValue(context, "test:isInstance(//strings, $List.class)", Boolean.TRUE);
        assertXPathValue(context, "test:isInstance(//strings, $NodeSet.class)", Boolean.FALSE);
    }

    @Test
    void testBCNodeSetHack() {
        // Legacy converter reports NodeSets as NodeSet instead of List
        TypeUtils.setTypeConverter(new JXPath11CompatibleTypeConverter());

        assertXPathValue(context, "test:isInstance(//strings, $List.class)", Boolean.FALSE);
        assertXPathValue(context, "test:isInstance(//strings, $NodeSet.class)", Boolean.TRUE);
    }

    // ------------------------------------------------------------
    // Collections and NodeSets returned from functions
    // ------------------------------------------------------------

    @Test
    void testCollectionReturn() {
        assertXPathValueIterator(context, "test:collection()/name", list("foo", "bar"));
        assertXPathPointerIterator(context, "test:collection()/name", list("/.[1]/name", "/.[2]/name"));
        assertXPathValue(context, "test:collection()/name", "foo");
        assertXPathValue(context, "test:collection()/@name", "foo");

        final List<String> list = new ArrayList<>();
        list.add("foo");
        list.add("bar");
        context.getVariables().declareVariable("list", list);

        final Object values = context.getValue("test:items($list)");
        assertInstanceOf(Collection.class, values, "Expected a Collection to be returned");

        @SuppressWarnings("unchecked")
        final Collection<Object> collection = (Collection<Object>) values;
        assertEquals(list, new ArrayList<>(collection), "Returned collection should match the input");
    }

    @Test
    void testNodeSetReturn() {
        assertXPathValueIterator(context, "test:nodeSet()/name", list("Name 1", "Name 2"));
        assertXPathValueIterator(context, "test:nodeSet()", list(testBean.getBeans()[0], testBean.getBeans()[1]));
        assertXPathPointerIterator(context, "test:nodeSet()/name", list("/beans[1]/name", "/beans[2]/name"));
        assertXPathValueAndPointer(context, "test:nodeSet()/name", "Name 1", "/beans[1]/name");
        assertXPathValueAndPointer(context, "test:nodeSet()/@name", "Name 1", "/beans[1]/@name");
        assertEquals(2, ((Number) context.getValue("count(test:nodeSet())")).intValue(), "NodeSet should contain two items");
        assertXPathValue(context, "test:nodeSet()", testBean.getBeans()[0]);
    }

    // ------------------------------------------------------------
    // Calling methods on collections via extension functions
    // ------------------------------------------------------------

    @Test
    void testCollectionMethodCall() {
        final List<String> list = new ArrayList<>();
        list.add("foo");
        context.getVariables().declareVariable("myList", list);

        assertXPathValue(context, "size($myList)", Integer.valueOf(1));
        assertXPathValue(context, "size(beans)", Integer.valueOf(2));

        // Call mutating method through XPath
        context.getValue("add($myList, 'hello')");
        assertEquals(2, list.size(), "After adding an element via extension function");

        // Extension function on a root collection
        final JXPathContext rootContext = JXPathContext.newContext(new ArrayList<>());
        assertEquals("0", String.valueOf(rootContext.getValue("size(/)")), "Size of empty root collection");
    }

    // ------------------------------------------------------------
    // Static method calls
    // ------------------------------------------------------------

    @Test
    void testStaticMethodCall() {
        assertXPathValue(context, "string(test:build(8, 'goober'))", "foo=8; bar=goober");

        // Call a static method using PackageFunctions and class name
        assertXPathValue(context, "string(jxpathtest:TestFunctions.build(8, 'goober'))", "foo=8; bar=goober");

        // Call a static method using a fully qualified class name
        assertXPathValue(context, "string(" + TestFunctions.class.getName() + ".build(8, 'goober'))", "foo=8; bar=goober");

        // Two ClassFunctions share the same prefix "test" — this calls TestFunctions2.increment
        assertXPathValue(context, "string(test:increment(8))", "9");

        // A NodeSet should be converted to a string properly
        assertXPathValue(context, "test:string(/beans/name)", "Name 1");
    }

    @Test
    void testStaticMethodLookup() {
        final Object[] args = { Integer.valueOf(1), "x" };
        final Function func = functions.getFunction(PREFIX_TEST, "build", args);
        assertEquals("foo=1; bar=x", func.invoke(new TestExpressionContext(null), args).toString(), "test:build(1, 'x')");
    }

    @Test
    void testStaticMethodLookupWithConversion() {
        final Object[] args = { "7", Integer.valueOf(1) };
        final Function func = functions.getFunction(PREFIX_TEST, "build", args);
        assertEquals("foo=7; bar=1", func.invoke(new TestExpressionContext(null), args).toString(), "test:build('7', 1)");
    }

    @Test
    void testStaticMethodLookupWithExpressionContext() {
        final Object[] args = {};
        final Function func = functions.getFunction(PREFIX_TEST, "path", args);
        assertEquals("1", func.invoke(new TestExpressionContext(Integer.valueOf(1)), args), "test:path() should reflect context");
    }

    // ------------------------------------------------------------
    // Instance method calls
    // ------------------------------------------------------------

    @Test
    void testMethodCall() {
        assertXPathValue(context, "length('foo')", Integer.valueOf(3));

        // We are just calling a method - prefix is ignored
        assertXPathValue(context, "call:substring('foo', 1, 2)", "o");

        // Invoke a function implemented as a regular method
        assertXPathValue(context, "string(test:getFoo($test))", "4");

        // Prefix is ignored for method dispatch
        assertXPathValue(context, "string(call:getFoo($test))", "4");
        assertXPathValue(context, "string(getFoo($test))", "4");

        // Method with two arguments
        assertXPathValue(context, "string(test:setFooAndBar($test, 7, 'biz'))", "foo=7; bar=biz");
    }

    @Test
    void testMethodLookup() {
        final Object[] args = { new TestFunctions() };
        final Function func = functions.getFunction(PREFIX_TEST, "getFoo", args);
        assertEquals("0", func.invoke(new TestExpressionContext(null), args).toString(), "test:getFoo($test)");
    }

    @Test
    void testConstructorLookup() {
        final Object[] args = { Integer.valueOf(1), "x" };
        final Function func = functions.getFunction(PREFIX_TEST, "new", args);
        assertEquals("foo=1; bar=x", func.invoke(new TestExpressionContext(null), args).toString(), "test:new(1, 'x')");
    }

    // ------------------------------------------------------------
    // Functions that use ExpressionContext
    // ------------------------------------------------------------

    @Test
    void testConstructorLookupWithExpressionContext() {
        final Object[] args = { "baz" };
        final Function func = functions.getFunction(PREFIX_TEST, "new", args);
        assertEquals("foo=1; bar=baz", func.invoke(new TestExpressionContext(Integer.valueOf(1)), args).toString(), "test:new('baz') with context");
    }

    @Test
    void testMethodLookupWithExpressionContext() {
        final Object[] args = { new TestFunctions() };
        final Function func = functions.getFunction(PREFIX_TEST, "instancePath", args);
        assertEquals("1", func.invoke(new TestExpressionContext(Integer.valueOf(1)), args), "test:instancePath() reflects context");
    }

    @Test
    void testMethodLookupWithExpressionContextAndArgument() {
        final Object[] args = { new TestFunctions(), "*" };
        final Function func = functions.getFunction(PREFIX_TEST, "pathWithSuffix", args);
        assertEquals("1*", func.invoke(new TestExpressionContext(Integer.valueOf(1)), args), "test:pathWithSuffix('*') reflects context + suffix");
    }

    @Test
    void testExpressionContext() {
        // Execute a function for each node; function inspects the current node via ExpressionContext.
        assertXPathValue(context, "//.[test:isMap()]/Key1", "Value 1");

        // Function evaluates all nodes in the current context.
        assertXPathValue(context, "count(//.[test:count(strings) = 3])", Double.valueOf(7));

        // Function receives a collection of string values (type check)
        assertXPathValue(context, "test:count(//strings)", Integer.valueOf(21));

        // Function receives a collection of pointers (type check)
        assertXPathValue(context, "test:countPointers(//strings)", Integer.valueOf(21));

        // Function reads current pointer path from ExpressionContext
        assertXPathValue(context, "/beans[contains(test:path(), '[2]')]/name", "Name 2");
    }
}