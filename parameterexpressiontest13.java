package org.apache.ibatis.builder;

import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ParameterExpressionTestTest13 {

    @Test
    void invalidOldJdbcTypeFormat() {
        try {
            new ParameterExpression("id:");
            Assertions.fail();
        } catch (BuilderException e) {
            Assertions.assertTrue(e.getMessage().contains("Parsing error in {id:} in position 3"));
        }
    }
}
