package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.AbstractJXPathTest;
import org.apache.commons.jxpath.ClassFunctions;
import org.apache.commons.jxpath.FunctionLibrary;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.PackageFunctions;
import org.apache.commons.jxpath.TestBean;
import org.apache.commons.jxpath.util.TypeConverter;
import org.apache.commons.jxpath.util.TypeUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for extension function calls in JXPath.
 * This suite verifies various ways of defining and calling static Java methods
 * from XPath expressions.
 */
@DisplayName("Extension Function Tests")
class ExtensionFunctionTest {

    private static JXPathContext context;

    private TypeConverter originalTypeConverter;

    @BeforeAll
    static void setUpClass() {
        // The JXPathContext and its function library are expensive to set up
        // and are read-only for these tests. Using @BeforeAll makes the setup
        // run only once for all tests in this class.
        final TestBean testBean = new TestBean();
        context = JXPathContext.newContext(testBean);

        final FunctionLibrary functionLibrary = new FunctionLibrary();
        // Maps static methods in TestFunctions to the "test:" prefix.
        functionLibrary.addFunctions(new ClassFunctions(TestFunctions.class, "test"));
        // Maps static methods in TestFunctions2 to the same "test:" prefix.
        // JXPath resolves this by using the last one added for a given function signature.
        functionLibrary.addFunctions(new ClassFunctions(TestFunctions2.class, "test"));
        // Allows calling static methods from a specific package using the "jxpathtest:" prefix.
        functionLibrary.addFunctions(new PackageFunctions("org.apache.commons.jxpath.ri.compiler.", "jxpathtest"));
        // Allows calling static methods using their fully qualified class name without a prefix.
        functionLibrary.addFunctions(new PackageFunctions("", null));

        context.setFunctions(functionLibrary);
    }

    @BeforeEach
    void setUp() {
        // Back up the original type converter to ensure test isolation.
        originalTypeConverter = TypeUtils.getTypeConverter();
    }

    @AfterEach
    void tearDown() {
        // Restore the original type converter to avoid side effects on other tests.
        TypeUtils.setTypeConverter(originalTypeConverter);
    }

    @Test
    @DisplayName("Call static method via ClassFunctions prefix")
    void callStaticMethodUsingClassFunctions() {
        // Asserts that a static method can be called using a prefix defined by ClassFunctions.
        assertXPathValue(context, "string(test:build(8, 'goober'))", "foo=8; bar=goober");
    }

    @Test
    @DisplayName("Call static method via PackageFunctions prefix")
    void callStaticMethodUsingPackageFunctions() {
        // Asserts that a static method can be called using a prefix and class name
        // as defined by PackageFunctions.
        assertXPathValue(context, "string(jxpathtest:TestFunctions.build(8, 'goober'))", "foo=8; bar=goober");
    }

    @Test
    @DisplayName("Call static method via fully qualified class name")
    void callStaticMethodUsingFullyQualifiedClassName() {
        // Asserts that a static method can be called using its fully qualified class name
        // when PackageFunctions is configured with no prefix.
        final String fqcn = TestFunctions.class.getName();
        final String xpath = String.format("string(%s.build(8, 'goober'))", fqcn);
        assertXPathValue(context, xpath, "foo=8; bar=goober");
    }

    @Test
    @DisplayName("Function resolution with conflicting prefixes favors the last one added")
    void functionResolutionWithConflictingPrefixes() {
        // TestFunctions and TestFunctions2 both register functions with the "test:" prefix.
        // TestFunctions2 was added last, so its 'increment' method should be resolved.
        assertXPathValue(context, "string(test:increment(8))", "9");
    }

    @Test
    @DisplayName("Function argument converts NodeSet to its value")
    void functionArgumentConvertsNodeSet() {
        // Asserts that a NodeSet passed as an argument to an extension function
        // is correctly converted to its underlying value (in this case, a String).
        assertXPathValue(context, "test:string(/beans/name)", "Name 1");
    }
}