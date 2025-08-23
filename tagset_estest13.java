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

public class TagSet_ESTestTest13 extends TagSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        TagSet tagSet0 = TagSet.HtmlTagSet;
        ParseSettings parseSettings0 = ParseSettings.preserveCase;
        // Undeclared exception!
        try {
            tagSet0.valueOf("", "", parseSettings0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // String must not be empty
            //
            verifyException("org.jsoup.helper.Validate", e);
        }
    }
}
