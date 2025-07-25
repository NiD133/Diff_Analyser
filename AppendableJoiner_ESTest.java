import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppendableJoinerTest {
    
    @Test
    public void joinNumbers_shouldReturnCorrectResult() {
        String[] numbers = {"1", "2", "3"};
        assertEquals("[1, 2, 3]", new AppendableJoiner().join(numbers));
    }
    
    @Test
    public void joinNames_shouldReturnCorrectResult() {
        String[] names = {"John", "Jane", "Bob"};
        assertEquals("[John, Jane, Bob]", new AppendableJoiner().join(names));
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"1", "2", "3"})
    public void join_shouldReturnCorrectResultForEachInput(String input) {
        String[] numbers = {input};
        assertEquals("[" + input + "]", new AppendableJoiner().join(numbers));
    }
    
}