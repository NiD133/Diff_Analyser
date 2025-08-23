package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.AbstractJXPathTest;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Variables;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests the behavior of XPath relational operators (e.g., '=', '>', '<')
 * when comparing a node-set against a single value.
 *
 * <p>
 * According to XPath 1.0 specification, such a comparison evaluates to true
 * if the relation is true for <strong>any</strong> node in the node-set.
 * </p>
 */
@DisplayName("Core Operation: Node-Set Comparison")
public class CoreOperationNodeSetComparisonTest extends AbstractJXPathTest {

    private JXPathContext context;
    private static final double[] TEST_ARRAY = {0.25, 0.5, 0.75};

    @BeforeEach
    public void setUp() {
        context = JXPathContext.newContext(null);
        Variables vars = context.getVariables();
        // The test array represents a node-set with values {0.25, 0.5, 0.75}
        vars.declareVariable("array", TEST_ARRAY);
    }

    @DisplayName("should evaluate based on 'any' match in the set")
    @ParameterizedTest(name = "XPath ''{0}'' should be {1}")
    @CsvSource({
            // Equality (=) checks if ANY element in {0.25, 0.5, 0.75} matches
            "'$array = 0.25',    true",
            "'$array = 0.5',     true",
            "'$array = 0.75',    true",
            "'$array = 0.50000', true",  // Verifies numeric precision
            "'$array = 0',       false", // No element is 0
            "'$array = 1',       false", // No element is 1

            // Greater than (>) checks if ANY element is greater than the value
            "'$array > 0',       true",  // All elements are > 0
            "'$array > 0.5',     true",  // 0.75 is > 0.5
            "'$array > 0.74',    true",  // 0.75 is > 0.74
            "'$array > 0.75',    false", // No element is > 0.75
            "'$array > 1',       false", // No element is > 1

            // Less than (<) checks if ANY element is less than the value
            "'$array < 1',       true",  // All elements are < 1
            "'$array < 0.5',     true",  // 0.25 is < 0.5
            "'$array < 0.26',    true",  // 0.25 is < 0.26
            "'$array < 0.25',    false", // No element is < 0.25
            "'$array < 0',       false", // No element is < 0

            // Greater than or equal (>=)
            "'$array >= 0.75',   true",  // 0.75 is >= 0.75
            "'$array >= 0',      true",  // All elements are >= 0

            // Less than or equal (<=)
            "'$array <= 0.25',   true",  // 0.25 is <= 0.25
            "'$array <= 1',      true"   // All elements are <= 1
    })
    void testNodeSetComparison(String xpath, boolean expectedResult) {
        assertXPathValue(context, xpath, expectedResult, Boolean.class);
    }
}