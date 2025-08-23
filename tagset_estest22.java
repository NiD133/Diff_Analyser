package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.function.Consumer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class TagSet_ESTestTest22 extends TagSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test21() throws Throwable {
        boolean boolean0 = Tag.isKnownTag("cxpPE");
        assertFalse(boolean0);
    }
}
