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

public class ArrayTypeHandlerTestTest1 extends BaseTypeHandlerTest {

    private static final TypeHandler<Object> TYPE_HANDLER = new ArrayTypeHandler();

    @Mock
    Array mockArray;

    @Override
    @Test
    public void shouldSetParameter() throws Exception {
        TYPE_HANDLER.setParameter(ps, 1, mockArray, null);
        verify(ps).setArray(1, mockArray);
    }
}
