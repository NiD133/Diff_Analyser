package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.EOFException;
import java.io.IOException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NullReader_ESTestTest26 extends NullReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test25() throws Throwable {
        NullReader nullReader0 = new NullReader((-3294L), false, false);
        boolean boolean0 = nullReader0.markSupported();
        assertEquals((-3294L), nullReader0.getSize());
        assertFalse(boolean0);
    }
}
