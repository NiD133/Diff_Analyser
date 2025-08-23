package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;

import static org.jsoup.select.EvaluatorDebug.asElement;
import static org.jsoup.select.EvaluatorDebug.sexpr;
import static org.jsoup.select.Selector.SelectorParseException;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Selector Query Parser.
 * This suite verifies the correct parsing and evaluation of CSS-like selectors.
 * It ensures that the query parser correctly interprets and processes various selector syntaxes.
 */
public class QueryParserTest {

    @Test
    public void testConsumeSubQuery() {
        // Test parsing of sub-queries and immediate child selectors
        Document doc = Jsoup.parse("<html><head>h</head><body>" +
                "<li><strong>l1</strong></li>" +
                "<a><li><strong>l2</strong></li></a>" +
                "<p><strong>yes</strong></p>" +
                "</body></html>");

        // Select immediate children of body
        assertEquals("l1 yes", doc.body().select(">p>strong,>li>strong").text());

        // Test with spaces in the query
        assertEquals("l1 yes", doc.body().select(" > p > strong , > li > strong").text());

        // Test selecting with wildcard and specific tags
        assertEquals("l2 yes", doc.select("body>p>strong,body>*>li>strong").text());
        assertEquals("l2 yes", doc.select("body>*>li>strong,body>p>strong").text());
        assertEquals("l2 yes", doc.select("body>p>strong,body>*>li>strong").text());
    }

    @Test
    public void testImmediateParentRun() {
        // Test parsing of immediate parent run with tag and class selectors
        String query = "div > p > bold.brass";
        String expectedSexpr = "(ImmediateParentRun (Tag 'div')(Tag 'p')(And (Tag 'bold')(Class '.brass')))";
        assertEquals(expectedSexpr, sexpr(query));
    }

    @Test
    public void testOrGetsCorrectPrecedence() {
        // Test precedence of OR and AND operations in selector parsing
        String query = "a b, c d, e f";
        String expectedSexpr = "(Or (And (Tag 'b')(Ancestor (Tag 'a')))(And (Tag 'd')(Ancestor (Tag 'c')))(And (Tag 'f')(Ancestor (Tag 'e'))))";
        assertEquals(expectedSexpr, sexpr(query));
    }

    @Test
    public void testParsesMultiCorrectly() {
        // Test parsing of complex multi-part selectors
        String query = ".foo.qux[attr=bar] > ol.bar, ol > li + li";
        String expectedSexpr = "(Or (And (Tag 'li')(ImmediatePreviousSibling (ImmediateParentRun (Tag 'ol')(Tag 'li'))))(ImmediateParentRun (And (AttributeWithValue '[attr=bar]')(Class '.foo')(Class '.qux'))(And (Tag 'ol')(Class '.bar'))))";
        assertEquals(expectedSexpr, sexpr(query));
    }

    @Test
    public void testIdDescenderClassOrder() {
        // Test parsing of ID and class selectors with descendant relationships
        String query = "#id .class";
        String expectedSexpr = "(And (Class '.class')(Ancestor (Id '#id')))";
        assertEquals(expectedSexpr, sexpr(query));
    }

    @Test
    public void exceptionOnUncloseAttribute() {
        // Test exception for unclosed attribute selector
        SelectorParseException exception = assertThrows(SelectorParseException.class, () -> QueryParser.parse("section > a[href=\"]"));
        assertEquals("Did not find balanced marker at 'href=\"]'", exception.getMessage());
    }

    @Test
    public void testParsesSingleQuoteInContains() {
        // Test exception for unbalanced quotes in :contains pseudo-class
        SelectorParseException exception = assertThrows(SelectorParseException.class, () -> QueryParser.parse("p:contains(One \" One)"));
        assertEquals("Did not find balanced marker at 'One \" One)'", exception.getMessage());
    }

    @Test
    public void exceptOnEmptySelector() {
        // Test exception for empty selector
        SelectorParseException exception = assertThrows(SelectorParseException.class, () -> QueryParser.parse(""));
        assertEquals("String must not be empty", exception.getMessage());
    }

    @Test
    public void exceptOnNullSelector() {
        // Test exception for null selector
        SelectorParseException exception = assertThrows(SelectorParseException.class, () -> QueryParser.parse(null));
        assertEquals("String must not be empty", exception.getMessage());
    }

    @Test
    public void exceptOnUnhandledEvaluator() {
        // Test exception for unhandled evaluator
        SelectorParseException exception = assertThrows(SelectorParseException.class, () -> QueryParser.parse("div / foo"));
        assertEquals("Could not parse query 'div / foo': unexpected token at '/ foo'", exception.getMessage());
    }

