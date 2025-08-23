package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class QueryParser_ESTestTest52 extends QueryParser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test51() throws Throwable {
        Evaluator evaluator0 = QueryParser.parse(":not(selector) subselect must not be empty");
        assertNotNull(evaluator0);
    }
}
