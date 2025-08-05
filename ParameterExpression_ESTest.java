package org.apache.ibatis.builder;

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.ibatis.builder.ParameterExpression;

/**
 * Test suite for ParameterExpression class which parses MyBatis parameter expressions.
 * 
 * Parameter expressions follow the grammar:
 * - propertyName [: jdbcType] [, attribute=value]*
 * - (expression) [: jdbcType] [, attribute=value]*
 */
public class ParameterExpressionTest {

    @Test
    public void shouldParsePropertyNameWithJdbcTypeAndAttribute() {
        // Given a parameter expression with property name, JDBC type, and attribute
        String expression = "name:VARCHAR,length=50";
        
        // When parsing the expression
        ParameterExpression parameterExpression = new ParameterExpression(expression);
        
        // Then it should contain 2 parsed elements (property + attribute)
        assertEquals("Should parse property name and attribute", 2, parameterExpression.size());
    }

    @Test
    public void shouldThrowRuntimeExceptionForInvalidExpressionSyntax() {
        // Given an invalid expression with malformed syntax
        String invalidExpression = "(name)invalid";
        
        // When parsing the invalid expression
        // Then it should throw RuntimeException with parsing error details
        try {
            new ParameterExpression(invalidExpression);
            fail("Should throw RuntimeException for invalid expression syntax");
        } catch (RuntimeException e) {
            assertTrue("Should contain parsing error message", 
                      e.getMessage().contains("Parsing error"));
            assertTrue("Should indicate position of error", 
                      e.getMessage().contains("position"));
        }
    }

    @Test
    public void shouldThrowStringIndexOutOfBoundsForMalformedBrackets() {
        // Given an expression with malformed bracket structure
        String malformedExpression = " ([invalid";
        
        // When parsing the malformed expression
        // Then it should throw StringIndexOutOfBoundsException
        try {
            new ParameterExpression(malformedExpression);
            fail("Should throw StringIndexOutOfBoundsException for malformed brackets");
        } catch (StringIndexOutOfBoundsException e) {
            // Expected exception for malformed bracket structure
        }
    }

    @Test
    public void shouldParseSimplePropertyName() {
        // Given a simple property name expression
        String simpleProperty = "userName";
        
        // When parsing the expression
        ParameterExpression parameterExpression = new ParameterExpression(simpleProperty);
        
        // Then it should contain 1 parsed element
        assertEquals("Should parse simple property name", 1, parameterExpression.size());
    }

    @Test
    public void shouldThrowNullPointerExceptionForNullInput() {
        // Given a null expression
        String nullExpression = null;
        
        // When parsing null expression
        // Then it should throw NullPointerException
        try {
            new ParameterExpression(nullExpression);
            fail("Should throw NullPointerException for null input");
        } catch (NullPointerException e) {
            // Expected exception for null input
        }
    }

    @Test
    public void shouldThrowStringIndexOutOfBoundsForInvalidBracketSequence() {
        // Given an expression starting with closing bracket
        String invalidBracketSequence = "], invalid";
        
        // When parsing the invalid bracket sequence
        // Then it should throw StringIndexOutOfBoundsException
        try {
            new ParameterExpression(invalidBracketSequence);
            fail("Should throw StringIndexOutOfBoundsException for invalid bracket sequence");
        } catch (StringIndexOutOfBoundsException e) {
            // Expected exception for invalid bracket sequence
        }
    }

    @Test
    public void shouldParsePropertyWithMultipleAttributes() {
        // Given a property with multiple comma-separated attributes
        String propertyWithAttributes = "userId,type=INTEGER,nullable=false";
        
        // When parsing the expression
        ParameterExpression parameterExpression = new ParameterExpression(propertyWithAttributes);
        
        // Then it should contain 2 parsed elements (property + attributes)
        assertEquals("Should parse property with multiple attributes", 2, parameterExpression.size());
    }

    @Test
    public void shouldThrowRuntimeExceptionForUnterminatedExpression() {
        // Given an expression that is not properly terminated
        String unterminatedExpression = "property:VARCHAR ";
        
        // When parsing the unterminated expression
        // Then it should throw RuntimeException
        try {
            new ParameterExpression(unterminatedExpression);
            fail("Should throw RuntimeException for unterminated expression");
        } catch (RuntimeException e) {
            assertTrue("Should contain parsing error message", 
                      e.getMessage().contains("Parsing error"));
        }
    }

    @Test
    public void shouldParsePropertyWithJdbcType() {
        // Given a property with JDBC type specification
        String propertyWithJdbcType = "email:VARCHAR";
        
        // When parsing the expression
        ParameterExpression parameterExpression = new ParameterExpression(propertyWithJdbcType);
        
        // Then it should contain 2 parsed elements (property + JDBC type)
        assertEquals("Should parse property with JDBC type", 2, parameterExpression.size());
    }

    @Test
    public void shouldThrowStringIndexOutOfBoundsForIncompleteParentheses() {
        // Given an expression with incomplete parentheses
        String incompleteParentheses = "( property incomplete";
        
        // When parsing the incomplete expression
        // Then it should throw StringIndexOutOfBoundsException
        try {
            new ParameterExpression(incompleteParentheses);
            fail("Should throw StringIndexOutOfBoundsException for incomplete parentheses");
        } catch (StringIndexOutOfBoundsException e) {
            // Expected exception for incomplete parentheses
        }
    }

    @Test
    public void shouldParseValidParenthesizedExpression() {
        // Given a valid parenthesized expression
        String validExpression = "(user.id)";
        
        // When parsing the expression
        ParameterExpression parameterExpression = new ParameterExpression(validExpression);
        
        // Then it should contain 1 parsed element
        assertEquals("Should parse valid parenthesized expression", 1, parameterExpression.size());
    }

    @Test
    public void shouldThrowRuntimeExceptionForInvalidCharacterAfterParentheses() {
        // Given an expression with invalid character after parentheses
        String invalidAfterParentheses = "(id)$";
        
        // When parsing the invalid expression
        // Then it should throw RuntimeException
        try {
            new ParameterExpression(invalidAfterParentheses);
            fail("Should throw RuntimeException for invalid character after parentheses");
        } catch (RuntimeException e) {
            assertTrue("Should contain parsing error message", 
                      e.getMessage().contains("Parsing error"));
        }
    }

    @Test
    public void shouldParsePropertyWithAttributeAssignment() {
        // Given a property with attribute assignment
        String propertyWithAssignment = " status,default=ACTIVE";
        
        // When parsing the expression
        ParameterExpression parameterExpression = new ParameterExpression(propertyWithAssignment);
        
        // Then it should contain 2 parsed elements (property + attribute)
        assertEquals("Should parse property with attribute assignment", 2, parameterExpression.size());
    }
}