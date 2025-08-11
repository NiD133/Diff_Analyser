package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;

import static org.jsoup.select.EvaluatorDebug.sexpr;
import static org.jsoup.select.Selector.SelectorParseException;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the CSS Selector Query Parser.
 * 
 * These tests verify that CSS selectors are correctly parsed into evaluator trees
 * and that various selector combinations work as expected.
 *
 * @author Jonathan Hedley
 */
public class QueryParserTest {

    // Test data constants for better maintainability
    private static final String SAMPLE_HTML = 
        "<html><head>h</head><body>" +
        "<li><strong>l1</strong></li>" +
        "<a><li><strong>l2</strong></li></a>" +
        "<p><strong>yes</strong></p>" +
        "</body></html>";

    private static final String ESCAPED_CLASSES_HTML = 
        "<div class='-4a'>One</div> <div id='-4a'>Two</div>";

    @Test 
    public void shouldParseMultipleSelectorsWithCommas() {
        // Given: HTML with nested strong elements in different containers
        Document doc = Jsoup.parse(SAMPLE_HTML);
        
        // When: Selecting immediate children with multiple selectors
        String immediateChildrenText = doc.body().select(">p>strong,>li>strong").text();
        String immediateChildrenWithSpacesText = doc.body().select(" > p > strong , > li > strong").text();
        
        // Then: Should select direct children only
        assertEquals("l1 yes", immediateChildrenText, "Should select immediate children from body");
        assertEquals("l1 yes", immediateChildrenWithSpacesText, "Should handle spaces in selectors");
        
        // When: Using wildcard selectors
        String wildcardResults1 = doc.select("body>p>strong,body>*>li>strong").text();
        String wildcardResults2 = doc.select("body>*>li>strong,body>p>strong").text();
        
        // Then: Should find nested elements through wildcards
        assertEquals("l2 yes", wildcardResults1, "Should find elements through wildcard selector");
        assertEquals("l2 yes", wildcardResults2, "Selector order should not affect results");
    }

    @Test 
    public void shouldParseImmediateParentChainSelectors() {
        // Given: A selector with immediate parent chain
        String cssSelector = "div > p > bold.brass";
        
        // When: Parsing the selector into expression tree
        String parsedExpression = sexpr(cssSelector);
        
        // Then: Should create correct ImmediateParentRun structure
        String expectedExpression = "(ImmediateParentRun (Tag 'div')(Tag 'p')(And (Tag 'bold')(Class '.brass')))";
        assertEquals(expectedExpression, parsedExpression, 
            "Should parse immediate parent chain with class selector correctly");
    }

    @Test 
    public void shouldRespectOperatorPrecedenceInOrExpressions() {
        // Given: Multiple selectors separated by commas (OR operations)
        String multipleSelectors = "a b, c d, e f";
        
        // When: Parsing the selector
        String parsedExpression = sexpr(multipleSelectors);
        
        // Then: Should group as (a AND b) OR (c AND d) OR (e AND f)
        String expectedExpression = "(Or " +
            "(And (Tag 'b')(Ancestor (Tag 'a')))" +
            "(And (Tag 'd')(Ancestor (Tag 'c')))" +
            "(And (Tag 'f')(Ancestor (Tag 'e')))" +
            ")";
        assertEquals(expectedExpression, parsedExpression, 
            "Should correctly group AND operations within OR expressions");
    }

    @Test 
    public void shouldParseComplexSelectorsWithMultipleCombinators() {
        // Given: Complex selector with classes, attributes, and combinators
        String complexSelector = ".foo.qux[attr=bar] > ol.bar, ol > li + li";
        
        // When: Parsing the complex selector
        String parsedExpression = sexpr(complexSelector);
        
        // Then: Should handle all selector types and combinators correctly
        String expectedExpression = "(Or " +
            "(And (Tag 'li')(ImmediatePreviousSibling (ImmediateParentRun (Tag 'ol')(Tag 'li'))))" +
            "(ImmediateParentRun " +
                "(And (AttributeWithValue '[attr=bar]')(Class '.foo')(Class '.qux'))" +
                "(And (Tag 'ol')(Class '.bar'))" +
            ")" +
            ")";
        assertEquals(expectedExpression, parsedExpression, 
            "Should parse complex selectors with multiple combinators correctly");
    }

