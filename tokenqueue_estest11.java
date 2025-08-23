package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class TokenQueue_ESTestTest11 extends TokenQueue_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        TokenQueue tokenQueue0 = new TokenQueue("Jkl):ip4E/0M");
        boolean boolean0 = tokenQueue0.matches("rD@)L`r>t");
        assertFalse(boolean0);
    }
}
