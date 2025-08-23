package org.jsoup.helper;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.sun.org.apache.xerces.internal.dom.DocumentImpl;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.imageio.metadata.IIOMetadataNode;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.junit.runner.RunWith;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class W3CDom_ESTestTest30 extends W3CDom_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test29() throws Throwable {
        Document document0 = Document.createShell("554\"");
        W3CDom w3CDom0 = new W3CDom();
        org.w3c.dom.Document document1 = w3CDom0.fromJsoup(document0);
        Attributes attributes0 = new Attributes();
        Attributes attributes1 = attributes0.put("", true);
        Map<String, String> map0 = attributes1.dataset();
        // Undeclared exception!
        try {
            W3CDom.asString(document1, map0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // String must not be empty
            //
            verifyException("org.jsoup.helper.Validate", e);
        }
    }
}
