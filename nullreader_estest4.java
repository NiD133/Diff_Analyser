package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.EOFException;
import java.io.IOException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NullReader_ESTestTest4 extends NullReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        NullReader nullReader0 = new NullReader((-995L));
        nullReader0.skip((-5663L));
        int int0 = nullReader0.read((char[]) null, 0, 0);
        assertEquals((-5663L), nullReader0.getPosition());
        assertEquals(0, int0);
    }
}
