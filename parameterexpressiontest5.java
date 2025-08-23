package org.apache.ibatis.builder;

import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ParameterExpressionTestTest5 {

    @Test
    void expressionWithOldStyleJdbcType() {
        Map<String, String> result = new ParameterExpression("(id.toString()):VARCHAR");
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("id.toString()", result.get("expression"));
        Assertions.assertEquals("VARCHAR", result.get("jdbcType"));
    }
}
