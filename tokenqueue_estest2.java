package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class TokenQueue_ESTestTest2 extends TokenQueue_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        String string0 = TokenQueue.unescape("H<b:oK|jCxQ\\P4C3U.");
        assertEquals("H<b:oK|jCxQP4C3U.", string0);
    }
}
