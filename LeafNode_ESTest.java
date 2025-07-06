package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.OutputStreamWriter;
import java.util.List;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.evosuite.runtime.mock.java.io.MockPrintStream;
import org.jsoup.internal.QuietAppendable;
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

    @Test(timeout = 4000)
    public void testDataNodeCoreValueEmpty() throws Throwable {
        DataNode dataNode = new DataNode("s:.U");
        dataNode.coreValue("");
        assertEquals(0, dataNode.childNodeSize());
    }

    @Test(timeout = 4000)
    public void testCommentSiblingIndex() throws Throwable {
        Comment comment = new Comment("?X4WRmKC`*");
        comment.setSiblingIndex(150);
        Node node = comment.removeAttr("#dan");
        assertEquals(150, node.siblingIndex());
    }

    @Test(timeout = 4000)
    public void testRemoveAttrReturnsSameNode() throws Throwable {
        DataNode dataNode = new DataNode("org.jsoup.nodes.LeafNode");
        dataNode.setSiblingIndex(-462);
        Node node = dataNode.removeAttr("org.jsoup.helper.HttpConnection$KeyVal");
        assertSame(dataNode, node);
    }

    @Test(timeout = 4000)
    public void testDocumentTypeWrapAndRemoveAttr() throws Throwable {
        DocumentType documentType = new DocumentType("", "xf", "#cdata");
        documentType.wrap("5Hs(;P}i}q3q");
        Node node = documentType.removeAttr("]dO2ohy.");
        assertSame(documentType, node);
    }

    @Test(timeout = 4000)
    public void testCommentHasNoAttributes() throws Throwable {
        Comment comment = new Comment("#cdata");
        boolean hasAttributes = comment.hasAttributes();
        assertFalse(hasAttributes);
    }

    @Test(timeout = 4000)
    public void testDataNodeHasAttr() throws Throwable {
        DataNode dataNode = new DataNode("L");
        dataNode.attr("L", "r23cLuT{[9UM)e");
        boolean hasAttr = dataNode.hasAttr("L");
        assertTrue(hasAttr);
    }

    @Test(timeout = 4000)
    public void testCDataNodeEmpty() throws Throwable {
        CDataNode cDataNode = new CDataNode("8]BXhO<");
        cDataNode.setSiblingIndex(3372);
        Node node = cDataNode.empty();
        assertSame(cDataNode, node);
    }

    @Test(timeout = 4000)
    public void testTextNodeEmpty() throws Throwable {
        TextNode textNode = TextNode.createFromEncoded("");
        textNode.setSiblingIndex(-1882);
        Node node = textNode.empty();
        assertSame(textNode, node);
    }

    @Test(timeout = 4000)
    public void testCDataNodeEmptyChildNodeSize() throws Throwable {
        CDataNode cDataNode = new CDataNode("8]BXhO<");
        cDataNode.setParentNode(cDataNode);
        Node node = cDataNode.empty();
        assertEquals(0, node.childNodeSize());
    }

    @Test(timeout = 4000)
    public void testDocumentTypeCloneHasParent() throws Throwable {
        DataNode dataNode = new DataNode("h4");
        DocumentType documentType = new DocumentType("jY\"]/f;V!h|,v%A", "", "");
        documentType.siblingIndex = 1;
        LeafNode leafNode = documentType.doClone(dataNode);
        assertTrue(leafNode.hasParent());
    }

    @Test(timeout = 4000)
    public void testDataNodeCloneHasParent() throws Throwable {
        DataNode dataNode = new DataNode("#danU");
        dataNode.setSiblingIndex(-1995955295);
        LeafNode leafNode = dataNode.doClone(dataNode);
        assertTrue(leafNode.hasParent());
    }

    @Test(timeout = 4000)
    public void testDocumentTypeCloneNotSame() throws Throwable {
        DocumentType documentType = new DocumentType("", "", "org.jsoup.nodes.LeafNode");
        LeafNode leafNode = documentType.doClone(null);
        assertNotSame(leafNode, documentType);
    }

    @Test(timeout = 4000)
    public void testDataNodeSetWholeData() throws Throwable {
        DataNode dataNode = new DataNode("org.jsoup.nodes.LeafNode");
        DataNode dataNode1 = dataNode.setWholeData("");
        String coreValue = dataNode1.coreValue();
        assertEquals("", coreValue);
    }

    @Test(timeout = 4000)
    public void testDataNodeChildNodeSize() throws Throwable {
        DataNode dataNode = new DataNode("org.jsoup.nodes.LeafNode");
        int childNodeSize = dataNode.childNodeSize();
        assertEquals(0, childNodeSize);
    }

    @Test(timeout = 4000)
    public void testDataNodeBaseUri() throws Throwable {
        DataNode dataNode = new DataNode("g{NE1)F.(MU,Z%?E@bL");
        Document document = Parser.parse("org.jsoup.nodes.LeafNode", "2:.BR9s");
        dataNode.setParentNode(document);
        String baseUri = dataNode.baseUri();
        assertEquals("2:.BR9s", baseUri);
    }

    @Test(timeout = 4000)
    public void testCommentAttributes() throws Throwable {
        Comment comment = new Comment("#cdata");
        comment.attributes();
        boolean hasAttributes = comment.hasAttributes();
        assertTrue(hasAttributes);
    }

    @Test(timeout = 4000)
    public void testDataNodeAttrSiblingIndex() throws Throwable {
        DataNode dataNode = new DataNode("G?'A>*{dY\"SZ~");
        dataNode.siblingIndex = 1;
        Node node = dataNode.attr("", "");
        assertEquals(1, node.siblingIndex());
    }

    @Test(timeout = 4000)
    public void testTextNodeAttrNodeName() throws Throwable {
        TextNode textNode = new TextNode("");
        textNode.siblingIndex = -2269;
        Node node = textNode.attr("", "G*RUC4oF90:a>8E)");
        assertEquals("#text", node.nodeName());
    }

    @Test(timeout = 4000)
    public void testDataNodeWrapAndAttr() throws Throwable {
        DataNode dataNode = new DataNode("21sO[s,HTO4|W");
        dataNode.wrap("21sO[s,HTO4|W");
        Node node = dataNode.attr("2$lX__Nhg/ 55:", "2$lX__Nhg/ 55:");
        assertEquals(0, node.childNodeSize());
    }

    @Test(timeout = 4000)
    public void testCDataNodeAbsUrl() throws Throwable {
        CDataNode cDataNode = new CDataNode("_.|M*I]Z,CiM/");
        String absUrl = cDataNode.absUrl("_.|M*I]Z,CiM/");
        assertEquals("", absUrl);
    }

    @Test(timeout = 4000)
    public void testCDataNodeRemoveAttrNull() throws Throwable {
        CDataNode cDataNode = new CDataNode("C");
        try {
            cDataNode.removeAttr(null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testDataNodeHasAttrNull() throws Throwable {
        DataNode dataNode = new DataNode("");
        try {
            dataNode.hasAttr(null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testDocumentTypeCoreValueClassCastException() throws Throwable {
        DocumentType documentType = new DocumentType("jY\"]/f;V!h|,v%A", "", "");
        Object object = new Object();
        documentType.value = object;
        try {
            documentType.coreValue();
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            verifyException("org.jsoup.nodes.LeafNode", e);
        }
    }

    @Test(timeout = 4000)
    public void testCommentAttributesClassCastException() throws Throwable {
        Comment comment = new Comment("N}d87KvAu}u%=Xor:");
        Comment comment1 = comment.setData("ZBO");
        MockFile mockFile = new MockFile("ZBO", "");
        MockPrintStream mockPrintStream = new MockPrintStream(mockFile);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(mockPrintStream);
        comment1.value = outputStreamWriter;
        try {
            comment1.attributes();
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            verifyException("org.jsoup.nodes.LeafNode", e);
        }
    }

    @Test(timeout = 4000)
    public void testDataNodeAttrWithParentNode() throws Throwable {
        DataNode dataNode = new DataNode("#danU");
        dataNode.parentNode = dataNode;
        dataNode.attr("#danU", "#danU");
    }

    @Test(timeout = 4000)
    public void testDataNodeAttrNullPointerException() throws Throwable {
        DataNode dataNode = new DataNode("#danG");
        try {
            dataNode.attr(null, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
        }
    }

    @Test(timeout = 4000)
    public void testDataNodeAttrIllegalArgumentException() throws Throwable {
        DataNode dataNode = new DataNode("#danU");
        dataNode.attr("#danU", "#danU");
        try {
            dataNode.attr(null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testCDataNodeAttr() throws Throwable {
        CDataNode cDataNode = new CDataNode("#dajG");
        Node node = cDataNode.attr("#cdata", "ascii");
        assertSame(cDataNode, node);
    }

    @Test(timeout = 4000)
    public void testCommentAttr() throws Throwable {
        Comment comment = new Comment("#text");
        String attrValue = comment.attr("#comment");
        assertEquals("#text", attrValue);
    }

    @Test(timeout = 4000)
    public void testDataNodeWrapBaseUri() throws Throwable {
        DataNode dataNode = new DataNode("L");
        dataNode.wrap("L");
        String baseUri = dataNode.baseUri();
        assertEquals("", baseUri);
    }

    @Test(timeout = 4000)
    public void testDataNodeAttrChildNodeSize() throws Throwable {
        DataNode dataNode = new DataNode("#danU");
        dataNode.attr("#danU", "org.jsoup.parser.CharacterReader");
        Node node = dataNode.attr("VM~yOM{=Xo3Hy", "tsp=");
        assertEquals(0, node.childNodeSize());
    }

    @Test(timeout = 4000)
    public void testDataNodeAttrNull() throws Throwable {
        DataNode dataNode = new DataNode("novalidate");
        String attrValue = dataNode.attr(null);
        assertEquals("", attrValue);
    }

    @Test(timeout = 4000)
    public void testCDataNodeAbsUrlIllegalArgumentException() throws Throwable {
        CDataNode cDataNode = new CDataNode("");
        try {
            cDataNode.absUrl("");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testCDataNodeOuterHtmlTail() throws Throwable {
        CDataNode cDataNode = new CDataNode("#dajG");
        StringBuilder stringBuilder = new StringBuilder();
        QuietAppendable quietAppendable = QuietAppendable.wrap(stringBuilder);
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        cDataNode.outerHtmlTail(quietAppendable, outputSettings);
        assertEquals(0, cDataNode.siblingIndex());
    }

    @Test(timeout = 4000)
    public void testDataNodeHasAttrFalse() throws Throwable {
        DataNode dataNode = new DataNode("#danG");
        boolean hasAttr = dataNode.hasAttr("#danG");
        assertFalse(hasAttr);
    }

    @Test(timeout = 4000)
    public void testDataNodeCoreValue() throws Throwable {
        DataNode dataNode = new DataNode("novalidate");
        String coreValue = dataNode.coreValue();
        assertEquals("novalidate", coreValue);
    }

    @Test(timeout = 4000)
    public void testDataNodeCloneClassCastException() throws Throwable {
        DataNode dataNode = new DataNode("#danU");
        DataNode dataNodeClone = dataNode.clone();
        Object object = new Object();
        dataNodeClone.value = object;
        try {
            dataNodeClone.attr("#danU", "dd");
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            verifyException("org.jsoup.nodes.LeafNode", e);
        }
    }

    @Test(timeout = 4000)
    public void testDataNodeDoSetBaseUri() throws Throwable {
        DataNode dataNode = new DataNode("]-(qwv");
        dataNode.doSetBaseUri("");
        assertFalse(dataNode.hasParent());
    }

    @Test(timeout = 4000)
    public void testDocumentTypeEnsureChildNodes() throws Throwable {
        DocumentType documentType = new DocumentType("", "", "");
        List<Node> childNodes = documentType.ensureChildNodes();
        assertTrue(childNodes.isEmpty());
    }

    @Test(timeout = 4000)
    public void testDataNodeSetWholeDataNull() throws Throwable {
        DataNode dataNode = new DataNode("novalidate");
        dataNode.setWholeData(null);
        String coreValue = dataNode.coreValue();
        assertNull(coreValue);
    }
}