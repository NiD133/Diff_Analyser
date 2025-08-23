package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeFilter;
import org.jsoup.select.NodeVisitor;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class Elements_ESTest extends Elements_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testGetElementByIndex() throws Throwable {
        Document document = Document.createShell("<m-2,eXTA:N5y7");
        Elements elements = document.getAllElements();
        Elements result = elements.eq(4);
        assertFalse(result.equals((Object) elements));
    }

    @Test(timeout = 4000)
    public void testExpectFirstThrowsException() throws Throwable {
        Document document = Document.createShell("<m-2,eXTA:N5y7");
        Elements elements = document.getAllElements();
        try {
            elements.expectFirst("BogusComment");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testWrapEmptyElements() throws Throwable {
        Elements elements = new Elements();
        Elements wrappedElements = elements.wrap("91S%gN|]>`");
        assertEquals(0, wrappedElements.size());
    }

    @Test(timeout = 4000)
    public void testValEmptyElements() throws Throwable {
        Elements elements = new Elements();
        Elements valuedElements = elements.val("}X2q[rR~");
        assertEquals(0, valuedElements.size());
    }

    @Test(timeout = 4000)
    public void testUnwrapElements() throws Throwable {
        Document document = new Document("]RCd 5P[V");
        Element element = document.body();
        Elements elements = element.getAllElements();
        Elements unwrappedElements = elements.unwrap();
        assertFalse(unwrappedElements.isEmpty());
    }

    @Test(timeout = 4000)
    public void testTraverseEmptyElements() throws Throwable {
        Elements elements = new Elements();
        NodeVisitor nodeVisitor = mock(NodeVisitor.class, new ViolatedAssumptionAnswer());
        Elements traversedElements = elements.traverse(nodeVisitor);
        assertTrue(traversedElements.isEmpty());
    }

    @Test(timeout = 4000)
    public void testTraverseNonEmptyElements() throws Throwable {
        Document document = Document.createShell("<m-2,eXTA:N5y7");
        Elements elements = document.getAllElements();
        NodeVisitor nodeVisitor = mock(NodeVisitor.class, new ViolatedAssumptionAnswer());
        Elements traversedElements = elements.traverse(nodeVisitor);
        assertFalse(traversedElements.isEmpty());
    }

    @Test(timeout = 4000)
    public void testToggleClassEmptyElements() throws Throwable {
        Elements elements = new Elements();
        Elements toggledElements = elements.toggleClass("");
        assertTrue(toggledElements.isEmpty());
    }

    @Test(timeout = 4000)
    public void testToStringEmptyElements() throws Throwable {
        Elements elements = new Elements();
        String result = elements.toString();
        assertEquals("", result);
    }

    @Test(timeout = 4000)
    public void testTextNodesEmptyElements() throws Throwable {
        Elements elements = new Elements();
        List<TextNode> textNodes = elements.textNodes();
        assertTrue(textNodes.isEmpty());
    }

    @Test(timeout = 4000)
    public void testTextEmptyElements() throws Throwable {
        Elements elements = new Elements();
        String text = elements.text();
        assertEquals("", text);
    }

    @Test(timeout = 4000)
    public void testTagNameSameElements() throws Throwable {
        Document document = Document.createShell("<m-2,eXTA:N5y7");
        Elements elements = document.getAllElements();
        Elements taggedElements = elements.tagName("<m-2,eXTA:N5y7");
        assertSame(elements, taggedElements);
    }

    @Test(timeout = 4000)
    public void testSetElement() throws Throwable {
        Document document = Parser.parseBodyFragment("", "");
        Elements elements = document.getAllElements();
        Element element = elements.set(1, (Element) document);
        assertEquals("html", element.normalName());
    }

    @Test(timeout = 4000)
    public void testSelectFirstReturnsNull() throws Throwable {
        Document document = new Document("b*LY=0yr*g]q30");
        Elements elements = document.getAllElements();
        Element element = elements.selectFirst("Bb,76");
        assertNull(element);
    }

    @Test(timeout = 4000)
    public void testSelectReturnsEmpty() throws Throwable {
        Document document = new Document("]RCd 5P[V");
        Elements elements = document.getAllElements();
        Elements selectedElements = elements.select(" H-zp5");
        assertEquals(0, selectedElements.size());
    }

    @Test(timeout = 4000)
    public void testRemoveClassEmptyElements() throws Throwable {
        Elements elements = new Elements();
        Elements result = elements.removeClass("");
        assertEquals(0, result.size());
    }

    @Test(timeout = 4000)
    public void testRemoveAttrEmptyElements() throws Throwable {
        Elements elements = new Elements();
        Elements result = elements.removeAttr("No elements matched the query '%s' in the elements.");
        assertEquals(0, result.size());
    }

    @Test(timeout = 4000)
    public void testRemoveAllElements() throws Throwable {
        Document document = Document.createShell("<m-2,eXTA:N5y7");
        Elements elements = document.getAllElements();
        Elements elementsToRemove = new Elements((List<Element>) elements);
        boolean removed = elements.removeAll(elementsToRemove);
        assertEquals(0, elements.size());
        assertTrue(removed);
    }

    @Test(timeout = 4000)
    public void testRemoveElement() throws Throwable {
        Document document = new Document("b*RY=0r*g]q0");
        Elements elements = document.getAllElements();
        Element element = elements.remove(0);
        assertFalse(element.isBlock());
    }

    @Test(timeout = 4000)
    public void testRemoveElementWithChildren() throws Throwable {
        Document document = Parser.parse("", "");
        Elements elements = document.children();
        Element element = elements.remove(0);
        assertEquals(0, element.siblingIndex());
    }

    @Test(timeout = 4000)
    public void testRemoveElements() throws Throwable {
        Elements elements = new Elements();
        Elements result = elements.remove();
        assertSame(result, elements);
    }

    @Test(timeout = 4000)
    public void testPrevAllElements() throws Throwable {
        Document document = Parser.parse("<m-2,eSTL:N5y7", "<m-2,eSTL:N5y7");
        Elements elements = document.getAllElements();
        Elements prevAllElements = elements.prevAll();
        assertEquals(1, prevAllElements.size());
    }

    @Test(timeout = 4000)
    public void testPrevElements() throws Throwable {
        Document document = new Document("b*LY=0yr*g]q30");
        Elements elements = document.getAllElements();
        Elements prevElements = elements.prev("Bb,76");
        assertEquals(0, prevElements.size());
    }

    @Test(timeout = 4000)
    public void testPrependEmptyElements() throws Throwable {
        Elements elements = new Elements();
        Elements result = elements.prepend("");
        assertSame(elements, result);
    }

    @Test(timeout = 4000)
    public void testParentsNotEmpty() throws Throwable {
        Document document = Document.createShell("");
        Elements elements = document.getAllElements();
        Elements parentElements = elements.parents();
        assertFalse(parentElements.isEmpty());
    }

    @Test(timeout = 4000)
    public void testOuterHtml() throws Throwable {
        Document document = Document.createShell("<m-2,eXTA:N5y7");
        Elements elements = document.getAllElements();
        String outerHtml = elements.outerHtml();
        assertEquals("<html>\n <head></head>\n <body></body>\n</html>\n<html>\n <head></head>\n <body></body>\n</html>\n<head></head>\n<body></body>", outerHtml);
    }

    @Test(timeout = 4000)
    public void testNotEmptyElements() throws Throwable {
        Elements elements = new Elements();
        Elements result = elements.not("DQw|");
        assertEquals(0, result.size());
    }

    @Test(timeout = 4000)
    public void testNotElements() throws Throwable {
        Document document = Document.createShell("");
        Elements elements = document.getAllElements();
        Elements result = elements.not(":containsWholeOwnText(%s)");
        assertEquals(4, result.size());
    }

    @Test(timeout = 4000)
    public void testNextAllElements() throws Throwable {
        Elements elements = new Elements();
        Elements result = elements.nextAll((String) null);
        assertNotSame(result, elements);
    }

    @Test(timeout = 4000)
    public void testNextAllEmptyElements() throws Throwable {
        Elements elements = new Elements();
        Elements result = elements.nextAll();
        assertNotSame(result, elements);
    }

    @Test(timeout = 4000)
    public void testNextAllNonEmptyElements() throws Throwable {
        Document document = Document.createShell("<m-2,eXTA:N5y7");
        Elements elements = document.getAllElements();
        Elements result = elements.nextAll();
        assertEquals(1, result.size());
    }

    @Test(timeout = 4000)
    public void testNextElements() throws Throwable {
        Document document = Parser.parseBodyFragment("Only http & https protocols supported", "Only http & https protocols supported");
        Elements elements = document.getAllElements();
        Elements result = elements.next((String) null);
        assertEquals(1, result.size());
    }

    @Test(timeout = 4000)
    public void testNextElementsByAttributeValueNot() throws Throwable {
        Document document = Parser.parse("*Vq9|=C*", "ch}9X");
        Elements elements = document.getElementsByAttributeValueNot("*Vq9|=C*", "*Vq9|=C*");
        Elements result = elements.next();
        assertEquals(1, result.size());
    }

    @Test(timeout = 4000)
    public void testLastElementIsDocument() throws Throwable {
        Document document = new Document("b*LY=0yr*g]q30");
        Elements elements = document.getAllElements();
        Document lastElement = (Document) elements.last();
        assertEquals(Document.QuirksMode.noQuirks, lastElement.quirksMode());
    }

    @Test(timeout = 4000)
    public void testHtmlEmptyElements() throws Throwable {
        Elements elements = new Elements();
        Elements result = elements.html("");
        assertEquals(0, result.size());
    }

    @Test(timeout = 4000)
    public void testHtmlNonEmptyElements() throws Throwable {
        Document document = Document.createShell("<m-2,eXTAN5y");
        Elements elements = document.getAllElements();
        String html = elements.html();
        assertEquals("<html>\n <head></head>\n <body></body>\n</html>\n<head></head>\n<body></body>\n\n", html);
    }

    @Test(timeout = 4000)
    public void testHtmlEmptyString() throws Throwable {
        Elements elements = new Elements();
        String html = elements.html();
        assertEquals("", html);
    }

    @Test(timeout = 4000)
    public void testPrependElement() throws Throwable {
        Document document = Document.createShell("<m-2,eXTA:N5y7");
        Elements elements = document.getAllElements();
        Elements prevElements = elements.prev();
        elements.prepend("<m-2,eXTA:N5y7");
        Element firstElement = prevElements.first();
        assertEquals(1, prevElements.size());
        assertEquals(2, firstElement.siblingIndex());
    }

    @Test(timeout = 4000)
    public void testFirstElementIsHtml() throws Throwable {
        Document document = Parser.parseBodyFragment("Invalid xmlns attribute [%s] on tag [%s]", "\"D%\fn");
        Elements elements = document.children();
        Element firstElement = elements.first();
        assertEquals("html", firstElement.normalName());
    }

    @Test(timeout = 4000)
    public void testFilterEmptyElements() throws Throwable {
        Elements elements = new Elements();
        NodeFilter nodeFilter = mock(NodeFilter.class, new ViolatedAssumptionAnswer());
        Elements result = elements.filter(nodeFilter);
        assertTrue(result.isEmpty());
    }

    @Test(timeout = 4000)
    public void testFilterNonEmptyElements() throws Throwable {
        Document document = new Document("b*LY=0yr*g]q30");
        Elements elements = document.getAllElements();
        NodeFilter nodeFilter = mock(NodeFilter.class, new ViolatedAssumptionAnswer());
        doReturn((NodeFilter.FilterResult) null).when(nodeFilter).head(any(org.jsoup.nodes.Node.class), anyInt());
        Elements result = elements.filter(nodeFilter);
        assertSame(elements, result);
    }

    @Test(timeout = 4000)
    public void testEqFirstElement() throws Throwable {
        Document document = new Document("b*RY=0r*g]q0");
        Elements elements = document.getAllElements();
        Elements result = elements.eq(0);
        assertEquals(1, result.size());
    }

    @Test(timeout = 4000)
    public void testEmptyElements() throws Throwable {
        Elements elements = new Elements();
        Elements result = elements.empty();
        assertTrue(result.isEmpty());
    }

    @Test(timeout = 4000)
    public void testDeselectElement() throws Throwable {
        Document document = new Document("b*LY=0yr*g]q30");
        Elements elements = document.getAllElements();
        boolean deselected = elements.deselect((Object) document);
        assertTrue(deselected);
    }

    @Test(timeout = 4000)
    public void testDeselectElementByIndex() throws Throwable {
        Document document = Document.createShell("Only http & https protocols supported");
        Elements elements = document.getAllElements();
        Element element = elements.deselect(3);
        assertEquals("body", element.nodeName());
    }

    @Test(timeout = 4000)
    public void testDeselectFirstElement() throws Throwable {
        Document document = new Document("Bb,Y6");
        Elements elements = document.getAllElements();
        Document deselectedElement = (Document) elements.deselect(0);
        assertEquals("Bb,Y6", deselectedElement.location());
    }

    @Test(timeout = 4000)
    public void testCloneElements() throws Throwable {
        Elements elements = new Elements();
        Elements clonedElements = elements.clone();
        assertNotSame(clonedElements, elements);
    }

    @Test(timeout = 4000)
    public void testBeforeElement() throws Throwable {
        Document document = Parser.parse("<m-2,eXTA:N5y7", "<m-2,eXTA:N5y7");
        Elements elements = document.children();
        Elements result = elements.before("<m-2,eXTA:N5y7");
        assertEquals(1, result.size());
    }

    @Test(timeout = 4000)
    public void testAttrEmptyElements() throws Throwable {
        Elements elements = new Elements();
        Elements result = elements.attr("INXd(U<", "INXd(U<");
        assertTrue(result.isEmpty());
    }

    @Test(timeout = 4000)
    public void testAsListElements() throws Throwable {
        Document document = Document.createShell("<m-2,eXTA:N5y7");
        Elements elements = document.getAllElements();
        ArrayList<Element> elementList = elements.asList();
        assertEquals(4, elementList.size());
    }

    @Test(timeout = 4000)
    public void testAppendEmptyElements() throws Throwable {
        Elements elements = new Elements();
        Elements result = elements.append("");
        assertEquals(0, result.size());
    }

    @Test(timeout = 4000)
    public void testAfterElement() throws Throwable {
        Document document = new Document("]RCd 5P[V");
        Element element = document.body();
        Elements elements = element.getAllElements();
        Elements result = elements.after("]RCd 5P[V");
        assertSame(result, elements);
    }

    @Test(timeout = 4000)
    public void testAddClassEmptyElements() throws Throwable {
        Document document = new Document("", "");
        Elements elements = document.getAllElements();
        Elements result = elements.addClass("");
        assertSame(elements, result);
    }

    @Test(timeout = 4000)
    public void testWrapNullThrowsException() throws Throwable {
        Elements elements = new Elements();
        try {
            elements.wrap((String) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testWrapNullElementThrowsException() throws Throwable {
        Element[] elementArray = new Element[3];
        Elements elements = new Elements(elementArray);
        try {
            elements.wrap("org.jsoup.parser.Token$1");
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.select.Elements", e);
        }
    }

    @Test(timeout = 4000)
    public void testWrapEmptyListThrowsException() throws Throwable {
        Document document = Parser.parse("<m-2,eXTA:N5y7", "<m-2,eXTA:N5y7");
        Elements elements = document.getAllElements();
        try {
            elements.wrap("<m-2,eXTA:N5y7");
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verifyException("java.util.Collections$EmptyList", e);
        }
    }

    @Test(timeout = 4000)
    public void testValNullElementThrowsException() throws Throwable {
        Elements elements = new Elements();
        elements.add((Element) null);
        try {
            elements.val("");
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.select.Elements", e);
        }
    }

    @Test(timeout = 4000)
    public void testValNullArrayThrowsException() throws Throwable {
        Element[] elementArray = new Element[6];
        Elements elements = new Elements(elementArray);
        try {
            elements.val();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.select.Elements", e);
        }
    }

    @Test(timeout = 4000)
    public void testToggleClassNullThrowsException() throws Throwable {
        Document document = Parser.parseBodyFragment("Bb,Y6", "Bb,Y6");
        Elements elements = document.getAllElements();
        try {
            elements.toggleClass((String) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testTagNameEmptyThrowsException() throws Throwable {
        Document document = Parser.parse("<m-2,eXTA:N5y7", "<m-2,eXTA:N5y7");
        Elements elements = document.children();
        try {
            elements.tagName("");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testSetNullElementThrowsException() throws Throwable {
        Elements elements = new Elements();
        try {
            elements.set((-1), (Element) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testSetOutOfBoundsThrowsException() throws Throwable {
        Document document = Document.createShell("ol");
        Elements elements = document.getAllElements();
        try {
            elements.set(126, (Element) document);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verifyException("java.util.ArrayList", e);
        }
    }

    @Test(timeout = 4000)
    public void testSetNegativeIndexThrowsException() throws Throwable {
        Elements elements = new Elements();
        Element element = new Element("4[]rS^,A(7");
        try {
            elements.set((-1588), element);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }

    @Test(timeout = 4000)
    public void testSelectFirstInvalidQueryThrowsException() throws Throwable {
        Elements elements = new Elements();
        try {
            elements.selectFirst(",P<");
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("org.jsoup.select.QueryParser", e);
        }
    }

    @Test(timeout = 4000)
    public void testSelectEmptyQueryThrowsException() throws Throwable {
        Elements elements = new Elements();
        try {
            elements.select("");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testRetainAllNullThrowsException() throws Throwable {
        Document document = Document.createShell("<m-2,eXTA:N5y7");
        Elements elements = document.getAllElements();
        try {
            elements.retainAll((Collection<?>) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
        }
    }

    @Test(timeout = 4000)
    public void testReplaceAllNullElementThrowsException() throws Throwable {
        Document document = Document.createShell("<m-2,eXTA:N5y7");
        Elements elements = document.getAllElements();
        UnaryOperator<Element> unaryOperator = UnaryOperator.identity();
        try {
            elements.replaceAll(unaryOperator);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testReplaceAllNullThrowsException() throws Throwable {
        Document document = Parser.parse("", "");
        Elements elements = document.getAllElements();
        try {
            elements.replaceAll((UnaryOperator<Element>) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.select.Elements", e);
        }
    }

    @Test(timeout = 4000)
    public void testRemoveIfNullThrowsException() throws Throwable {
        Document document = new Document("b*LY=0yr*g]q30");
        Elements elements = document.getAllElements();
        try {
            elements.removeIf((Predicate<? super Element>) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.select.Elements", e);
        }
    }

    @Test(timeout = 4000)
    public void testRemoveClassNullThrowsException() throws Throwable {
        Document document = Parser.parseBodyFragment("Only http & https protocols supported", "Only http & https protocols supported");
        Elements elements = document.getAllElements();
        try {
            elements.removeClass((String) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testRemoveAttrNullThrowsException() throws Throwable {
        Document document = new Document("X");
        Elements elements = document.getAllElements();
        try {
            elements.removeAttr((String) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testRemoveAllNullThrowsException() throws Throwable {
        Elements elements = new Elements();
        try {
            elements.removeAll((Collection<?>) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.select.Elements", e);
        }
    }

    @Test(timeout = 4000)
    public void testRemoveOutOfBoundsThrowsException() throws Throwable {
        Elements elements = new Elements();
        try {
            elements.remove(401);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verifyException("java.util.ArrayList", e);
        }
    }

    @Test(timeout = 4000)
    public void testPrevAllEmptyQueryThrowsException() throws Throwable {
        Elements elements = new Elements();
        try {
            elements.prevAll("");
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("org.jsoup.select.QueryParser", e);
        }
    }

    @Test(timeout = 4000)
    public void testPrevEmptyQueryThrowsException() throws Throwable {
        Elements elements = new Elements();
        try {
            elements.prev("");
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("org.jsoup.select.QueryParser", e);
        }
    }

    @Test(timeout = 4000)
    public void testPrependNullThrowsException() throws Throwable {
        Document document = Document.createShell("fC4Ep$");
        Elements elements = document.getAllElements();
        try {
            elements.prepend((String) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testNotNullThrowsException() throws Throwable {
        Elements elements = new Elements();
        try {
            elements.not((String) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testNextInvalidQueryThrowsException() throws Throwable {
        Elements elements = new Elements();
        try {
            elements.next("AQkzr.");
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("org.jsoup.select.QueryParser", e);
        }
    }

    @Test(timeout = 4000)
    public void testNextNullElementThrowsException() throws Throwable {
        Element[] elementArray = new Element[20];
        Elements elements = new Elements(elementArray);
        try {
            elements.next("Bb,76");
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.select.Elements", e);
        }
    }

    @Test(timeout = 4000)
    public void testIsInvalidQueryThrowsException() throws Throwable {
        Elements elements = new Elements();
        try {
            elements.is("{xrk0 ");
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("org.jsoup.select.QueryParser", e);
        }
    }

    @Test(timeout = 4000)
    public void testHtmlNullThrowsException() throws Throwable {
        Document document = Parser.parse("<m-2,eXTA)>:N5y7", "<m-2,eXTA)>:N5y7");
        Elements elements = document.getAllElements();
        try {
            elements.html((String) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testHasClassNullThrowsException() throws Throwable {
        Document document = Document.createShell("Only http & https protocols supported");
        Elements elements = document.getAllElements();
        try {
            elements.hasClass((String) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.nodes.Element", e);
        }
    }

    @Test(timeout = 4000)
    public void testHasAttrNullThrowsException() throws Throwable {
        Document document = Document.createShell("<m-2,eXTA:N5y7");
        Elements elements = document.getAllElements();
        try {
            elements.hasAttr((String) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testEachAttrNullThrowsException() throws Throwable {
        Document document = Document.createShell("<m-2,eXTA:N5y7");
        Elements elements = document.children();
        try {
            elements.eachAttr((String) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testDeselectOutOfBoundsThrowsException() throws Throwable {
        Elements elements = new Elements();
        try {
            elements.deselect((-2136991809));
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }

    @Test(timeout = 4000)
    public void testBeforeThrowsException() throws Throwable {
        Document document = new Document("No elements matched the query '%s' in the elements.");
        Elements elements = document.getAllElements();
        document.appendChildren(elements);
        try {
            elements.before("No elements matched the query '%s' in the elements.");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testAttrNullThrowsException() throws Throwable {
        Document document = Parser.parse("<m-2,eXTA)>:N5y7", "<m-2,eXTA)>:N5y7");
        Elements elements = document.getAllElements();
        try {
            elements.attr((String) null, "<m-2,eXTA)>:N5y7");
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.parser.ParseSettings", e);
        }
    }

    @Test(timeout = 4000)
    public void testAttrNullKeyThrowsException() throws Throwable {
        Document document = Parser.parseBodyFragment("", "");
        Elements elements = document.getAllElements();
        try {
            elements.attr((String) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testAppendNullThrowsException() throws Throwable {
        Document document = Document.createShell("Bb,76");
        Elements elements = document.getAllElements();
        try {
            elements.append((String) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testAppendNullElementThrowsException() throws Throwable {
        Element[] elementArray = new Element[8];
        Elements elements = new Elements(elementArray);
        try {
            elements.append("PkaW");
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.select.Elements", e);
        }
    }

    @Test(timeout = 4000)
    public void testElementsNullArrayThrowsException() throws Throwable {
        Elements elements = null;
        try {
            elements = new Elements((Element[]) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testElementsNullCollectionThrowsException() throws Throwable {
        Elements elements = null;
        try {
            elements = new Elements((Collection<Element>) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.ArrayList", e);
        }
    }

    @Test(timeout = 4000)
    public void testElementsNegativeCapacityThrowsException() throws Throwable {
        Elements elements = null;
        try {
            elements = new Elements((-1));
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("java.util.ArrayList", e);
        }
    }

    @Test(timeout = 4000)
    public void testRemoveElementFromDocument() throws Throwable {
        Document document = new Document("Bb,76");
        Elements elements = document.getAllElements();
        boolean removed = elements.remove((Object) document);
        assertTrue(elements.isEmpty());
        assertTrue(removed);
    }

    @Test(timeout = 4000)
    public void testRemoveElementNotInCollection() throws Throwable {
        Elements elements = new Elements();
        boolean removed = elements.remove((Object) elements);
        assertFalse(removed);
    }

    @Test(timeout = 4000)
    public void testGetElementsContainingText() throws Throwable {
        Document document = Parser.parseBodyFragment("", "");
        Elements elements = document.getElementsContainingText("");
        Element firstElement = elements.first();
        assertSame(firstElement, document);
    }

    @Test(timeout = 4000)
    public void testElementsFromCollection() throws Throwable {
        Elements elements = new Elements(115);
        Elements newElements = new Elements((Collection<Element>) elements);
        assertEquals(0, newElements.size());
    }

    @Test(timeout = 4000)
    public void testOuterHtmlEmpty() throws Throwable {
        Document document = new Document("b*LY=0yr*g]q30");
        Elements elements = document.getAllElements();
        String outerHtml = elements.outerHtml();
        assertEquals("", outerHtml);
    }

    @Test(timeout = 4000)
    public void testRemoveNegativeIndexThrowsException() throws Throwable {
        Elements elements = new Elements();
        try {
            elements.remove((-12));
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }

    @Test(timeout = 4000)
    public void testRemoveIfPredicate() throws Throwable {
        Document document = Document.createShell("\n");
        Elements elements = document.getAllElements();
        Document.OutputSettings.Syntax syntax = Document.OutputSettings.Syntax.xml;
        Predicate<Object> predicate = Predicate.isEqual((Object) syntax);
        boolean removed = elements.removeIf(predicate);
        assertFalse(removed);
    }

    @Test(timeout = 4000)
    public void testRemoveIfPredicateMatches() throws Throwable {
        Document document = new Document("Bb,Y6");
        Elements elements = document.getAllElements();
        Predicate<Object> predicate = Predicate.isEqual((Object) document);
        boolean removed = elements.removeIf(predicate);
        assertTrue(elements.isEmpty());
        assertTrue(removed);
    }

    @Test(timeout = 4000)
    public void testRetainAllEmptyCollection() throws Throwable {
        Document document = Parser.parseBodyFragment("<m-2,eXTA:N5y7", "<m-2,eXTA:N5y7");
        Elements elements = document.getAllElements();
        LinkedList<Object> emptyList = new LinkedList<Object>();
        boolean retained = elements.retainAll(emptyList);
        assertEquals(0, elements.size());
        assertTrue(retained);
    }

    @Test(timeout = 4000)
    public void testRetainAllSameCollection() throws Throwable {
        Document document = new Document("I");
        Elements elements = document.getAllElements();
        boolean retained = elements.retainAll(elements);
        assertFalse(retained);
    }

    @Test(timeout = 4000)
    public void testRemoveAllNonMatchingCollection() throws Throwable {
        Elements elements = new Elements();
        LinkedHashSet<Object> linkedHashSet = new LinkedHashSet<Object>();
        linkedHashSet.add(elements);
        boolean removed = elements.removeAll(linkedHashSet);
        assertFalse(removed);
    }

    @Test(timeout = 4000)
    public void testGetFormsFromElements() throws Throwable {
        Document document = Parser.parseBodyFragment("", "");
        Elements elements = document.getElementsContainingText("");
        List<FormElement> forms = elements.forms();
        assertTrue(forms.isEmpty());
    }

    @Test(timeout = 4000)
    public void testLastElementIsNull() throws Throwable {
        Elements elements = new Elements();
        Element lastElement = elements.last();
        assertNull(lastElement);
    }

    @Test(timeout = 4000)
    public void testLastElementIsBlock() throws Throwable {
        Document document = Document.createShell("Only http & https protocols supported");
        Elements elements = document.getAllElements();
        Element lastElement = elements.last();
        assertTrue(lastElement.isBlock());
    }

    @Test(timeout = 4000)
    public void testFirstElementIsNull() throws Throwable {
        Elements elements = new Elements();
        Element firstElement = elements.first();
        assertNull(firstElement);
    }

    @Test(timeout = 4000)
    public void testParentsEmpty() throws Throwable {
        Document document = Document.createShell("<m-2,eXTA:N5y7");
        Elements elements = document.children();
        Elements parentElements = elements.parents();
        assertEquals(0, parentElements.size());
    }

    @Test(timeout = 4000)
    public void testPrevAllNullQuery() throws Throwable {
        Document document = Parser.parse("T5r?AQ)SywgoY6", "");
        Elements elements = document.getAllElements();
        Elements result = elements.prevAll((String) null);
        assertEquals(1, result.size());
    }

    @Test(timeout = 4000)
    public void testPrevQuery() throws Throwable {
        Document document = Parser.parse("*", "*");
        Elements elements = document.getAllElements();
        Elements result = elements.prev("*");
        assertEquals(1, result.size());
    }

    @Test(timeout = 4000)
    public void testIsQueryMatches() throws Throwable {
        Document document = Parser.parseBodyFragment("*", "*");
        Elements elements = document.getAllElements();
        boolean matches = elements.is("*");
        assertTrue(matches);
    }

    @Test(timeout = 4000)
    public void testIsQueryNotMatches() throws Throwable {
        Document document = Parser.parseBodyFragment("Bb,Y6", "Bb,Y6");
        Elements elements = document.getAllElements();
        boolean matches = elements.is("Bb,Y6");
        assertFalse(matches);
    }

    @Test(timeout = 4000)
    public void testEqNegativeIndexThrowsException() throws Throwable {
        Elements elements = new Elements();
        try {
            elements.eq((-2129));
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }

    @Test(timeout = 4000)
    public void testEqOutOfBoundsReturnsEmpty() throws Throwable {
        Elements elements = new Elements();
        Elements result = elements.eq(409);
        assertTrue(result.equals((Object) elements));
    }

    @Test(timeout = 4000)
    public void testRemoveElementsReturnsSame() throws Throwable {
        Document document = Parser.parseBodyFragment("Bb,Y|aK", "Bb,Y|aK");
        Elements elements = document.getAllElements();
        Elements result = elements.remove();
        assertSame(result, elements);
    }

    @Test(timeout = 4000)
    public void testEmptyElementsReturnsSame() throws Throwable {
        Document document = Parser.parseBodyFragment("Bb,Y|6", "Bb,Y|6");
        Elements elements = document.getAllElements();
        Elements result = elements.empty();
        assertFalse(result.isEmpty());
    }

    @Test(timeout = 4000)
    public void testUnwrapNullThrowsException() throws Throwable {
        Document document = Parser.parse("Bb,Y|aK", "Bb,Y|aK");
        Elements elements = document.getAllElements();
        try {
            elements.unwrap();
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testUnwrapEmptyElements() throws Throwable {
        Elements elements = new Elements();
        Elements result = elements.unwrap();
        assertEquals(0, result.size());
    }

    @Test(timeout = 4000)
    public void testWrapNonEmptyElements() throws Throwable {
        Document document = Parser.parse("<m-2,eXTA)>:N5y7", "<m-2,eXTA)>:N5y7");
        Elements elements = document.getAllElements();
        elements.wrap("<m-2,eXTA)>:N5y7");
        assertEquals(1, document.siblingIndex());
        assertTrue(document.hasParent());
    }

    @Test(timeout = 4000)
    public void testAfterNullThrowsException() throws Throwable {
        Document document = Parser.parseBodyFragment("b,Y6", "b,Y6");
        Elements elements = document.getAllElements();
        try {
            elements.after("b,Y6");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testAfterEmptyElements() throws Throwable {
        Elements elements = new Elements();
        Elements result = elements.after("");
        assertSame(result, elements);
    }

    @Test(timeout = 4000)
    public void testBeforeNullThrowsException() throws Throwable {
        Document document = new Document("No elements matched the query '%s' in the elements.");
        Elements elements = document.getAllElements();
        try {
            elements.before("No elements matched the query '%s' in the elements.");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testBeforeEmptyElements() throws Throwable {
        Elements elements = new Elements();
        Elements result = elements.before("3\"S4UaramE=:hhtC");
        assertEquals(0, result.size());
    }

    @Test(timeout = 4000)
    public void testAppendEmptyElementsReturnsSame() throws Throwable {
        Document document = Parser.parseBodyFragment("", "");
        Elements elements = document.getElementsContainingText("");
        Elements result = elements.append("");
        assertFalse(result.isEmpty());
    }

    @Test(timeout = 4000)
    public void testHtmlNonEmptyElementsReturnsSame() throws Throwable {
        Document document = Parser.parseBodyFragment("b-];s%E8k", "b-];s%E8k");
        Elements elements = document.getAllElements();
        Elements result = elements.html("b-];s%E8k");
        assertFalse(result.isEmpty());
    }

    @Test(timeout = 4000)
    public void testTagNameNullElementThrowsException() throws Throwable {
        Element[] elementArray = new Element[12];
        Elements elements = new Elements(elementArray);
        try {
            elements.tagName("org.jsoup.select.Elements");
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.select.Elements", e);
        }
    }

    @Test(timeout = 4000)
    public void testTagNameEmptyReturnsSame() throws Throwable {
        Elements elements = new Elements();
        Elements result = elements.tagName("");
        assertSame(elements, result);
    }

    @Test(timeout = 4000)
    public void testEachTextContainsText() throws Throwable {
        Document document = Parser.parseBodyFragment("Bb,Y6", "Bb,Y6");
        Elements elements = document.getAllElements();
        List<String> textList = elements.eachText();
        assertTrue(textList.contains("Bb,Y6"));
    }

    @Test(timeout = 4000)
    public void testEachTextDoesNotContainEmpty() throws Throwable {
        Document document = Parser.parseBodyFragment("", "");
        Elements elements = document.getAllElements();
        List<String> textList = elements.eachText();
        assertFalse(textList.contains(""));
    }

    @Test(timeout = 4000)
    public void testHasTextReturnsFalse() throws Throwable {
        Document document = Document.createShell("<m-2,eXTA:N5y7");
        Elements elements = document.children();
        boolean hasText = elements.hasText();
        assertFalse(hasText);
    }

    @Test(timeout = 4000)
    public void testHasTextReturnsTrue() throws Throwable {
        Document document = Parser.parseBodyFragment("Only http & https protocols supported", "Only http & https protocols supported");
        Elements elements = document.getAllElements();
        boolean hasText = elements.hasText();
        assertTrue(hasText);
    }

    @Test(timeout = 4000)
    public void testValSetAndGet() throws Throwable {
        Document document = Parser.parse("nly http & htps prtocols supported", "nly http & htps prtocols supported");
        Elements elements = document.getAllElements();
        elements.val("*mxmNg");
        String value = elements.val();
        assertEquals("*mxmNg", value);
    }

    @Test(timeout = 4000)
    public void testValEmpty() throws Throwable {
        Document document = Parser.parse("nly http & htps prtocols supported", "nly http & htps prtocols supported");
        Elements elements = document.getAllElements();
        String value = elements.val();
        assertEquals("", value);
    }

    @Test(timeout = 4000)
    public void testValEmptyElements() throws Throwable {
        Elements elements = new Elements();
        String value = elements.val();
        assertEquals("", value);
    }

    @Test(timeout = 4000)
    public void testHasClassReturnsFalse() throws Throwable {
        Document document = new Document("No elements matched the query '%s' in the elements.");
        Elements elements = document.getAllElements();
        boolean hasClass = elements.hasClass("No elements matched the query '%s' in the elements.");
        assertFalse(hasClass);
    }

    @Test(timeout = 4000)
    public void testToggleClassNonEmptyElements() throws Throwable {
        Document document = Parser.parseBodyFragment("", "");
        Elements elements = document.getElementsContainingText("");
        Elements result = elements.toggleClass("   ");
        assertFalse(result.isEmpty());
    }

    @Test(timeout = 4000)
    public void testRemoveClassReturnsSame() throws Throwable {
        Document document = Document.createShell("");
        Elements elements = document.getAllElements();
        Elements result = elements.removeClass("UWU-l)n?KV");
        assertSame(elements, result);
    }

    @Test(timeout = 4000)
    public void testAddClassNullThrowsException() throws Throwable {
        Document document = new Document("I");
        Elements elements = document.getAllElements();
        try {
            elements.addClass((String) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testAddClassReturnsSame() throws Throwable {
        Elements elements = new Elements();
        Elements result = elements.addClass("org.jsoup.helper.HttpConnection$Response");
        assertSame(elements, result);
    }

    @Test(timeout = 4000)
    public void testRemoveAttrReturnsSame() throws Throwable {
        Document document = new Document("Bb,Y6");
        Elements elements = document.getAllElements();
        Elements result = elements.removeAttr("Bb,Y6");
        assertSame(result, elements);
    }

    @Test(timeout = 4000)
    public void testEachAttrContainsValue() throws Throwable {
        Document document = Document.createShell("<m-2,eXTA:N5y7");
        Elements elements = document.getAllElements();
        Elements result = elements.attr("x", "x");
        List<String> attrList = result.eachAttr("x");
        assertTrue(attrList.contains("x"));
    }

    @Test(timeout = 4000)
    public void testEachAttrEmpty() throws Throwable {
        Document document = Document.createShell("Bb,76");
        Elements elements = document.children();
        List<String> attrList = elements.eachAttr("Bb,76");
        assertTrue(attrList.isEmpty());
    }

    @Test(timeout = 4000)
    public void testHasAttrReturnsTrue() throws Throwable {
        Document document = Document.createShell("<m-2,eXTA:N5y7");
        Elements elements = document.getAllElements();
        elements.attr("", "\n");
        boolean hasAttr = elements.hasAttr("");
        assertTrue(hasAttr);
    }

    @Test(timeout = 4000)
    public void testHasAttrReturnsFalse() throws Throwable {
        Document document = Parser.parseBodyFragment("Only http & https protocols supported", "Only http & https protocols supported");
        Elements elements = document.getAllElements();
        boolean hasAttr = elements.hasAttr("Only http & https protocols supported");
        assertFalse(hasAttr);
    }

    @Test(timeout = 4000)
    public void testAttrReturnsValue() throws Throwable {
        Document document = new Document("Bb,76");
        document.attr("Bb,76", "Bb,76");
        Elements elements = document.getAllElements();
        String value = elements.attr("Bb,76");
        assertEquals("Bb,76", value);
    }

    @Test(timeout = 4000)
    public void testAttrEmptyReturnsEmpty() throws Throwable {
        Document document = Parser.parse("", "");
        Elements elements = document.children();
        String value = elements.attr("");
        assertEquals("", value);
    }

    @Test(timeout = 4000)
    public void testNextReturnsDifferentInstance() throws Throwable {
        Elements elements = new Elements();
        Elements result = elements.next();
        assertNotSame(result, elements);
    }

    @Test(timeout = 4000)
    public void testReplaceAllElements() throws Throwable {
        Document document = Parser.parseBodyFragment("Bb,Y|aK", "Bb,Y|aK");
        Elements elements = document.getAllElements();
        elements.set(3, (Element) document);
        UnaryOperator<Element> unaryOperator = UnaryOperator.identity();
        elements.replaceAll(unaryOperator);
        assertEquals(1, document.siblingIndex());
    }

    @Test(timeout = 4000)
    public void testSelectInvalidQueryThrowsException() throws Throwable {
        Elements elements = new Elements();
        try {
            elements.select("<_+];Lxz(u?A|i1x");
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("org.jsoup.select.QueryParser", e);
        }
    }

    @Test(timeout = 4000)
    public void testFilterNullThrowsException() throws Throwable {
        Elements elements = new Elements();
        try {
            elements.filter((NodeFilter) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testPrevReturnsDifferentInstance() throws Throwable {
        Elements elements = new Elements();
        Elements result = elements.prev();
        assertNotSame(result, elements);
    }

    @Test(timeout = 4000)
    public void testAsListEmptyElements() throws Throwable {
        Elements elements = new Elements();
        ArrayList<Element> elementList = elements.asList();
        assertTrue(elementList.isEmpty());
    }

    @Test(timeout = 4000)
    public void testDeselectOutOfBoundsThrowsException() throws Throwable {
        Elements elements = new Elements();
        try {
            elements.deselect(665);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verifyException("java.util.ArrayList", e);
        }
    }

    @Test(timeout = 4000)
    public void testExpectFirstInvalidQueryThrowsException() throws Throwable {
        Elements elements = new Elements();
        try {
            elements.expectFirst(")XEN M$jX~$HL\"kq-_^");
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("org.jsoup.select.QueryParser", e);
        }
    }

    @Test(timeout = 4000)
    public void testPrevAllReturnsDifferentInstance() throws Throwable {
        Elements elements = new Elements();
        Elements result = elements.prevAll();
        assertNotSame(result, elements);
    }

    @Test(timeout = 4000)
    public void testTextReturnsWhitespace() throws Throwable {
        Document document = Parser.parseBodyFragment("", "");
        Elements elements = document.getElementsContainingText("");
        String text = elements.text();
        assertEquals("   ", text);
    }

    @Test(timeout = 4000)
    public void testTextNodesNonEmpty() throws Throwable {
        Document document = Parser.parseBodyFragment("Tm", "Tm");
        Elements elements = document.getAllElements();
        List<TextNode> textNodes = elements.textNodes();
        assertFalse(textNodes.isEmpty());
    }

    @Test(timeout = 4000)
    public void testCommentsEmpty() throws Throwable {
        Document document = new Document("Bb,76");
        Elements elements = document.getAllElements();
        List<Comment> comments = elements.comments();
        assertEquals(0, comments.size());
    }

    @Test(timeout = 4000)
    public void testToStringNonEmpty() throws Throwable {
        Document document = Parser.parse("", "");
        Elements elements = document.children();
        String result = elements.toString();
        assertEquals("<html>\n <head></head>\n <body></body>\n</html>", result);
    }

    @Test(timeout = 4000)
    public void testNotInvalidQueryThrowsException() throws Throwable {
        Elements elements = new Elements();
        try {
            elements.not("XS,a&m{A4{");
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("org.jsoup.select.QueryParser", e);
        }
    }

    @Test(timeout = 4000)
    public void testClearEmptyElements() throws Throwable {
        Elements elements = new Elements();
        elements.clear();
        assertTrue(elements.isEmpty());
    }

    @Test(timeout = 4000)
    public void testTraverseNullThrowsException() throws Throwable {
        Elements elements = new Elements();
        try {
            elements.traverse((NodeVisitor) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testHtmlNullElementThrowsException() throws Throwable {
        Element[] elementArray = new Element[8];
        Elements elements = new Elements(elementArray);
        try {
            elements.html();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.stream.ReferencePipeline$3$1", e);
        }
    }

    @Test(timeout = 4000)
    public void testNextAllNullElementThrowsException() throws Throwable {
        Element[] elementArray = new Element[9];
        Elements elements = new Elements(elementArray);
        try {
            elements.nextAll();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jsoup.select.Elements", e);
        }
    }

    @Test(timeout = 4000)
    public void testSelectFirstEmptyQueryThrowsException() throws Throwable {
        Elements elements = new Elements();
        try {
            elements.selectFirst("");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testElementsNullListThrowsException() throws Throwable {
        Elements elements = null;
        try {
            elements = new Elements((List<Element>) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.ArrayList", e);
        }
    }

    @Test(timeout = 4000)
    public void testDataNodesEmpty() throws Throwable {
        Document document = new Document("Bb,Y6");
        Elements elements = document.getAllElements();
        List<DataNode> dataNodes = elements.dataNodes();
        assertTrue(dataNodes.isEmpty());
    }

    @Test(timeout = 4000)
    public void testCloneNonEmptyElements() throws Throwable {
        Document document = Parser.parse("Bb,Y|a8", "Bb,Y|a8");
        Elements elements = document.getAllElements();
        Elements clonedElements = elements.clone();
        assertNotSame(clonedElements, elements);
        assertEquals(4, clonedElements.size());
    }

    @Test(timeout = 4000)
    public void testPrevAllNullQueryReturnsDifferentInstance() throws Throwable {
        Elements elements = new Elements();
        Elements result = elements.prevAll((String) null);
        assertNotSame(result, elements);
    }

    @Test(timeout = 4000)
    public void testDeselectAllEmptyElements() throws Throwable {
        Elements elements = new Elements();
        elements.deselectAll();
        assertTrue(elements.isEmpty());
    }

    @Test(timeout = 4000)
    public void testNextQueryReturnsEmpty() throws Throwable {
        Document document = Parser.parseBodyFragment("Only http & https protocols supported", "Only http & https protocols supported");
        Elements elements = document.getAllElements();
        Elements result = elements.next("o");
        assertEquals(0, result.size());
    }

    @Test(timeout = 4000)
    public void testDeselectElementNotInCollection() throws Throwable {
        Elements elements = new Elements();
        boolean deselected = elements.deselect((Object) elements);
        assertFalse(deselected);
    }

    @Test(timeout = 4000)
    public void testNextAllInvalidQueryThrowsException() throws Throwable {
        Document document = Parser.parse("nly http & htps prtocols supported", "nly http & htps prtocols supported");
        Elements elements = document.getAllElements();
        try {
            elements.nextAll("lkr.7#E@P2PwMVQQ");
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("org.jsoup.select.QueryParser", e);
        }
    }

    @Test(timeout = 4000)
    public void testRemoveAllThrowsException() throws Throwable {
        Document document = Document.createShell("");
        Elements elements = document.getAllElements();
        try {
            elements.removeAll(elements);
            fail("Expecting exception: ConcurrentModificationException");
        } catch (ConcurrentModificationException e) {
            verifyException("java.util.ArrayList$Itr", e);
        }
    }
}