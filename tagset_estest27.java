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

public class TagSet_ESTestTest27 extends TagSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test26() throws Throwable {
        TagSet tagSet0 = new TagSet();
        TagSet tagSet1 = new TagSet(tagSet0);
        tagSet1.valueOf("wtt=`d4p|", "Must set charset arg to character set of file to parse. Set to null to attempt to detect from HTML");
        assertFalse(tagSet1.equals((Object) tagSet0));
    }
}
