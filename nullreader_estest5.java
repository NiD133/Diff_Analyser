package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.EOFException;
import java.io.IOException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NullReader_ESTestTest5 extends NullReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        NullReader nullReader0 = new NullReader(959L, true, true);
        char[] charArray0 = new char[4];
        int int0 = nullReader0.read(charArray0, (-2654), 616);
        assertEquals(616L, nullReader0.getPosition());
        assertEquals(616, int0);
    }
}