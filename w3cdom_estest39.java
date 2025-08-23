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

public class W3CDom_ESTestTest39 extends W3CDom_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test38() throws Throwable {
        Document document0 = Parser.parse("", "V5");
        org.w3c.dom.Document document1 = W3CDom.convert(document0);
        W3CDom.W3CBuilder w3CDom_W3CBuilder0 = new W3CDom.W3CBuilder(document1);
        Parser parser0 = Parser.xmlParser();
        StringReader stringReader0 = new StringReader("xmlns");
        Document document2 = parser0.parseInput((Reader) stringReader0, "$.|O:LS7]cD\u0001o5k\"5h");
        Element element0 = document2.attr("http://www.w3.org/1998/Math/MathML", "http://www.w3.org/2000/svg");
        // Undeclared exception!
        try {
            w3CDom_W3CBuilder0.traverse(element0);
            fail("Expecting exception: DOMException");
        } catch (DOMException e) {
        }
    }
}
