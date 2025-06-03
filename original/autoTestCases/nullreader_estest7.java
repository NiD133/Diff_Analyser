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
    public void test06() throws Throwable {
        NullReader nullReader0 = new NullReader(1592L);
        char[] charArray0 = new char[5];
        int int0 = nullReader0.read(charArray0, 2146694131, 2146694131);
        assertEquals(1592L, nullReader0.getPosition());
        assertEquals(1592, int0);
    }
}
