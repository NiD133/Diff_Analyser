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

public class LeafNode_ESTestTest27 extends LeafNode_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test26() throws Throwable {
        CDataNode cDataNode0 = new CDataNode("-cOata");
        cDataNode0.parentNode = (Node) cDataNode0;
        // Undeclared exception!
        cDataNode0.attr("-cOata", "-cOata");
    }
}
