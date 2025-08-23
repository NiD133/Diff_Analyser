package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Soundex_ESTestTest18 extends Soundex_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test17() throws Throwable {
        Soundex soundex0 = new Soundex();
        Object object0 = soundex0.encode((Object) "atW+2N,x7`1kf@");
        assertNotNull(object0);
        assertEquals(4, soundex0.getMaxLength());
        assertEquals("A352", object0);
    }
}
