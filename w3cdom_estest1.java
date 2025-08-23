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

public class W3CDom_ESTestTest1 extends W3CDom_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        Document document0 = Parser.parse("", "V5");
        Document document1 = document0.clone();
        Element element0 = document1.attr("http://www.w3.org/1998/Math/MathML", "http://www.w3.org/2000/svg");
        W3CDom w3CDom0 = new W3CDom();
        Element element1 = document0.prependChild(element0);
        w3CDom0.fromJsoup(element1);
        assertTrue(w3CDom0.namespaceAware());
    }
}
