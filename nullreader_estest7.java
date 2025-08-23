package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.EOFException;
import java.io.IOException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NullReader_ESTestTest7 extends NullReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        NullReader nullReader0 = new NullReader((-476L));
        int int0 = nullReader0.processChar();
        assertEquals(0, int0);
        assertEquals((-476L), nullReader0.getSize());
        assertTrue(nullReader0.markSupported());
    }
}
