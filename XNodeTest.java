/*
 *    Copyright 2009-2024 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.parsing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Properties;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link XNode}.
 * Focuses on the string representation and variable substitution capabilities.
 */
class XNodeTest {

  private static final String SAMPLE_XML = """
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

  @ParameterizedTest
  @MethodSource("provideXPathsAndExpectedFormattedStrings")
  @DisplayName("toString() should produce a pretty-printed XML string")
  void shouldProduceFormattedXmlString(String xpath, String expectedXml) {
    // Arrange
    XPathParser parser = new XPathParser(SAMPLE_XML);

    // Act
    String actualXml = parser.evalNode(xpath).toString();

    // Assert
    assertEquals(expectedXml, actualXml);
  }

  private static Stream<Arguments> provideXPathsAndExpectedFormattedStrings() {
    String expectedUsersNode = """
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

    String expectedUserNode = """
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

    String expectedCarsNode = """
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

    return Stream.of(
        Arguments.of("/users", expectedUsersNode),
        Arguments.of("/users/user", expectedUserNode),
        Arguments.of("/users/user/cars", expectedCarsNode)
    );
  }

  /**
   * The toString() method provides a reasonable, but not perfect, pretty-print format.
   * This test verifies the current behavior, including known formatting quirks with mixed content.
   */
  @Test
  void shouldPreserveMixedContentAndFormattingInStringRepresentation() {
    // Arrange
    String complexXml = """
        <mapper>
          <select id='select' resultType='map'>
            select
            <var set='foo' value='bar' />
              ID,
              NAME
            from STUDENT
            <where>
              <if test="name != null">
                NAME = #{name}
              </if>
              and DISABLED = false
            </where>
            order by ID
            <choose>
              <when test='limit10'>
                limit 10
              </when>
              <otherwise>limit 20</otherwise>
            </choose>
          </select>
        </mapper>
        """;

    String expectedXml = """
        <select id="select" resultType="map">
          select
          <var set="foo" value="bar" />
          ID,
              NAME
            from STUDENT
          <where>
            <if test="name != null">
              NAME = #{name}
            </if>
            and DISABLED = false
          </where>
          order by ID
          <choose>
            <when test="limit10">
              limit 10
            </when>
            <otherwise>
              limit 20
            </otherwise>
          </choose>
        </select>
        """;

    XPathParser parser = new XPathParser(complexXml);
    XNode selectNode = parser.evalNode("/mapper/select");

    // Act
    String actualXml = selectNode.toString();

    // Assert
    assertEquals(expectedXml, actualXml);
  }

  @Test
  void shouldSubstituteVariablesInContentAndAttributes() {
    // Arrange
    Properties variables = new Properties();
    variables.put("x", "foo");
    variables.put("y", "bar");

    String xmlWithPlaceholders = "<root attr='${x}'>y = ${y}<sub attr='${y}'>x = ${x}</sub></root>";
    XPathParser parser = new XPathParser(xmlWithPlaceholders, false, variables);

    // The expected string has a trailing newline, which is preserved by adding a blank line at the end of the text block.
    String expectedXml = """
        <root attr="foo">
          y = bar
          <sub attr="bar">
            x = foo
          </sub>
        </root>
        """;

    XNode rootNode = parser.evalNode("/root");

    // Act
    String actualXml = rootNode.toString();

    // Assert
    assertEquals(expectedXml, actualXml);
  }
}