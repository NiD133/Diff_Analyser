package org.apache.ibatis.parsing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Properties;

import org.junit.jupiter.api.Test;

class XNodeTest {

  @Test
  void testFormatXNodeToString() {
    // Arrange: Create an XML structure for testing
    String xml = """
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
        </users>
        """;
    XPathParser parser = new XPathParser(xml);

    // Act: Evaluate nodes to string representations
    String actualUsersNode = parser.evalNode("/users").toString();
    String actualUserNode = parser.evalNode("/users/user").toString();
    String actualCarsNode = parser.evalNode("/users/user/cars").toString();

    // Expected string representations
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

    // Assert: Verify that the actual strings match the expected strings
    assertEquals(expectedUsersNode, actualUsersNode);
    assertEquals(expectedUserNode, actualUserNode);
    assertEquals(expectedCarsNode, actualCarsNode);
  }

  @Test
  void testXNodeToString() {
    // Arrange: Define an XML with a SQL-like structure
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

    // Expected string representation
    String expectedSelectNode = """
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

    // Act: Parse the XML and evaluate the select node
    XPathParser parser = new XPathParser(xml);
    XNode selectNode = parser.evalNode("/mapper/select");

    // Assert: Verify that the actual string matches the expected string
    assertEquals(expectedSelectNode, selectNode.toString());
  }

  @Test
  void testXNodeToStringWithVariables() throws Exception {
    // Arrange: Define an XML with variables
    String xmlWithVariables = "<root attr='${x}'>y = ${y}<sub attr='${y}'>x = ${x}</sub></root>";
    Properties variables = new Properties();
    variables.put("x", "foo");
    variables.put("y", "bar");

    // Expected string representation after variable substitution
    String expected = """
        <root attr="foo">
          y = bar
          <sub attr="bar">
            x = foo
          </sub>
        </root>
        """;

    // Act: Parse the XML with variables
    XPathParser parser = new XPathParser(xmlWithVariables, false, variables);
    XNode rootNode = parser.evalNode("/root");

    // Assert: Verify that the actual string matches the expected string
    assertEquals(expected, rootNode.toString());
  }
}