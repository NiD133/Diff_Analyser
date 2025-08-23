package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This test class focuses on the val() methods of the Elements class.
 * The original test was automatically generated and tested a confusing edge case.
 * This version clarifies the intended behavior.
 */
public class Elements_ESTestTest133 {

    /**
     * Verifies that val(String) sets the value for all form elements in the collection,
     * and val() retrieves the value from the first element.
     */
    @Test
    public void val_setsValueOnAllElements_andGetsValueFromFirst() {
        // Arrange: Create a document with form elements and select them.
        // The val() methods are designed to work with form elements like <input>, <textarea>, etc.
        Document doc = Jsoup.parse("<input name='one' value='a'> <input name='two' value='b'>");
        Elements inputs = doc.select("input");

        // Act: Set a new value on all selected elements.
        // The val(String) setter updates all elements in the collection.
        inputs.val("new value");
        
        // The val() getter retrieves the value from the *first* element in the collection.
        String retrievedVal = inputs.val();

        // Assert: Verify that the retrieved value matches the value that was set.
        assertEquals("The value from the first element should be updated.", "new value", retrievedVal);
        
        // For completeness, also verify that the second element was updated too.
        assertEquals("The value of the second element should also be updated.", "new value", inputs.get(1).val());
    }
}