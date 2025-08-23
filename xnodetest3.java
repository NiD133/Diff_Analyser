package org.apache.ibatis.parsing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Properties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the XNode class, focusing on its string representation.
 */
// The class was renamed from XNodeTestTest3 to XNodeTest for clarity and convention.
class XNodeTest {

    @Test
    @DisplayName("toString() should substitute variables and format the XML output")
    void toStringShouldSubstituteVariablesAndPrettyPrintXml() throws Exception {
        // Arrange
        // An XML string with placeholders in an attribute and in the text content.
        final String xmlWithPlaceholders = "<root attr='${x}'>y = ${y}<sub attr='${y}'>x = ${x}</sub></root>";

        // The variables to be substituted into the placeholders.
        final Properties variables = new Properties();
        variables.put("x", "foo");
        variables.put("y", "bar");

        // The expected output is a formatted XML string with variables substituted.
        // Using a text block makes the expected structure much clearer than a single-line string with escaped newlines.
        final String expectedXml = """
            <root attr="foo">
              y = bar
              <sub attr="bar">
                x = foo
              </sub>
            </root>
            """;

        final XPathParser parser = new XPathParser(xmlWithPlaceholders, false, variables);
        final XNode rootNode = parser.evalNode("/root");

        // Act
        // The toString() method is called, which should trigger variable substitution and formatting.
        final String actualXml = rootNode.toString();

        // Assert
        // The text block provides a trailing newline, matching the original test's expectation.
        assertEquals(expectedXml, actualXml);
    }
}