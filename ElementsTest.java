package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.TextUtil;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Elements collection operations and DOM manipulation.
 * Elements is a list-like collection of Element objects with methods that act on every element in the list.
 *
 * @author Jonathan Hedley, jonathan@hedley.net
 */
public class ElementsTest {
    
    @Test 
    public void canFilterElementsWithCssSelectors() {
        String htmlWithNestedElements = "<p>Excl</p><div class=headline><p>Hello</p><p>There</p></div><div class=headline><h1>Headline</h1></div>";
        Document document = Jsoup.parse(htmlWithNestedElements);
        
        // Select paragraphs within elements that have class 'headline'
        Elements paragraphsInHeadlines = document.select(".headline").select("p");
        
        assertEquals(2, paragraphsInHeadlines.size());
        assertEquals("Hello", paragraphsInHeadlines.get(0).text());
        assertEquals("There", paragraphsInHeadlines.get(1).text());
    }

    @Test 
    public void canManipulateAttributesOnMultipleElements() {
        String htmlWithVariousAttributes = "<p title=foo><p title=bar><p class=foo><p class=bar>";
        Document document = Jsoup.parse(htmlWithVariousAttributes);
        
        Elements elementsWithTitle = document.select("p[title]");
        assertEquals(2, elementsWithTitle.size());
        assertTrue(elementsWithTitle.hasAttr("title"));
        assertFalse(elementsWithTitle.hasAttr("class"));
        
        // Should return the first element's title attribute value
        assertEquals("foo", elementsWithTitle.attr("title"));

        // Remove title attribute from all selected elements
        elementsWithTitle.removeAttr("title");
        assertEquals(2, elementsWithTitle.size()); // Elements collection size unchanged
        assertEquals(0, document.select("p[title]").size()); // But no elements in DOM have title anymore

        // Set style attribute on all paragraph elements
        Elements allParagraphs = document.select("p").attr("style", "classy");
        assertEquals(4, allParagraphs.size());
        assertEquals("classy", allParagraphs.last().attr("style"));
        assertEquals("bar", allParagraphs.last().attr("class")); // Other attributes preserved
    }

    @Test 
    public void canCheckIfAnyElementHasAttribute() {
        Document document = Jsoup.parse("<p title=foo><p title=bar><p class=foo><p class=bar>");
        Elements paragraphs = document.select("p");
        
        assertTrue(paragraphs.hasAttr("class")); // At least one element has class
        assertFalse(paragraphs.hasAttr("style")); // No elements have style
    }

    @Test 
    public void canCheckForAbsoluteUrlAttributes() {
        Document document = Jsoup.parse("<a id=1 href='/foo'>One</a> <a id=2 href='https://jsoup.org'>Two</a>");
        Elements relativeLink = document.select("#1");
        Elements absoluteLink = document.select("#2");
        Elements allLinks = document.select("a");
        
        assertFalse(relativeLink.hasAttr("abs:href")); // Relative URL, no absolute version
        assertTrue(absoluteLink.hasAttr("abs:href")); // Already absolute URL
        assertTrue(allLinks.hasAttr("abs:href")); // Returns true if ANY element has it (element #2)
    }

    @Test 
    public void attributeMethodReturnsFirstMatchingValue() {
        Document document = Jsoup.parse("<p title=foo><p title=bar><p class=foo><p class=bar>");
        
        // Should return the class value from the first element that has it
        String firstClassValue = document.select("p").attr("class");
        assertEquals("foo", firstClassValue);
    }

    @Test 
    public void canGetAbsoluteUrlFromRelativeHref() {
        Document document = Jsoup.parse("<a id=1 href='/foo'>One</a> <a id=2 href='https://jsoup.org'>Two</a>");
        Elements relativeLink = document.select("#1");
        Elements absoluteLink = document.select("#2");
        Elements allLinks = document.select("a");

        assertEquals("", relativeLink.attr("abs:href")); // No base URL set, so empty
        assertEquals("https://jsoup.org", absoluteLink.attr("abs:href"));
        assertEquals("https://jsoup.org", allLinks.attr("abs:href")); // Returns first non-empty value
    }

