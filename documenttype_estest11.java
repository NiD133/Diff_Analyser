package org.jsoup.nodes;

import org.jsoup.internal.QuietAppendable;
import org.jsoup.nodes.Document.OutputSettings;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

// The original test class name is kept for context, though in a real scenario,
// it would be renamed to something like DocumentTypeTest.
public class DocumentType_ESTestTest11 extends DocumentType_ESTest_scaffolding {

    /**
     * Verifies that a DocumentType node is rendered correctly as a string
     * when the output syntax is set to XML.
     */
    @Test(timeout = 4000)
    public void doctypeRendersCorrectlyInXmlMode() {
        // Arrange: Set up the test objects and state.
        // 1. Use realistic and descriptive data for the DocumentType.
        String name = "html";
        String publicId = "-//W3C//DTD XHTML 1.0 Transitional//EN";
        String systemId = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd";
        DocumentType docType = new DocumentType(name, publicId, systemId);

        // 2. Configure the output settings for XML syntax.
        OutputSettings settings = new OutputSettings();
        settings.syntax(OutputSettings.Syntax.xml);

        // 3. Prepare a StringBuilder to capture the rendered output.
        StringBuilder output = new StringBuilder();
        QuietAppendable appendable = new QuietAppendable(output);

        // Act: Execute the method under test.
        // We call the package-private `outerHtmlHead` method directly. This is the
        // core rendering logic for the node, and calling it isolates the test
        // to this specific component's behavior.
        docType.outerHtmlHead(appendable, settings);

        // Assert: Verify the outcome.
        // The expected string is clear and directly corresponds to the inputs.
        String expectedXmlDoctype = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">";
        assertEquals(expectedXmlDoctype, output.toString());
    }
}