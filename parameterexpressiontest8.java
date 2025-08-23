package org.apache.ibatis.builder;

import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ParameterExpressionTestTest8 {

    @Test
    void simplePropertyWithManyAttributes() {
        Map<String, String> result = new ParameterExpression("id, attr1=val1, attr2=val2, attr3=val3");
        Assertions.assertEquals(4, result.size());
        Assertions.assertEquals("id", result.get("property"));
        Assertions.assertEquals("val1", result.get("attr1"));
        Assertions.assertEquals("val2", result.get("attr2"));
        Assertions.assertEquals("val3", result.get("attr3"));
    }
}
