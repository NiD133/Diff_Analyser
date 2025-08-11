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

import org.junit.jupiter.api.Test;

/**
 * Tests for XNode's toString() method functionality.
 * Verifies that XNode correctly formats XML content with proper indentation
 * and handles variable substitution.
 */
class XNodeTest {

  @Test
  void shouldFormatNestedXmlWithProperIndentation() {
    // Given: A complex nested XML structure with users, user details, and cars
    String inputXml = buildUserXmlWithCars();
    XPathParser parser = new XPathParser(inputXml);
    
    // When: Converting different node levels to string representation
    String actualUsersNode = parser.evalNode("/users").toString();
    String actualUserNode = parser.evalNode("/users/user").toString();
    String actualCarsNode = parser.evalNode("/users/user/cars").toString();

    // Then: Each node should be formatted with proper indentation
    assertEquals(getExpectedUsersNodeString(), actualUsersNode);
    assertEquals(getExpectedUserNodeString(), actualUserNode);
    assertEquals(getExpectedCarsNodeString(), actualCarsNode);
  }

  @Test
  void shouldFormatMyBatisMapperXmlCorrectly() {
    // Given: A MyBatis mapper XML with select statement and dynamic SQL elements
    String mapperXml = buildMyBatisMapperXml();
    String expectedFormattedOutput = getExpectedMapperOutput();

    // When: Parsing and converting the select node to string
    XPathParser parser = new XPathParser(mapperXml);
    XNode selectNode = parser.evalNode("/mapper/select");
    String actualOutput = selectNode.toString();

    // Then: The output should match expected formatting (with known formatting quirks)
    assertEquals(expectedFormattedOutput, actualOutput);
  }

  @Test
  void shouldSubstituteVariablesInXmlContent() {
    // Given: XML with variable placeholders and corresponding variable values
    String xmlWithVariables = "<root attr='${x}'>y = ${y}<sub attr='${y}'>x = ${x}</sub></root>";
    Properties variables = createTestVariables();
    String expectedWithSubstitution = "<root attr=\"foo\">\n  y = bar\n  <sub attr=\"bar\">\n    x = foo\n  </sub>\n</root>\n";
    
    // When: Parsing XML with variable substitution enabled
    XPathParser parser = new XPathParser(xmlWithVariables, false, variables);
    XNode rootNode = parser.evalNode("/root");
    String actualOutput = rootNode.toString();
    
    // Then: Variables should be properly substituted in both attributes and content
    assertEquals(expectedWithSubstitution, actualOutput);
  }

  // Helper methods to improve readability and reduce duplication

  private String buildUserXmlWithCars() {
    return "<users>" +
           "<user>" +
           "<id>100</id>" +
           "<name>Tom</name>" +
           "<age>30</age>" +
           "<cars>" +
           "<car index=\"1\">BMW</car>" +
           "<car index=\"2\">Audi</car>" +
           "<car index=\"3\">Benz</car>" +
           "</cars>" +
           "</user>" +
           "</users>";
  }

  private String buildMyBatisMapperXml() {
    return """
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
  }

  private Properties createTestVariables() {
    Properties variables = new Properties();
    variables.put("x", "foo");
    variables.put("y", "bar");
    return variables;
  }

  private String getExpectedUsersNodeString() {
    return """
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
  }

  private String getExpectedUserNodeString() {
    return """
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
  }

  private String getExpectedCarsNodeString() {
    return """
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
  }

  private String getExpectedMapperOutput() {
    // Note: Contains known formatting quirks with ID/NAME line breaks
    return """
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
  }
}