package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class TokenQueue_ESTestTest15 extends TokenQueue_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        TokenQueue tokenQueue0 = new TokenQueue("$dXr'&T^\"0qAav");
        char char0 = tokenQueue0.current();
        assertEquals('$', char0);
    }
}
