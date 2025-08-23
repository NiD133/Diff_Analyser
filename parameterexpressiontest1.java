package org.apache.ibatis.builder;

import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ParameterExpressionTestTest1 {

    @Test
    void simpleProperty() {
        Map<String, String> result = new ParameterExpression("id");
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("id", result.get("property"));
    }
}
