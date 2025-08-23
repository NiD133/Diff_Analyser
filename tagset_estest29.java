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

public class TagSet_ESTestTest29 extends TagSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test28() throws Throwable {
        TagSet tagSet0 = TagSet.initHtmlDefault();
        Consumer<Tag> consumer0 = (Consumer<Tag>) mock(Consumer.class, new ViolatedAssumptionAnswer());
        TagSet tagSet1 = tagSet0.onNewTag(consumer0);
        Tag tag0 = tagSet1.valueOf("bp", "bp");
        assertEquals("bp", tag0.toString());
    }
}
