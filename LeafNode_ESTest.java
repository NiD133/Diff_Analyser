package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jsoup.internal.QuietAppendable;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class LeafNode_ESTest extends LeafNode_ESTest_scaffolding {

    private static final String SAMPLE_TEXT = "Gyb$AWbT${";
    private static final String EMPTY_STRING = "";
    private static final String CDATA_ATTR = "#cdata";
    private static final String PUBLIC_ATTR = "PUBLIC";

    @Test(timeout = 4000)
    public void testEnsureChildNodes_EmptyList() {
        CDataNode cDataNode = new CDataNode("Gl&1]d*:{^7FUW]?uZ");
        List<Node> childNodes = cDataNode.ensureChildNodes();
        assertTrue(childNodes.isEmpty());
    }

    @Test(timeout = 4000)
    public void testDocumentTypeHasNoParent() {
        DocumentType documentType = new DocumentType(SAMPLE_TEXT, SAMPLE_TEXT, SAMPLE_TEXT);
        documentType.doSetBaseUri(EMPTY_STRING);
        assertFalse(documentType.hasParent());
    }

    @Test(timeout = 4000)
    public void testCoreValueAndSiblingIndex() {
        CDataNode cDataNode = new CDataNode("~p1SkcF]Q'5D");
        cDataNode.coreValue(EMPTY_STRING);
        assertEquals(0, cDataNode.siblingIndex());
    }

    @Test(timeout = 4000)
    public void testRemoveAttrFromCDataNode() {
        CDataNode cDataNode = new CDataNode("%:~n@");
        cDataNode.setSiblingIndex(1);
        Node node = cDataNode.removeAttr(EMPTY_STRING);
        assertEquals(0, node.childNodeSize());
    }

    @Test(timeout = 4000)
    public void testRemoveAttrFromDataNode() {
        DataNode dataNode = new DataNode("gMz?D>{rCwB%:");
        dataNode.setSiblingIndex(-1447);
        Node node = dataNode.removeAttr("gMz?D>{rCwB%:");
        assertFalse(node.hasParent());
    }

    @Test(timeout = 4000)
    public void testCDataNodeHasAttributes() {
        CDataNode cDataNode = new CDataNode(SAMPLE_TEXT);
        cDataNode.attr(PUBLIC_ATTR, null);
        assertTrue(cDataNode.hasAttributes());
    }

    @Test(timeout = 4000)
    public void testCDataNodeHasNoAttributes() {
        CDataNode cDataNode = new CDataNode(EMPTY_STRING);
        assertFalse(cDataNode.hasAttributes());
    }

    @Test(timeout = 4000)
    public void testCDataNodeHasCDataAttr() {
        CDataNode cDataNode = new CDataNode(EMPTY_STRING);
        assertTrue(cDataNode.hasAttr(CDATA_ATTR));
    }

    @Test(timeout = 4000)
    public void testCommentHasNoAttr() {
        Comment comment = new Comment("H4Q{hWcU$Y*");
        assertFalse(comment.hasAttr("W"));
    }

    @Test(timeout = 4000)
    public void testXmlDeclarationEmptyNode() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("<eaHch", false);
        xmlDeclaration.siblingIndex = 1574;
        Node node = xmlDeclaration.empty();
        assertSame(xmlDeclaration, node);
    }

    @Test(timeout = 4000)
    public void testTextNodeEmptyNode() {
        TextNode textNode = TextNode.createFromEncoded(".");
        textNode.setSiblingIndex(-2159);
        Node node = textNode.empty();
        assertSame(node, textNode);
    }

    @Test(timeout = 4000)
    public void testCloneCDataNode() {
        CDataNode cDataNode = new CDataNode("%:~n@");
        LeafNode clonedNode = cDataNode.doClone(cDataNode);
        Node node = clonedNode.empty();
        assertTrue(node.hasParent());
    }

    @Test(timeout = 4000)
    public void testCloneDataNode() {
        DataNode dataNode = new DataNode("org.jsoup.select.Evaluator$IndexGreaterThan");
        dataNode.siblingIndex = 74;
        CDataNode cDataNode = new CDataNode("'mMH");
        LeafNode clonedNode = dataNode.doClone(cDataNode);
        assertTrue(clonedNode.hasParent());
    }

    @Test(timeout = 4000)
    public void testCloneCDataNodeWithComment() {
        CDataNode cDataNode = new CDataNode(">eaHx`");
        Comment comment = new Comment(EMPTY_STRING);
        cDataNode.siblingIndex = -1;
        LeafNode clonedNode = cDataNode.doClone(comment);
        assertTrue(clonedNode.hasParent());
    }

    @Test(timeout = 4000)
    public void testCloneTextNode() {
        TextNode textNode = new TextNode("$j");
        LeafNode clonedNode = textNode.doClone(null);
        assertNotSame(clonedNode, textNode);
    }

    @Test(timeout = 4000)
    public void testCloneCDataNodeWithDocument() {
        CDataNode cDataNode = new CDataNode(">eaHxh");
        Document document = new Document("/4k>HzZAM{i:j+  b");
        LeafNode clonedNode = cDataNode.doClone(document);
        Node node = clonedNode.removeAttr("/4k>HzZAM{i:j+  b");
        assertTrue(node.hasParent());
    }

    @Test(timeout = 4000)
    public void testTextNodeCoreValue() {
        TextNode textNode = new TextNode("gMz?D>{rCwB%:");
        assertEquals("gMz?D>{rCwB%:", textNode.coreValue());
    }

    @Test(timeout = 4000)
    public void testXmlDeclarationCoreValue() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration(EMPTY_STRING, false);
        assertEquals(EMPTY_STRING, xmlDeclaration.coreValue());
    }

    @Test(timeout = 4000)
    public void testDataNodeChildNodeSize() {
        DataNode dataNode = new DataNode("7mqe");
        assertEquals(0, dataNode.childNodeSize());
    }

    @Test(timeout = 4000)
    public void testCDataNodeAttr() {
        CDataNode cDataNode = new CDataNode("SYSTEM");
        cDataNode.setSiblingIndex(5696);
        Node node = cDataNode.attr(EMPTY_STRING, "<![CDATA[SYSTEM]]>");
        assertEquals(0, node.childNodeSize());
    }

    @Test(timeout = 4000)
    public void testDataNodeAttrWithNullValue() {
        DataNode dataNode = new DataNode("gMz?D>{rCwB%:");
        dataNode.setSiblingIndex(-1447);
        Node node = dataNode.attr("                 ", null);
        assertEquals(0, node.childNodeSize());
    }

    @Test(timeout = 4000)
    public void testDataNodeWrapAndAttr() {
        DataNode dataNode = new DataNode("PUBLIC");
        Node wrappedNode = dataNode.wrap("org.jsoup.nodes.LeafNode");
        Node node = wrappedNode.attr("org.jsoup.nodes.LeafNode", "1jNKR5#n*");
        assertSame(node, wrappedNode);
    }

    @Test(timeout = 4000)
    public void testCDataNodeAttrValue() {
        CDataNode cDataNode = new CDataNode("roS]");
        assertEquals("roS]", cDataNode.attr(CDATA_ATTR));
    }

    @Test(timeout = 4000)
    public void testRemoveAttrWithNullKeyThrowsException() {
        CDataNode cDataNode = new CDataNode("org.jsoup.nodes.LeafNode");
        try {
            cDataNode.removeAttr(null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Object must not be null", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testRemoveAttrWithInvalidValueThrowsException() {
        CDataNode cDataNode = new CDataNode("org.jsoup.nodes.LeafNode");
        cDataNode.value = cDataNode;
        try {
            cDataNode.removeAttr("org.jsoup.nodes.LeafNode");
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            assertTrue(e.getMessage().contains("org.jsoup.nodes.CDataNode cannot be cast to java.lang.String"));
        }
    }

    @Test(timeout = 4000)
    public void testAttributesWithInvalidValueThrowsException() {
        CDataNode cDataNode = new CDataNode("-/9fsuZ");
        cDataNode.value = cDataNode;
        try {
            cDataNode.attributes();
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            assertTrue(e.getMessage().contains("org.jsoup.nodes.CDataNode cannot be cast to java.lang.String"));
        }
    }

    @Test(timeout = 4000)
    public void testAttrWithSelfParentNode() {
        CDataNode cDataNode = new CDataNode("-cOata");
        cDataNode.parentNode = cDataNode;
        cDataNode.attr("-cOata", "-cOata");
    }

    @Test(timeout = 4000)
    public void testAttrWithNullKeyThrowsException() {
        CDataNode cDataNode = new CDataNode("%:~n@");
        try {
            cDataNode.attr(null, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testAttrWithInvalidValueThrowsException() {
        CDataNode cDataNode = new CDataNode(">eaHxh");
        Object object = new Object();
        cDataNode.value = object;
        try {
            cDataNode.attr(">eaHxh", ">eaHxh");
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            assertTrue(e.getMessage().contains("java.lang.Object cannot be cast to java.lang.String"));
        }
    }

    @Test(timeout = 4000)
    public void testAttrWithNullKeyThrowsException() {
        CDataNode cDataNode = new CDataNode("Rcdata");
        Node node = cDataNode.attr("Rcdata", "Rcdata");
        try {
            node.attr(null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Object must not be null", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testAbsUrlWithEmptyKeyThrowsException() {
        CDataNode cDataNode = new CDataNode("iX.>E!");
        try {
            cDataNode.absUrl(EMPTY_STRING);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("String must not be empty", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testAttrWithCData() {
        CDataNode cDataNode = new CDataNode("6'z rm]|lT");
        Node node = cDataNode.attr(CDATA_ATTR, "Accept-Encoding");
        assertEquals(0, node.childNodeSize());
    }

    @Test(timeout = 4000)
    public void testCDataNodeAttrEmptyValue() {
        CDataNode cDataNode = new CDataNode(EMPTY_STRING);
        assertEquals(EMPTY_STRING, cDataNode.attr(CDATA_ATTR));
    }

    @Test(timeout = 4000)
    public void testTextNodeAttributesAndShallowClone() {
        TextNode textNode = new TextNode("body");
        textNode.attributes();
        Node node = textNode.shallowClone();
        assertNotSame(node, textNode);
    }

    @Test(timeout = 4000)
    public void testTextNodeBaseUri() {
        TextNode textNode = new TextNode("body");
        Document document = Parser.parseBodyFragment("body", "org.jsoup.internal.Normalizer");
        textNode.parentNode = document;
        assertEquals("org.jsoup.internal.Normalizer", textNode.baseUri());
    }

    @Test(timeout = 4000)
    public void testCDataNodeBaseUri() {
        CDataNode cDataNode = new CDataNode(SAMPLE_TEXT);
        assertEquals(EMPTY_STRING, cDataNode.baseUri());
    }

    @Test(timeout = 4000)
    public void testAttrWithCDataNode() {
        CDataNode cDataNode = new CDataNode(">eaHx`");
        cDataNode.attr(">eaHx`", ">eaHx`");
        Node node = cDataNode.attr(">eaHx`", ">eaHx`");
        assertFalse(node.hasParent());
    }

    @Test(timeout = 4000)
    public void testCDataNodeAttrEmptyString() {
        CDataNode cDataNode = new CDataNode(SAMPLE_TEXT);
        assertEquals(EMPTY_STRING, cDataNode.attr(SAMPLE_TEXT));
    }

    @Test(timeout = 4000)
    public void testCommentAbsUrlEmptyString() {
        Comment comment = new Comment("QuR~!GB`!_");
        assertEquals(EMPTY_STRING, comment.absUrl("QuR~!GB`!_"));
    }

    @Test(timeout = 4000)
    public void testOuterHtmlTail() {
        CDataNode cDataNode = new CDataNode("Must set charset arg to character set of file to parse. Set to null to attempt to detect from HTML");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        cDataNode.outerHtmlTail(null, outputSettings);
        assertEquals(0, cDataNode.siblingIndex());
    }

    @Test(timeout = 4000)
    public void testDataNodeSetWholeData() {
        DataNode dataNode = new DataNode("oML5cI2?Z,t2RXsqa");
        DataNode updatedDataNode = dataNode.setWholeData(null);
        assertNull(updatedDataNode.coreValue());
    }

    @Test(timeout = 4000)
    public void testHasAttrWithNullKeyThrowsException() {
        CDataNode cDataNode = new CDataNode("1");
        try {
            cDataNode.hasAttr(null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Object must not be null", e.getMessage());
        }
    }
}