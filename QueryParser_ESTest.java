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

    @Test(timeout = 4000)
    public void testOrEvaluatorWithSameInstance() throws Throwable {
        Evaluator.IsLastChild isLastChildEvaluator = new Evaluator.IsLastChild();
        Evaluator combinedEvaluator = QueryParser.or(isLastChildEvaluator, isLastChildEvaluator);
        Evaluator finalEvaluator = QueryParser.or(combinedEvaluator, isLastChildEvaluator);
        assertSame(finalEvaluator, combinedEvaluator);
    }

    @Test(timeout = 4000)
    public void testParseInvalidQueryThrowsIllegalStateException() throws Throwable {
        assertThrows(IllegalStateException.class, () -> {
            QueryParser.parse("RV,LfO:4},X");
        });
    }

    @Test(timeout = 4000)
    public void testParseUnexpectedTokenThrowsIllegalStateException() throws Throwable {
        assertThrows(IllegalStateException.class, () -> {
            QueryParser.parse(":is(:eq(1114)) ~ ");
        });
    }

    @Test(timeout = 4000)
    public void testParseWithUnexpectedTokenAtEndThrowsIllegalStateException() throws Throwable {
        assertThrows(IllegalStateException.class, () -> {
            QueryParser.parse("#P>hfIV>e@0fe");
        });
    }

    @Test(timeout = 4000)
    public void testCombinatorCreatesNewEvaluator() throws Throwable {
        Evaluator.AttributeWithValue attributeEvaluator = new Evaluator.AttributeWithValue("6N8}sq&/", "org.jsoup.internal.SimpleStreamReader");
        Evaluator combinedEvaluator = QueryParser.and(attributeEvaluator, attributeEvaluator);
        Evaluator finalEvaluator = QueryParser.combinator(attributeEvaluator, ' ', combinedEvaluator);
        assertNotSame(combinedEvaluator, finalEvaluator);
    }

    @Test(timeout = 4000)
    public void testAndWithNullEvaluatorsReturnsNull() throws Throwable {
        Evaluator evaluator = QueryParser.and(null, null);
        assertNull(evaluator);
    }

    @Test(timeout = 4000)
    public void testCombinatorWithNullEvaluatorsThrowsNullPointerException() throws Throwable {
        assertThrows(NullPointerException.class, () -> {
            QueryParser.combinator(null, '+', null);
        });
    }

    @Test(timeout = 4000)
    public void testAndWithNullSecondEvaluatorThrowsNullPointerException() throws Throwable {
        StructuralEvaluator.Not notEvaluator = new StructuralEvaluator.Not(null);
        assertThrows(NullPointerException.class, () -> {
            QueryParser.and(notEvaluator, null);
        });
    }

    @Test(timeout = 4000)
    public void testParseWithInvalidQueryThrowsIllegalStateException() throws Throwable {
        assertThrows(IllegalStateException.class, () -> {
            QueryParser.parse("37m~T/SMo)=E[BHj8|{");
        });
    }

    @Test(timeout = 4000)
    public void testParseValidMatchesWholeOwnText() throws Throwable {
        Evaluator evaluator = QueryParser.parse(":matchesWholeOwnText(%s)");
        assertNotNull(evaluator);
    }

    @Test(timeout = 4000)
    public void testParseValidMatchesWholeText() throws Throwable {
        Evaluator evaluator = QueryParser.parse(":matchesWholeText(%s)");
        assertNotNull(evaluator);
    }

    @Test(timeout = 4000)
    public void testParseValidMatchesOwn() throws Throwable {
        Evaluator evaluator = QueryParser.parse(":matchesOwn(%s)");
        assertNotNull(evaluator);
    }

    @Test(timeout = 4000)
    public void testParseWithDanglingMetaCharacterThrowsIllegalStateException() throws Throwable {
        assertThrows(IllegalStateException.class, () -> {
            QueryParser.parse(":matches?holeText(1s)");
        });
    }

    @Test(timeout = 4000)
    public void testParseValidContainsWholeOwnText() throws Throwable {
        Evaluator evaluator = QueryParser.parse(":containsWholeOwnText(%s)");
        assertNotNull(evaluator);
    }

    @Test(timeout = 4000)
    public void testParseValidContainsWholeText() throws Throwable {
        Evaluator evaluator = QueryParser.parse(":containsWholeText(%s)");
        assertNotNull(evaluator);
    }

    @Test(timeout = 4000)
    public void testParseValidContainsOwn() throws Throwable {
        Evaluator evaluator = QueryParser.parse(":containsOwn(%s)");
        assertNotNull(evaluator);
    }

    @Test(timeout = 4000)
    public void testParseValidContains() throws Throwable {
        Evaluator evaluator = QueryParser.parse(":contains(%s)");
        assertNotNull(evaluator);
    }

    @Test(timeout = 4000)
    public void testParseValidAttributeWithTildeEquals() throws Throwable {
        Evaluator evaluator = QueryParser.parse("[%s~=%s]");
        assertNotNull(evaluator);
    }

    @Test(timeout = 4000)
    public void testParseValidAttributeWithAsteriskEquals() throws Throwable {
        Evaluator evaluator = QueryParser.parse("[%s*=%s]");
        assertNotNull(evaluator);
    }

    @Test(timeout = 4000)
    public void testParseValidAttributeWithCaretEquals() throws Throwable {
        Evaluator evaluator = QueryParser.parse("[%s^=%s]");
        assertNotNull(evaluator);
    }

    @Test(timeout = 4000)
    public void testParseValidAttributeWithDollarEquals() throws Throwable {
        Evaluator evaluator = QueryParser.parse("[%s$=%s]");
        assertNotNull(evaluator);
    }

    @Test(timeout = 4000)
    public void testParseValidAttributeWithEquals() throws Throwable {
        Evaluator evaluator = QueryParser.parse("[%s=%s]");
        assertNotNull(evaluator);
    }

    @Test(timeout = 4000)
    public void testParseWithUnexpectedTokenThrowsIllegalStateException() throws Throwable {
        assertThrows(IllegalStateException.class, () -> {
            QueryParser.parse("wj[b]_o4~M");
        });
    }

    @Test(timeout = 4000)
    public void testParseWithInvalidCharacterRangeThrowsIllegalStateException() throws Throwable {
        assertThrows(IllegalStateException.class, () -> {
            QueryParser.parse("[^-a-zA-Z0-9_:.]+");
        });
    }

    @Test(timeout = 4000)
    public void testParseValidAttributeWithNotEquals() throws Throwable {
        Evaluator evaluator = QueryParser.parse("[%s!=%s]");
        assertNotNull(evaluator);
    }

    @Test(timeout = 4000)
    public void testParseWithUnexpectedTokenAtMiddleThrowsIllegalStateException() throws Throwable {
        assertThrows(IllegalStateException.class, () -> {
            QueryParser.parse("1+RZ-2|k}9kC<q");
        });
    }

    @Test(timeout = 4000)
    public void testParseValidQueryWithPipeAsterisk() throws Throwable {
        Evaluator evaluator = QueryParser.parse("r8qm5ctg|*");
        assertNotNull(evaluator);
    }

    @Test(timeout = 4000)
    public void testParseWithUnexpectedTokenAtStartThrowsIllegalStateException() throws Throwable {
        assertThrows(IllegalStateException.class, () -> {
            QueryParser.parse("x:jT)sndzU!E|jR3");
        });
    }

    @Test(timeout = 4000)
    public void testParseWithUnexpectedTokenAtEndThrowsIllegalStateException() throws Throwable {
        assertThrows(IllegalStateException.class, () -> {
            QueryParser.parse("9F>R8Qm5cTG:&@z");
        });
    }

    @Test(timeout = 4000)
    public void testParseValidLastOfType() throws Throwable {
        Evaluator evaluator = QueryParser.parse(":last-of-type");
        assertEquals(":last-of-type", evaluator.toString());
    }

    @Test(timeout = 4000)
    public void testParseValidOnlyOfType() throws Throwable {
        Evaluator evaluator = QueryParser.parse(":only-of-type");
        assertEquals(":only-of-type", evaluator.toString());
    }

    @Test(timeout = 4000)
    public void testParseValidFirstOfType() throws Throwable {
        Evaluator evaluator = QueryParser.parse(":first-of-type");
        assertEquals(":first-of-type", evaluator.toString());
    }

    @Test(timeout = 4000)
    public void testParseValidLastChild() throws Throwable {
        Evaluator evaluator = QueryParser.parse(":last-child");
        assertEquals(":last-child", evaluator.toString());
    }

    @Test(timeout = 4000)
    public void testParseValidMatchText() throws Throwable {
        Evaluator evaluator = QueryParser.parse(":matchText");
        assertEquals(":matchText", evaluator.toString());
    }

    @Test(timeout = 4000)
    public void testParseValidEmpty() throws Throwable {
        Evaluator evaluator = QueryParser.parse(":empty");
        assertEquals(":empty", evaluator.toString());
    }

    @Test(timeout = 4000)
    public void testParseValidRoot() throws Throwable {
        Evaluator evaluator = QueryParser.parse(":root");
        assertEquals(":root", evaluator.toString());
    }

    @Test(timeout = 4000)
    public void testParseWithInvalidIndexThrowsIllegalStateException() throws Throwable {
        assertThrows(IllegalStateException.class, () -> {
            QueryParser.parse(":lt(%d)");
        });
    }

    @Test(timeout = 4000)
    public void testParseWithNonNumericIndexThrowsIllegalStateException() throws Throwable {
        assertThrows(IllegalStateException.class, () -> {
            QueryParser.parse("I>tD:eq");
        });
    }

    @Test(timeout = 4000)
    public void testParseWithInvalidNthLastOfTypeThrowsIllegalStateException() throws Throwable {
        assertThrows(IllegalStateException.class, () -> {
            QueryParser.parse(":nth-last-of-type(-464n-464) ~ ");
        });
    }

    @Test(timeout = 4000)
    public void testParseWithInvalidNthOfTypeThrowsIllegalStateException() throws Throwable {
        assertThrows(IllegalStateException.class, () -> {
            QueryParser.parse(":nth-of-type(16n+24) ~ ");
        });
    }

    @Test(timeout = 4000)
    public void testParseValidOnlyChild() throws Throwable {
        Evaluator evaluator = QueryParser.parse(":only-child");
        assertEquals(":only-child", evaluator.toString());
    }

    @Test(timeout = 4000)
    public void testParseWithInvalidNthLastChildThrowsIllegalStateException() throws Throwable {
        assertThrows(IllegalStateException.class, () -> {
            QueryParser.parse(":nth-last-child(0) ~ ");
        });
    }

    @Test(timeout = 4000)
    public void testParseWithInvalidNthChildThrowsIllegalStateException() throws Throwable {
        assertThrows(IllegalStateException.class, () -> {
            QueryParser.parse(":nth-child(%d)");
        });
    }

    @Test(timeout = 4000)
    public void testParseValidFirstChild() throws Throwable {
        Evaluator evaluator = QueryParser.parse(":first-child");
        assertEquals(":first-child", evaluator.toString());
    }

    @Test(timeout = 4000)
    public void testParseWithUnexpectedTokenAtMiddleThrowsIllegalStateException() throws Throwable {
        assertThrows(IllegalStateException.class, () -> {
            QueryParser.parse("org.jsoup.select.StructuralEvaluator$Not");
        });
    }

    @Test(timeout = 4000)
    public void testOrWithNullEvaluatorsThrowsNullPointerException() throws Throwable {
        assertThrows(NullPointerException.class, () -> {
            QueryParser.or(null, null);
        });
    }

    @Test(timeout = 4000)
    public void testCombinatorWithInvalidCombinatorThrowsIllegalStateException() throws Throwable {
        assertThrows(IllegalStateException.class, () -> {
            QueryParser.combinator(null, ']', null);
        });
    }

    @Test(timeout = 4000)
    public void testParseValidMatchesWholeOwnTextWithSpecialCharacters() throws Throwable {
        Evaluator evaluator = QueryParser.parse(">mttchesWholeOw>Text");
        assertNotNull(evaluator);
    }

    @Test(timeout = 4000)
    public void testParseValidAsterisk() throws Throwable {
        Evaluator evaluator = QueryParser.parse("*");
        assertEquals("*", evaluator.toString());
    }

    @Test(timeout = 4000)
    public void testParseValidPipeAsterisk() throws Throwable {
        Evaluator evaluator = QueryParser.parse("*|9");
        assertNotNull(evaluator);
    }

    @Test(timeout = 4000)
    public void testParseWithInvalidGtIndexThrowsIllegalStateException() throws Throwable {
        assertThrows(IllegalStateException.class, () -> {
            QueryParser.parse(":gt(%d)");
        });
    }

    @Test(timeout = 4000)
    public void testParseValidNotSelector() throws Throwable {
        Evaluator evaluator = QueryParser.parse(":not(selector) subselect must not be empty");
        assertNotNull(evaluator);
    }

    @Test(timeout = 4000)
    public void testParseValidContainsData() throws Throwable {
        Evaluator evaluator = QueryParser.parse(":containsData(text) query must not be empty");
        assertNotNull(evaluator);
    }

    @Test(timeout = 4000)
    public void testParseWithInvalidHasSelectorThrowsIllegalStateException() throws Throwable {
        assertThrows(IllegalStateException.class, () -> {
            QueryParser.parse(":has() must have a selector");
        });
    }
}