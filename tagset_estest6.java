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

public class TagSet_ESTestTest6 extends TagSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        TagSet tagSet0 = TagSet.initHtmlDefault();
        Tag.Block = 179;
        Tag tag0 = tagSet0.valueOf("noscript", "noscript");
        TagSet tagSet1 = tagSet0.add(tag0);
        Tag tag1 = tagSet1.valueOf("noscript", "noscript", "noscript", false);
        assertEquals("noscript", tag1.namespace());
    }
}
