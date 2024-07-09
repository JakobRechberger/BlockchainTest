import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SampleTests {
    @Test
    void testSubtraction() {
        int result = 5 - 3;
        assertEquals(2, result, "5 - 3 should equal 2");
    }

    @Test
    void testMultiplication() {
        int result = 2 * 3;
        assertEquals(6, result, "2 * 3 should equal 6");
    }
}