    @Test
    public void okOnSpacesForeAndAft() {
        // Test parsing with leading and trailing spaces
        Evaluator parse = QueryParser.parse(" span div  ");
        assertEquals("span div", parse.toString());
    }

    @Test
    public void structuralEvaluatorsToString() {
        // Test complex structural evaluators
        String query = "a:not(:has(span.foo)) b d > e + f ~ g";
        Evaluator parse = QueryParser.parse(query);
        assertEquals(query, parse.toString());

        String expectedSexpr = "(And (Tag 'g')(PreviousSibling (And (Tag 'f')(ImmediatePreviousSibling (ImmediateParentRun (And (Tag 'd')(Ancestor (And (Tag 'b')(Ancestor (And (Tag 'a')(Not (Has (And (Tag 'span')(Class '.foo')))))))))(Tag 'e'))))))";
        assertEquals(expectedSexpr, sexpr(query));
    }

    @Test
    public void parsesOrAfterAttribute() {
        // Test parsing of OR after attribute selectors
        String query = "#parent [class*=child], .some-other-selector .nested";
        String expectedSexpr = "(Or (And (AttributeWithValueContaining '[class*=child]')(Ancestor (Id '#parent')))(And (Class '.nested')(Ancestor (Class '.some-other-selector'))))";
        assertEquals(expectedSexpr, sexpr(query));

        assertEquals("(Or (Class '.some-other-selector')(And (AttributeWithValueContaining '[class*=child]')(Ancestor (Id '#parent'))))", sexpr("#parent [class*=child], .some-other-selector"));
        assertEquals("(Or (And (Id '#el')(AttributeWithValueContaining '[class*=child]'))(Class '.some-other-selector'))", sexpr("#el[class*=child], .some-other-selector"));
        assertEquals(expectedSexpr, sexpr("#parent [class*=child], .some-other-selector .nested"));
    }

    @Test
    public void parsesEscapedSubqueries() {
        // Test parsing of escaped subqueries
        String html = "<div class='-4a'>One</div> <div id='-4a'>Two</div>";
        Document doc = Jsoup.parse(html);

        String classQuery = "div.-\\34 a";
        Element div1 = doc.expectFirst(classQuery);
        assertEquals("One", div1.wholeText());

        String idQuery = "#-\\34 a";
        Element div2 = doc.expectFirst(idQuery);
        assertEquals("Two", div2.wholeText());

        String generalClassQuery = "html > body > div.-\\34 a";
        assertEquals(generalClassQuery, div1.cssSelector());
        assertSame(div1, doc.expectFirst(generalClassQuery));

        String deepIdQuery = "html > body > #-\\34 a";
        assertEquals(idQuery, div2.cssSelector());
        assertSame(div2, doc.expectFirst(deepIdQuery));

        assertEquals("(ImmediateParentRun (Tag 'html')(Tag 'body')(And (Tag 'div')(Class '.-4a')))", sexpr(generalClassQuery));
        assertEquals("(ImmediateParentRun (Tag 'html')(Tag 'body')(Id '#-4a'))", sexpr(deepIdQuery));
    }

    @Test
    public void trailingParens() {
        // Test exception for trailing parentheses
        SelectorParseException exception = assertThrows(SelectorParseException.class, () -> QueryParser.parse("div:has(p))"));
        assertEquals("Could not parse query 'div:has(p))': unexpected token at ')'", exception.getMessage());
    }

    @Test
    public void consecutiveCombinators() {
        // Test exception for consecutive combinators
        SelectorParseException exception1 = assertThrows(SelectorParseException.class, () -> QueryParser.parse("div>>p"));
        assertEquals("Could not parse query 'div>>p': unexpected token at '>p'", exception1.getMessage());

        SelectorParseException exception2 = assertThrows(SelectorParseException.class, () -> QueryParser.parse("+ + div"));
        assertEquals("Could not parse query '+ + div': unexpected token at '+ div'", exception2.getMessage());
    }

    @Test
    public void hasNodeSelector() {
        // Test parsing of node selectors with :has pseudo-class
        String query = "p:has(::comment:contains(some text))";
        Evaluator evaluator = QueryParser.parse(query);
        String expectedSexpr = "(And (Tag 'p')(Has (And (InstanceType '::comment')(ContainsValue ':contains(some text)'))))";
        assertEquals(expectedSexpr, sexpr(evaluator));
        assertEquals(query, evaluator.toString());
    }
}