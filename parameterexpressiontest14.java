package org.apache.ibatis.builder;

import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ParameterExpressionTestTest14 {

    @Test
    void invalidJdbcTypeOptUsingExpression() {
        try {
            new ParameterExpression("(expression)+");
            Assertions.fail();
        } catch (BuilderException e) {
            Assertions.assertTrue(e.getMessage().contains("Parsing error in {(expression)+} in position 12"));
        }
    }
}
