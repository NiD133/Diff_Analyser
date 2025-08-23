package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class BoundedReader_ESTestTest6 extends BoundedReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        StringReader stringReader0 = new StringReader(".''L5DuTEy{jV3");
        BoundedReader boundedReader0 = new BoundedReader(stringReader0, 1729);
        char[] charArray0 = new char[8];
        int int0 = boundedReader0.read(charArray0, 1, 1);
        assertEquals(1, int0);
        assertArrayEquals(new char[] { '\u0000', '.', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000' }, charArray0);
    }
}
