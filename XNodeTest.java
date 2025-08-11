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

class XNodeTest {
    // Test data constants
    private static final String USERS_XML = """
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
    
    private static final String MAPPER_XML = """
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

    @Test
    void toString_ShouldFormatUsersNodeWithProperIndentation() {
        XPathParser parser = new XPathParser(USERS_XML);
        XNode usersNode = parser.evalNode("/users");
        String actual = usersNode.toString();

        String expected = """
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
        
        assertEquals(expected, actual);
    }

    @Test
    void toString_ShouldFormatUserNodeWithProperIndentation() {
        XPathParser parser = new XPathParser(USERS_XML);
        XNode userNode = parser.evalNode("/users/user");
        String actual = userNode.toString();

        String expected = """
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
        
        assertEquals(expected, actual);
    }

    @Test
    void toString_ShouldFormatCarsNodeWithProperIndentation() {
        XPathParser parser = new XPathParser(USERS_XML);
        XNode carsNode = parser.evalNode("/users/user/cars");
        String actual = carsNode.toString();

        String expected = """
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
        
        assertEquals(expected, actual);
    }

    @Test
    void toString_ShouldFormatComplexSelectNodeWithProperIndentation() {
        XPathParser parser = new XPathParser(MAPPER_XML);
        XNode selectNode = parser.evalNode("/mapper/select");
        String actual = selectNode.toString();

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
        
        assertEquals(expected, actual);
    }

    @Test
    void toString_ShouldResolveVariablesAndFormatNode() {
        // Setup
        String xml = "<root attr='${x}'>y = ${y}<sub attr='${y}'>x = ${x}</sub></root>";
        Properties variables = new Properties();
        variables.put("x", "foo");
        variables.put("y", "bar");

        // Execute
        XPathParser parser = new XPathParser(xml, false, variables);
        XNode rootNode = parser.evalNode("/root");
        String actual = rootNode.toString();

        // Verify
        String expected = """
            <root attr="foo">
              y = bar
              <sub attr="bar">
                x = foo
              </sub>
            </root>
            """;
        
        assertEquals(expected, actual);
    }
}