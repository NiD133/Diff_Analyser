package org.jsoup.select;

import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.Evaluator;
import org.jsoup.select.QueryParser;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test suite for {@link QueryParser}.
 * This suite focuses on verifying the correct parsing of CSS selectors into Evaluator trees
 * and ensuring that invalid syntax is handled gracefully with appropriate exceptions.
 */
public class QueryParserTest {

    /**
     * Tests the parsing of various valid attribute selectors.
     */
    @Test
    public void parsesAttributeSelectors() {
        assertEquals("[attr=val]", QueryParser.parse("[attr=val]").toString());
        assertEquals("[attr^=val]", QueryParser.parse("[attr^=val]").toString());
        assertEquals("[attr$=val]", QueryParser.parse("[attr$=val]").toString());
        assertEquals("[attr*=val]", QueryParser.parse("[attr*=val]").toString());
        assertEquals("[attr~=val]", QueryParser.parse("[attr~=val]").toString());
        assertEquals("[attr!=val]", QueryParser.parse("[attr!=val]").toString());
        assertEquals("[^attr]", QueryParser.parse("[^attr]").toString());
    }

    /**
     * Tests the parsing of various pseudo-selectors.
     */
    @Test
    public void parsesPseudoSelectors() {
        assertEquals(":lt(0)", QueryParser.parse(":lt(0)").toString());
        assertEquals(":gt(0)", QueryParser.parse(":gt(0)").toString());
        assertEquals(":eq(0)", QueryParser.parse(":eq(0)").toString());

        assertEquals(":has(div)", QueryParser.parse(":has(div)").toString());
        assertEquals(":is(div)", QueryParser.parse(":is(div)").toString());
        assertEquals(":not(div)", QueryParser.parse(":not(div)").toString());

        assertEquals(":contains(text)", QueryParser.parse(":contains(text)").toString());
        assertEquals(":containsOwn(text)", QueryParser.parse(":containsOwn(text)").toString());
        assertEquals(":containsData(text)", QueryParser.parse(":containsData(text)").toString());

        assertEquals(":matches(regex)", QueryParser.parse(":matches(regex)").toString());
        assertEquals(":matchesOwn(regex)", QueryParser.parse(":matchesOwn(regex)").toString());
        assertEquals(":matchesWholeText(regex)", QueryParser.parse(":matchesWholeText(regex)").toString());
        assertEquals(":matchesWholeOwnText(regex)", QueryParser.parse(":matchesWholeOwnText(regex)").toString());

        assertEquals(":matchText", QueryParser.parse(":matchText").toString());
    }

    /**
     * Tests the parsing of structural pseudo-selectors like :nth-child, :first-child, etc.
     */
    @Test
    public void parsesStructuralPseudoSelectors() {
        assertEquals(":root", QueryParser.parse(":root").toString());
        assertEquals(":empty", QueryParser.parse(":empty").toString());

        assertEquals(":first-child", QueryParser.parse(":first-child").toString());
        assertEquals(":last-child", QueryParser.parse(":last-child").toString());
        assertEquals(":only-child", QueryParser.parse(":only-child").toString());

        assertEquals(":first-of-type", QueryParser.parse(":first-of-type").toString());
        assertEquals(":last-of-type", QueryParser.parse(":last-of-type").toString());
        assertEquals(":only-of-type", QueryParser.parse(":only-of-type").toString());

        assertEquals(":nth-child(2n+1)", QueryParser.parse(":nth-child(2n+1)").toString());
        assertEquals(":nth-last-child(3n)", QueryParser.parse(":nth-last-child(3n)").toString());
        assertEquals(":nth-of-type(n)", QueryParser.parse(":nth-of-type(n)").toString());
        assertEquals(":nth-last-of-type(odd)", QueryParser.parse(":nth-last-of-type(odd)").toString());
    }

    /**
     * Tests parsing of basic selectors, tags, and combinators.
     */
    @Test
    public void parsesBasicSelectorsAndCombinators() {
        assertEquals("*", QueryParser.parse("*").toString());
        assertEquals("ns|*", QueryParser.parse("ns|*").toString());
        assertEquals("tag", QueryParser.parse("tag").toString());
        assertEquals("ns|tag", QueryParser.parse("ns|tag").toString());
        assertEquals("#id", QueryParser.parse("#id").toString());
        assertEquals(".class", QueryParser.parse(".class").toString());

        // Combinators
        assertEquals("div > p", QueryParser.parse("div > p").toString());
        assertEquals("div + p", QueryParser.parse("div + p").toString());
        assertEquals("div ~ p", QueryParser.parse("div ~ p").toString());
        assertEquals("div p", QueryParser.parse("div p").toString());

        // Grouping
        assertEquals("div, p", QueryParser.parse("div, p").toString());
    }

