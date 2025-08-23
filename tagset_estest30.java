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

public class TagSet_ESTestTest30 extends TagSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test29() throws Throwable {
        TagSet tagSet0 = TagSet.Html();
        Consumer<Tag> consumer0 = (Consumer<Tag>) mock(Consumer.class, new ViolatedAssumptionAnswer());
        TagSet tagSet1 = tagSet0.onNewTag(consumer0);
        Consumer<Tag> consumer1 = (Consumer<Tag>) mock(Consumer.class, new ViolatedAssumptionAnswer());
        TagSet tagSet2 = tagSet1.onNewTag(consumer1);
        assertSame(tagSet0, tagSet2);
    }
}