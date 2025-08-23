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

public class W3CDom_ESTestTest47 extends W3CDom_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test46() throws Throwable {
        W3CDom w3CDom0 = new W3CDom();
        assertTrue(w3CDom0.namespaceAware());
        Element element0 = new Element("javax.xml.xpath.XPathFactory:jsoup");
        W3CDom w3CDom1 = w3CDom0.namespaceAware(false);
        w3CDom1.fromJsoup(element0);
        assertFalse(w3CDom0.namespaceAware());
    }
}
