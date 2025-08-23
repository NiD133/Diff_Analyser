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

public class TagSet_ESTestTest1 extends TagSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        TagSet tagSet0 = TagSet.Html();
        TagSet tagSet1 = new TagSet(tagSet0);
        boolean boolean0 = tagSet1.equals(tagSet0);
        assertTrue(boolean0);
    }
}
