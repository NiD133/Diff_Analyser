package org.jsoup.nodes;

import org.jsoup.internal.QuietAppendable;
import org.junit.Test;
import java.io.StringWriter;
import static org.junit.Assert.assertEquals;

/**
 * This test class contains an improved version of a previously auto-generated test.
 * The original test had several issues that made it difficult to understand and maintain:
 * 1. A non-descriptive name ("test25").
 * 2. Unclear variable names (e.g., "mockFileWriter0").
 * 3. It used a complex MockFileWriter where a simple StringWriter is more appropriate.
 * 4. It called a package-private escape method with a magic number '34' for its options.
 * 5. The only assertion was unrelated to the escape method's behavior and did not verify the result.
 * This revised version addresses these issues to create a clear, correct, and meaningful test.
 */
public class Entities_ESTestTest26 extends Entities_ESTest_scaffolding {

    /**
     * Verifies that the escape method correctly escapes the less-than character ('<')
     * to its corresponding entity ('&lt;') when using XML syntax. The output is
     * written to an Appendable.
     */
    @Test(timeout = 4000)
    public void escapeToAppendableWithXmlSyntaxEscapesLessThanCharacter() {
        // Arrange
        StringWriter writer = new StringWriter();
        // The QuietAppendable is a jsoup internal wrapper that suppresses IOExceptions.
        QuietAppendable appendable = QuietAppendable.wrap(writer);

        String stringToEscape = "l#C$31bf_{ww<5";
        String expectedOutput = "l#C$31bf_{ww&lt;5";

        Document.OutputSettings xmlSettings = new Document.OutputSettings();
        xmlSettings.syntax(Document.OutputSettings.Syntax.xml);

        // The 'options' parameter for the internal escape method is a bitfield.
        // We test for attribute-style escaping. The original test used a magic number '34',
        // which was likely an error. 'ForAttribute' (value 2) is a valid and meaningful option.
        final int escapeOptions = Entities.ForAttribute;

        // Act
        // We are testing the package-private variant of escape that writes to an Appendable.
        Entities.escape(appendable, stringToEscape, xmlSettings, escapeOptions);

        // Assert
        assertEquals("The '<' character should be escaped to '&lt;' in XML mode.",
                expectedOutput, writer.toString());
    }
}