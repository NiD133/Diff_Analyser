package org.apache.ibatis.type;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;

public class EnumOrdinalTypeHandlerTestTest3 extends BaseTypeHandlerTest {

    private static final TypeHandler<MyEnum> TYPE_HANDLER = new EnumOrdinalTypeHandler<>(MyEnum.class);

    @Override
    @Test
    public void shouldGetResultFromResultSetByName() throws Exception {
        when(rs.getInt("column")).thenReturn(0);
        when(rs.wasNull()).thenReturn(false);
        assertEquals(MyEnum.ONE, TYPE_HANDLER.getResult(rs, "column"));
    }
}
