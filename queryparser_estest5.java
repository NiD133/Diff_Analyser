package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class QueryParser_ESTestTest5 extends QueryParser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        Evaluator.AttributeWithValue evaluator_AttributeWithValue0 = new Evaluator.AttributeWithValue("6N8}sq&/", "org.jsoup.internal.SimpleStreamReader");
        Evaluator evaluator0 = QueryParser.and(evaluator_AttributeWithValue0, evaluator_AttributeWithValue0);
        Evaluator evaluator1 = QueryParser.combinator(evaluator_AttributeWithValue0, ' ', evaluator0);
        assertNotSame(evaluator0, evaluator1);
    }
}
