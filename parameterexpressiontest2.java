package org.apache.ibatis.builder;

import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ParameterExpressionTestTest2 {

    @Test
    void propertyWithSpacesInside() {
        Map<String, String> result = new ParameterExpression(" with spaces ");
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("with spaces", result.get("property"));
    }
}
