package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.select.Evaluator;
import org.jsoup.select.QueryParser;
import org.jsoup.select.StructuralEvaluator;

/**
 * Test suite for QueryParser functionality including CSS selector parsing,
 * evaluator combinations, and error handling for malformed queries.
 */
public class QueryParserTest {

    // ========== Evaluator Combination Tests ==========
    
    @Test
    public void testOrCombination_WithSameEvaluator_ReturnsOptimizedResult() {
        Evaluator.IsLastChild lastChildEvaluator = new Evaluator.IsLastChild();
        
        Evaluator firstOr = QueryParser.or(lastChildEvaluator, lastChildEvaluator);
        Evaluator secondOr = QueryParser.or(firstOr, lastChildEvaluator);
        
        // When combining the same evaluator, optimization should occur
        assertSame("Expected optimization to return the same evaluator instance", 
                   secondOr, firstOr);
    }

    @Test
    public void testAndCombination_WithNullEvaluators_ReturnsNull() {
        Evaluator result = QueryParser.and(null, null);
        
        assertNull("AND combination of null evaluators should return null", result);
    }

    @Test
    public void testAndCombination_WithValidEvaluators_CreatesNewEvaluator() {
        Evaluator.AttributeWithValue attributeEvaluator = 
            new Evaluator.AttributeWithValue("testAttr", "testValue");
        
        Evaluator andResult = QueryParser.and(attributeEvaluator, attributeEvaluator);
        Evaluator combinatorResult = QueryParser.combinator(attributeEvaluator, ' ', andResult);
        
        assertNotSame("Combinator should create a new evaluator instance", 
                      andResult, combinatorResult);
    }

    // ========== Error Handling Tests ==========

