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
 * Test suite for verifying the functionality of extension functions in JXPath.
 */
class ExtensionFunctionTest extends AbstractJXPathTest {

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
    private TypeConverter originalTypeConverter;

    @BeforeEach
    public void setUp() {
        if (context == null) {
            initializeContext();
        }
        functions = new ClassFunctions(TestFunctions.class, "test");
        originalTypeConverter = TypeUtils.getTypeConverter();
    }

    @AfterEach
    public void tearDown() {
        TypeUtils.setTypeConverter(originalTypeConverter);
    }

    private void initializeContext() {
        testBean = new TestBean();
        context = JXPathContext.newContext(testBean);
        Variables vars = context.getVariables();
        vars.declareVariable("test", new TestFunctions(4, "test"));

        FunctionLibrary functionLibrary = new FunctionLibrary();
        functionLibrary.addFunctions(new ClassFunctions(TestFunctions.class, "test"));
        functionLibrary.addFunctions(new ClassFunctions(TestFunctions2.class, "test"));
        functionLibrary.addFunctions(new PackageFunctions("", "call"));
        functionLibrary.addFunctions(new PackageFunctions("org.apache.commons.jxpath.ri.compiler.", "jxpathtest"));
        functionLibrary.addFunctions(new PackageFunctions("", null));
        context.setFunctions(functionLibrary);

        vars.declareVariable("List.class", List.class);
        vars.declareVariable("NodeSet.class", NodeSet.class);
    }

    @Test
    void testObjectAllocation() {
        // Test object allocation using different constructors
        assertXPathValue(context, "string(test:new())", "foo=0; bar=null");
        assertXPathValue(context, "string(jxpathtest:TestFunctions.new())", "foo=0; bar=null");
        assertXPathValue(context, "string(" + TestFunctions.class.getName() + ".new())", "foo=0; bar=null");
        assertXPathValue(context, "string(test:new(3, 'baz'))", "foo=3; bar=baz");
        assertXPathValue(context, "string(test:new('3', 4))", "foo=3; bar=4.0");

        context.getVariables().declareVariable("A", "baz");
        assertXPathValue(context, "string(test:new(2, $A, false))", "foo=2; bar=baz");
    }

    @Test
    void testNodeSetCompatibility() {
        TypeUtils.setTypeConverter(new JXPath11CompatibleTypeConverter());
        assertXPathValue(context, "test:isInstance(//strings, $List.class)", Boolean.FALSE);
        assertXPathValue(context, "test:isInstance(//strings, $NodeSet.class)", Boolean.TRUE);
    }

    @Test
    void testCollectionOperations() {
        List<String> list = new ArrayList<>();
        list.add("foo");
        context.getVariables().declareVariable("myList", list);

        assertXPathValue(context, "size($myList)", 1);
        assertXPathValue(context, "size(beans)", 2);

        context.getValue("add($myList, 'hello')");
        assertEquals(2, list.size(), "After adding an element");

        JXPathContext rootContext = JXPathContext.newContext(new ArrayList<>());
        assertEquals("0", String.valueOf(rootContext.getValue("size(/)")), "Extension function on root collection");
    }

    @Test
    void testCollectionReturnValues() {
        assertXPathValueIterator(context, "test:collection()/name", list("foo", "bar"));
        assertXPathPointerIterator(context, "test:collection()/name", list("/.[1]/name", "/.[2]/name"));
        assertXPathValue(context, "test:collection()/name", "foo");
        assertXPathValue(context, "test:collection()/@name", "foo");

        List<String> list = new ArrayList<>();
        list.add("foo");
        list.add("bar");
        context.getVariables().declareVariable("list", list);

        Object values = context.getValue("test:items($list)");
        assertInstanceOf(Collection.class, values, "Return type: ");
        assertEquals(list, new ArrayList<>((Collection<?>) values), "Return values: ");
    }

    @Test
    void testConstructorFunctionLookup() {
        Object[] args = {1, "x"};
        Function func = functions.getFunction("test", "new", args);
        assertEquals("foo=1; bar=x", func.invoke(new Context(null), args).toString(), "test:new(1, x)");
    }

    @Test
    void testConstructorFunctionLookupWithContext() {
        Object[] args = {"baz"};
        Function func = functions.getFunction("test", "new", args);
        assertEquals("foo=1; bar=baz", func.invoke(new Context(1), args).toString(), "test:new('baz')");
    }

