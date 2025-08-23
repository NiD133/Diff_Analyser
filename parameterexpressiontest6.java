package org.apache.ibatis.builder;

import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ParameterExpressionTestTest6 {

    @Test
    void simplePropertyWithOneAttribute() {
        Map<String, String> result = new ParameterExpression("id,name=value");
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("id", result.get("property"));
        Assertions.assertEquals("value", result.get("name"));
    }
}
