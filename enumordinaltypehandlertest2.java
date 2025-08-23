package org.apache.ibatis.type;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;

public class EnumOrdinalTypeHandlerTestTest2 extends BaseTypeHandlerTest {

    private static final TypeHandler<MyEnum> TYPE_HANDLER = new EnumOrdinalTypeHandler<>(MyEnum.class);

    @Test
    void shouldSetNullParameter() throws Exception {
        TYPE_HANDLER.setParameter(ps, 1, null, JdbcType.VARCHAR);
        verify(ps).setNull(1, JdbcType.VARCHAR.TYPE_CODE);
    }
}
