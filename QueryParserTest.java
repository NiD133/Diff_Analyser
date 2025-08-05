package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;

import static org.jsoup.select.EvaluatorDebug.sexpr;
import static org.jsoup.select.Selector.SelectorParseException;
import static org.junit.jupiter.api.Assertions.*;

public class QueryParserTest {
    private static final String EMPTY_QUERY_MESSAGE = "String must not be empty";
    private static final String UNCLOSED_ATTRIBUTE_MSG = "Did not find balanced marker at 'href=\"]'";
    private static final String UNBALANCED_QUOTE_MSG = "Did not find balanced marker at 'One \" One)'";
    private static final String UNHANDLED_EVALUATOR_MSG = "Could not parse query 'div / foo': unexpected token at '/ foo'";
    private static final String TRAILING_PAREN_MSG = "Could not parse query 'div:has(p))': unexpected token at ')'";
    private static final String CONSECUTIVE_GT_MSG = "Could not parse query 'div>>p': unexpected token at '>p'";
    private static final String CONSECUTIVE_PLUS_MSG = "Could not parse query '+ + div': unexpected token at '+ div'";

    private Document createTestDocument() {
        String html = "<html><head>h</head><body>" +
            "<li><strong>l1</strong></li>" +
            "<a><li><strong>l2</strong></li></a>" +
            "<p><strong>yes</strong></p>" +
            "</body></html>";
        return Jsoup.parse(html);
    }

    @Test
    public void immediateChildCombinatorWithVariants() {
        Document doc = createTestDocument();
        
        // Selecting immediate children from body
        assertEquals("l1 yes", doc.body().select(">p>strong,>li>strong").text());
        
        // Space variants
        assertEquals("l1 yes", doc.body().select(" > p > strong , > li > strong").text());
        
        // Mixed immediate and descendant selectors
        assertEquals("l2 yes", doc.select("body>p>strong,body>*>li>strong").text());
        assertEquals("l2 yes", doc.select("body>*>li>strong,body>p>strong").text());
    }

    @Test
    public void parseImmediateParentRun() {
        String query = "div > p > bold.brass";
        String expectedSexpr = "(ImmediateParentRun (Tag 'div')(Tag 'p')(And (Tag 'bold')(Class '.brass')))";
        
        assertEquals(expectedSexpr, sexpr(query));
    }

    @Test
    public void orPrecedenceWithMultipleSelectors() {
        String query = "a b, c d, e f";
        String expectedSexpr = "(Or (And (Tag 'b')(Ancestor (Tag 'a')))(And (Tag 'd')(Ancestor (Tag 'c')))(And (Tag 'f')(Ancestor (Tag 'e'))))";
        
        assertEquals(expectedSexpr, sexpr(query));
    }

    @Test
    public void parseComplexSelectorWithMultipleComponents() {
        String query = ".foo.qux[attr=bar] > ol.bar, ol > li + li";
        String expectedSexpr = "(Or (And (Tag 'li')(ImmediatePreviousSibling (ImmediateParentRun (Tag 'ol')(Tag 'li'))))"
            + "(ImmediateParentRun (And (AttributeWithValue '[attr=bar]')(Class '.foo')(Class '.qux'))(And (Tag 'ol')(Class '.bar'))))";
        
        assertEquals(expectedSexpr, sexpr(query));
    }

    @Test
    public void idDescendantClassOrder() {
        String query = "#id .class";
        String expectedSexpr = "(And (Class '.class')(Ancestor (Id '#id')))";
        
        assertEquals(expectedSexpr, sexpr(query));
    }

    @Test
    public void exceptionOnUnclosedAttribute() {
        SelectorParseException exception = assertThrows(
            SelectorParseException.class,
            () -> QueryParser.parse("section > a[href=\"]")
        );
        
        assertEquals(UNCLOSED_ATTRIBUTE_MSG, exception.getMessage());
    }

    @Test
    public void exceptionOnUnbalancedQuoteInContains() {
        SelectorParseException exception = assertThrows(
            SelectorParseException.class,
            () -> QueryParser.parse("p:contains(One \" One)")
        );
        
        assertEquals(UNBALANCED_QUOTE_MSG, exception.getMessage());
    }

    @Test
    public void exceptionOnEmptySelector() {
        SelectorParseException exception = assertThrows(
            SelectorParseException.class,
            QueryParser::parse
        );
        
        assertEquals(EMPTY_QUERY_MESSAGE, exception.getMessage());
    }

