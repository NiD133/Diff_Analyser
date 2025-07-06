package org.jsoup.select;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.jsoup.select.Evaluator;
import org.jsoup.select.NodeFilter;
import org.jsoup.select.NodeVisitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class Elements_ESTest extends Elements_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testSelectElementsByWholeText() {
        Document document = Parser.parseBodyFragment("String must not be empty", "String must not be empty");
        Evaluator.ContainsWholeText evaluator = new Evaluator.ContainsWholeText("org.jsoup.select.Elements");
        Elements elements = document.select(evaluator);
        Elements selectedElements = elements.eq(0);
        assertNotSame(selectedElements, elements);
    }

    @Test(timeout = 4000)
    public void testEmptyElementsSize() {
        Elements elements = new Elements(0);
        assertEquals(0, elements.size());
    }

    @Test(timeout = 4000)
    public void testWrapEmptyElements() {
        Elements elements = new Elements();
        Elements wrappedElements = elements.wrap("1:DKpJ$!<ko/csNY");
        assertSame(wrappedElements, elements);
    }

    @Test(timeout = 4000)
    public void testSetValOnEmptyElements() {
        Elements elements = new Elements();
        Elements valElements = elements.val("map");
        assertSame(valElements, elements);
    }

    @Test(timeout = 4000)
    public void testGetAllElementsAndSetVal() {
        Tag tag = new Tag("imap");
        Element element = new Element(tag, "imap");
        Elements elements = element.getAllElements();
        Elements valElements = elements.val("imap");
        String value = valElements.val();
        assertEquals("imap", value);
    }

    @Test(timeout = 4000)
    public void testUnwrapChildren() {
        Document document = Parser.parseBodyFragment("String must not be empty", ";c");
        Elements elements = document.children();
        Elements unwrappedElements = elements.unwrap();
        assertSame(elements, unwrappedElements);
    }

    @Test(timeout = 4000)
    public void testTraverseEmptyElements() {
        Document document = Parser.parseBodyFragment("String must not be empty", ";c");
        Elements elements = document.children();
        Elements parentElements = elements.parents();
        NodeVisitor nodeVisitor = mock(NodeVisitor.class, new ViolatedAssumptionAnswer());
        Elements traversedElements = parentElements.traverse(nodeVisitor);
        assertTrue(traversedElements.isEmpty());
    }

    @Test(timeout = 4000)
    public void testTraverseSelectedElements() {
        Document document = Parser.parseBodyFragment("String must not be empty", "String must not be empty");
        Element element = document.text("org.jsoup.select.Elements");
        Evaluator.ContainsWholeText evaluator = new Evaluator.ContainsWholeText("org.jsoup.select.Elements");
        Elements elements = element.select(evaluator);
        NodeVisitor nodeVisitor = mock(NodeVisitor.class, new ViolatedAssumptionAnswer());
        Elements traversedElements = elements.traverse(nodeVisitor);
        assertSame(elements, traversedElements);
    }

    @Test(timeout = 4000)
    public void testToggleClassOnEmptyElements() {
        Elements elements = new Elements();
        Elements toggledElements = elements.toggleClass("Stack unexpectedly empty");
        assertTrue(toggledElements.isEmpty());
    }

    @Test(timeout = 4000)
    public void testToStringOnEmptyElements() {
        Elements elements = new Elements();
        String result = elements.toString();
        assertEquals("", result);
    }

    @Test(timeout = 4000)
    public void testTextNodesOnEmptyElements() {
        Elements elements = new Elements();
        List<TextNode> textNodes = elements.textNodes();
        assertTrue(textNodes.isEmpty());
    }

    @Test(timeout = 4000)
    public void testTextOnChildren() {
        Document document = Parser.parseBodyFragment("String must not be empty", ";c");
        Elements elements = document.children();
        String text = elements.text();
        assertEquals("String must not be empty", text);
    }

    @Test(timeout = 4000)
    public void testSetElementInChildren() {
        Document document = Parser.parseBodyFragment("String must not be empty", "String must not be empty");
        Elements elements = document.children();
        elements.set(0, document);
        assertTrue(document.hasParent());
    }

    @Test(timeout = 4000)
    public void testSelectFirstReturnsNull() {
        Document document = Parser.parseBodyFragment("String must not be empty", ";c");
        Elements elements = document.children();
        Element element = elements.selectFirst("org.jsoup.select.Elements");
        assertNull(element);
    }

    @Test(timeout = 4000)
    public void testSelectOnEmptyElements() {
        Elements elements = new Elements();
        Elements selectedElements = elements.select("lt");
        assertTrue(selectedElements.equals(elements));
    }

    @Test(timeout = 4000)
    public void testRemoveClassOnEmptyElements() {
        Elements elements = new Elements();
        Elements result = elements.removeClass(null);
        assertSame(result, elements);
    }

    @Test(timeout = 4000)
    public void testRemoveAttrOnEmptyElements() {
        Elements elements = new Elements();
        Elements result = elements.removeAttr("");
        assertSame(elements, result);
    }

    @Test(timeout = 4000)
    public void testRemoveAllElements() {
        Document document = Parser.parseBodyFragment("String must not be empty", "String must not be empty");
        Elements elements = document.getAllElements();
        Elements children = document.children();
        boolean removed = elements.removeAll(children);
        assertEquals(3, elements.size());
        assertTrue(removed);
    }

    @Test(timeout = 4000)
    public void testRemoveElementFromDocument() {
        Document document = new Document("#declaration");
        Elements elements = document.getAllElements();
        Document removedDocument = (Document) elements.remove(0);
        assertEquals(Document.QuirksMode.noQuirks, removedDocument.quirksMode());
    }

    @Test(timeout = 4000)
    public void testRemoveElementFromShell() {
        Document document = Document.createShell("String must not be empty");
        Elements elements = document.getAllElements();
        Element removedElement = elements.remove(1);
        assertTrue(removedElement.isBlock());
    }

    @Test(timeout = 4000)
    public void testRemoveOnEmptyElements() {
        Elements elements = new Elements();
        Elements removedElements = elements.remove();
        assertEquals(0, removedElements.size());
    }

    @Test(timeout = 4000)
    public void testPrevAllWithNullQuery() {
        Document document = Document.createShell("%FypL.Ou}k)'");
        Elements elements = document.getElementsByAttributeValueEnding("%FypL.Ou}k)'", "%FypL.Ou}k)'");
        Elements prevAllElements = elements.prevAll(null);
        assertNotSame(elements, prevAllElements);
    }

    @Test(timeout = 4000)
    public void testPrevAllOnElements() {
        Document document = Parser.parseBodyFragment("Q,G", "Q,G");
        Elements elements = document.getElementsByIndexLessThan(201);
        Elements prevAllElements = elements.prevAll();
        assertNotSame(prevAllElements, elements);
    }

    @Test(timeout = 4000)
    public void testPrevOnEmptyElements() {
        Elements elements = new Elements();
        Elements prevElements = elements.prev();
        assertTrue(prevElements.isEmpty());
    }

    @Test(timeout = 4000)
    public void testPrependOnEmptyElements() {
        Elements elements = new Elements();
        Elements prependedElements = elements.prepend("[=yZ}b4OR F!QP+");
        assertEquals(0, prependedElements.size());
    }

    @Test(timeout = 4000)
    public void testParentsOnSelectedElements() {
        Document document = Parser.parseBodyFragment("String must not be empty", "String must not be empty");
        Element element = document.text("org.jsoup.select.Elements");
        Evaluator.ContainsWholeText evaluator = new Evaluator.ContainsWholeText("org.jsoup.select.Elements");
        Elements elements = element.select(evaluator);
        Elements parentElements = elements.parents();
        assertEquals(1, parentElements.size());
    }

    @Test(timeout = 4000)
    public void testOuterHtmlOnElement() {
        Tag tag = new Tag("ismap", "");
        Element element = new Element(tag, "\"");
        Elements elements = element.getAllElements();
        String outerHtml = elements.outerHtml();
        assertEquals("<ismap></ismap>", outerHtml);
    }

    @Test(timeout = 4000)
    public void testGetElementsMatchingText() {
        Tag tag = new Tag("ismap", "");
        Element element = new Element(tag, "\"");
        Pattern pattern = Pattern.compile("\"", 1);
        Elements elements = element.getElementsMatchingText(pattern);
        String outerHtml = elements.outerHtml();
        assertEquals("", outerHtml);
    }

    @Test(timeout = 4000)
    public void testNotOnSelectedElements() {
        Document document = Parser.parseBodyFragment("]S,", "]S,");
        Evaluator.ContainsWholeText evaluator = new Evaluator.ContainsWholeText("ismap");
        StructuralEvaluator.ImmediateParentRun structuralEvaluator = new StructuralEvaluator.ImmediateParentRun(evaluator);
        Elements elements = document.select(structuralEvaluator);
        Elements notElements = elements.not("Content-Encoding");
        assertNotSame(elements, notElements);
    }

    @Test(timeout = 4000)
    public void testNotOnChildren() {
        Document document = Parser.parseBodyFragment("String must not be empty", "String must not be empty");
        Elements elements = document.children();
        Elements notElements = elements.not("String must not be empty");
        assertEquals(1, notElements.size());
    }

    @Test(timeout = 4000)
    public void testNextAllWithNullQuery() {
        Document document = Parser.parse("|", "");
        Elements elements = document.getAllElements();
        Elements nextAllElements = elements.nextAll(null);
        assertNotSame(nextAllElements, elements);
    }

    @Test(timeout = 4000)
    public void testNextAllOnEmptyElements() {
        Elements elements = new Elements();
        Elements nextAllElements = elements.nextAll();
        assertTrue(nextAllElements.equals(elements));
    }

    @Test(timeout = 4000)
    public void testNextWithQuery() {
        Tag tag = new Tag("R", "R");
        Attributes attributes = new Attributes();
        PseudoTextElement pseudoTextElement = new PseudoTextElement(tag, "org.jsoup.select.Elements", attributes);
        Elements elements = pseudoTextElement.getElementsByIndexLessThan(512);
        Elements nextElements = elements.next("org.jsoup.select.Elements");
        assertNotSame(nextElements, elements);
    }

    @Test(timeout = 4000)
    public void testNextOnEmptyElements() {
        Elements elements = new Elements();
        Elements nextElements = elements.next();
        assertNotSame(nextElements, elements);
    }

    @Test(timeout = 4000)
    public void testGetElementsMatchingTextPattern() {
        Document document = Parser.parseBodyFragment("String must not be empty", "String must not be empty");
        Pattern pattern = Pattern.compile("", 32);
        Elements elements = document.getElementsMatchingText(pattern);
        Element lastElement = elements.last();
        assertEquals("body", lastElement.nodeName());
    }

    @Test(timeout = 4000)
    public void testHtmlOnEmptyElements() {
        Elements elements = new Elements();
        String html = elements.html();
        assertEquals("", html);
    }

    @Test(timeout = 4000)
    public void testGetElementsContainingOwnText() {
        Document document = Parser.parse(".KK`)n7Yo+S(:._|)", ".KK`)n7Yo+S(:._|)");
        Elements elements = document.getElementsContainingOwnText(".KK`)n7Yo+S(:._|)");
        Element firstElement = elements.first();
        assertEquals(1, firstElement.siblingIndex());
    }

    @Test(timeout = 4000)
    public void testFirstElementHasNoParent() {
        Tag tag = new Tag("ismap", "");
        Element element = new Element(tag, "\"");
        Elements elements = element.getAllElements();
        Element firstElement = elements.first();
        assertFalse(firstElement.hasParent());
    }

    @Test(timeout = 4000)
    public void testFilterWithNodeFilter() {
        Document document = Document.createShell("%FypL.Ou}k)'");
        Elements elements = document.getElementsByAttributeValueEnding("%FypL.Ou}k)'", "%FypL.Ou}k)'");
        NodeFilter nodeFilter = mock(NodeFilter.class, new ViolatedAssumptionAnswer());
        Elements filteredElements = elements.filter(nodeFilter);
        assertTrue(filteredElements.isEmpty());
    }

    @Test(timeout = 4000)
    public void testFilterWithMockNodeFilter() {
        Document document = Parser.parseBodyFragment("String must not be empty", "String must not be empty");
        Element element = document.text("org.jsoup.select.Elements");
        Evaluator.ContainsWholeText evaluator = new Evaluator.ContainsWholeText("org.jsoup.select.Elements");
        Elements elements = element.select(evaluator);
        NodeFilter nodeFilter = mock(NodeFilter.class, new ViolatedAssumptionAnswer());
        doReturn(null, null, null).when(nodeFilter).head(any(Node.class), anyInt());
        Elements filteredElements = elements.filter(nodeFilter);
        assertEquals(3, filteredElements.size());
    }

    @Test(timeout = 4000)
    public void testExpectFirstElement() {
        Tag tag = new Tag("imap");
        Element element = new Element(tag, "imap");
        Elements elements = element.getAllElements();
        Element expectedElement = elements.expectFirst("imap");
        assertFalse(expectedElement.hasParent());
    }

    @Test(timeout = 4000)
    public void testEqOnChildren() {
        Document document = Parser.parseBodyFragment("String must not be empty", ";c");
        Elements elements = document.children();
        Elements eqElements = elements.eq(0);
        assertFalse(eqElements.isEmpty());
    }

    @Test(timeout = 4000)
    public void testEmptyOnEmptyElements() {
        Elements elements = new Elements();
        Elements emptiedElements = elements.empty();
        assertSame(emptiedElements, elements);
    }

    @Test(timeout = 4000)
    public void testDeselectElement() {
        Document document = new Document("E", "E");
        Elements elements = document.getAllElements();
        boolean deselected = elements.deselect(document);
        assertTrue(deselected);
    }

    @Test(timeout = 4000)
    public void testDeselectElementByIndex() {
        Tag tag = new Tag("", "");
        Element element = new Element(tag, "");
        Elements elements = element.getAllElements();
        Element deselectedElement = elements.deselect(0);
        assertEquals(0, deselectedElement.siblingIndex());
    }

    @Test(timeout = 4000)
    public void testCloneOnEmptyElements() {
        Elements elements = new Elements();
        Elements clonedElements = elements.clone();
        assertNotSame(clonedElements, elements);
    }

    @Test(timeout = 4000)
    public void testBeforeOnChildren() {
        Document document = Parser.parseBodyFragment("String must not be empty", "String must not be empty");
        Elements elements = document.children();
        Elements beforeElements = elements.before("+locPfd1R']\"Xmtz8N");
        assertSame(beforeElements, elements);
    }

    @Test(timeout = 4000)
    public void testAttrOnEmptyElements() {
        Elements elements = new Elements();
        Elements attrElements = elements.attr(":", ":");
        assertSame(elements, attrElements);
    }

    @Test(timeout = 4000)
    public void testAsListOnElements() {
        Tag tag = new Tag("ismap", "ismap");
        Element element = new Element(tag, "\"");
        Elements elements = element.getAllElements();
        ArrayList<Element> elementList = elements.asList();
        assertFalse(elementList.isEmpty());
    }

    @Test(timeout = 4000)
    public void testAppendOnEmptyElements() {
        Elements elements = new Elements();
        Elements appendedElements = elements.append("");
        assertSame(appendedElements, elements);
    }

    @Test(timeout = 4000)
    public void testAfterOnChildren() {
        Document document = Parser.parseBodyFragment("String must not be empty", "String must not be empty");
        Elements elements = document.children();
        Elements afterElements = elements.after("org.jsoup.select.Elements");
        assertEquals(1, afterElements.size());
    }

    @Test(timeout = 4000)
    public void testAddClassOnEmptyElements() {
        Elements elements = new Elements();
        Elements classAddedElements = elements.addClass("7wat9qt*uppi");
        assertSame(classAddedElements, elements);
    }

    @Test(timeout = 4000)
    public void testWrapThrowsException() {
        Elements elements = new Elements();
        try {
            elements.wrap("");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jsoup.helper.Validate", e);
        }
    }

    @Test(timeout = 4000)
    public void testToggleClassThrowsException() {
        Document document = Parser.parse