package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class TokenQueue_ESTestTest70 extends TokenQueue_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test69() throws Throwable {
        TokenQueue tokenQueue0 = new TokenQueue("%>-8wJ/e4'{T9H");
        String string0 = tokenQueue0.chompBalanced('(', 'W');
        assertEquals("%", string0);
        String[] stringArray0 = new String[3];
        stringArray0[0] = "%>-8wJ/e4'{T9H";
        stringArray0[1] = "org.jsoup.helper.ValidationException";
        stringArray0[2] = "%>-8wJ/e4'{T9H";
        String string1 = tokenQueue0.consumeToAny(stringArray0);
        assertEquals(">-8wJ/e4'{T9H", string1);
    }
}
