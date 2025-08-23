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

public class TagSet_ESTestTest10 extends TagSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        TagSet tagSet0 = new TagSet();
        ParseSettings parseSettings0 = ParseSettings.htmlDefault;
        Tag tag0 = tagSet0.valueOf("wtt=`D4p|", "wtt=`D4p|", parseSettings0);
        Tag.FormSubmittable = (-41);
        tag0.options = 46;
        Tag tag1 = tagSet0.valueOf("wtt=`D4p|", "wtt=`D4p|");
        assertEquals("wtt=`d4p|", tag1.normalName());
        assertNotSame(tag1, tag0);
        assertFalse(tag1.equals((Object) tag0));
        assertFalse(tag1.isInline());
    }
}
