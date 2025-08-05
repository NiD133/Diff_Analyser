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
 * Tests for the Selector Query Parser.
 */
public class QueryParserTest {

    @Nested
    @DisplayName("Functional Selection Tests")
    class SelectionTests {
        private final Document doc = Jsoup.parse("""
            <html><head>h</head><body>
              <li><strong>l1</strong></li>
              <a><li><strong>l2</strong></li></a>
              <p><strong>yes</strong></p>
            </body></html>""");

        @Test
        @DisplayName("should select immediate children with an OR condition")
        void selectsWithImmediateChildAndOr() {
            // Arrange: Selects <strong> elements that are immediate children of <p> or <li>,
            // which are in turn immediate children of <body>. The <a> tag blocks the second <li>.
            String query = ">p>strong,>li>strong";

            // Act
            String text = doc.body().select(query).text();

            // Assert
            assertEquals("l1 yes", text);
        }

        @Test
        @DisplayName("should select correctly regardless of OR clause order")
        void selectsWithWildcardAndOr() {
            // Arrange: Uses a wildcard to find the nested <li>.
            String query1 = "body>p>strong,body>*>li>strong";
            String query2 = "body>*>li>strong,body>p>strong"; // Same query, different order

            // Act
            String text1 = doc.select(query1).text();
            String text2 = doc.select(query2).text();

            // Assert
            assertEquals("l2 yes", text1);
            assertEquals(text1, text2, "Query order should not affect the result");
        }

        @Test
        @DisplayName("should parse and select elements with escaped CSS identifiers")
        void parsesAndSelectsWithEscapedIdentifiers() {
            // Arrange
            Document doc = Jsoup.parse("""
                <div class='-4a'>One</div>
                <div id='-4a'>Two</div>""");

            // Act & Assert for class selector
            String classQuery = "div.-\\34 a"; // Corresponds to .--4a
            Element div1 = doc.expectFirst(classQuery);
            assertEquals("One", div1.wholeText());
            assertEquals("(ImmediateParentRun (Tag 'html')(Tag 'body')(And (Tag 'div')(Class '.-4a')))", sexpr("html > body > " + classQuery));


            // Act & Assert for ID selector
            String idQuery = "#-\\34 a"; // Corresponds to #--4a
            Element div2 = doc.expectFirst(idQuery);
            assertEquals("Two", div2.wholeText());
            assertEquals("(ImmediateParentRun (Tag 'html')(Tag 'body')(Id '#-4a'))", sexpr("html > body > " + idQuery));
        }
    }

    @Nested
    @DisplayName("Parser Structure (S-Expression) Tests")
    class ParserStructureTests {

        @Test
        @DisplayName("should parse immediate child combinators into an ImmediateParentRun evaluator")
        void parsesImmediateChildCombinator() {
            String query = "div > p > bold.brass";
            String expectedSexpr = "(ImmediateParentRun (Tag 'div')(Tag 'p')(And (Tag 'bold')(Class '.brass')))";
            assertEquals(expectedSexpr, sexpr(query));
        }

        @Test
        @DisplayName("should give OR combinators correct precedence over descendant selectors")
        void parsesOrCombinatorWithCorrectPrecedence() {
            // Tests that "a b, c d" is parsed as (a AND b) OR (c AND d)
            String query = "a b, c d, e f";
            String expectedSexpr = "(Or (And (Tag 'b')(Ancestor (Tag 'a')))(And (Tag 'd')(Ancestor (Tag 'c')))(And (Tag 'f')(Ancestor (Tag 'e'))))";
            assertEquals(expectedSexpr, sexpr(query));
        }

        @Test
        @DisplayName("should correctly parse a complex selector with multiple combinators and groups")
        void parsesComplexSelector() {
            String query = ".foo.qux[attr=bar] > ol.bar, ol > li + li";
            String expectedSexpr = "(Or (And (Tag 'li')(ImmediatePreviousSibling (ImmediateParentRun (Tag 'ol')(Tag 'li'))))(ImmediateParentRun (And (AttributeWithValue '[attr=bar]')(Class '.foo')(Class '.qux'))(And (Tag 'ol')(Class '.bar'))))";
            assertEquals(expectedSexpr, sexpr(query));
        }

        @Test
        @DisplayName("should correctly parse an ID followed by a descendant class")
        void parsesIdAndDescendantClass() {
            // See https://github.com/jhy/jsoup/issues/2254
            String query = "#id .class";
            String expectedSexpr = "(And (Class '.class')(Ancestor (Id '#id')))";
            assertEquals(expectedSexpr, sexpr(query));
        }

        @Test
        @DisplayName("should correctly parse a complex structural selector")
        void parsesComplexStructuralSelector() {
            String query = "a:not(:has(span.foo)) b d > e + f ~ g";
            String expectedSexpr = "(And (Tag 'g')(PreviousSibling (And (Tag 'f')(ImmediatePreviousSibling (ImmediateParentRun (And (Tag 'd')(Ancestor (And (Tag 'b')(Ancestor (And (Tag 'a')(Not (Has (And (Tag 'span')(Class '.foo')))))))))(Tag 'e'))))))";
            assertEquals(expectedSexpr, sexpr(query));
        }

