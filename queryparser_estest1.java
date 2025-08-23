package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class QueryParser_ESTestTest1 extends QueryParser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        Evaluator.IsLastChild evaluator_IsLastChild0 = new Evaluator.IsLastChild();
        Evaluator evaluator0 = QueryParser.or(evaluator_IsLastChild0, evaluator_IsLastChild0);
        Evaluator evaluator1 = QueryParser.or(evaluator0, evaluator_IsLastChild0);
        assertSame(evaluator1, evaluator0);
    }
}