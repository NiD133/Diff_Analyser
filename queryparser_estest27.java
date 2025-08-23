package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the {@link QueryParser} class.
 */
public class QueryParserTest {

    /**
     * Verifies that the parser correctly handles a CSS selector
     * that specifies a namespace and a universal tag selector (*).
     */
    @Test
    public void parse_withNamespaceAndUniversalSelector_shouldCreateCorrectTagEvaluator() {
        // The "ns|*" selector syntax is used to select any element (*) within a given namespace ("r8qm5ctg").
        String namespaceQuery = "r8qm5ctg|*";

        // The parser is expected to handle the '|' character, typically by normalizing it.
        // In jsoup's implementation, "ns|tag" becomes an evaluator for the tag "ns:tag".
        String expectedEvaluatorString = "r8qm5ctg:*";

        // Action: Parse the query.
        Evaluator evaluator = QueryParser.parse(namespaceQuery);

        // Assertions:
        // 1. Ensure the parser returns a non-null Evaluator.
        assertNotNull("The parsed evaluator should not be null.", evaluator);

        // 2. Verify that the correct type of Evaluator was created.
        assertTrue("The evaluator should be of type Tag.", evaluator instanceof Evaluator.Tag);

        // 3. Check the internal state of the evaluator to confirm the query was parsed as expected.
        assertEquals("The evaluator's string representation should match the expected normalized tag.",
                expectedEvaluatorString, evaluator.toString());
    }
}