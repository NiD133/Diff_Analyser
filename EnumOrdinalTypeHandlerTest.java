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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

class EnumOrdinalTypeHandlerTest extends BaseTypeHandlerTest {

    enum MyEnum {
        FIRST, SECOND
    }

    private static final TypeHandler<MyEnum> TYPE_HANDLER = new EnumOrdinalTypeHandler<>(MyEnum.class);
    private static final String COLUMN_NAME = "column";
    private static final int COLUMN_INDEX = 1;
    private static final int PARAMETER_INDEX = 1;
    private static final int FIRST_ORDINAL = MyEnum.FIRST.ordinal();

    /**
     * Verifies that a non-null enum parameter is correctly set
     * as its ordinal value in the prepared statement.
     */
    @Override
    @Test
    public void shouldSetParameter() throws Exception {
        TYPE_HANDLER.setParameter(ps, PARAMETER_INDEX, MyEnum.FIRST, null);
        verify(ps).setInt(PARAMETER_INDEX, FIRST_ORDINAL);
    }

    /**
     * Verifies that a null enum parameter is correctly set
     * as a JDBC NULL value using the specified JDBC type.
     */
    @Test
    void shouldSetNullParameterWithJdbcType() throws Exception {
        TYPE_HANDLER.setParameter(ps, PARAMETER_INDEX, null, JdbcType.VARCHAR);
        verify(ps).setNull(PARAMETER_INDEX, JdbcType.VARCHAR.TYPE_CODE);
    }

    /**
     * Verifies that an enum result is correctly retrieved
     * from a ResultSet by column name when the value is non-null.
     */
    @Override
    @Test
    public void shouldGetResultFromResultSetByName() throws Exception {
        when(rs.getInt(COLUMN_NAME)).thenReturn(FIRST_ORDINAL);
        when(rs.wasNull()).thenReturn(false);
        assertEquals(MyEnum.FIRST, TYPE_HANDLER.getResult(rs, COLUMN_NAME));
    }

    /**
     * Verifies that a null enum result is correctly returned
     * from a ResultSet by column name when the JDBC value is NULL.
     */
    @Override
    @Test
    public void shouldGetResultNullFromResultSetByName() throws Exception {
        when(rs.getInt(COLUMN_NAME)).thenReturn(0);
        when(rs.wasNull()).thenReturn(true);
        assertNull(TYPE_HANDLER.getResult(rs, COLUMN_NAME));
    }

    /**
     * Verifies that an enum result is correctly retrieved
     * from a ResultSet by column index when the value is non-null.
     */
    @Override
    @Test
    public void shouldGetResultFromResultSetByPosition() throws Exception {
        when(rs.getInt(COLUMN_INDEX)).thenReturn(FIRST_ORDINAL);
        when(rs.wasNull()).thenReturn(false);
        assertEquals(MyEnum.FIRST, TYPE_HANDLER.getResult(rs, COLUMN_INDEX));
    }

    /**
     * Verifies that a null enum result is correctly returned
     * from a ResultSet by column index when the JDBC value is NULL.
     */
    @Override
    @Test
    public void shouldGetResultNullFromResultSetByPosition() throws Exception {
        when(rs.getInt(COLUMN_INDEX)).thenReturn(0);
        when(rs.wasNull()).thenReturn(true);
        assertNull(TYPE_HANDLER.getResult(rs, COLUMN_INDEX));
    }

    /**
     * Verifies that an enum result is correctly retrieved
     * from a CallableStatement when the value is non-null.
     */
    @Override
    @Test
    public void shouldGetResultFromCallableStatement() throws Exception {
        when(cs.getInt(COLUMN_INDEX)).thenReturn(FIRST_ORDINAL);
        when(cs.wasNull()).thenReturn(false);
        assertEquals(MyEnum.FIRST, TYPE_HANDLER.getResult(cs, COLUMN_INDEX));
    }

    /**
     * Verifies that a null enum result is correctly returned
     * from a CallableStatement when the JDBC value is NULL.
     */
    @Override
    @Test
    public void shouldGetResultNullFromCallableStatement() throws Exception {
        when(cs.getInt(COLUMN_INDEX)).thenReturn(0);
        when(cs.wasNull()).thenReturn(true);
        assertNull(TYPE_HANDLER.getResult(cs, COLUMN_INDEX));
    }

}