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
    public void test29() throws Throwable {
        NullReader nullReader0 = new NullReader((-970L));
        char[] charArray0 = new char[14];
        nullReader0.read(charArray0);
        int int0 = nullReader0.read();
        assertEquals((-970L), nullReader0.getPosition());
        assertEquals((-1), int0);
    }
}