    @Test 
    public void canManipulateCssClassesOnMultipleElements() {
        Document document = Jsoup.parse("<div><p class='mellow yellow'></p><p class='red green'></p>");

        Elements paragraphs = document.select("p");
        assertTrue(paragraphs.hasClass("red")); // At least one element has 'red' class
        assertFalse(paragraphs.hasClass("blue")); // No elements have 'blue' class
        
        paragraphs.addClass("blue");
        paragraphs.removeClass("yellow");
        paragraphs.toggleClass("mellow"); // Remove from first p, add to second p

        assertEquals("blue", paragraphs.get(0).className()); // 'mellow' removed, 'yellow' removed, 'blue' added
        assertEquals("red green blue mellow", paragraphs.get(1).className()); // 'blue' and 'mellow' added
    }

    @Test 
    public void cssClassCheckingIsCaseInsensitive() {
        Elements paragraphs = Jsoup.parse("<p Class=One>One <p class=Two>Two <p CLASS=THREE>THREE").select("p");
        Element firstParagraph = paragraphs.get(0);
        Element secondParagraph = paragraphs.get(1);
        Element thirdParagraph = paragraphs.get(2);

        // Class checking should be case-insensitive
        assertTrue(firstParagraph.hasClass("One"));
        assertTrue(firstParagraph.hasClass("ONE"));

        assertTrue(secondParagraph.hasClass("TWO"));
        assertTrue(secondParagraph.hasClass("Two"));

        assertTrue(thirdParagraph.hasClass("ThreE"));
        assertTrue(thirdParagraph.hasClass("three"));
    }

    @Test 
    public void canGetCombinedTextFromMultipleElements() {
        String htmlWithMultipleParagraphs = "<div><p>Hello<p>there<p>world</div>";
        Document document = Jsoup.parse(htmlWithMultipleParagraphs);
        
        assertEquals("Hello there world", document.select("div > *").text());
    }

    @Test 
    public void canCheckIfAnyElementHasText() {
        Document document = Jsoup.parse("<div><p>Hello</p></div><div><p></p></div>");
        Elements allDivs = document.select("div");
        Elements secondDiv = document.select("div + div");
        
        assertTrue(allDivs.hasText()); // First div has text content
        assertFalse(secondDiv.hasText()); // Second div is empty
    }

    @Test 
    public void canGetCombinedInnerHtmlFromMultipleElements() {
        Document document = Jsoup.parse("<div><p>Hello</p></div><div><p>There</p></div>");
        Elements divs = document.select("div");
        
        assertEquals("<p>Hello</p>\n<p>There</p>", divs.html());
    }

    @Test 
    public void canGetCombinedOuterHtmlFromMultipleElements() {
        Document document = Jsoup.parse("<div><p>Hello</p></div><div><p>There</p></div>");
        Elements divs = document.select("div");
        
        assertEquals("<div><p>Hello</p></div><div><p>There</p></div>", TextUtil.stripNewlines(divs.outerHtml()));
    }

    @Test 
    public void canModifyHtmlContentOfMultipleElements() {
        Document document = Jsoup.parse("<p>One</p><p>Two</p><p>Three</p>");
        Elements paragraphs = document.select("p");

        // Chain prepend and append operations
        paragraphs.prepend("<b>Bold</b>").append("<i>Ital</i>");
        assertEquals("<p><b>Bold</b>Two<i>Ital</i></p>", TextUtil.stripNewlines(paragraphs.get(1).outerHtml()));

        // Replace all inner HTML
        paragraphs.html("<span>Gone</span>");
        assertEquals("<p><span>Gone</span></p>", TextUtil.stripNewlines(paragraphs.get(1).outerHtml()));
    }

    @Test 
    public void canGetAndSetFormElementValues() {
        Document document = Jsoup.parse("<input value='one' /><textarea>two</textarea>");
        Elements formElements = document.select("input, textarea");
        
        assertEquals(2, formElements.size());
        assertEquals("one", formElements.val()); // Returns value from first element
        assertEquals("two", formElements.last().val());

        // Set value on all form elements
        formElements.val("three");
        assertEquals("three", formElements.first().val());
        assertEquals("three", formElements.last().val());
        assertEquals("<textarea>three</textarea>", formElements.last().outerHtml());
    }