    @Test
    void testNodeSetBaseline() {
        assertXPathValue(context, "test:isInstance(//strings, $List.class)", Boolean.TRUE);
        assertXPathValue(context, "test:isInstance(//strings, $NodeSet.class)", Boolean.FALSE);
    }

    @Test
    void testExpressionContextUsage() {
        assertXPathValue(context, "//.[test:isMap()]/Key1", "Value 1");
        assertXPathValue(context, "count(//.[test:count(strings) = 3])", 7.0);
        assertXPathValue(context, "test:count(//strings)", 21);
        assertXPathValue(context, "test:countPointers(//strings)", 21);
        assertXPathValue(context, "/beans[contains(test:path(), '[2]')]/name", "Name 2");
    }

    @Test
    void testMethodInvocation() {
        assertXPathValue(context, "length('foo')", 3);
        assertXPathValue(context, "call:substring('foo', 1, 2)", "o");
        assertXPathValue(context, "string(test:getFoo($test))", "4");
        assertXPathValue(context, "string(call:getFoo($test))", "4");
        assertXPathValue(context, "string(getFoo($test))", "4");
        assertXPathValue(context, "string(test:setFooAndBar($test, 7, 'biz'))", "foo=7; bar=biz");
    }

    @Test
    void testMethodFunctionLookup() {
        Object[] args = {new TestFunctions()};
        Function func = functions.getFunction("test", "getFoo", args);
        assertEquals("0", func.invoke(new Context(null), args).toString(), "test:getFoo($test, 1, x)");
    }

    @Test
    void testMethodFunctionLookupWithContext() {
        Object[] args = {new TestFunctions()};
        Function func = functions.getFunction("test", "instancePath", args);
        assertEquals("1", func.invoke(new Context(1), args), "test:instancePath()");
    }

    @Test
    void testMethodFunctionLookupWithContextAndArgument() {
        Object[] args = {new TestFunctions(), "*"};
        Function func = functions.getFunction("test", "pathWithSuffix", args);
        assertEquals("1*", func.invoke(new Context(1), args), "test:pathWithSuffix('*')");
    }

    @Test
    void testNodeSetReturnValues() {
        assertXPathValueIterator(context, "test:nodeSet()/name", list("Name 1", "Name 2"));
        assertXPathValueIterator(context, "test:nodeSet()", list(testBean.getBeans()[0], testBean.getBeans()[1]));
        assertXPathPointerIterator(context, "test:nodeSet()/name", list("/beans[1]/name", "/beans[2]/name"));
        assertXPathValueAndPointer(context, "test:nodeSet()/name", "Name 1", "/beans[1]/name");
        assertXPathValueAndPointer(context, "test:nodeSet()/@name", "Name 1", "/beans[1]/@name");
        assertEquals(2, ((Number) context.getValue("count(test:nodeSet())")).intValue());
        assertXPathValue(context, "test:nodeSet()", testBean.getBeans()[0]);
    }

    @Test
    void testStaticMethodInvocation() {
        assertXPathValue(context, "string(test:build(8, 'goober'))", "foo=8; bar=goober");
        assertXPathValue(context, "string(jxpathtest:TestFunctions.build(8, 'goober'))", "foo=8; bar=goober");
        assertXPathValue(context, "string(" + TestFunctions.class.getName() + ".build(8, 'goober'))", "foo=8; bar=goober");
        assertXPathValue(context, "string(test:increment(8))", "9");
        assertXPathValue(context, "test:string(/beans/name)", "Name 1");
    }

    @Test
    void testStaticMethodFunctionLookup() {
        Object[] args = {1, "x"};
        Function func = functions.getFunction("test", "build", args);
        assertEquals("foo=1; bar=x", func.invoke(new Context(null), args).toString(), "test:build(1, x)");
    }

    @Test
    void testStaticMethodFunctionLookupWithConversion() {
        Object[] args = {"7", 1};
        Function func = functions.getFunction("test", "build", args);
        assertEquals("foo=7; bar=1", func.invoke(new Context(null), args).toString(), "test:build('7', 1)");
    }

    @Test
    void testStaticMethodFunctionLookupWithContext() {
        Object[] args = {};
        Function func = functions.getFunction("test", "path", args);
        assertEquals("1", func.invoke(new Context(1), args), "test:path()");
    }
}