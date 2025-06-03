package org.example;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.EOFException;
import java.io.IOException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class GeneratedTestCase {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        NullReader nullReader0 = new NullReader((-1401L), true, false);
        int int0 = nullReader0.processChar();
        assertEquals((-1401L), nullReader0.getSize());
        assertTrue(nullReader0.markSupported());
        assertEquals(0, int0);
    }
}