    @Test 
    public void canInsertHtmlBeforeMultipleElements() {
        Document document = Jsoup.parse("<p>This <a>is</a> <a>jsoup</a>.</p>");
        
        document.select("a").before("<span>foo</span>");
        
        String expectedHtml = "<p>This <span>foo</span><a>is</a> <span>foo</span><a>jsoup</a>.</p>";
        assertEquals(expectedHtml, TextUtil.stripNewlines(document.body().html()));
    }

    @Test 
    public void canInsertHtmlAfterMultipleElements() {
        Document document = Jsoup.parse("<p>This <a>is</a> <a>jsoup</a>.</p>");
        
        document.select("a").after("<span>foo</span>");
        
        String expectedHtml = "<p>This <a>is</a><span>foo</span> <a>jsoup</a><span>foo</span>.</p>";
        assertEquals(expectedHtml, TextUtil.stripNewlines(document.body().html()));
    }

    @Test 
    public void canWrapMultipleElementsWithHtml() {
        String originalHtml = "<p><b>This</b> is <b>jsoup</b></p>";
        Document document = Jsoup.parse(originalHtml);
        
        document.select("b").wrap("<i></i>");
        
        assertEquals("<p><i><b>This</b></i> is <i><b>jsoup</b></i></p>", document.body().html());
    }

    @Test 
    public void canWrapElementsWithComplexHtmlStructure() {
        String originalHtml = "<p><b>This</b> is <b>jsoup</b>.</p> <p>How do you like it?</p>";
        Document document = Jsoup.parse(originalHtml);
        
        document.select("p").wrap("<div></div>");
        
        String expectedHtml = "<div>\n <p><b>This</b> is <b>jsoup</b>.</p>\n</div>\n<div>\n <p>How do you like it?</p>\n</div>";
        assertEquals(expectedHtml, document.body().html());
    }

    @Test 
    public void canUnwrapElementsWhileKeepingContent() {
        String htmlWithFontTags = "<div><font>One</font> <font><a href=\"/\">Two</a></font></div>";
        Document document = Jsoup.parse(htmlWithFontTags);
        
        // Remove font tags but keep their content
        document.select("font").unwrap();
        
        String expectedHtml = "<div>\n One <a href=\"/\">Two</a>\n</div>";
        assertEquals(expectedHtml, document.body().html());
    }

    @Test 
    public void unwrapPreservesSpacingBetweenElements() {
        String htmlWithSpans = "<p>One <span>two</span> <span>three</span> four</p>";
        Document document = Jsoup.parse(htmlWithSpans);
        
        document.select("span").unwrap();
        
        assertEquals("<p>One two three four</p>", document.body().html());
    }

    @Test 
    public void canEmptyMultipleElementsContent() {
        Document document = Jsoup.parse("<div><p>Hello <b>there</b></p> <p>now!</p></div>");
        document.outputSettings().prettyPrint(false);

        document.select("p").empty(); // Remove all child nodes but keep the elements
        
        assertEquals("<div><p></p> <p></p></div>", document.body().html());
    }

    @Test 
    public void canRemoveMultipleElementsFromDom() {
        Document document = Jsoup.parse("<div><p>Hello <b>there</b></p> jsoup <p>now!</p></div>");
        document.outputSettings().prettyPrint(false);

        document.select("p").remove(); // Remove elements entirely from DOM
        
        assertEquals("<div> jsoup </div>", document.body().html());
    }

    @Test 
    public void canGetElementAtSpecificIndex() {
        String htmlWithMultipleParagraphs = "<p>Hello<p>there<p>world";
        Document document = Jsoup.parse(htmlWithMultipleParagraphs);
        
        assertEquals("there", document.select("p").eq(1).text()); // Get as Elements collection
        assertEquals("there", document.select("p").get(1).text()); // Get as single Element
    }

