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

public class ArrayTypeHandlerTestTest9 extends BaseTypeHandlerTest {

    private static final TypeHandler<Object> TYPE_HANDLER = new ArrayTypeHandler();

    @Mock
    Array mockArray;

    @Override
    @Test
    public void shouldGetResultFromCallableStatement() throws Exception {
        when(cs.getArray(1)).thenReturn(mockArray);
        String[] stringArray = { "a", "b" };
        when(mockArray.getArray()).thenReturn(stringArray);
        assertEquals(stringArray, TYPE_HANDLER.getResult(cs, 1));
        verify(mockArray).free();
    }
}