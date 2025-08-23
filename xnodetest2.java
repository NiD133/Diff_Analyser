package org.apache.ibatis.parsing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link XNode#toString()} method.
 */
@DisplayName("XNode toString()")
class XNodeTest {

    /**
     * Verifies that the toString() method correctly serializes an XNode
     * and its children back into a well-formatted XML string. This includes
     * normalizing attribute quotes and adjusting element indentation.
     */
    @Test
    @DisplayName("should return correctly formatted string representation of a node")
    void shouldReturnCorrectlyFormattedStringRepresentationOfNode() {
        // Arrange
        // The input XML uses single quotes for attributes and has some inconsistent formatting.
        String inputXml = """
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

        // The expected output should have normalized formatting:
        // - Double quotes for all attributes.
        // - Consistent indentation for all elements and text nodes.
        String expectedXmlString = """
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

        XPathParser parser = new XPathParser(inputXml);

        // Act
        XNode selectNode = parser.evalNode("/mapper/select");
        String actualXmlString = selectNode.toString();

        // Assert
        assertEquals(expectedXmlString, actualXmlString);
    }
}