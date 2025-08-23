package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class QueryParser_ESTestTest32 extends QueryParser_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test31() throws Throwable {
        Evaluator evaluator0 = QueryParser.parse(":first-of-type");
        assertEquals(":first-of-type", evaluator0.toString());
    }
}
