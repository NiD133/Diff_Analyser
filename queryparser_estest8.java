package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class QueryParser_ESTestTest8 extends QueryParser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        StructuralEvaluator.Not structuralEvaluator_Not0 = new StructuralEvaluator.Not((Evaluator) null);
        // Undeclared exception!
        try {
            QueryParser.and(structuralEvaluator_Not0, (Evaluator) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.jsoup.select.StructuralEvaluator$Not", e);
        }
    }
}
