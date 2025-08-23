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

public class TagSet_ESTestTest26 extends TagSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test25() throws Throwable {
        Tag tag0 = Tag.valueOf("U");
        assertNotNull(tag0);
        assertEquals("U", tag0.toString());
        assertTrue(tag0.isKnownTag());
    }
}
