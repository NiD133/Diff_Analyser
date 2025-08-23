package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.List;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jsoup.internal.QuietAppendable;
import org.jsoup.parser.Parser;
import org.junit.runner.RunWith;

public class LeafNode_ESTestTest39 extends LeafNode_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test38() throws Throwable {
        Comment comment0 = new Comment("QuR~!GB`!_");
        String string0 = comment0.absUrl("QuR~!GB`!_");
        assertEquals("", string0);
    }
}
