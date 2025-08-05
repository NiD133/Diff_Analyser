import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.builder.ParameterExpression;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link ParameterExpression} parser.
 *
 * The tests are structured to cover valid syntax for properties and expressions,
 * as well as various invalid inputs that should result in exceptions.
 */
@DisplayName("ParameterExpression Parser")
class ParameterExpressionTest {

    @Nested
    @DisplayName("when parsing simple properties")
    class SimplePropertyParsing {

        @Test
        @DisplayName("should parse a simple property name")
        void shouldParseSimpleProperty() {
            // Given
            String input = "username";

            // When
            Map<String, String> result = new ParameterExpression(input);

            // Then
            assertAll(
                () -> assertEquals(1, result.size()),
                () -> assertEquals("username", result.get("property"))
            );
        }

        @Test
        @DisplayName("should parse a property with a legacy-style jdbcType")
        void shouldParsePropertyWithOldStyleJdbcType() {
            // Given
            String input = "age:NUMERIC";

            // When
            Map<String, String> result = new ParameterExpression(input);

            // Then
            assertAll(
                () -> assertEquals(2, result.size()),
                () -> assertEquals("age", result.get("property")),
                () -> assertEquals("NUMERIC", result.get("jdbcType"))
            );
        }
    }

    @Nested
    @DisplayName("when parsing properties with attributes")
    class PropertyWithAttributesParsing {

        @Test
        @DisplayName("should parse a property with a single attribute")
        void shouldParsePropertyWithSingleAttribute() {
            // Given
            String input = "user.name,javaType=String";

            // When
            Map<String, String> result = new ParameterExpression(input);

            // Then
            assertAll(
                () -> assertEquals(2, result.size()),
                () -> assertEquals("user.name", result.get("property")),
                () -> assertEquals("String", result.get("javaType"))
            );
        }

        @Test
        @DisplayName("should parse a property with multiple attributes")
        void shouldParsePropertyWithMultipleAttributes() {
            // Given
            String input = "score,javaType=int,jdbcType=NUMERIC";

            // When
            Map<String, String> result = new ParameterExpression(input);

            // Then
            assertAll(
                () -> assertEquals(3, result.size()),
                () -> assertEquals("score", result.get("property")),
                () -> assertEquals("int", result.get("javaType")),
                () -> assertEquals("NUMERIC", result.get("jdbcType"))
            );
        }

        @Test
        @DisplayName("should correctly handle whitespace around properties and attributes")
        void shouldHandleWhitespace() {
            // Given
            String input = "  user.name ,  javaType = String , jdbcType = VARCHAR  ";

            // When
            Map<String, String> result = new ParameterExpression(input);

            // Then
            assertAll(
                () -> assertEquals(3, result.size()),
                () -> assertEquals("user.name", result.get("property")),
                () -> assertEquals("String", result.get("javaType")),
                () -> assertEquals("VARCHAR", result.get("jdbcType"))
            );
        }
    }

    @Nested
    @DisplayName("when parsing parenthesized expressions")
    class ExpressionParsing {

        @Test
        @DisplayName("should parse a simple parenthesized expression")
        void shouldParseSimpleExpression() {
            // Given
            String input = "(user.age > 18)";

            // When
            Map<String, String> result = new ParameterExpression(input);

            // Then
            assertAll(
                () -> assertEquals(1, result.size()),
                () -> assertEquals("user.age > 18", result.get("expression"))
            );
        }

        @Test
        @DisplayName("should parse an expression with attributes")
        void shouldParseExpressionWithAttributes() {
            // Given
            String input = "(user.name != null),javaType=boolean,jdbcType=BOOLEAN";

            // When
            Map<String, String> result = new ParameterExpression(input);

            // Then
            assertAll(
                () -> assertEquals(3, result.size()),
                () -> assertEquals("user.name != null", result.get("expression")),
                () -> assertEquals("boolean", result.get("javaType")),
                () -> assertEquals("BOOLEAN", result.get("jdbcType"))
            );
        }
    }

    @Nested
    @DisplayName("when parsing invalid input")
    class InvalidInputParsing {

        @Test
        @DisplayName("should throw BuilderException for null input")
        void shouldThrowExceptionForNullInput() {
            // Given
            String nullInput = null;

            // When & Then
            BuilderException exception = assertThrows(BuilderException.class, () -> {
                new ParameterExpression(nullInput);
            });
            assertEquals("Parsing error in {null}", exception.getMessage());
        }

        @Test
        @DisplayName("should throw BuilderException for an unclosed expression")
        void shouldThrowExceptionForUnclosedExpression() {
            // Given
            String invalidInput = "(user.id";

            // When & Then
            BuilderException exception = assertThrows(BuilderException.class, () -> {
                new ParameterExpression(invalidInput);
            });
            assertTrue(exception.getMessage().startsWith("Parsing error in {(user.id}"));
        }

        @Test
        @DisplayName("should throw BuilderException for text after a closed expression without a separator")
        void shouldThrowExceptionForTextAfterExpression() {
            // Given
            String invalidInput = "(user.id)someText";

            // When & Then
            BuilderException exception = assertThrows(BuilderException.class, () -> {
                new ParameterExpression(invalidInput);
            });
            assertEquals("Parsing error in {(user.id)someText} in position 9", exception.getMessage());
        }

        @Test
        @DisplayName("should throw BuilderException for a jdbcType colon without a type name")
        void shouldThrowExceptionForJdbcTypeWithoutName() {
            // Given
            String invalidInput = "property:";

            // When & Then
            BuilderException exception = assertThrows(BuilderException.class, () -> {
                new ParameterExpression(invalidInput);
            });
            assertEquals("Parsing error in {property:}. A JDBC type was specified as a colon ':' but no type name was provided.", exception.getMessage());
        }

        @Test
        @DisplayName("should throw BuilderException for an attribute without an equals sign")
        void shouldThrowExceptionForAttributeWithoutEquals() {
            // Given
            String invalidInput = "property,javaType";

            // When & Then
            BuilderException exception = assertThrows(BuilderException.class, () -> {
                new ParameterExpression(invalidInput);
            });
            assertEquals("Parsing error in {property,javaType}.  Attribute 'javaType' must be followed by an equals sign.", exception.getMessage());
        }
    }
}