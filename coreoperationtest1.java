package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.AbstractJXPathTest;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Variables;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests the behavior of core comparison operations when one of the operands is an empty node-set.
 * <p>
 * According to the XPath 1.0 specification, if a comparison involves an empty node-set,
 * the result is always {@code false}. This test suite verifies that JXPath correctly
 * implements this rule for all standard comparison operators.
 *
 * @see <a href="https://www.w3.org/TR/xpath-10/#booleans">XPath 1.0 Specification, Section 3.4 Booleans</a>
 */
@DisplayName("Core Operation with an Empty Node-Set")
public class CoreOperationEmptyNodeSetComparisonTest extends AbstractJXPathTest {

    private JXPathContext context;

    @BeforeEach
    @Override
    public void setUp() {
        context = JXPathContext.newContext(null);
        Variables vars = context.getVariables();
        // This variable is used to create an empty node-set via a filter
        vars.declareVariable("array", new double[]{0.25, 0.5, 0.75});
    }

    @DisplayName("Comparison with a non-existent path should always be false")
    @ParameterizedTest(name = "XPath: ''{0}''")
    @ValueSource(strings = {
            "/idonotexist = 0",
            "/idonotexist != 0",
            "/idonotexist < 0",
            "/idonotexist > 0",
            "/idonotexist >= 0",
            "/idonotexist <= 0"
    })
    void whenComparingWithNonExistentPath_thenResultIsFalse(String xpath) {
        // An XPath pointing to a non-existent path results in an empty node-set.
        // Any comparison with an empty node-set must evaluate to false.
        assertXPathValue(context, xpath, Boolean.FALSE, Boolean.class);
    }

    @DisplayName("Comparison with an empty filtered collection should always be false")
    @ParameterizedTest(name = "XPath: ''{0}''")
    @ValueSource(strings = {
            "$array[position() < 1] = 0",
            "$array[position() < 1] != 0",
            "$array[position() < 1] < 0",
            "$array[position() < 1] > 0",
            "$array[position() < 1] >= 0",
            "$array[position() < 1] <= 0"
    })
    void whenComparingWithEmptyFilteredCollection_thenResultIsFalse(String xpath) {
        // The predicate [position() < 1] filters the array, resulting in an empty node-set
        // because XPath positions are 1-based.
        // Any comparison with this empty node-set must evaluate to false.
        assertXPathValue(context, xpath, Boolean.FALSE, Boolean.class);
    }
}