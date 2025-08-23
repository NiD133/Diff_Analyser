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

public class TagSet_ESTestTest11 extends TagSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test10() throws Throwable {
        TagSet tagSet0 = TagSet.Html();
        Tag tag0 = tagSet0.valueOf("_-$>", "pr!", "pr!", true);
        Tag.FormSubmittable = 1;
        TagSet tagSet1 = tagSet0.add(tag0);
        Tag tag1 = tagSet1.get("_-$>", "pr!");
        assertEquals("pr!", tag1.normalName());
        assertNotNull(tag1);
        assertFalse(tag1.isEmpty());
    }
}
