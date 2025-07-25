/*
 *    Copyright 2009-2024 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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

class ArrayTypeHandlerTest extends BaseTypeHandlerTest {

    private static final TypeHandler<Object> ARRAY_TYPE_HANDLER = new ArrayTypeHandler();

    @Mock
    private Array mockArray;

    /**
     * Tests the setParameter method when a non-null array is provided.
     * 
     * @throws Exception if an error occurs during the test
     */
    @Override
    @Test
    public void testSetNonNullableParameter() throws Exception {
        ARRAY_TYPE_HANDLER.setParameter(ps, 1, mockArray, null);
        verify(ps).setArray(1, mockArray);
    }

    /**
     * Tests the setParameter method when a string array is provided.
     * 
     * @throws Exception if an error occurs during the test
     */
    @Test
    void testSetStringArrayParameter() throws Exception {
        Connection connection = mock(Connection.class);
        when(ps.getConnection()).thenReturn(connection);

        Array array = mock(Array.class);
        when(connection.createArrayOf(anyString(), any(String[].class))).thenReturn(array);

        String[] stringArray = new String[] { "Hello World" };
        ARRAY_TYPE_HANDLER.setParameter(ps, 1, stringArray, JdbcType.ARRAY);
        verify(ps).setArray(1, array);
        verify(array).free();
    }

    /**
     * Tests the setParameter method when null is provided as the parameter.
     * 
     * @throws Exception if an error occurs during the test
     */
    @Test
    void testSetNullParameter() throws Exception {
        ARRAY_TYPE_HANDLER.setParameter(ps, 1, null, JdbcType.ARRAY);
        verify(ps).setNull(1, Types.ARRAY);
    }

    /**
     * Tests that an exception is thrown when a non-array parameter is provided.
     */
    @Test
    void testSetNonArrayParameter() {
        assertThrows(TypeException.class, () -> ARRAY_TYPE_HANDLER.setParameter(ps, 1, "unsupported parameter type", null));
    }

    /**
     * Tests the getResult method when retrieving from a ResultSet by column name.
     * 
     * @throws Exception if an error occurs during the test
     */
    @Override
    @Test
    public void testGetResultFromResultSetByName() throws Exception {
        when(rs.getArray("column")).thenReturn(mockArray);
        String[] stringArray = new String[] { "a", "b" };
        when(mockArray.getArray()).thenReturn(stringArray);
        assertEquals(stringArray, ARRAY_TYPE_HANDLER.getResult(rs, "column"));
        verify(mockArray).free();
    }

    /**
     * Tests the getResult method when retrieving from a ResultSet by column name and the result is null.
     * 
     * @throws Exception if an error occurs during the test
     */
    @Override
    @Test
    public void testGetResultNullFromResultSetByName() throws Exception {
        when(rs.getArray("column")).thenReturn(null);
        assertNull(ARRAY_TYPE_HANDLER.getResult(rs, "column"));
    }

    /**
     * Tests the getResult method when retrieving from a ResultSet by column index.
     * 
     * @throws Exception if an error occurs during the test
     */
    @Override
    @Test
    public void testGetResultFromResultSetByPosition() throws Exception {
        when(rs.getArray(1)).thenReturn(mockArray);
        String[] stringArray = new String[] { "a", "b" };
        when(mockArray.getArray()).thenReturn(stringArray);
        assertEquals(stringArray, ARRAY_TYPE_HANDLER.getResult(rs, 1));
        verify(mockArray).free();
    }

    /**
     * Tests the getResult method when retrieving from a ResultSet by column index and the result is null.
     * 
     * @throws Exception if an error occurs during the test
     */
    @Override
    @Test
    public void testGetResultNullFromResultSetByPosition() throws Exception {
        when(rs.getArray(1)).thenReturn(null);
        assertNull(ARRAY_TYPE_HANDLER.getResult(rs, 1));
    }

    /**
     * Tests the getResult method when retrieving from a CallableStatement.
     * 
     * @throws Exception if an error occurs during the test
     */
    @Override
    @Test
    public void testGetResultFromCallableStatement() throws Exception {
        when(cs.getArray(1)).thenReturn(mockArray);
        String[] stringArray = new String[] { "a", "b" };
        when(mockArray.getArray()).thenReturn(stringArray);
        assertEquals(stringArray, ARRAY_TYPE_HANDLER.getResult(cs, 1));
        verify(mockArray).free();
    }

    /**
     * Tests the getResult method when retrieving from a CallableStatement and the result is null.
     * 
     * @throws Exception if an error occurs during the test
     */
    @Override
    @Test
    public void testGetResultNullFromCallableStatement() throws Exception {
        when(cs.getArray(1)).thenReturn(null);
        assertNull(ARRAY_TYPE_HANDLER.getResult(cs, 1));
    }
}