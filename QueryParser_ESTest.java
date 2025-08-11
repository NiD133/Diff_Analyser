package org.jsoup.select;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

/**
 * Readable, behavior-focused tests for QueryParser.
 *
 * The goals of these tests are:
 * - demonstrate how common selector strings are parsed
 * - validate helpful error messages for invalid input
 * - exercise combinators and boolean evaluator utilities
 *
 * Notes:
 * - We avoid relying on internal implementation classes or exact exception types.
 * - When checking error conditions, we assert key substrings in the message instead of full messages.
 */
public class QueryParserReadableTest {

    // -----------------------------
    // Helpers
    // -----------------------------

    private static Evaluator parse(String query) {
        return QueryParser.parse(query);
    }

    private static Throwable expectParseError(String query, String... messageSnippets) {
        try {
            parse(query);
            fail("Expected an exception when parsing: " + query);
            return null; // unreachable
        } catch (RuntimeException e) {
            // Loosely assert message content to keep tests stable across minor changes
            String msg = String.valueOf(e.getMessage());
            for (String snippet : messageSnippets) {
                assertThat("Error message should contain: " + snippet + " but was: " + msg,
                    msg, containsString(snippet));
            }
            return e;
        }
    }

    // -----------------------------
    // Happy-path parsing
    // -----------------------------

    @Test
    public void parsesCommonPseudoSelectors_toStringMatches() {
        assertEquals(":first-child", parse(":first-child").toString());
        assertEquals(":last-child", parse(":last-child").toString());
        assertEquals(":only-child", parse(":only-child").toString());

        assertEquals(":first-of-type", parse(":first-of-type").toString());
        assertEquals(":last-of-type", parse(":last-of-type").toString());
        assertEquals(":only-of-type", parse(":only-of-type").toString());

        assertEquals(":matchText", parse(":matchText").toString());
        assertEquals(":empty", parse(":empty").toString());
        assertEquals(":root", parse(":root").toString());
    }

    @Test
    public void parsesContainsAndMatchesVariants() {
        assertNotNull(parse(":contains(hello)"));
        assertNotNull(parse(":containsOwn(hello)"));
        assertNotNull(parse(":containsWholeText(hello world)"));
        assertNotNull(parse(":containsWholeOwnText(hello world)"));

        assertNotNull(parse(":matches(^foo$)"));
        assertNotNull(parse(":matchesOwn(^foo$)"));
        assertNotNull(parse(":matchesWholeText(^bar$)"));
        assertNotNull(parse(":matchesWholeOwnText(^bar$)"));
    }

    @Test
    public void parsesAttributeOperators() {
        assertNotNull(parse("[name=value]"));
        assertNotNull(parse("[name!=value]"));
        assertNotNull(parse("[name^=pre]"));
        assertNotNull(parse("[name$=suf]"));
        assertNotNull(parse("[name*=mid]"));
        assertNotNull(parse("[name~=word]"));
    }

    @Test
    public void parsesWildcardsAndNamespaces() {
        assertEquals("*", parse("*").toString());  // universal selector
        assertNotNull(parse("*|*"));               // any namespace, any tag
    }

    @Test
    public void parsesSimpleCombinators() {
        assertNotNull(parse("div > p"));
        assertNotNull(parse("ul + li"));
        assertNotNull(parse("h1 ~ p"));
        assertNotNull(parse("article section p")); // descendant (whitespace)
    }

    // -----------------------------
    // Error handling and validation
    // -----------------------------

    @Test
    public void errorsOnTrailingCombinator() {
        expectParseError("div ~", "Could not parse query", "unexpected token");
        expectParseError(":is(:eq(2)) ~ ", "Could not parse query", "unexpected token");
    }

    @Test
    public void errorsOnUnknownCombinator() {
        // Directly testing the low-level API with an unknown combinator
        Throwable t = null;
        try {
            QueryParser.combinator(null, ']', null);
        } catch (RuntimeException e) {
            t = e;
        }
        assertNotNull("Expected an exception for unknown combinator", t);
        assertThat(t.getMessage(), containsString("Unknown combinator"));
    }

    @Test
    public void errorsWhenIndexMustBeNumeric() {
        expectParseError(":lt(z)", "Index must be numeric");
        expectParseError(":gt(x)", "Index must be numeric");
        expectParseError("div:eq(a)", "Index must be numeric");
    }

    @Test
    public void errorsOnInvalidNthChildFormat() {
        expectParseError(":nth-child(%d)", "nth-index", "unexpected format");
    }

    @Test
    public void errorsOnInvalidRegexInMatches() {
        // Invalid regex triggers a helpful message (e.g., "Dangling meta character '?'")
        expectParseError(":matches(?)", "Dangling meta character");
    }

    @Test
    public void errorsOnEmptyOrInvalidSubselectors() {
        expectParseError(":has()", ":has(", "must have a selector");
        expectParseError(":not()", ":not", "must not be empty");
    }

    // -----------------------------
    // Boolean evaluator utilities
    // -----------------------------

    @Test
    public void andReturnsRightWhenLeftNull_andBothNullReturnsNull() {
        Evaluator right = new Evaluator.IsLastChild();
        assertSame(right, QueryParser.and(null, right));
        assertNull(QueryParser.and(null, null));
    }

    @Test
    public void orThrowsOnNulls() {
        try {
            QueryParser.or(null, null);
            fail("Expected NullPointerException when both arguments are null");
        } catch (NullPointerException expected) {
            // expected
        }
    }

    @Test
    public void combinatorThrowsOnNullChildrenForSibling() {
        try {
            QueryParser.combinator(null, '+', null);
            fail("Expected NullPointerException when both sides are null for '+' combinator");
        } catch (NullPointerException expected) {
            // expected
        }
    }
}