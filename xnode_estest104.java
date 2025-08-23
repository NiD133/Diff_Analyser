package org.apache.ibatis.parsing;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.function.Supplier;
import javax.imageio.metadata.IIOMetadataNode;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.ext.DefaultHandler2;

public class XNode_ESTestTest104 extends XNode_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test103() throws Throwable {
        IIOMetadataNode iIOMetadataNode0 = new IIOMetadataNode();
        IIOMetadataNode iIOMetadataNode1 = new IIOMetadataNode();
        Node node0 = iIOMetadataNode0.insertBefore(iIOMetadataNode1, iIOMetadataNode1);
        XNode xNode0 = new XNode((XPathParser) null, node0, (Properties) null);
        // Undeclared exception!
        try {
            xNode0.getValueBasedIdentifier();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }
}
