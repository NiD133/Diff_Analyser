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
    public void test05() throws Throwable {
        NullReader nullReader0 = new NullReader(1279L);
        char[] charArray0 = new char[4];
        int int0 = nullReader0.read(charArray0, 938, 0);
        assertTrue(nullReader0.markSupported());
        assertEquals(0, int0);
        assertEquals(0L, nullReader0.getPosition());
    }
}
