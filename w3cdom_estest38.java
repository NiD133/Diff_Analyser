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

public class W3CDom_ESTestTest38 extends W3CDom_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test37() throws Throwable {
        W3CDom w3CDom0 = new W3CDom();
        Document document0 = Parser.parse(")S", "jsoupSource");
        org.w3c.dom.Document document1 = w3CDom0.fromJsoup(document0);
        String string0 = W3CDom.asString(document1, (Map<String, String>) null);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><html xmlns=\"http://www.w3.org/1999/xhtml\"><head/><body>)S</body></html>", string0);
    }
}