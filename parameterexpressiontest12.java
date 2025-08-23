package org.apache.ibatis.builder;

import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ParameterExpressionTestTest12 {

    @Test
    void shouldIgnoreLeadingAndTrailingSpaces() {
        Map<String, String> result = new ParameterExpression(" id , jdbcType =  VARCHAR,  attr1 = val1 ,  attr2 = val2 ");
        Assertions.assertEquals(4, result.size());
        Assertions.assertEquals("id", result.get("property"));
        Assertions.assertEquals("VARCHAR", result.get("jdbcType"));
        Assertions.assertEquals("val1", result.get("attr1"));
        Assertions.assertEquals("val2", result.get("attr2"));
    }
}
