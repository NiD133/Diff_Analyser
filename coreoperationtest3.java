package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.AbstractJXPathTest;
import org.apache.commons.jxpath.JXPathContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

/**
 * Tests the behavior of JXPath's core comparison operations when an operand is Double.NaN.
 * According to the XPath 1.0 specification and IEEE 754 standard, any ordered comparison
 * involving NaN should result in false. This test suite verifies JXPath's adherence to these rules.
 */
@DisplayName("Core Operation with NaN")
public class CoreOperationNanComparisonTest extends AbstractJXPathTest {

    private JXPathContext context;

    @BeforeEach
    void setUp() {
        context = JXPathContext.newContext(null);
        context.getVariables().declareVariable("nan", Double.NaN);
    }

    private static Stream<String> standardNanComparisonExpressions() {
        return Stream.of(
                // NaN compared to itself
                "$nan > $nan",
                "$nan < $nan",
                "$nan >= $nan",
                "$nan <= $nan",
                "$nan = $nan",
                "$nan >= $nan and $nan <= $nan",

                // NaN compared to the number 0
                "$nan > 0",
                "$nan < 0",
                "$nan >= 0",
                "$nan <= 0",
                "$nan = 0",
                "$nan >= 0 and $nan <= 0",

                // NaN compared to the number 1
                "$nan > 1",
                "$nan < 1",
                "$nan >= 1",
                "$nan <= 1",
                "$nan = 1",
                "$nan >= 1 and $nan <= 1"
        );
    }

    @DisplayName("Standard comparisons with NaN should evaluate to false")
    @ParameterizedTest(name = "Expression: ''{0}''")
    @MethodSource("standardNanComparisonExpressions")
    void testStandardNanComparisons_shouldBeFalse(final String expression) {
        // Verifies that any ordered comparison or equality check with NaN is false,
        // which aligns with IEEE 754 and XPath standards.
        assertXPathValue(context, expression, Boolean.FALSE, Boolean.class);
    }

    private static Stream<String> notEqualNanComparisonExpressions() {
        return Stream.of(
                "$nan != $nan",
                "$nan != 0",
                "$nan != 1"
        );
    }

    @DisplayName("Not-equal '!=' comparison with NaN should be false (JXPath-specific behavior)")
    @ParameterizedTest(name = "Expression: ''{0}''")
    @MethodSource("notEqualNanComparisonExpressions")
    void testNotEqualNanComparisons_shouldBeFalse(final String expression) {
        // NOTE: The XPath 1.0 spec defines '!=' as 'not(=)'. Based on that,
        // '$nan != <anything>' should evaluate to 'true' because '$nan = <anything>' is 'false'.
        // This test verifies JXPath's current (and likely non-compliant) behavior, which returns 'false'.
        assertXPathValue(context, expression, Boolean.FALSE, Boolean.class);
    }
}