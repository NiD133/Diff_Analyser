package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.jsoup.select.EvaluatorDebug.sexpr;
import static org.jsoup.select.Selector.SelectorParseException;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Readability-focused tests for QueryParser and selector parsing behavior.
 * - Short, intention-revealing test names
 * - Small helpers to remove duplication and clarify assertions
 * - Minimal but clear comments describing the behavior under test
 */
public class QueryParserTest {

    // ---------- Small assertion helpers ----------

    private static void assertSExpr(String query, String expectedSExpr) {
        assertEquals(expectedSExpr, sexpr(query), "Unexpected S-expression for query: " + query);
    }

    private static void assertSExpr(Evaluator evaluator, String expectedSExpr) {
        assertEquals(expectedSExpr, sexpr(evaluator), "Unexpected S-expression for evaluator");
    }

    private static SelectorParseException expectParseFailure(String query, String expectedMessage) {
        SelectorParseException ex = assertThrows(SelectorParseException.class, () -> QueryParser.parse(query));
        assertEquals(expectedMessage, ex.getMessage());
        return ex;
    }

    // ---------- Test groups ----------

    @Nested
    @DisplayName("Basic parsing and whitespace handling")
    class Basics {
        @Test
        @DisplayName("Consumes sub-queries with leading combinator and supports comma-separated selectors")
        void consumesSubQueries() {
            Document doc = Jsoup.parse(
                "<html><head>h</head><body>" +
                "<li><strong>l1</strong></li>" +
                "<a><li><strong>l2</strong></li></a>" +
                "<p><strong>yes</strong></p>" +
                "</body></html>"
            );

            // Selecting immediate children from body
            assertEquals("l1 yes", doc.body().select(">p>strong,>li>strong").text());
            assertEquals("l1 yes", doc.body().select(" > p > strong , > li > strong").text());

            // Order and wildcard do not affect results
            assertEquals("l2 yes", doc.select("body>p>strong,body>*>li>strong").text());
            assertEquals("l2 yes", doc.select("body>*>li>strong,body>p>strong").text());
            assertEquals("l2 yes", doc.select("body>p>strong,body>*>li>strong").text());
        }

        @Test
        @DisplayName("Trims leading and trailing whitespace around the selector")
        void trimsLeadingAndTrailingWhitespace() {
            Evaluator e = QueryParser.parse(" span div  ");
            assertEquals("span div", e.toString());
        }
    }

    @Nested
    @DisplayName("Precedence, structure and serialization")
    class PrecedenceAndStructure {
        @Test
        @DisplayName("Immediate parent run is parsed as a single unit")
        void immediateParentRun() {
            String query = "div > p > bold.brass";
            assertSExpr(query, "(ImmediateParentRun (Tag 'div')(Tag 'p')(And (Tag 'bold')(Class '.brass')))");
        }

        @Test
        @DisplayName("Top-level OR has correct precedence over descendant ANDs")
        void orGetsCorrectPrecedence() {
            String query = "a b, c d, e f";
            assertSExpr(query,
                "(Or (And (Tag 'b')(Ancestor (Tag 'a')))" +
                "(And (Tag 'd')(Ancestor (Tag 'c')))" +
                "(And (Tag 'f')(Ancestor (Tag 'e'))))"
            );
        }

        @Test
        @DisplayName("Parses multi-part selectors with attributes and siblings")
        void parsesMultiCorrectly() {
            String query = ".foo.qux[attr=bar] > ol.bar, ol > li + li";
            assertSExpr(query,
                "(Or (And (Tag 'li')(ImmediatePreviousSibling (ImmediateParentRun (Tag 'ol')(Tag 'li'))))" +
                "(ImmediateParentRun (And (AttributeWithValue '[attr=bar]')(Class '.foo')(Class '.qux'))" +
                "(And (Tag 'ol')(Class '.bar'))))"
            );
        }

        @Test
        @DisplayName("Cost / order for '#id .class' id-descender-class is stable")
        void idDescenderClassOrder() {
            String query = "#id .class";
            assertSExpr(query, "(And (Class '.class')(Ancestor (Id '#id')))");
        }

        @Test
        @DisplayName("Complex structural evaluators preserve toString and structure")
        void structuralEvaluatorsToString() {
            String q = "a:not(:has(span.foo)) b d > e + f ~ g";
            Evaluator e = QueryParser.parse(q);
            assertEquals(q, e.toString());
            assertSExpr(e,
                "(And (Tag 'g')(PreviousSibling (And (Tag 'f')" +
                "(ImmediatePreviousSibling (ImmediateParentRun (And (Tag 'd')" +
                "(Ancestor (And (Tag 'b')(Ancestor (And (Tag 'a')" +
                "(Not (Has (And (Tag 'span')(Class '.foo')))))))))(Tag 'e'))))))"
            );
        }

