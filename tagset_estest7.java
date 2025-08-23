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

public class TagSet_ESTestTest7 extends TagSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        TagSet tagSet0 = new TagSet();
        Tag.InlineContainer = 1629;
        ParseSettings parseSettings0 = ParseSettings.preserveCase;
        Tag tag0 = tagSet0.valueOf(":_{P!x%}X,o'^63]{>", ":_{P!x%}X,o'^63]{>", parseSettings0);
        TagSet tagSet1 = tagSet0.add(tag0);
        Tag tag1 = tagSet1.valueOf(":_{P!x%}X,o'^63]{>", ":_{P!x%}X,o'^63]{>", ":_{P!x%}X,o'^63]{>", true);
        assertEquals(":_{p!x%}x,o'^63]{>", tag1.normalName());
        assertFalse(tag1.isSelfClosing());
    }
}
