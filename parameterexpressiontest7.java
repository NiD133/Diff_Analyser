package org.apache.ibatis.builder;

import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ParameterExpressionTestTest7 {

    @Test
    void expressionWithOneAttribute() {
        Map<String, String> result = new ParameterExpression("(id.toString()),name=value");
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("id.toString()", result.get("expression"));
        Assertions.assertEquals("value", result.get("name"));
    }
}
