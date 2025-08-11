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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test suite for {@link ArrayTypeHandler}.
 * This version focuses on clarity, descriptive naming, and standard testing practices.
 */
@RunWith(MockitoJUnitRunner.class)
public class ArrayTypeHandlerTest {

    private ArrayTypeHandler arrayTypeHandler;

    @Mock
    private PreparedStatement mockPreparedStatement;
    @Mock
    private CallableStatement mockCallableStatement;
    @Mock
    private ResultSet mockResultSet;
    @Mock
    private Array mockSqlArray;

    @Before
    public void setUp() {
        arrayTypeHandler = new ArrayTypeHandler();
    }

    //region setNonNullParameter Tests
    @Test
    public void setNonNullParameter_shouldThrowException_whenParameterIsNotSqlOrJavaArray() {
        // Arrange
        Time unsupportedParameter = new Time(226L);
        JdbcType jdbcType = JdbcType.BINARY;

        // Act & Assert
        try {
            arrayTypeHandler.setNonNullParameter(mockPreparedStatement, 1, unsupportedParameter, jdbcType);
            fail("Should have thrown a RuntimeException for an unsupported parameter type.");
        } catch (Exception e) {
            // The handler should reject types that are not SQL or Java arrays.
            assertEquals("ArrayType Handler requires SQL array or java array parameter and does not support type class java.sql.Time", e.getMessage());
        }
    }

    @Test(expected = NullPointerException.class)
    public void setNonNullParameter_shouldThrowNullPointerException_whenPreparedStatementIsNull() throws SQLException {
        // Arrange
        Object[] parameter = new String[]{"test"};

        // Act
        arrayTypeHandler.setNonNullParameter(null, 1, parameter, JdbcType.ARRAY);
    }
    //endregion

    //region getNullableResult Tests
    @Test
    public void getNullableResultFromCallableStatement_shouldReturnExtractedArrayValue() throws SQLException {
        // Arrange
        int columnIndex = 1;
        String[] expectedArray = {"value1", "value2"};
        when(mockCallableStatement.getArray(columnIndex)).thenReturn(mockSqlArray);
        when(mockSqlArray.getArray()).thenReturn(expectedArray);

        // Act
        Object result = arrayTypeHandler.getNullableResult(mockCallableStatement, columnIndex);

        // Assert
        assertSame(expectedArray, result);
    }

    @Test
    public void getNullableResultFromCallableStatement_shouldReturnNull_whenArrayFromStatementIsNull() throws SQLException {
        // Arrange
        int columnIndex = 1;
        when(mockCallableStatement.getArray(columnIndex)).thenReturn(null);

        // Act
        Object result = arrayTypeHandler.getNullableResult(mockCallableStatement, columnIndex);

        // Assert
        assertNull(result);
    }

    @Test(expected = NullPointerException.class)
    public void getNullableResultFromCallableStatement_shouldThrowNullPointerException_whenStatementIsNull() throws SQLException {
        // Act
        arrayTypeHandler.getNullableResult((CallableStatement) null, 1);
    }

    @Test(expected = NullPointerException.class)
    public void getNullableResultFromResultSetByName_shouldThrowNullPointerException_whenResultSetIsNull() throws SQLException {
        // Act
        arrayTypeHandler.getNullableResult((ResultSet) null, "columnName");
    }

    @Test(expected = NullPointerException.class)
    public void getNullableResultFromResultSetByIndex_shouldThrowNullPointerException_whenResultSetIsNull() throws SQLException {
        // Act
        arrayTypeHandler.getNullableResult((ResultSet) null, 1);
    }
    //endregion

    //region resolveTypeName Tests
    @Test
    public void resolveTypeName_shouldReturnTimestamp_forUtilDate() {
        // Arrange
        Class<?> type = Date.class;
        String expectedTypeName = "TIMESTAMP";

        // Act
        String actualTypeName = arrayTypeHandler.resolveTypeName(type);

        // Assert
        assertEquals(expectedTypeName, actualTypeName);
    }

    @Test(expected = NullPointerException.class)
    public void resolveTypeName_shouldThrowNullPointerException_whenClassIsNull() {
        // Act
        arrayTypeHandler.resolveTypeName(null);
    }
    //endregion

    //region extractArray Tests (protected method)
    @Test
    public void extractArray_shouldReturnTheResultOfGetArray_whenArrayIsNotNull() throws SQLException {
        // Arrange
        Object[] expectedUnderlyingArray = {"item1", 2, new Timestamp(0)};
        when(mockSqlArray.getArray()).thenReturn(expectedUnderlyingArray);

        // Act
        Object result = arrayTypeHandler.extractArray(mockSqlArray);

        // Assert
        assertSame(expectedUnderlyingArray, result);
    }

    @Test
    public void extractArray_shouldReturnNull_whenGivenNullArray() throws SQLException {
        // Act
        Object result = arrayTypeHandler.extractArray(null);

        // Assert
        assertNull(result);
    }
    //endregion
}