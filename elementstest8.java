package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ElementsTestTest8 {

    @Test
    @DisplayName("Element.hasClass() should perform a case-insensitive check")
    void elementHasClassIsCaseInsensitive() {
        // Arrange: Create HTML with p tags having class attributes with different casings.
        String html = "<p Class=One>One</p> <p class=Two>Two</p> <p CLASS=THREE>THREE</p>";
        Elements elements = Jsoup.parse(html).select("p");

        Element pWithMixedCaseClass = elements.get(0); // <p Class=One>
        Element pWithLowerCaseClass = elements.get(1); // <p class=Two>
        Element pWithUpperCaseClass = elements.get(2); // <p CLASS=THREE>

        // Assert: Verify that hasClass() finds the class regardless of the casing
        // in both the attribute definition and the search query.
        assertAll("Case-insensitive checks for hasClass",
            () -> {
                assertTrue(pWithMixedCaseClass.hasClass("One"), "Check with original casing 'One'");
                assertTrue(pWithMixedCaseClass.hasClass("ONE"), "Check with uppercase 'ONE'");
                assertTrue(pWithMixedCaseClass.hasClass("one"), "Check with lowercase 'one'");
            },
            () -> {
                assertTrue(pWithLowerCaseClass.hasClass("Two"), "Check with original casing 'Two'");
                assertTrue(pWithLowerCaseClass.hasClass("TWO"), "Check with uppercase 'TWO'");
            },
            () -> {
                assertTrue(pWithUpperCaseClass.hasClass("three"), "Check with lowercase 'three'");
                assertTrue(pWithUpperCaseClass.hasClass("ThreE"), "Check with mixed case 'ThreE'");
                assertTrue(pWithUpperCaseClass.hasClass("THREE"), "Check with original casing 'THREE'");
            }
        );
    }
}