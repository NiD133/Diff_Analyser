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

public class TagSet_ESTestTest21 extends TagSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test20() throws Throwable {
        TagSet tagSet0 = TagSet.Html();
        Tag tag0 = tagSet0.get("pre", "http://www.w3.org/1999/xhtml");
        assertEquals(4, Tag.Block);
    }
}
