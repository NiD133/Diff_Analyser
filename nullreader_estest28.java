package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.EOFException;
import java.io.IOException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NullReader_ESTestTest28 extends NullReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test27() throws Throwable {
        NullReader nullReader0 = new NullReader((-3294L), false, false);
        int int0 = nullReader0.read();
        assertEquals(1L, nullReader0.getPosition());
        assertEquals(0, int0);
    }
}
