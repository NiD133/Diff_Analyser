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

public class BoundedReader_ESTestTest22 extends BoundedReader_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test21() throws Throwable {
        StringReader stringReader0 = new StringReader("");
        BoundedReader boundedReader0 = new BoundedReader(stringReader0, 1);
        boundedReader0.reset();
    }
}
