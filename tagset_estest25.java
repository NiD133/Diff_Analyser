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

public class TagSet_ESTestTest25 extends TagSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test24() throws Throwable {
        TagSet tagSet0 = new TagSet();
        Object object0 = new Object();
        boolean boolean0 = tagSet0.equals(object0);
        assertFalse(boolean0);
    }
}