    @Test 
    void shouldOptimizeIdDescendantClassSelectors() {
        // Given: ID followed by descendant class selector
        String idClassSelector = "#id .class";
        
        // When: Parsing the selector
        String parsedExpression = sexpr(idClassSelector);
        
        // Then: Should optimize for performance (class first, then ancestor ID)
        String expectedExpression = "(And (Class '.class')(Ancestor (Id '#id')))";
        assertEquals(expectedExpression, parsedExpression, 
            "Should optimize ID descendant class selectors for better performance");
    }

    // Exception handling tests
    @Test
    public void shouldThrowExceptionForUnbalancedAttributeQuotes() {
        // Given: Selector with unclosed attribute quotes
        String invalidSelector = "section > a[href=\"]";
        
        // When & Then: Should throw exception with clear message
        SelectorParseException exception = assertThrows(SelectorParseException.class, 
            () -> QueryParser.parse(invalidSelector));
        assertEquals("Did not find balanced marker at 'href=\"]'", exception.getMessage(),
            "Should provide clear error message for unbalanced quotes");
    }

    @Test
    public void shouldThrowExceptionForUnbalancedContainsQuotes() {
        // Given: Contains selector with unbalanced quotes
        String invalidContainsSelector = "p:contains(One \" One)";
        
        // When & Then: Should throw exception
        SelectorParseException exception = assertThrows(SelectorParseException.class, 
            () -> QueryParser.parse(invalidContainsSelector));
        assertEquals("Did not find balanced marker at 'One \" One)'", exception.getMessage(),
            "Should handle unbalanced quotes in contains selectors");
    }

    @Test
    public void shouldRejectEmptySelectors() {
        // When & Then: Empty selector should throw exception
        SelectorParseException emptyException = assertThrows(SelectorParseException.class, 
            () -> QueryParser.parse(""));
        assertEquals("String must not be empty", emptyException.getMessage());
        
        // When & Then: Null selector should throw exception  
        SelectorParseException nullException = assertThrows(SelectorParseException.class, 
            () -> QueryParser.parse(null));
        assertEquals("String must not be empty", nullException.getMessage());
    }

    @Test
    public void shouldRejectInvalidSelectorSyntax() {
        // Given: Invalid selector with unsupported operator
        String invalidSelector = "div / foo";
        
        // When & Then: Should throw exception with helpful message
        SelectorParseException exception = assertThrows(SelectorParseException.class, 
            () -> QueryParser.parse(invalidSelector));
        assertEquals("Could not parse query 'div / foo': unexpected token at '/ foo'", 
            exception.getMessage(), "Should identify problematic token in error message");
    }

    @Test 
    public void shouldHandleLeadingAndTrailingWhitespace() {
        // Given: Selector with surrounding whitespace
        String selectorWithSpaces = " span div  ";
        
        // When: Parsing the selector
        Evaluator parsedSelector = QueryParser.parse(selectorWithSpaces);
        
        // Then: Should trim whitespace and parse correctly
        assertEquals("span div", parsedSelector.toString(), 
            "Should handle leading and trailing whitespace");
    }

    @Test 
    public void shouldPreserveComplexSelectorStringRepresentation() {
        // Given: Complex selector with multiple structural evaluators
        String complexStructuralSelector = "a:not(:has(span.foo)) b d > e + f ~ g";
        
        // When: Parsing and converting back to string
        Evaluator parsedSelector = QueryParser.parse(complexStructuralSelector);
        String reconstructedSelector = parsedSelector.toString();
        
        // Then: Should preserve original selector structure
        assertEquals(complexStructuralSelector, reconstructedSelector, 
            "Should preserve complex selector string representation");
        
        // And: Should parse into correct expression tree
        String expectedExpression = "(And (Tag 'g')" +
            "(PreviousSibling (And (Tag 'f')" +
                "(ImmediatePreviousSibling (ImmediateParentRun " +
                    "(And (Tag 'd')(Ancestor (And (Tag 'b')" +
                        "(Ancestor (And (Tag 'a')(Not (Has (And (Tag 'span')(Class '.foo'))))))" +
                    ")))" +
                    "(Tag 'e')" +
                "))" +
            "))" +
            ")";
        assertEquals(expectedExpression, sexpr(complexStructuralSelector),
            "Should create correct expression tree for complex structural selectors");
    }

