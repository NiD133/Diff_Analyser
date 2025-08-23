package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The original test combined getter and setter logic into a single, dense method.
 * This refactored version splits the test into two separate, focused tests within a
 * nested class structure for better clarity, isolation, and readability.
 */
public class ElementsTestTest14 {

    @Nested
    class ValAccessor {
        private Elements formElements;

        @BeforeEach
        void setUp() {
            Document doc = Jsoup.parse("<input value='one' /><textarea>two</textarea>");
            formElements = doc.select("input, textarea");
        }

        @Test
        void val_whenCalledOnMultipleElements_getsValueFromFirstElement() {
            // Arrange
            // Pre-condition check to ensure setup is correct
            assertEquals(2, formElements.size(), "Should find two form elements");
            assertEquals("two", formElements.last().val(), "Initial value of the second element should be 'two'");

            // Act
            String value = formElements.val();

            // Assert
            assertEquals("one", value, "val() should return the value of the first element in the collection");
        }

        @Test
        void val_whenNewValueIsProvided_setsValueOnAllElements() {
            // Arrange
            String newValue = "three";

            // Act
            formElements.val(newValue);

            // Assert
            // Verify the value property for both elements has been updated
            assertEquals(newValue, formElements.first().val(), "Value of the first element (input) should be updated");
            assertEquals(newValue, formElements.last().val(), "Value of the last element (textarea) should be updated");

            // Verify the underlying HTML has changed correctly for both element types
            assertEquals("<input value=\"three\">", formElements.first().outerHtml(),
                "The 'value' attribute of the input tag should be updated");
            assertEquals("<textarea>three</textarea>", formElements.last().outerHtml(),
                "The inner text of the textarea tag should be updated");
        }
    }
}