    @Test(expected = NullPointerException.class)
    public void testOrCombination_WithNullEvaluators_ThrowsException() {
        QueryParser.or(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testCombinator_WithNullEvaluatorsAndPlusOperator_ThrowsException() {
        QueryParser.combinator(null, '+', null);
    }

    @Test(expected = NullPointerException.class)
    public void testAndCombination_WithNotEvaluatorContainingNull_ThrowsException() {
        StructuralEvaluator.Not notEvaluator = new StructuralEvaluator.Not(null);
        QueryParser.and(notEvaluator, null);
    }

    @Test(expected = IllegalStateException.class)
    public void testCombinator_WithInvalidCombinatorCharacter_ThrowsException() {
        QueryParser.combinator(null, ']', null);
    }

    // ========== Malformed Query Tests ==========

    @Test
    public void testParse_MalformedQueryWithCommaAndSpecialChars_ThrowsIllegalStateException() {
        String malformedQuery = "RV,LfO:4},X";
        
        try {
            QueryParser.parse(malformedQuery);
            fail("Expected IllegalStateException for malformed query");
        } catch (IllegalStateException e) {
            assertTrue("Error message should mention unexpected token", 
                      e.getMessage().contains("unexpected token"));
            assertTrue("Error message should mention the problematic part", 
                      e.getMessage().contains("fO:4},X"));
        }
    }

    @Test
    public void testParse_IncompleteIsEqQuery_ThrowsIllegalStateException() {
        String incompleteQuery = ":is(:eq(1114)) ~ ";
        
        try {
            QueryParser.parse(incompleteQuery);
            fail("Expected IllegalStateException for incomplete query");
        } catch (IllegalStateException e) {
            assertTrue("Error message should mention unexpected token", 
                      e.getMessage().contains("unexpected token"));
        }
    }

    @Test
    public void testParse_QueryWithAtSymbol_ThrowsIllegalStateException() {
        String queryWithAtSymbol = "#P>hfIV>e@0fe";
        
        try {
            QueryParser.parse(queryWithAtSymbol);
            fail("Expected IllegalStateException for query with @ symbol");
        } catch (IllegalStateException e) {
            assertTrue("Error message should mention the @ symbol part", 
                      e.getMessage().contains("@0fe"));
        }
    }

    @Test
    public void testParse_QueryWithInvalidRegexMetaChar_ThrowsIllegalStateException() {
        String queryWithDanglingMeta = ":matches?holeText(1s)";
        
        try {
            QueryParser.parse(queryWithDanglingMeta);
            fail("Expected IllegalStateException for dangling meta character");
        } catch (IllegalStateException e) {
            assertTrue("Error message should mention dangling meta character", 
                      e.getMessage().contains("Dangling meta character"));
        }
    }

    // ========== Valid Selector Tests ==========

    @Test
    public void testParse_MatchesWholeOwnTextSelector_ReturnsValidEvaluator() {
        Evaluator evaluator = QueryParser.parse(":matchesWholeOwnText(%s)");
        
        assertNotNull("Should parse :matchesWholeOwnText selector successfully", evaluator);
    }

    @Test
    public void testParse_MatchesWholeTextSelector_ReturnsValidEvaluator() {
        Evaluator evaluator = QueryParser.parse(":matchesWholeText(%s)");
        
        assertNotNull("Should parse :matchesWholeText selector successfully", evaluator);
    }

    @Test
    public void testParse_MatchesOwnSelector_ReturnsValidEvaluator() {
        Evaluator evaluator = QueryParser.parse(":matchesOwn(%s)");
        
        assertNotNull("Should parse :matchesOwn selector successfully", evaluator);
    }

    @Test
    public void testParse_ContainsWholeOwnTextSelector_ReturnsValidEvaluator() {
        Evaluator evaluator = QueryParser.parse(":containsWholeOwnText(%s)");
        
        assertNotNull("Should parse :containsWholeOwnText selector successfully", evaluator);
    }

    @Test
    public void testParse_ContainsWholeTextSelector_ReturnsValidEvaluator() {
        Evaluator evaluator = QueryParser.parse(":containsWholeText(%s)");
        
        assertNotNull("Should parse :containsWholeText selector successfully", evaluator);
    }

    @Test
    public void testParse_ContainsOwnSelector_ReturnsValidEvaluator() {
        Evaluator evaluator = QueryParser.parse(":containsOwn(%s)");
        
        assertNotNull("Should parse :containsOwn selector successfully", evaluator);
    }

    @Test
    public void testParse_ContainsSelector_ReturnsValidEvaluator() {
        Evaluator evaluator = QueryParser.parse(":contains(%s)");
        
        assertNotNull("Should parse :contains selector successfully", evaluator);
    }

    // ========== Attribute Selector Tests ==========

    @Test
    public void testParse_AttributeContainsWordSelector_ReturnsValidEvaluator() {
        Evaluator evaluator = QueryParser.parse("[%s~=%s]");
        
        assertNotNull("Should parse attribute contains word selector successfully", evaluator);
    }

    @Test
    public void testParse_AttributeContainsSelector_ReturnsValidEvaluator() {
        Evaluator evaluator = QueryParser.parse("[%s*=%s]");
        
        assertNotNull("Should parse attribute contains selector successfully", evaluator);
    }

    @Test
    public void testParse_AttributeStartsWithSelector_ReturnsValidEvaluator() {
        Evaluator evaluator = QueryParser.parse("[%s^=%s]");
        
        assertNotNull("Should parse attribute starts with selector successfully", evaluator);
    }

    @Test
    public void testParse_AttributeEndsWithSelector_ReturnsValidEvaluator() {
        Evaluator evaluator = QueryParser.parse("[%s$=%s]");
        
        assertNotNull("Should parse attribute ends with selector successfully", evaluator);
    }

    @Test
    public void testParse_AttributeEqualsSelector_ReturnsValidEvaluator() {
        Evaluator evaluator = QueryParser.parse("[%s=%s]");
        
        assertNotNull("Should parse attribute equals selector successfully", evaluator);
    }

    @Test
    public void testParse_AttributeNotEqualsSelector_ReturnsValidEvaluator() {
        Evaluator evaluator = QueryParser.parse("[%s!=%s]");
        
        assertNotNull("Should parse attribute not equals selector successfully", evaluator);
    }

    // ========== Pseudo-class Selector Tests ==========

    @Test
    public void testParse_LastOfTypeSelector_ReturnsCorrectString() {
        Evaluator evaluator = QueryParser.parse(":last-of-type");
        
        assertEquals("Should return correct string representation", 
                    ":last-of-type", evaluator.toString());
    }

    @Test
    public void testParse_OnlyOfTypeSelector_ReturnsCorrectString() {
        Evaluator evaluator = QueryParser.parse(":only-of-type");
        
        assertEquals("Should return correct string representation", 
                    ":only-of-type", evaluator.toString());
    }

    @Test
    public void testParse_FirstOfTypeSelector_ReturnsCorrectString() {
        Evaluator evaluator = QueryParser.parse(":first-of-type");
        
        assertEquals("Should return correct string representation", 
                    ":first-of-type", evaluator.toString());
    }

    @Test
    public void testParse_LastChildSelector_ReturnsCorrectString() {
        Evaluator evaluator = QueryParser.parse(":last-child");
        
        assertEquals("Should return correct string representation", 
                    ":last-child", evaluator.toString());
    }

    @Test
    public void testParse_OnlyChildSelector_ReturnsCorrectString() {
        Evaluator evaluator = QueryParser.parse(":only-child");
        
        assertEquals("Should return correct string representation", 
                    ":only-child", evaluator.toString());
    }

    @Test
    public void testParse_FirstChildSelector_ReturnsCorrectString() {
        Evaluator evaluator = QueryParser.parse(":first-child");
        
        assertEquals("Should return correct string representation", 
                    ":first-child", evaluator.toString());
    }

    @Test
    public void testParse_MatchTextSelector_ReturnsCorrectString() {
        Evaluator evaluator = QueryParser.parse(":matchText");
        
        assertEquals("Should return correct string representation", 
                    ":matchText", evaluator.toString());
    }

    @Test
    public void testParse_EmptySelector_ReturnsCorrectString() {
        Evaluator evaluator = QueryParser.parse(":empty");
        
        assertEquals("Should return correct string representation", 
                    ":empty", evaluator.toString());
    }

    @Test
    public void testParse_RootSelector_ReturnsCorrectString() {
        Evaluator evaluator = QueryParser.parse(":root");
        
        assertEquals("Should return correct string representation", 
                    ":root", evaluator.toString());
    }

    // ========== Universal Selector Tests ==========

    @Test
    public void testParse_UniversalSelector_ReturnsCorrectString() {
        Evaluator evaluator = QueryParser.parse("*");
        
        assertEquals("Should return correct string representation", 
                    "*", evaluator.toString());
    }

    @Test
    public void testParse_NamespacedUniversalSelector_ReturnsValidEvaluator() {
        Evaluator evaluator = QueryParser.parse("*|9");
        
        assertNotNull("Should parse namespaced universal selector successfully", evaluator);
    }

    // ========== Complex Selector Tests ==========

    @Test
    public void testParse_ComplexSelectorWithCombinators_ReturnsValidEvaluator() {
        Evaluator evaluator = QueryParser.parse(">mttchesWholeOw>Text");
        
        assertNotNull("Should parse complex selector with combinators successfully", evaluator);
    }

    @Test
    public void testParse_ElementWithPipeAndWildcard_ReturnsValidEvaluator() {
        Evaluator evaluator = QueryParser.parse("r8qm5ctg|*");
        
        assertNotNull("Should parse element with pipe and wildcard successfully", evaluator);
    }

    // ========== Index-based Selector Error Tests ==========

    @Test
    public void testParse_LessThanWithNonNumericIndex_ThrowsIllegalStateException() {
        try {
            QueryParser.parse(":lt(%d)");
            fail("Expected IllegalStateException for non-numeric index");
        } catch (IllegalStateException e) {
            assertTrue("Error message should mention numeric requirement", 
                      e.getMessage().contains("Index must be numeric"));
        }
    }

    @Test
    public void testParse_GreaterThanWithNonNumericIndex_ThrowsIllegalStateException() {
        try {
            QueryParser.parse(":gt(%d)");
            fail("Expected IllegalStateException for non-numeric index");
        } catch (IllegalStateException e) {
            assertTrue("Error message should mention numeric requirement", 
                      e.getMessage().contains("Index must be numeric"));
        }
    }

    @Test
    public void testParse_EqWithoutIndex_ThrowsIllegalStateException() {
        try {
            QueryParser.parse("I>tD:eq");
            fail("Expected IllegalStateException for eq without index");
        } catch (IllegalStateException e) {
            assertTrue("Error message should mention numeric requirement", 
                      e.getMessage().contains("Index must be numeric"));
        }
    }

    // ========== Nth-child Selector Error Tests ==========

    @Test
    public void testParse_NthChildWithInvalidFormat_ThrowsIllegalStateException() {
        try {
            QueryParser.parse(":nth-child(%d)");
            fail("Expected IllegalStateException for invalid nth-child format");
        } catch (IllegalStateException e) {
            assertTrue("Error message should mention unexpected format", 
                      e.getMessage().contains("unexpected format"));
        }
    }

    // ========== Valid Complex Selectors ==========

    @Test
    public void testParse_NotSelectorWithText_ReturnsValidEvaluator() {
        Evaluator evaluator = QueryParser.parse(":not(selector) subselect must not be empty");
        
        assertNotNull("Should parse :not selector with text successfully", evaluator);
    }

    @Test
    public void testParse_ContainsDataSelectorWithText_ReturnsValidEvaluator() {
        Evaluator evaluator = QueryParser.parse(":containsData(text) query must not be empty");
        
        assertNotNull("Should parse :containsData selector with text successfully", evaluator);
    }

    // ========== Additional Error Cases ==========

    @Test
    public void testParse_HasSelectorWithoutContent_ThrowsIllegalStateException() {
        try {
            QueryParser.parse(":has() must have a selector");
            fail("Expected IllegalStateException for empty :has() selector");
        } catch (IllegalStateException e) {
            assertTrue("Error message should mention unexpected token", 
                      e.getMessage().contains("unexpected token"));
        }
    }
}