    @Test 
    public void shouldParseSelectorsAfterAttributes() {
        // Given: Selectors with attributes followed by OR operations
        String attributeWithOrSelector = "#parent [class*=child], .some-other-selector .nested";
        
        // When: Parsing the selector
        String parsedExpression = sexpr(attributeWithOrSelector);
        
        // Then: Should correctly handle OR after attribute selectors
        String expectedExpression = "(Or " +
            "(And (AttributeWithValueContaining '[class*=child]')(Ancestor (Id '#parent')))" +
            "(And (Class '.nested')(Ancestor (Class '.some-other-selector')))" +
            ")";
        assertEquals(expectedExpression, parsedExpression,
            "Should parse OR operations after attribute selectors correctly");
    }

    @Test 
    void shouldHandleEscapedCharactersInSelectors() {
        // Given: HTML with CSS identifiers requiring escaping
        Document doc = Jsoup.parse(ESCAPED_CLASSES_HTML);
        
        // When: Using escaped selectors
        String escapedClassSelector = "div.-\\34 a";
        Element elementWithEscapedClass = doc.expectFirst(escapedClassSelector);
        
        String escapedIdSelector = "#-\\34 a";  
        Element elementWithEscapedId = doc.expectFirst(escapedIdSelector);
        
        // Then: Should correctly match elements with escaped identifiers
        assertEquals("One", elementWithEscapedClass.wholeText(), 
            "Should match element with escaped class name");
        assertEquals("Two", elementWithEscapedId.wholeText(),
            "Should match element with escaped ID");
        
        // And: Should generate correct CSS selectors
        String generatedClassSelector = "html > body > div.-\\34 a";
        assertEquals(generatedClassSelector, elementWithEscapedClass.cssSelector(),
            "Should generate correct escaped class selector");
        
        // And: Should parse escaped selectors into correct expressions
        assertEquals("(ImmediateParentRun (Tag 'html')(Tag 'body')(And (Tag 'div')(Class '.-4a')))", 
            sexpr(generatedClassSelector), "Should parse escaped class selectors correctly");
        assertEquals("(ImmediateParentRun (Tag 'html')(Tag 'body')(Id '#-4a'))", 
            sexpr("html > body > #-\\34 a"), "Should parse escaped ID selectors correctly");
    }

    @Test 
    void shouldRejectTrailingParentheses() {
        // Given: Selector with extra closing parenthesis
        String selectorWithTrailingParen = "div:has(p))";
        
        // When & Then: Should throw exception for unmatched parenthesis
        SelectorParseException exception = assertThrows(SelectorParseException.class, 
            () -> QueryParser.parse(selectorWithTrailingParen));
        assertEquals("Could not parse query 'div:has(p))': unexpected token at ')'", 
            exception.getMessage(), "Should detect and report trailing parentheses");
    }

    @Test 
    void shouldRejectConsecutiveCombinators() {
        // Given: Selectors with consecutive combinators (invalid syntax)
        String doubleChildCombinator = "div>>p";
        String doubleAdjacentCombinator = "+ + div";
        
        // When & Then: Should reject consecutive child combinators
        SelectorParseException exception1 = assertThrows(SelectorParseException.class, 
            () -> QueryParser.parse(doubleChildCombinator));
        assertEquals("Could not parse query 'div>>p': unexpected token at '>p'", 
            exception1.getMessage(), "Should reject consecutive child combinators");
        
        // When & Then: Should reject consecutive adjacent sibling combinators
        SelectorParseException exception2 = assertThrows(SelectorParseException.class, 
            () -> QueryParser.parse(doubleAdjacentCombinator));
        assertEquals("Could not parse query '+ + div': unexpected token at '+ div'", 
            exception2.getMessage(), "Should reject consecutive adjacent sibling combinators");
    }

    @Test 
    void shouldParseNodeSelectorsWithContains() {
        // Given: Selector targeting comment nodes with content
        String nodeSelector = "p:has(::comment:contains(some text))";
        
        // When: Parsing the node selector
        Evaluator parsedEvaluator = QueryParser.parse(nodeSelector);
        
        // Then: Should create correct expression tree for node selectors
        String expectedExpression = "(And (Tag 'p')" +
            "(Has (And (InstanceType '::comment')(ContainsValue ':contains(some text)')))" +
            ")";
        assertEquals(expectedExpression, sexpr(parsedEvaluator),
            "Should parse node selectors with contains correctly");
        
        // And: Should preserve original selector string
        assertEquals(nodeSelector, parsedEvaluator.toString(),
            "Should preserve node selector string representation");
    }
}