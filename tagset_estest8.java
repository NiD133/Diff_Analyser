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

public class TagSet_ESTestTest8 extends TagSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        TagSet tagSet0 = new TagSet();
        ParseSettings parseSettings0 = ParseSettings.htmlDefault;
        Tag tag0 = tagSet0.valueOf("wtt=`D4p|", "wtt=`D4p|", parseSettings0);
        tag0.options = 64;
        Tag tag1 = tagSet0.valueOf("wtt=`D4p|", "wtt=`D4p|");
        assertNotSame(tag1, tag0);
        assertFalse(tag1.equals((Object) tag0));
        assertEquals("wtt=`d4p|", tag1.normalName());
        assertTrue(tag1.preserveWhitespace());
    }
}