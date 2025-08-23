package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class QueryParser_ESTestTest45 extends QueryParser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test44() throws Throwable {
        // Undeclared exception!
        try {
            QueryParser.parse("org.jsoup.select.StructuralEvaluator$Not");
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            //
            // Could not parse query 'org.jsoup.select.StructuralEvaluator$Not': unexpected token at '$Not'
            //
            verifyException("org.jsoup.select.QueryParser", e);
        }
    }
}