    @Test 
    public void canTestIfAnyElementMatchesSelector() {
        String htmlWithTitleAttribute = "<p>Hello<p title=foo>there<p>world";
        Document document = Jsoup.parse(htmlWithTitleAttribute);
        Elements paragraphs = document.select("p");
        
        assertTrue(paragraphs.is("[title=foo]")); // At least one element matches
        assertFalse(paragraphs.is("[title=bar]")); // No elements match
    }

    @Test 
    public void canGetAllParentElements() {
        Document document = Jsoup.parse("<div><p>Hello</p></div><p>There</p>");
        Elements allParents = document.select("p").parents();

        assertEquals(3, allParents.size());
        assertEquals("div", allParents.get(0).tagName()); // Immediate parent
        assertEquals("body", allParents.get(1).tagName()); // Body element
        assertEquals("html", allParents.get(2).tagName()); // Root element
    }

    @Test 
    public void canFilterOutElementsWithNotSelector() {
        Document document = Jsoup.parse("<div id=1><p>One</p></div> <div id=2><p><span>Two</span></p></div>");

        // Get divs that don't contain p > span
        Elements divsWithoutNestedSpan = document.select("div").not(":has(p > span)");
        assertEquals(1, divsWithoutNestedSpan.size());
        assertEquals("1", divsWithoutNestedSpan.first().id());

        // Get divs that don't have id=1
        Elements divsNotId1 = document.select("div").not("#1");
        assertEquals(1, divsNotId1.size());
        assertEquals("2", divsNotId1.first().id());
    }

    @Test 
    public void canChangeTagNameOfMultipleElements() {
        Document document = Jsoup.parse("<p>Hello <i>there</i> <i>now</i></p>");
        
        document.select("i").tagName("em"); // Change all <i> tags to <em>

        assertEquals("<p>Hello <em>there</em> <em>now</em></p>", document.body().html());
    }

    @Test 
    public void canTraverseAllNodesInElements() {
        Document document = Jsoup.parse("<div><p>Hello</p></div><div>There</div>");
        final StringBuilder visitLog = new StringBuilder();
        
        document.select("div").traverse(new NodeVisitor() {
            @Override
            public void head(Node node, int depth) {
                visitLog.append("<").append(node.nodeName()).append(">");
            }

            @Override
            public void tail(Node node, int depth) {
                visitLog.append("</").append(node.nodeName()).append(">");
            }
        });
        
        String expectedTraversal = "<div><p><#text></#text></p></div><div><#text></#text></div>";
        assertEquals(expectedTraversal, visitLog.toString());
    }

    @Test 
    public void canExtractFormElementsFromMixedElements() {
        Document document = Jsoup.parse("<form id=1><input name=q></form><div /><form id=2><input name=f></form>");
        Elements mixedElements = document.select("form, div");
        assertEquals(3, mixedElements.size()); // 2 forms + 1 div

        List<FormElement> formsOnly = mixedElements.forms();
        assertEquals(2, formsOnly.size()); // Only the form elements
        assertNotNull(formsOnly.get(0));
        assertNotNull(formsOnly.get(1));
        assertEquals("1", formsOnly.get(0).id());
        assertEquals("2", formsOnly.get(1).id());
    }

    @Test 
    public void canExtractCommentNodesFromElements() {
        Document document = Jsoup.parse("<!-- comment1 --><p><!-- comment2 --><p class=two><!-- comment3 -->");
        
        List<Comment> commentsInParagraphs = document.select("p").comments();
        assertEquals(2, commentsInParagraphs.size());
        assertEquals(" comment2 ", commentsInParagraphs.get(0).getData());
        assertEquals(" comment3 ", commentsInParagraphs.get(1).getData());

        List<Comment> commentsInSpecificParagraph = document.select("p.two").comments();
        assertEquals(1, commentsInSpecificParagraph.size());
        assertEquals(" comment3 ", commentsInSpecificParagraph.get(0).getData());
    }

