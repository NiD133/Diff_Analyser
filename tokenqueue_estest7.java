package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class TokenQueue_ESTestTest7 extends TokenQueue_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        TokenQueue tokenQueue0 = new TokenQueue("d:,&:wx");
        String string0 = tokenQueue0.remainder();
        String string1 = tokenQueue0.remainder();
        assertFalse(string1.equals((Object) string0));
    }
}
