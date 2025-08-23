package org.apache.ibatis.parsing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Properties;
import org.junit.jupiter.api.Test;

public class XNodeTestTest2 {

    @Test
    void xNodeToString() {
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
        // a little bit ugly with id/name break, but not a blocker
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
        assertEquals(expected, selectNode.toString());
    }
}
