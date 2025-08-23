package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.EOFException;
import java.io.IOException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class NullReader_ESTestTest23 extends NullReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test22() throws Throwable {
        NullReader nullReader0 = new NullReader((-3294L), false, false);
        char[] charArray0 = new char[7];
        nullReader0.read(charArray0, 2144545913, 0);
        long long0 = nullReader0.skip((-2904L));
        assertEquals((-3294L), nullReader0.getPosition());
        assertEquals((-1L), long0);
    }
}
