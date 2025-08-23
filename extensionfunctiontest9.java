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

public class ExtensionFunctionTestTest9 extends AbstractJXPathTest {

    private Functions functions;

    private JXPathContext context;

    private TestBean testBean;

    private TypeConverter typeConverter;

    @Override
    @BeforeEach
    public void setUp() {
        if (context == null) {
            testBean = new TestBean();
            context = JXPathContext.newContext(testBean);
            final Variables vars = context.getVariables();
            vars.declareVariable("test", new TestFunctions(4, "test"));
            final FunctionLibrary lib = new FunctionLibrary();
            lib.addFunctions(new ClassFunctions(TestFunctions.class, "test"));
            lib.addFunctions(new ClassFunctions(TestFunctions2.class, "test"));
            lib.addFunctions(new PackageFunctions("", "call"));
            lib.addFunctions(new PackageFunctions("org.apache.commons.jxpath.ri.compiler.", "jxpathtest"));
            lib.addFunctions(new PackageFunctions("", null));
            context.setFunctions(lib);
            context.getVariables().declareVariable("List.class", List.class);
            context.getVariables().declareVariable("NodeSet.class", NodeSet.class);
        }
        functions = new ClassFunctions(TestFunctions.class, "test");
        typeConverter = TypeUtils.getTypeConverter();
    }

    @AfterEach
    public void tearDown() {
        TypeUtils.setTypeConverter(typeConverter);
    }

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

    @Test
    void testMethodCall() {
        assertXPathValue(context, "length('foo')", Integer.valueOf(3));
        // We are just calling a method - prefix is ignored
        assertXPathValue(context, "call:substring('foo', 1, 2)", "o");
        // Invoke a function implemented as a regular method
        assertXPathValue(context, "string(test:getFoo($test))", "4");
        // Note that the prefix is ignored anyway, we are just calling a method
        assertXPathValue(context, "string(call:getFoo($test))", "4");
        // We don't really need to supply a prefix in this case
        assertXPathValue(context, "string(getFoo($test))", "4");
        // Method with two arguments
        assertXPathValue(context, "string(test:setFooAndBar($test, 7, 'biz'))", "foo=7; bar=biz");
    }
}
