package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class QueryParser_ESTestTest50 extends QueryParser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test49() throws Throwable {
        Evaluator evaluator0 = QueryParser.parse("*|9");
        assertNotNull(evaluator0);
    }
}