    @Test 
    public void canExtractTextNodesFromElements() {
        Document document = Jsoup.parse("One<p>Two<a>Three</a><p>Four</p>Five");
        
        List<TextNode> textNodesInParagraphs = document.select("p").textNodes();
        assertEquals(2, textNodesInParagraphs.size());
        assertEquals("Two", textNodesInParagraphs.get(0).text());
        assertEquals("Four", textNodesInParagraphs.get(1).text());
    }

    @Test 
    public void canExtractDataNodesFromScriptAndStyleElements() {
        Document document = Jsoup.parse("<p>One</p><script>Two</script><style>Three</style>");
        
        List<DataNode> dataNodes = document.select("p, script, style").dataNodes();
        assertEquals(2, dataNodes.size()); // Only script and style have data nodes
        assertEquals("Two", dataNodes.get(0).getWholeData());
        assertEquals("Three", dataNodes.get(1).getWholeData());

        // Test with JSON script tag
        Document jsonDocument = Jsoup.parse("<head><script type=application/json><crux></script><script src=foo>Blah</script>");
        Elements jsonScript = jsonDocument.select("script[type=application/json]");
        List<DataNode> jsonDataNodes = jsonScript.dataNodes();
        assertEquals(1, jsonDataNodes.size());
        DataNode jsonDataNode = jsonDataNodes.get(0);
        assertEquals("<crux>", jsonDataNode.getWholeData());

        // Verify data nodes are live - changes affect the DOM
        jsonDataNode.setWholeData("<cromulent>");
        assertEquals("<script type=\"application/json\"><cromulent></script>", jsonScript.outerHtml());
    }

    @Test 
    public void nodeExtractionReturnsEmptyListWhenNoMatches() {
        Document document = Jsoup.parse("<p>");
        assertEquals(0, document.select("form").textNodes().size());
    }

    @Test 
    public void canSelectElementsWithHyphenatedClassNames() {
        Document document = Jsoup.parse("<p class='tab-nav'>Check</p>");
        Elements elementsWithHyphenatedClass = document.getElementsByClass("tab-nav");
        
        assertEquals(1, elementsWithHyphenatedClass.size());
        assertEquals("Check", elementsWithHyphenatedClass.text());
    }

    @Test 
    public void canNavigateBetweenSiblingElements() {
        Document document = Jsoup.parse("<div><p>1<p>2<p>3<p>4<p>5<p>6</div><div><p>7<p>8<p>9<p>10<p>11<p>12</div>");

        Elements fourthParagraphs = document.select("p:eq(3)"); // Gets p4 and p10 (4th in each div)
        assertEquals(2, fourthParagraphs.size());

        // Test next sibling navigation
        Elements nextSiblings = fourthParagraphs.next();
        assertEquals(2, nextSiblings.size());
        assertEquals("5", nextSiblings.first().text());
        assertEquals("11", nextSiblings.last().text());

        // Test filtered next sibling
        assertEquals(0, fourthParagraphs.next("p:contains(6)").size()); // No immediate next sibling contains "6"
        Elements nextContaining5 = fourthParagraphs.next("p:contains(5)");
        assertEquals(1, nextContaining5.size());
        assertEquals("5", nextContaining5.first().text());

        // Test all following siblings
        Elements allFollowing = fourthParagraphs.nextAll();
        assertEquals(4, allFollowing.size()); // p5,p6 from first div + p11,p12 from second div
        assertEquals("5", allFollowing.first().text());
        assertEquals("12", allFollowing.last().text());

        // Test previous sibling navigation
        Elements previousSiblings = fourthParagraphs.prev();
        assertEquals(2, previousSiblings.size());
        assertEquals("3", previousSiblings.first().text());
        assertEquals("9", previousSiblings.last().text());

        // Test all previous siblings
        Elements allPrevious = fourthParagraphs.prevAll();
        assertEquals(6, allPrevious.size()); // p1,p2,p3 from first div + p7,p8,p9 from second div
        assertEquals("3", allPrevious.first().text());
        assertEquals("7", allPrevious.last().text());
    }

