package org.apache.ibatis.parsing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for the XNode.toString() method.
 * This class focuses on verifying that XNode correctly formats its XML content into a readable, indented string.
 */
@DisplayName("XNode toString()")
class XNodeToStringTest {

  private static final String XML_DATA = """
      <users>
        <user>
          <id>100</id>
          <name>Tom</name>
          <age>30</age>
          <cars>
            <car index="1">BMW</car>
            <car index="2">Audi</car>
            <car index="3">Benz</car>
          </cars>
        </user>
      </users>""";

  private XPathParser parser;

  @BeforeEach
  void setUp() {
    // The parser is initialized with the same XML data for each test.
    parser = new XPathParser(XML_DATA);
  }

  @Test
  @DisplayName("should correctly format the root node")
  void shouldFormatRootNode() {
    // Arrange
    XNode rootNode = parser.evalNode("/users");
    String expectedXml = """
        <users>
          <user>
            <id>
              100
            </id>
            <name>
              Tom
            </name>
            <age>
              30
            </age>
            <cars>
              <car index="1">
                BMW
              </car>
              <car index="2">
                Audi
              </car>
              <car index="3">
                Benz
              </car>
            </cars>
          </user>
        </users>
        """;

    // Act
    String actualXml = rootNode.toString();

    // Assert
    assertEquals(expectedXml, actualXml);
  }

  @Test
  @DisplayName("should correctly format a direct child node")
  void shouldFormatChildNode() {
    // Arrange
    XNode userNode = parser.evalNode("/users/user");
    String expectedXml = """
        <user>
          <id>
            100
          </id>
          <name>
            Tom
          </name>
          <age>
            30
          </age>
          <cars>
            <car index="1">
              BMW
            </car>
            <car index="2">
              Audi
            </car>
            <car index="3">
              Benz
            </car>
          </cars>
        </user>
        """;

    // Act
    String actualXml = userNode.toString();

    // Assert
    assertEquals(expectedXml, actualXml);
  }

  @Test
  @DisplayName("should correctly format a nested child node")
  void shouldFormatNestedChildNode() {
    // Arrange
    XNode carsNode = parser.evalNode("/users/user/cars");
    String expectedXml = """
        <cars>
          <car index="1">
            BMW
          </car>
          <car index="2">
            Audi
          </car>
          <car index="3">
            Benz
          </car>
        </cars>
        """;

    // Act
    String actualXml = carsNode.toString();

    // Assert
    assertEquals(expectedXml, actualXml);
  }
}