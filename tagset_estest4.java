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

public class TagSet_ESTestTest4 extends TagSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        TagSet tagSet0 = TagSet.Html();
        Tag tag0 = new Tag("fg6U2^oJ<:", "fg6U2^oJ<:");
        tagSet0.add(tag0);
        assertFalse(tag0.isFormSubmittable());
        Tag.FormSubmittable = (-1);
        Tag tag1 = tagSet0.valueOf("fg6U2^oJ<:", "html", "fg6U2^oJ<:", false);
        assertEquals("fg6U2^oJ<", tag1.prefix());
    }
}