        @Test
        @DisplayName("Parses OR after an attribute selector")
        void parsesOrAfterAttribute() {
            assertSExpr("#parent [class*=child], .some-other-selector .nested",
                "(Or (And (AttributeWithValueContaining '[class*=child]')(Ancestor (Id '#parent')))" +
                "(And (Class '.nested')(Ancestor (Class '.some-other-selector'))))"
            );

            assertSExpr("#parent [class*=child], .some-other-selector",
                "(Or (Class '.some-other-selector')" +
                "(And (AttributeWithValueContaining '[class*=child]')(Ancestor (Id '#parent'))))"
            );

            assertSExpr("#el[class*=child], .some-other-selector",
                "(Or (And (Id '#el')(AttributeWithValueContaining '[class*=child]'))(Class '.some-other-selector'))"
            );

            assertSExpr("#parent [class*=child], .some-other-selector .nested",
                "(Or (And (AttributeWithValueContaining '[class*=child]')(Ancestor (Id '#parent')))" +
                "(And (Class '.nested')(Ancestor (Class '.some-other-selector'))))"
            );
        }
    }

    @Nested
    @DisplayName("Escaping and node selectors")
    class EscapingAndNodes {
        @Test
        @DisplayName("Parses escaped sub-queries for class and id")
        void parsesEscapedSubqueries() {
            String html = "<div class='-4a'>One</div> <div id='-4a'>Two</div>";
            Document doc = Jsoup.parse(html);

            String classQuery = "div.-\\34 a"; // div.-4a
            Element div1 = doc.expectFirst(classQuery);
            assertEquals("One", div1.wholeText());

            String idQuery = "#-\\34 a"; // #-4a
            Element div2 = doc.expectFirst(idQuery);
            assertEquals("Two", div2.wholeText());

            String deepClassQuery = "html > body > div.-\\34 a";
            assertEquals(deepClassQuery, div1.cssSelector());
            assertSame(div1, doc.expectFirst(deepClassQuery));

            String deepIdQuery = "html > body > #-\\34 a";
            assertEquals(idQuery, div2.cssSelector());
            assertSame(div2, doc.expectFirst(deepIdQuery));

            assertSExpr(deepClassQuery,
                "(ImmediateParentRun (Tag 'html')(Tag 'body')(And (Tag 'div')(Class '.-4a')))"
            );
            assertSExpr(deepIdQuery,
                "(ImmediateParentRun (Tag 'html')(Tag 'body')(Id '#-4a'))"
            );
        }

        @Test
        @DisplayName("Has pseudo supports node selectors like ::comment with contains")
        void hasNodeSelector() {
            String q = "p:has(::comment:contains(some text))";
            Evaluator e = QueryParser.parse(q);
            assertSExpr(e,
                "(And (Tag 'p')(Has (And (InstanceType '::comment')(ContainsValue ':contains(some text)'))))"
            );
            assertEquals(q, e.toString());
        }
    }

    @Nested
    @DisplayName("Error handling and diagnostics")
    class Errors {
        @Test
        @DisplayName("Reports unclosed attribute value")
        void exceptionOnUnclosedAttribute() {
            expectParseFailure("section > a[href=\"]",
                "Did not find balanced marker at 'href=\"]'"
            );
        }

        @Test
        @DisplayName("Reports unbalanced quotes in :contains")
        void exceptionOnUnbalancedContains() {
            expectParseFailure("p:contains(One \" One)",
                "Did not find balanced marker at 'One \" One)'"
            );
        }

        @Test
        @DisplayName("Rejects empty selector")
        void exceptOnEmptySelector() {
            expectParseFailure("", "String must not be empty");
        }

        @Test
        @DisplayName("Rejects null selector")
        void exceptOnNullSelector() {
            SelectorParseException ex =
                assertThrows(SelectorParseException.class, () -> QueryParser.parse(null));
            assertEquals("String must not be empty", ex.getMessage());
        }

        @Test
        @DisplayName("Reports unhandled tokens and unexpected combinators")
        void exceptOnUnhandledEvaluator() {
            expectParseFailure("div / foo",
                "Could not parse query 'div / foo': unexpected token at '/ foo'"
            );
        }

        @Test
        @DisplayName("Reports trailing parenthesis")
        void trailingParens() {
            expectParseFailure("div:has(p))",
                "Could not parse query 'div:has(p))': unexpected token at ')'"
            );
        }

        @Test
        @DisplayName("Reports consecutive combinators")
        void consecutiveCombinators() {
            expectParseFailure("div>>p",
                "Could not parse query 'div>>p': unexpected token at '>p'"
            );
            expectParseFailure("+ + div",
                "Could not parse query '+ + div': unexpected token at '+ div'"
            );
        }
    }
}