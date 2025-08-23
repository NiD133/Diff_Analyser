package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.EOFException;
import java.io.IOException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NullReader_ESTestTest8 extends NullReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        NullReader nullReader0 = new NullReader((-1L));
        long long0 = nullReader0.getSize();
        assertTrue(nullReader0.markSupported());
        assertEquals((-1L), long0);
    }
}
