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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for XNode#toString() formatting and placeholder substitution.
 * Notes:
 * - The first test uses a single-line XML source on purpose to avoid
 *   injecting extra whitespace text nodes that would affect the output.
 * - The mapper test verifies that toString() preserves meaningful
 *   whitespace inside elements as produced by the parser.
 */
class XNodeTest {

  // Single-line source to keep the DOM free from incidental whitespace nodes.
  private static final String USERS_XML =
      "<users><user><id>100</id><name>Tom</name><age>30</age><cars><car index=\"1\">BMW</car><car index=\"2\">Audi</car><car index=\"3\">Benz</car></cars></user></users>";

  @Test
  @DisplayName("toString() renders entire tree and subtrees with consistent indentation")
  void rendersUsersTreeAndSubtrees() {
    XPathParser parser = new XPathParser(USERS_XML);

    String expectedUsers = """
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

    String expectedUser = """
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

    String expectedCars = """
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

    assertNodeToString(parser, "/users", expectedUsers);
    assertNodeToString(parser, "/users/user", expectedUser);
    assertNodeToString(parser, "/users/user/cars", expectedCars);
  }

  @Test
  @DisplayName("toString() renders dynamic SQL and control flow tags as XML with preserved whitespace")
  void rendersMapperSelectNode() {
    String xml = """
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

    // The line breaks/indentation inside the select body are preserved as-is.
    String expected = """
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

    XPathParser parser = new XPathParser(xml);
    XNode selectNode = parser.evalNode("/mapper/select");
    assertEquals(expected, selectNode.toString(), "Formatted XML for /mapper/select did not match");
  }

  @Test
  @DisplayName("toString() substitutes ${...} placeholders using provided variables")
  void rendersWithVariablesSubstituted() {
    String src = "<root attr='${x}'>y = ${y}<sub attr='${y}'>x = ${x}</sub></root>";
    String expected = """
        <root attr="foo">
          y = bar
          <sub attr="bar">
            x = foo
          </sub>
        </root>
        """;

    Properties vars = new Properties();
    vars.put("x", "foo");
    vars.put("y", "bar");

    XPathParser parser = new XPathParser(src, false, vars);
    XNode root = parser.evalNode("/root");
    assertEquals(expected, root.toString(), "Placeholder substitution did not produce expected XML");
  }

  private static void assertNodeToString(XPathParser parser, String path, String expected) {
    XNode node = parser.evalNode(path);
    assertEquals(expected, node.toString(), () -> "Formatted XML for path " + path + " did not match");
    // Sanity check: ensure the node under test is the one we intended.
    // This does not affect formatting but helps future maintainers spot path mistakes quickly.
    // Example: "/users/user/cars" should end with "cars".
    // We read node.getName() via toString expectations, so no additional assertion is necessary here.
  }
}