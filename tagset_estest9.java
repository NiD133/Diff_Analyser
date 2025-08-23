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

public class TagSet_ESTestTest9 extends TagSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        TagSet tagSet0 = TagSet.initHtmlDefault();
        Tag tag0 = Tag.valueOf("kwt=j`D4p|");
        Tag tag1 = tag0.namespace("kwt=j`D4p|");
        tagSet0.add(tag1);
        assertEquals("kwt=j`D4p|", tag1.toString());
        Tag tag2 = tagSet0.valueOf("kwt=j`D4p|", "kwt=j`D4p|");
        assertEquals("kwt=j`D4p|", tag2.name());
    }
}
