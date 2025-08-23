package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;

import static org.jsoup.parser.Parser.NamespaceHtml;
import static org.jsoup.parser.Parser.NamespaceXml;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test suite for XML namespace handling in the {@link XmlTreeBuilder}.
 */
public class XmlTreeBuilderNamespaceTest {

    @Test
    void shouldResetDefaultNamespaceWhenEmptyXmlnsAttributeIsPresent() {
        // Arrange
        // This XML defines a default namespace (HTML) for the <table> element.
        // Inside the table, some elements use `xmlns=""` to reset the namespace to be empty
        // for that element and its children.
        String xml = """
            <?xml version='1.0'?>
            <Beers>
              <!-- The default namespace for <table> and its children is HTML -->
              <table xmlns='http://www.w3.org/1999/xhtml'>
               <th><td>Name</td><td>Origin</td><td>Description</td></th>
               <tr>
                 <!-- The default namespace is reset to empty here -->
                 <td><brandName xmlns="">Huntsman</brandName></td>
                 <td><origin xmlns="">Bath, UK</origin></td>
                 <td>
                   <details xmlns="">
                     <class>Bitter</class>
                     <hop>Fuggles</hop>
                     <pro>Wonderful hop, light alcohol, good summer beer</pro>
                     <con>Fragile; excessive variance pub to pub</con>
                   </details>
                 </td>
               </tr>
              </table>
            </Beers>""";

        // Act
        Document doc = Jsoup.parse(xml, Parser.xmlParser());

        // Assert
        // 1. The root <Beers> element should be in the default XML namespace.
        Element beers = doc.expectFirst("Beers");
        assertEquals(NamespaceXml, beers.tag().namespace(), "Root element should be in the default XML namespace.");

        // 2. The <td> inside <th> should inherit the HTML namespace from <table>.
        Element tdInHeader = doc.expectFirst("th > td");
        assertEquals(NamespaceHtml, tdInHeader.tag().namespace(), "<td> should inherit the HTML namespace from its <table> parent.");

        // 3. The <origin> element has `xmlns=""`, so its namespace should be empty.
        Element origin = doc.expectFirst("origin");
        assertEquals("", origin.tag().namespace(), "<origin> with `xmlns=\"\"` should have an empty namespace.");

        // 4. The <pro> element is a child of <details> which has `xmlns=""`, so it should also have an empty namespace.
        Element pro = doc.expectFirst("pro");
        assertEquals("", pro.tag().namespace(), "Child element <pro> should inherit the empty namespace from its <details> parent.");
    }
}