    /**
     * Verifies that the parser throws an IllegalStateException for various invalid query syntaxes.
     */
    @Test
    public void parse_withInvalidQuery_throwsIllegalStateException() {
        // Unexpected tokens
        assertParseError("RV,L fO: 4},X", "unexpected token at ' fO: 4},X'");
        assertParseError("#P>hfIV>e@0fe", "unexpected token at '@0fe'");
        assertParseError("37m~T/SMo)=E[BHj8|{", "unexpected token at '/SMo)=E[BHj8|{'");
        assertParseError("wj[b]_o4~M", "unexpected token at '_o4~M'");
        assertParseError("org.jsoup.select.StructuralEvaluator$Not", "unexpected token at '$Not'");

        // Incomplete queries or missing arguments
        assertParseError(":is(:eq(1114)) ~ ", "unexpected token at ''");
        assertParseError("[^-a-zA-Z0-9_:.]+", "unexpected token at ''");
        assertParseError("I>tD:eq", "Index must be numeric");
        assertParseError(":has()", "Did not find balanced marker"); // Message varies slightly based on implementation

        // Invalid arguments
        assertParseError(":lt(%d)", "Index must be numeric");
        assertParseError(":gt(%d)", "Index must be numeric");
        assertParseError(":nth-child(%d)", "Could not parse nth-index '%d'");
        assertParseError(":matches?holeText(1s)", "Dangling meta character '?'");
    }

    /**
     * Tests the behavior of the static {@link QueryParser#or} method.
     */
    @Test
    public void or_combinesEvaluators() {
        Evaluator first = new Evaluator.IsLastChild();
        Evaluator second = new Evaluator.IsFirstChild();
        Evaluator third = new Evaluator.IsRoot();

        // Create an initial OR evaluator
        Evaluator orEvaluator = QueryParser.or(first, second);
        assertTrue(orEvaluator instanceof CombiningEvaluator.Or);
        assertEquals(":last-child, :first-child", orEvaluator.toString());

        // Add another evaluator to the existing OR
        Evaluator extendedOrEvaluator = QueryParser.or(orEvaluator, third);

        // Assert that the original OR evaluator was modified and returned
        assertSame("When adding to an existing Or, the same instance should be returned",
            orEvaluator, extendedOrEvaluator);
        assertEquals(":last-child, :first-child, :root", extendedOrEvaluator.toString());
    }

    @Test(expected = NullPointerException.class)
    public void or_withNullEvaluators_throwsNPE() {
        QueryParser.or(null, null);
    }

    /**
     * Tests the behavior of the static {@link QueryParser#and} method.
     */
    @Test
    public void and_combinesEvaluators() {
        Evaluator first = new Evaluator.Tag("div");
        Evaluator second = new Evaluator.Class("main");

        Evaluator andEvaluator = QueryParser.and(first, second);
        assertTrue(andEvaluator instanceof CombiningEvaluator.And);
        assertEquals("div.main", andEvaluator.toString());
    }

    @Test
    public void and_handlesNullsCorrectly() {
        assertNull("ANDing two nulls should result in null", QueryParser.and(null, null));

        Evaluator evaluator = new Evaluator.AllElements();
        assertSame("ANDing with null should return the non-null evaluator",
            evaluator, QueryParser.and(evaluator, null));
        assertSame("ANDing with null should return the non-null evaluator",
            evaluator, QueryParser.and(null, evaluator));
    }

    @Test(expected = IllegalStateException.class)
    public void combinator_withUnknownCombinator_throwsException() {
        QueryParser.combinator(new Evaluator.AllElements(), ']', new Evaluator.AllElements());
    }

    @Test(expected = NullPointerException.class)
    public void combinator_withAdjacentSiblingAndNulls_throwsNPE() {
        // The implementation of ImmediatePreviousSibling requires non-null evaluators
        QueryParser.combinator('+', null, null);
    }

    /**
     * Helper method to assert that parsing a query throws an {@link IllegalStateException}
     * with an expected message fragment.
     *
     * @param query           The CSS query to parse.
     * @param expectedMessage The expected fragment in the exception message.
     */
    private void assertParseError(String query, String expectedMessage) {
        try {
            QueryParser.parse(query);
            fail("Should have thrown IllegalStateException for query: " + query);
        } catch (IllegalStateException e) {
            String actualMessage = e.getMessage();
            assertTrue(
                String.format("Expected message to contain '%s', but was '%s' for query '%s'",
                    expectedMessage, actualMessage, query),
                actualMessage.contains(expectedMessage)
            );
        }
    }
}