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

public class TagSet_ESTestTest14 extends TagSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        TagSet tagSet0 = new TagSet();
        // Undeclared exception!
        try {
            tagSet0.valueOf("org.jsoup.select.Evaluator$AttributeWithValueMatching", "org.jsoup.select.Evaluator$AttributeWithValueMatching", (ParseSettings) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.jsoup.parser.TagSet", e);
        }
    }
}