        @Test
        @DisplayName("should correctly parse an OR combinator following an attribute selector")
        void parsesOrCombinatorAfterAttribute() {
            // See https://github.com/jhy/jsoup/issues/2073
            String query = "#parent [class*=child], .some-other-selector .nested";
            String expectedSexpr = "(Or (And (AttributeWithValueContaining '[class*=child]')(Ancestor (Id '#parent')))(And (Class '.nested')(Ancestor (Class '.some-other-selector'))))";
            assertEquals(expectedSexpr, sexpr(query));
        }

        @Test
        @DisplayName("should correctly parse a :has pseudo-selector with a node type")
        void parsesHasPseudoSelectorWithNode() {
            String query = "p:has(::comment:contains(some text))";
            Evaluator parsed = QueryParser.parse(query);
            String expectedSexpr = "(And (Tag 'p')(Has (And (InstanceType '::comment')(ContainsValue ':contains(some text)'))))";

            assertEquals(expectedSexpr, sexpr(parsed));
            assertEquals(query, parsed.toString());
        }
    }

    @Nested
    @DisplayName("General Parsing Logic")
    class GeneralParsingLogicTests {

        @Test
        @DisplayName("should trim leading and trailing whitespace from the query")
        void trimsSurroundingWhitespaceFromQuery() {
            Evaluator parsed = QueryParser.parse(" span div  ");
            assertEquals("span div", parsed.toString());
        }

        @Test
        @DisplayName("should reconstruct the original query via toString()")
        void toStringOnParsedEvaluatorReconstructsOriginalQuery() {
            String query = "a:not(:has(span.foo)) b d > e + f ~ g";
            Evaluator parsed = QueryParser.parse(query);
            assertEquals(query, parsed.toString());
        }
    }

    @Nested
    @DisplayName("Error Handling for Invalid Queries")
    class InvalidQueryTests {

        @Test
        @DisplayName("should throw SelectorParseException for an unclosed attribute selector")
        void throwsOnUnclosedAttribute() {
            String query = "section > a[href=\"]";
            SelectorParseException exception =
                assertThrows(SelectorParseException.class, () -> QueryParser.parse(query));
            assertEquals("Did not find balanced marker at 'href=\"]'", exception.getMessage());
        }

        @Test
        @DisplayName("should throw SelectorParseException for an unbalanced quote in :contains")
        void throwsOnUnbalancedQuoteInContains() {
            String query = "p:contains(One \" One)";
            SelectorParseException exception =
                assertThrows(SelectorParseException.class, () -> QueryParser.parse(query));
            assertEquals("Did not find balanced marker at 'One \" One)'", exception.getMessage());
        }

        @Test
        @DisplayName("should throw SelectorParseException for an empty query")
        void throwsOnEmptyQuery() {
            SelectorParseException exception = assertThrows(SelectorParseException.class, () -> QueryParser.parse(""));
            assertEquals("String must not be empty", exception.getMessage());
        }

        @Test
        @DisplayName("should throw SelectorParseException for a null query")
        void throwsOnNullQuery() {
            SelectorParseException exception = assertThrows(SelectorParseException.class, () -> QueryParser.parse(null));
            assertEquals("String must not be empty", exception.getMessage());
        }

        @Test
        @DisplayName("should throw SelectorParseException for an unsupported combinator")
        void throwsOnUnsupportedCombinator() {
            String query = "div / foo";
            SelectorParseException exception =
                assertThrows(SelectorParseException.class, () -> QueryParser.parse(query));
            assertEquals("Could not parse query 'div / foo': unexpected token at '/ foo'", exception.getMessage());
        }

        @Test
        @DisplayName("should throw SelectorParseException for a trailing closing parenthesis")
        void throwsOnTrailingClosingParenthesis() {
            String query = "div:has(p))";
            SelectorParseException exception =
                assertThrows(SelectorParseException.class, () -> QueryParser.parse(query));
            assertEquals("Could not parse query 'div:has(p))': unexpected token at ')'", exception.getMessage());
        }

        @Test
        @DisplayName("should throw SelectorParseException for consecutive combinators")
        void throwsOnConsecutiveCombinators() {
            String query1 = "div>>p";
            SelectorParseException exception1 =
                assertThrows(SelectorParseException.class, () -> QueryParser.parse(query1));
            assertEquals("Could not parse query 'div>>p': unexpected token at '>p'", exception1.getMessage());

            String query2 = "+ + div";
            SelectorParseException exception2 =
                assertThrows(SelectorParseException.class, () -> QueryParser.parse(query2));
            assertEquals("Could not parse query '+ + div': unexpected token at '+ div'", exception2.getMessage());
        }
    }
}