    @Test 
    public void canGetTextFromEachElementSeparately() {
        Document document = Jsoup.parse("<div><p>1<p>2<p>3<p>4<p>5<p>6</div><div><p>7<p>8<p>9<p>10<p>11<p>12<p></p></div>");
        
        List<String> textFromEachDiv = document.select("div").eachText();
        assertEquals(2, textFromEachDiv.size());
        assertEquals("1 2 3 4 5 6", textFromEachDiv.get(0));
        assertEquals("7 8 9 10 11 12", textFromEachDiv.get(1));

        List<String> textFromEachParagraph = document.select("p").eachText();
        Elements allParagraphs = document.select("p");
        assertEquals(13, allParagraphs.size());
        assertEquals(12, textFromEachParagraph.size()); // Last paragraph is empty, so not included
        assertEquals("1", textFromEachParagraph.get(0));
        assertEquals("12", textFromEachParagraph.get(11));
    }

    @Test 
    public void canGetAttributeValueFromEachElement() {
        Document document = Jsoup.parse(
            "<div><a href='/foo'>1</a><a href='http://example.com/bar'>2</a><a href=''>3</a><a>4</a>",
            "http://example.com");

        List<String> hrefValues = document.select("a").eachAttr("href");
        assertEquals(3, hrefValues.size()); // 4th anchor has no href, so not included
        assertEquals("/foo", hrefValues.get(0));
        assertEquals("http://example.com/bar", hrefValues.get(1));
        assertEquals("", hrefValues.get(2));

        List<String> absoluteHrefValues = document.select("a").eachAttr("abs:href");
        assertEquals(3, absoluteHrefValues.size());
        assertEquals("http://example.com/foo", absoluteHrefValues.get(0)); // Resolved relative URL
        assertEquals("http://example.com/bar", absoluteHrefValues.get(1));
        assertEquals("http://example.com", absoluteHrefValues.get(2)); // Empty href resolves to base
    }

    @Test 
    public void canReplaceElementInCollectionAndDom() {
        Document document = Jsoup.parse("<p>One<p>Two<p>Three");
        Element newParagraph = document.createElement("p").text("New").attr("id", "new");

        Elements paragraphs = document.select("p");
        Element originalSecondParagraph = paragraphs.get(1);
        Element replacedElement = paragraphs.set(1, newParagraph);
        
        assertSame(replacedElement, originalSecondParagraph); // Returns the old element
        assertSame(newParagraph, paragraphs.get(1)); // New element is now in the collection
        assertEquals("<p>One</p>\n<p id=\"new\">New</p>\n<p>Three</p>", document.body().html()); // DOM updated
    }

    @Test 
    public void canRemoveElementByIndexFromCollectionAndDom() {
        Document document = Jsoup.parse("<p>One<p>Two<p>Three");

        Elements paragraphs = document.select("p");
        Element secondParagraph = paragraphs.get(1);
        assertTrue(paragraphs.contains(secondParagraph));
        
        Element removedElement = paragraphs.remove(1);
        
        assertSame(removedElement, secondParagraph);
        assertEquals(2, paragraphs.size()); // Removed from collection
        assertFalse(paragraphs.contains(removedElement));
        assertEquals("<p>One</p>\n<p>Three</p>", document.body().html()); // Removed from DOM
    }

    @Test 
    public void canRemoveElementByObjectFromCollectionAndDom() {
        Document document = Jsoup.parse("<p>One<p>Two<p>Three");

        Elements paragraphs = document.select("p");
        Element secondParagraph = paragraphs.get(1);
        assertTrue(paragraphs.contains(secondParagraph));
        
        boolean wasRemoved = paragraphs.remove(secondParagraph);
        
        assertTrue(wasRemoved);
        assertEquals(2, paragraphs.size()); // Removed from collection
        assertFalse(paragraphs.contains(secondParagraph));
        assertEquals("<p>One</p>\n<p>Three</p>", document.body().html()); // Removed from DOM
    }

