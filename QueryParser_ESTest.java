/*
 * Refactored for clarity and maintainability
 */
package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jsoup.select.Evaluator;
import org.jsoup.select.QueryParser;
import org.jsoup.select.StructuralEvaluator;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class QueryParser_ESTest extends QueryParser_ESTest_scaffolding {

    // ========================================================================
    // Tests for Evaluator combination logic
    // ========================================================================
    
    @Test(timeout = 4000)
    public void testOrWithSameEvaluatorReturnsSameInstance() {
        Evaluator.IsLastChild evaluator = new Evaluator.IsLastChild();
        Evaluator combined = QueryParser.or(evaluator, evaluator);
        Evaluator result = QueryParser.or(combined, evaluator);
        assertSame(result, combined);
    }

    @Test(timeout = 4000)
    public void testAndWithNullsReturnsNull() {
        Evaluator result = QueryParser.and(null, null);
        assertNull(result);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testOrWithNullsThrowsNPE() {
        QueryParser.or(null, null);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testCombinatorWithPlusAndNullsThrowsNPE() {
        QueryParser.combinator(null, '+', null);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testAndWithNotAndNullThrowsNPE() {
        StructuralEvaluator.Not not = new StructuralEvaluator.Not(null);
        QueryParser.and(not, null);
    }

    @Test(timeout = 4000)
    public void testCombinatorWithSpace() {
        Evaluator.AttributeWithValue attrEval = 
            new Evaluator.AttributeWithValue("key", "value");
        Evaluator combined = QueryParser.and(attrEval, attrEval);
        Evaluator result = QueryParser.combinator(attrEval, ' ', combined);
        assertNotSame(combined, result);
    }

    @Test(timeout = 4000, expected = IllegalStateException.class)
    public void testCombinatorWithUnknownCombinatorThrows() {
        QueryParser.combinator(null, ']', null);
    }

    // ========================================================================
    // Tests for valid query parsing
    // ========================================================================
    
    @Test(timeout = 4000)
    public void testParseUniversalSelector() {
        Evaluator eval = QueryParser.parse("*");
        assertEquals("*", eval.toString());
    }

    @Test(timeout = 4000)
    public void testParseTagWithNamespaceWildcard() {
        Evaluator eval = QueryParser.parse("*|9");
        assertNotNull(eval);
    }

    @Test(timeout = 4000)
    public void testParseValidQueryWithChildCombinator() {
        Evaluator eval = QueryParser.parse(">mttchesWholeOw>Text");
        assertNotNull(eval);
    }

    @Test(timeout = 4000)
    public void testParsePseudoSelectors() {
        assertEquals(":last-of-type", QueryParser.parse(":last-of-type").toString());
        assertEquals(":only-of-type", QueryParser.parse(":only-of-type").toString());
        assertEquals(":first-of-type", QueryParser.parse(":first-of-type").toString());
        assertEquals(":last-child", QueryParser.parse(":last-child").toString());
        assertEquals(":only-child", QueryParser.parse(":only-child").toString());
        assertEquals(":first-child", QueryParser.parse(":first-child").toString());
        assertEquals(":matchText", QueryParser.parse(":matchText").toString());
        assertEquals(":empty", QueryParser.parse(":empty").toString());
        assertEquals(":root", QueryParser.parse(":root").toString());
    }

    @Test(timeout = 4000)
    public void testParseContainsVariations() {
        assertNotNull(QueryParser.parse(":contains(%s)"));
        assertNotNull(QueryParser.parse(":containsOwn(%s)"));
        assertNotNull(QueryParser.parse(":containsWholeText(%s)"));
        assertNotNull(QueryParser.parse(":containsWholeOwnText(%s)"));
    }

    @Test(timeout = 4000)
    public void testParseMatchesVariations() {
        assertNotNull(QueryParser.parse(":matchesOwn(%s)"));
        assertNotNull(QueryParser.parse(":matchesWholeText(%s)"));
        assertNotNull(QueryParser.parse(":matchesWholeOwnText(%s)"));
    }

    @Test(timeout = 4000)
    public void testParseAttributeSelectors() {
        assertNotNull(QueryParser.parse("[%s=%s]"));
        assertNotNull(QueryParser.parse("[%s!=%s]"));
        assertNotNull(QueryParser.parse("[%s^=%s]"));
        assertNotNull(QueryParser.parse("[%s$=%s]"));
        assertNotNull(QueryParser.parse("[%s*=%s]"));
        assertNotNull(QueryParser.parse("[%s~=%s]"));
    }

    @Test(timeout = 4000)
    public void testParseSpecialCases() {
        assertNotNull(QueryParser.parse(":containsData(text) query must not be empty"));
        assertNotNull(QueryParser.parse(":not(selector) subselect must not be empty"));
    }

    // ========================================================================
    // Tests for invalid query parsing (expected exceptions)
    // ========================================================================
    
    @Test(timeout = 4000, expected = IllegalStateException.class)
    public void testParseInvalidQuery1() {
        QueryParser.parse("RV,LfO:4},X");
    }

    @Test(timeout = 4000, expected = IllegalStateException.class)
    public void testParseInvalidQuery2() {
        QueryParser.parse(":is(:eq(1114)) ~ ");
    }

    @Test(timeout = 4000, expected = IllegalStateException.class)
    public void testParseInvalidQuery3() {
        QueryParser.parse("#P>hfIV>e@0fe");
    }

    @Test(timeout = 4000, expected = IllegalStateException.class)
    public void testParseInvalidQuery4() {
        QueryParser.parse("37m~T/SMo)=E[BHj8|{");
    }

    @Test(timeout = 4000, expected = IllegalStateException.class)
    public void testParseInvalidQuery5() {
        QueryParser.parse("wj[b]_o4~M");
    }

    @Test(timeout = 4000, expected = IllegalStateException.class)
    public void testParseInvalidQuery6() {
        QueryParser.parse("[^-a-zA-Z0-9_:.]+");
    }

    @Test(timeout = 4000, expected = IllegalStateException.class)
    public void testParseInvalidQuery7() {
        QueryParser.parse("1+RZ-2|k}9kC<q");
    }

    @Test(timeout = 4000, expected = IllegalStateException.class)
    public void testParseInvalidQuery8() {
        QueryParser.parse("x:jT)sndzU!E|jR3");
    }

    @Test(timeout = 4000, expected = IllegalStateException.class)
    public void testParseInvalidQuery9() {
        QueryParser.parse("9F>R8Qm5cTG:&@z");
    }

    @Test(timeout = 4000, expected = IllegalStateException.class)
    public void testParseInvalidQuery10() {
        QueryParser.parse("org.jsoup.select.StructuralEvaluator$Not");
    }

    @Test(timeout = 4000, expected = IllegalStateException.class)
    public void testParseHasWithoutSelectorThrows() {
        QueryParser.parse(":has() must have a selector");
    }

    @Test(timeout = 4000, expected = IllegalStateException.class)
    public void testParseMatchesWithInvalidSyntaxThrows() {
        QueryParser.parse(":matches?holeText(1s)");
    }

    // ========================================================================
    // Tests for pseudo-selector edge cases
    // ========================================================================
    
    @Test(timeout = 4000, expected = IllegalStateException.class)
    public void testParseLtWithNonNumericThrows() {
        QueryParser.parse(":lt(%d)");
    }

    @Test(timeout = 4000, expected = IllegalStateException.class)
    public void testParseEqWithNonNumericThrows() {
        QueryParser.parse("I>tD:eq");
    }

    @Test(timeout = 4000, expected = IllegalStateException.class)
    public void testParseGtWithNonNumericThrows() {
        QueryParser.parse(":gt(%d)");
    }

    @Test(timeout = 4000, expected = IllegalStateException.class)
    public void testParseNthChildWithNonNumericThrows() {
        QueryParser.parse(":nth-child(%d)");
    }

    @Test(timeout = 4000, expected = IllegalStateException.class)
    public void testParseNthOfTypeWithInvalidIndex() {
        QueryParser.parse(":nth-of-type(16n+24) ~ ");
    }

    @Test(timeout = 4000, expected = IllegalStateException.class)
    public void testParseNthLastOfTypeWithInvalidIndex() {
        QueryParser.parse(":nth-last-of-type(-464n-464) ~ ");
    }

    @Test(timeout = 4000, expected = IllegalStateException.class)
    public void testParseNthLastChildWithZeroIndex() {
        QueryParser.parse(":nth-last-child(0) ~ ");
    }
}