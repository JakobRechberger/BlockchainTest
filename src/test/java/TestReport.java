import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestReport {
    @Test
    void UnitTestTrue(){
        int x = 0;
        int y = 0;
        for(int i = 0; i < 100; i++){
            y = i;
            for(int j = 0; j < 100; j++){
                x = j;
            }
        }
        assertEquals(x, 99);
        assertEquals(y, 99);
    }
    @Test
    void UnitTestFalse(){
        int x = 0;
        int y = 0;
        for(int i = 0; i < 100; i++){
            y = i;
            for(int j = 0; j < 100; j++){
                x = j;
            }
        }
        assertEquals(x, 10);
        assertEquals(y, 10);
    }
    @Test
    void testDivisionByZero() {
        Exception exception = assertThrows(ArithmeticException.class, () -> {
            int result = 1 / 0;
        });
        assertEquals("/ by zero", exception.getMessage());
    }
    @Test
    void testNull() {
        Object obj = null;
        assertNull(obj, "Object should be null");
    }
}