    @Test 
    public void removeElementDoesNothingWhenElementNotInCollection() {
        Document document = Jsoup.parse("<p>One<p>Two<p>Three");
        String originalHtml = document.html();
        Element unrelatedParagraph = document.createElement("p").text("New");

        Elements paragraphs = document.select("p");
        int originalSize = paragraphs.size();
        
        assertFalse(paragraphs.remove(unrelatedParagraph));
        assertFalse(paragraphs.remove(unrelatedParagraph.childNodes()));
        
        assertEquals(originalHtml, document.html()); // No changes to DOM
        assertEquals(originalSize, paragraphs.size()); // No changes to collection
    }

    @Test 
    public void canClearAllElementsFromCollection() {
        Document document = Jsoup.parse("<p>One</p><p>Two</p><div>Three</div>");
        Elements paragraphs = document.select("p");
        assertEquals(2, paragraphs.size());
        
        paragraphs.clear();
        
        assertEquals(0, paragraphs.size());
        assertEquals(0, document.select("p").size()); // Elements removed from DOM too
    }

    @Test 
    public void canRemoveMultipleSpecificElementsFromCollection() {
        Document document = Jsoup.parse("<p>One<p>Two<p>Three<p>Four</p><div>Div");
        Elements allParagraphs = document.select("p");
        assertEquals(4, allParagraphs.size());
        
        Elements middleParagraphs = document.select("p:gt(0):lt(3)"); // "Two" and "Three"
        assertEquals(2, middleParagraphs.size());

        boolean wasRemoved = allParagraphs.removeAll(middleParagraphs);
        
        assertTrue(wasRemoved);
        assertEquals(2, allParagraphs.size()); // Only "One" and "Four" remain
        assertEquals(2, middleParagraphs.size()); // Original collection unchanged

        // Test removing elements not in the collection
        Elements divs = document.select("div");
        assertFalse(allParagraphs.removeAll(divs)); // No divs in paragraph collection
        assertEquals(2, allParagraphs.size());

        assertEquals("<p>One</p>\n<p>Four</p>\n<div>Div</div>", document.body().html());
    }

    @Test 
    public void canRetainOnlySpecificElementsInCollection() {
        Document document = Jsoup.parse("<p>One<p>Two<p>Three<p>Four</p><div>Div");
        Elements allParagraphs = document.select("p");
        assertEquals(4, allParagraphs.size());
        
        Elements middleParagraphs = document.select("p:gt(0):lt(3)"); // "Two" and "Three"
        assertEquals(2, middleParagraphs.size());

        boolean wasModified = allParagraphs.retainAll(middleParagraphs);
        
        assertTrue(wasModified);
        assertEquals(2, allParagraphs.size()); // Only "Two" and "Three" remain
        assertEquals(2, middleParagraphs.size()); // Original collection unchanged

        assertEquals("<p>Two</p>\n<p>Three</p>\n<div>Div</div>", document.body().html());

        // Test retaining elements already in the collection (no-op)
        Elements currentParagraphs = document.select("p");
        assertFalse(middleParagraphs.retainAll(currentParagraphs)); // No change needed
        assertEquals("<p>Two</p>\n<p>Three</p>\n<div>Div</div>", document.body().html());
    }

    @Test 
    public void iteratorRemovalAffectsBothCollectionAndDom() {
        Document document = Jsoup.parse("<p>One<p>Two<p>Three<p>Four");
        Elements paragraphs = document.select("p");

        assertEquals(4, paragraphs.size());
        for (Iterator<Element> iterator = paragraphs.iterator(); iterator.hasNext(); ) {
            Element element = iterator.next();
            if (element.text().contains("Two")) {
                iterator.remove();
            }
        }
        
        assertEquals(3, paragraphs.size());
        assertEquals("<p>One</p>\n<p>Three</p>\n<p>Four</p>", document.body().html());
    }

