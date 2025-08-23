package org.apache.ibatis.builder;

import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ParameterExpressionTestTest3 {

    @Test
    void simplePropertyWithOldStyleJdbcType() {
        Map<String, String> result = new ParameterExpression("id:VARCHAR");
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("id", result.get("property"));
        Assertions.assertEquals("VARCHAR", result.get("jdbcType"));
    }
}