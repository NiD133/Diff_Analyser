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

public class TagSet_ESTestTest2 extends TagSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        TagSet tagSet0 = new TagSet();
        Tag tag0 = tagSet0.valueOf("tt=`d4!p|", "tt=`d4!p|");
        tag0.set((-301));
        ParseSettings parseSettings0 = ParseSettings.preserveCase;
        Tag tag1 = tagSet0.valueOf("tt=`d4!p|", "tt=`d4!p|", parseSettings0);
        assertTrue(tag1.preserveWhitespace());
    }
}