    @Test 
    public void canRemoveElementsUsingPredicate() {
        Document document = Jsoup.parse("<p>One<p>Two<p>Three<p>Four");
        Elements paragraphs = document.select("p");

        assertEquals(4, paragraphs.size());
        boolean wasRemoved = paragraphs.removeIf(element -> element.text().contains("Two"));
        
        assertTrue(wasRemoved);
        assertEquals(3, paragraphs.size());
        assertEquals("<p>One</p>\n<p>Three</p>\n<p>Four</p>", document.body().html());

        // Test predicate that matches nothing
        assertFalse(paragraphs.removeIf(element -> element.text().contains("Five")));
        assertEquals("<p>One</p>\n<p>Three</p>\n<p>Four</p>", document.body().html());
    }

    @Test 
    public void removeIfSupportsReadingCollectionDuringRemoval() {
        Document document = Jsoup.parse("<p>One<p>Two<p>Three<p>Four");
        Elements paragraphs = document.select("p");
        assertEquals(4, paragraphs.size());

        // This tests that removeIf can handle concurrent reads of the collection being modified
        boolean wasRemoved = paragraphs.removeIf(element -> paragraphs.contains(element));
        
        assertTrue(wasRemoved);
        assertEquals(0, paragraphs.size()); // All elements removed
        assertEquals("", document.body().html());
    }

    @Test 
    public void canReplaceAllElementsUsingFunction() {
        Document document = Jsoup.parse("<p>One<p>Two<p>Three<p>Four");
        Elements paragraphs = document.select("p");
        assertEquals(4, paragraphs.size());

        // Replace all paragraphs with divs containing the same text
        paragraphs.replaceAll(element -> {
            Element div = document.createElement("div");
            div.text(element.text());
            return div;
        });

        // Verify collection contains the new elements
        for (Element element : paragraphs) {
            assertEquals("div", element.tagName());
        }

        // Verify DOM was updated
        String expectedHtml = "<div>One</div><div>Two</div><div>Three</div><div>Four</div>";
        assertEquals(expectedHtml, TextUtil.normalizeSpaces(document.body().html()));
    }

    @Test 
    void canSelectFirstMatchingElementFromCollection() {
        Document document = Jsoup.parse("<p>One</p><p>Two <span>Jsoup</span></p><p><span>Three</span></p>");
        Element firstSpan = document.children().selectFirst("span");
        
        assertNotNull(firstSpan);
        assertEquals("Jsoup", firstSpan.text());
    }

    @Test 
    void selectFirstReturnsNullWhenNoMatch() {
        Document document = Jsoup.parse("<p>One</p><p>Two</p><p>Three</p>");
        Element span = document.children().selectFirst("span");
        
        assertNull(span);
    }

    @Test 
    void canExpectFirstMatchingElementFromCollection() {
        Document document = Jsoup.parse("<p>One</p><p>Two <span>Jsoup</span></p><p><span>Three</span></p>");
        Element firstSpan = document.children().expectFirst("span");
        
        assertNotNull(firstSpan);
        assertEquals("Jsoup", firstSpan.text());
    }

    @Test 
    void expectFirstThrowsExceptionWhenNoMatch() {
        Document document = Jsoup.parse("<p>One</p><p>Two</p><p>Three</p>");

        boolean exceptionThrown = false;
        try {
            Element span = document.children().expectFirst("span");
        } catch (IllegalArgumentException exception) {
            exceptionThrown = true;
            assertEquals("No elements matched the query 'span' in the elements.", exception.getMessage());
        }
        assertTrue(exceptionThrown);
    }

    @Test 
    void canSelectFirstFromPreviousSelection() {
        Document document = Jsoup.parse("<div><p>One</p></div><div><p><span>Two</span></p></div><div><p><span>Three</span></p></div>");
        Elements divs = document.select("div");
        assertEquals(3, divs.size());

        Element firstSpanInDivs = divs.selectFirst("p span");
        assertNotNull(firstSpanInDivs);
        assertEquals("Two", firstSpanInDivs.text());

        // Test edge cases with self-selection
        assertNotNull(firstSpanInDivs.selectFirst("span")); // Can reselect self
        assertNull(firstSpanInDivs.selectFirst(">span")); // No direct child span of span

        assertNotNull(divs.selectFirst("div")); // Can reselect self from collection
        assertNull(divs.selectFirst(">div")); // No direct child div of div
    }
}