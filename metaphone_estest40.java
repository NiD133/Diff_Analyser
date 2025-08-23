package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Metaphone_ESTestTest40 extends Metaphone_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test39() throws Throwable {
        Metaphone metaphone0 = new Metaphone();
        String string0 = metaphone0.metaphone("Am<p[7Pu444indX8o6");
        assertEquals("AMPP", string0);
    }
}
