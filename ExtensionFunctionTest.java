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
 * Tests extension function capabilities including object construction, method invocation, 
 * expression context handling, and collection/node set operations.
 */
class ExtensionFunctionTest extends AbstractJXPathTest {

    // Constants for common strings and paths
    private static final String TEST_FUNCTIONS_CLASS_NAME = TestFunctions.class.getName();
    private static final String BEANS_PATH = "beans";
    private static final String NAME_PATH = "name";
    private static final String STRINGS_PATH = "strings";
    private static final String TEST_PREFIX = "test";
    private static final String JXPATH_TEST_PREFIX = "jxpathtest";

    private static final class Context implements ExpressionContext {

        private final Object object;

        public Context(final Object object) {
            this.object = object;
        }

        @Override
        public List<Pointer> getContextNodeList() {
            return null;
        }

        @Override
        public Pointer getContextNodePointer() {
            return NodePointer.newNodePointer(null, object, Locale.getDefault());
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
    private TypeConverter typeConverter;

    @Override
    @BeforeEach
    public void setUp() {
        if (context == null) {
            // Initialize test context and variables
            testBean = new TestBean();
            context = JXPathContext.newContext(testBean);
            
            // Configure variables
            final Variables vars = context.getVariables();
            vars.declareVariable("test", new TestFunctions(4, "test"));
            vars.declareVariable("List.class", List.class);
            vars.declareVariable("NodeSet.class", NodeSet.class);
            
            // Set up function libraries
            final FunctionLibrary lib = new FunctionLibrary();
            lib.addFunctions(new ClassFunctions(TestFunctions.class, TEST_PREFIX));
            lib.addFunctions(new ClassFunctions(TestFunctions2.class, TEST_PREFIX));
            lib.addFunctions(new PackageFunctions("", "call"));
            lib.addFunctions(new PackageFunctions("org.apache.commons.jxpath.ri.compiler.", JXPATH_TEST_PREFIX));
            lib.addFunctions(new PackageFunctions("", null));
            context.setFunctions(lib);
        }
        functions = new ClassFunctions(TestFunctions.class, TEST_PREFIX);
        typeConverter = TypeUtils.getTypeConverter();
    }

    @AfterEach
    public void tearDown() {
        TypeUtils.setTypeConverter(typeConverter);
    }

    //-----------------------------------------------------
    // Tests for object construction
    //-----------------------------------------------------
    
    @Test
    void testNewObjectWithDefaultConstructor() {
        assertXPathValue(context, "string(test:new())", "foo=0; bar=null");
        assertXPathValue(context, "string(jxpathtest:TestFunctions.new())", "foo=0; bar=null");
        assertXPathValue(context, "string(" + TEST_FUNCTIONS_CLASS_NAME + ".new())", "foo=0; bar=null");
    }

    @Test
    void testNewObjectWithParameterizedConstructor() {
        assertXPathValue(context, "string(test:new(3, 'baz'))", "foo=3; bar=baz");
    }

    @Test
    void testNewObjectWithTypeConversion() {
        assertXPathValue(context, "string(test:new('3', 4))", "foo=3; bar=4.0");
    }

    @Test
    void testNewObjectWithVariableArgument() {
        context.getVariables().declareVariable("A", "baz");
        assertXPathValue(context, "string(test:new(2, $A, false))", "foo=2; bar=baz");
    }

    //-----------------------------------------------------
    // Tests for method invocation
    //-----------------------------------------------------
    
    @Test
    void testInstanceMethodCall() {
        assertXPathValue(context, "string(test:getFoo($test))", "4");
        assertXPathValue(context, "string(call:getFoo($test))", "4");
        assertXPathValue(context, "string(getFoo($test))", "4");
    }

    @Test
    void testInstanceMethodWithMultipleArguments() {
        assertXPathValue(context, "string(test:setFooAndBar($test, 7, 'biz'))", "foo=7; bar=biz");
    }

    @Test
    void testStaticMethodCall() {
        assertXPathValue(context, "string(test:build(8, 'goober'))", "foo=8; bar=goober");
        assertXPathValue(context, "string(jxpathtest:TestFunctions.build(8, 'goober'))", "foo=8; bar=goober");
        assertXPathValue(context, "string(" + TEST_FUNCTIONS_CLASS_NAME + ".build(8, 'goober'))", "foo=8; bar=goober");
    }

    @Test
    void testStaticMethodFromDifferentClass() {
        assertXPathValue(context, "string(test:increment(8))", "9");
    }

    @Test
    void testNodeSetToStringConversion() {
        assertXPathValue(context, "test:string(/beans/name)", "Name 1");
    }

    //-----------------------------------------------------
    // Tests for expression context features
    //-----------------------------------------------------
    
    @Test
    void testExpressionContextForCurrentNode() {
        assertXPathValue(context, "//.[test:isMap()]/Key1", "Value 1");
    }

    @Test
    void testExpressionContextForNodeCount() {
        assertXPathValue(context, "count(//.[test:count(strings) = 3])", Double.valueOf(7));
    }

    @Test
    void testExpressionContextForCollectionTypeCheck() {
        assertXPathValue(context, "test:count(//strings)", Integer.valueOf(21));
    }

    @Test
    void testExpressionContextForPointerCount() {
        assertXPathValue(context, "test:countPointers(//strings)", Integer.valueOf(21));
    }

    @Test
    void testExpressionContextForPathRetrieval() {
        assertXPathValue(context, "/beans[contains(test:path(), '[2]')]/name", "Name 2");
    }

    //-----------------------------------------------------
    // Tests for collection and node set operations
    //-----------------------------------------------------
    
    @Test
    void testCollectionReturnType() {
        assertXPathValueIterator(context, "test:collection()/name", list("foo", "bar"));
        assertXPathPointerIterator(context, "test:collection()/name", list("/.[1]/name", "/.[2]/name"));
        assertXPathValue(context, "test:collection()/name", "foo");
        assertXPathValue(context, "test:collection()/@name", "foo");
    }

    @Test
    void testCollectionVariablePassing() {
        final List<String> list = new ArrayList<>();
        list.add("foo");
        list.add("bar");
        context.getVariables().declareVariable("list", list);
        final Object values = context.getValue("test:items($list)");
        
        assertInstanceOf(Collection.class, values, "Return type should be Collection");
        assertEquals(list, new ArrayList<>((Collection<?>) values), "Return values should match input");
    }

    @Test
    void testNodeSetOperations() {
        assertXPathValueIterator(context, "test:nodeSet()/name", list("Name 1", "Name 2"));
        assertXPathPointerIterator(context, "test:nodeSet()/name", list("/beans[1]/name", "/beans[2]/name"));
        assertXPathValueAndPointer(context, "test:nodeSet()/name", "Name 1", "/beans[1]/name");
        assertXPathValueAndPointer(context, "test:nodeSet()/@name", "Name 1", "/beans[1]/@name");
        assertEquals(2, ((Number) context.getValue("count(test:nodeSet())")).intValue());
        assertXPathValue(context, "test:nodeSet()", testBean.getBeans()[0]);
    }

    @Test
    void testCollectionMethodInvocation() {
        final List<String> list = new ArrayList<>();
        list.add("foo");
        context.getVariables().declareVariable("myList", list);
        
        assertXPathValue(context, "size($myList)", Integer.valueOf(1));
        assertXPathValue(context, "size(beans)", Integer.valueOf(2));
        
        context.getValue("add($myList, 'hello')");
        assertEquals(2, list.size(), "List size should increase after element addition");
        
        final JXPathContext ctx = JXPathContext.newContext(new ArrayList<>());
        assertEquals("0", String.valueOf(ctx.getValue("size(/)")), 
            "Extension function should work on root collection");
    }

    //-----------------------------------------------------
    // Tests for type compatibility features
    //-----------------------------------------------------
    
    @Test
    void testNodeSetBaselineBehavior() {
        assertXPathValue(context, "test:isInstance(//strings, $List.class)", Boolean.TRUE);
        assertXPathValue(context, "test:isInstance(//strings, $NodeSet.class)", Boolean.FALSE);
    }

    @Test
    void testBackwardCompatibilityNodeSet() {
        TypeUtils.setTypeConverter(new JXPath11CompatibleTypeConverter());
        assertXPathValue(context, "test:isInstance(//strings, $List.class)", Boolean.FALSE);
        assertXPathValue(context, "test:isInstance(//strings, $NodeSet.class)", Boolean.TRUE);
    }

    //-----------------------------------------------------
    // Tests for function lookup mechanisms
    //-----------------------------------------------------
    
    @Test
    void testConstructorLookup() {
        final Object[] args = { Integer.valueOf(1), "x" };
        final Function func = functions.getFunction(TEST_PREFIX, "new", args);
        assertEquals("foo=1; bar=x", func.invoke(new Context(null), args).toString());
    }

    @Test
    void testConstructorLookupWithExpressionContext() {
        final Object[] args = { "baz" };
        final Function func = functions.getFunction(TEST_PREFIX, "new", args);
        assertEquals("foo=1; bar=baz", func.invoke(new Context(Integer.valueOf(1)), args).toString());
    }

    @Test
    void testMethodLookup() {
        final Object[] args = { new TestFunctions() };
        final Function func = functions.getFunction(TEST_PREFIX, "getFoo", args);
        assertEquals("0", func.invoke(new Context(null), args).toString());
    }

    @Test
    void testMethodLookupWithExpressionContext() {
        final Object[] args = { new TestFunctions() };
        final Function func = functions.getFunction(TEST_PREFIX, "instancePath", args);
        assertEquals("1", func.invoke(new Context(Integer.valueOf(1)), args));
    }

    @Test
    void testMethodLookupWithExpressionContextAndArgument() {
        final Object[] args = { new TestFunctions(), "*" };
        final Function func = functions.getFunction(TEST_PREFIX, "pathWithSuffix", args);
        assertEquals("1*", func.invoke(new Context(Integer.valueOf(1)), args));
    }

    @Test
    void testStaticMethodLookup() {
        final Object[] args = { Integer.valueOf(1), "x" };
        final Function func = functions.getFunction(TEST_PREFIX, "build", args);
        assertEquals("foo=1; bar=x", func.invoke(new Context(null), args).toString());
    }

    @Test
    void testStaticMethodLookupWithConversion() {
        final Object[] args = { "7", Integer.valueOf(1) };
        final Function func = functions.getFunction(TEST_PREFIX, "build", args);
        assertEquals("foo=7; bar=1", func.invoke(new Context(null), args).toString());
    }

    @Test
    void testStaticMethodLookupWithExpressionContext() {
        final Object[] args = {};
        final Function func = functions.getFunction(TEST_PREFIX, "path", args);
        assertEquals("1", func.invoke(new Context(Integer.valueOf(1)), args));
    }
}