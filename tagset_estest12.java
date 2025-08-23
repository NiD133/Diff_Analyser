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

public class TagSet_ESTestTest12 extends TagSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        TagSet tagSet0 = new TagSet();
        ParseSettings parseSettings0 = ParseSettings.preserveCase;
        Tag tag0 = tagSet0.valueOf(":_{P!x%}X,o'^63]{>", ":_{P!x%}X,o'^63]{>", parseSettings0);
        assertNotNull(tag0);
        Tag.Known = 55;
        tagSet0.add(tag0);
        Tag tag1 = tagSet0.get(":_{P!x%}X,o'^63]{>", ":_{P!x%}X,o'^63]{>");
        assertEquals(":_{p!x%}x,o'^63]{>", tag1.normalName());
        assertNotNull(tag1);
        assertFalse(tag1.formatAsBlock());
    }
}
