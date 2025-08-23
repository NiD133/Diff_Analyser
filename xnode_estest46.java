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

public class XNode_ESTestTest46 extends XNode_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test045() throws Throwable {
        Properties properties0 = new Properties();
        IIOMetadataNode iIOMetadataNode0 = new IIOMetadataNode();
        XNode xNode0 = new XNode((XPathParser) null, iIOMetadataNode0, properties0);
        // Undeclared exception!
        try {
            xNode0.getStringAttribute((String) null, "");
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }
}
