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

public class LeafNode_ESTestTest12 extends LeafNode_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        CDataNode cDataNode0 = new CDataNode("%:~n@");
        LeafNode leafNode0 = cDataNode0.doClone(cDataNode0);
        Node node0 = leafNode0.empty();
        assertTrue(node0.hasParent());
    }
}
