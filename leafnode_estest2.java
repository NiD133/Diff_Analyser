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

public class LeafNode_ESTestTest2 extends LeafNode_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        DocumentType documentType0 = new DocumentType("Gyb$AWbT${", "Gyb$AWbT${", "Gyb$AWbT${");
        documentType0.doSetBaseUri("");
        assertFalse(documentType0.hasParent());
    }
}
