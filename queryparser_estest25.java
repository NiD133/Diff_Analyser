package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class QueryParser_ESTestTest25 extends QueryParser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test24() throws Throwable {
        Evaluator evaluator0 = QueryParser.parse("[%s!=%s]");
        assertNotNull(evaluator0);
    }
}