    @Test
    public void exceptionOnNullSelector() {
        SelectorParseException exception = assertThrows(
            SelectorParseException.class,
            () -> QueryParser.parse(null)
        );
        
        assertEquals(EMPTY_QUERY_MESSAGE, exception.getMessage());
    }

    @Test
    public void exceptionOnUnhandledEvaluator() {
        SelectorParseException exception = assertThrows(
            SelectorParseException.class,
            () -> QueryParser.parse("div / foo")
        );
        
        assertEquals(UNHANDLED_EVALUATOR_MSG, exception.getMessage());
    }

    @Test
    public void trimsSpacesAroundSelector() {
        Evaluator parse = QueryParser.parse(" span div  ");
        assertEquals("span div", parse.toString());
    }

    @Test
    public void structuralEvaluatorsToString() {
        String query = "a:not(:has(span.foo)) b d > e + f ~ g";
        Evaluator parse = QueryParser.parse(query);
        
        assertEquals(query, parse.toString());
        String expectedSexpr = "(And (Tag 'g')(PreviousSibling (And (Tag 'f')(ImmediatePreviousSibling "
            + "(ImmediateParentRun (And (Tag 'd')(Ancestor (And (Tag 'b')(Ancestor (And (Tag 'a')(Not "
            + "(Has (And (Tag 'span')(Class '.foo'))))))))(Tag 'e'))))))";
        
        assertEquals(expectedSexpr, sexpr(query));
    }

    @Test
    public void parseOrAfterAttribute() {
        String query = "#parent [class*=child], .some-other-selector .nested";
        String expectedSexpr = "(Or (And (AttributeWithValueContaining '[class*=child]')(Ancestor (Id '#parent')))"
            + "(And (Class '.nested')(Ancestor (Class '.some-other-selector'))))";
        
        assertEquals(expectedSexpr, sexpr(query));
    }

    @Test
    public void parseEscapedSelectors() {
        String html = "<div class='-4a'>One</div> <div id='-4a'>Two</div>";
        Document doc = Jsoup.parse(html);

        // Test escaped class selector
        String classSelector = "div.-\\34 a";
        Element div1 = doc.expectFirst(classSelector);
        assertEquals("One", div1.wholeText());

        // Test escaped ID selector
        String idSelector = "#-\\34 a";
        Element div2 = doc.expectFirst(idSelector);
        assertEquals("Two", div2.wholeText());

        // Test generated selector with escaped class
        String genClassSelector = "html > body > div.-\\34 a";
        assertEquals(genClassSelector, div1.cssSelector());
        assertSame(div1, doc.expectFirst(genClassSelector));

        // Test generated selector with escaped ID
        String deepIdSelector = "html > body > #-\\34 a";
        assertEquals(idSelector, div2.cssSelector());
        assertSame(div2, doc.expectFirst(deepIdSelector));

        // Verify parsed expressions
        assertEquals(
            "(ImmediateParentRun (Tag 'html')(Tag 'body')(And (Tag 'div')(Class '.-4a')))", 
            sexpr(genClassSelector)
        );
        assertEquals(
            "(ImmediateParentRun (Tag 'html')(Tag 'body')(Id '#-4a'))", 
            sexpr(deepIdSelector)
        );
    }

    @Test
    public void exceptionOnTrailingParens() {
        SelectorParseException exception = assertThrows(
            SelectorParseException.class,
            () -> QueryParser.parse("div:has(p))")
        );
        
        assertEquals(TRAILING_PAREN_MSG, exception.getMessage());
    }

    @Test
    public void exceptionOnConsecutiveCombinators() {
        // Test consecutive child combinators
        SelectorParseException exception1 = assertThrows(
            SelectorParseException.class,
            () -> QueryParser.parse("div>>p")
        );
        assertEquals(CONSECUTIVE_GT_MSG, exception1.getMessage());

        // Test consecutive adjacent sibling combinators
        SelectorParseException exception2 = assertThrows(
            SelectorParseException.class,
            () -> QueryParser.parse("+ + div")
        );
        assertEquals(CONSECUTIVE_PLUS_MSG, exception2.getMessage());
    }

    @Test
    public void parseHasWithNodeSelector() {
        String query = "p:has(::comment:contains(some text))";
        Evaluator evaluator = QueryParser.parse(query);
        
        String expectedSexpr = "(And (Tag 'p')(Has (And (InstanceType '::comment')(ContainsValue ':contains(some text)'))))";
        assertEquals(expectedSexpr, sexpr(evaluator));
        assertEquals(query, evaluator.toString());
    }
}