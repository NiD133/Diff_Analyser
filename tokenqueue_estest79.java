package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class TokenQueue_ESTestTest79 extends TokenQueue_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test78() throws Throwable {
        TokenQueue tokenQueue0 = new TokenQueue("Z<}0z:#; O.[w{T3D");
        char[] charArray0 = new char[8];
        boolean boolean0 = tokenQueue0.matchesAny(charArray0);
        assertFalse(boolean0);
    }
}
