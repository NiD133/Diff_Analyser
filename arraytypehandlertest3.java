package org.apache.ibatis.type;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.sql.Array;
import java.sql.Connection;
import java.sql.Types;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class ArrayTypeHandlerTestTest3 extends BaseTypeHandlerTest {

    private static final TypeHandler<Object> TYPE_HANDLER = new ArrayTypeHandler();

    @Mock
    Array mockArray;

    @Test
    void shouldSetNullParameter() throws Exception {
        TYPE_HANDLER.setParameter(ps, 1, null, JdbcType.ARRAY);
        verify(ps).setNull(1, Types.ARRAY);
    }
}
