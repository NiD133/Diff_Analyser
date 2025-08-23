package org.apache.ibatis.type;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;

public class EnumOrdinalTypeHandlerTestTest8 extends BaseTypeHandlerTest {

    private static final TypeHandler<MyEnum> TYPE_HANDLER = new EnumOrdinalTypeHandler<>(MyEnum.class);

    @Override
    @Test
    public void shouldGetResultNullFromCallableStatement() throws Exception {
        when(cs.getInt(1)).thenReturn(0);
        when(cs.wasNull()).thenReturn(true);
        assertNull(TYPE_HANDLER.getResult(cs, 1));
    }
}
