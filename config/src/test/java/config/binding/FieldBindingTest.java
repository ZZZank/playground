package config.binding;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * @author ZZZank
 */
public class FieldBindingTest {

    public int intValue = -1;

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, Integer.MAX_VALUE, Integer.MIN_VALUE})
    public void testInt(int value) throws Exception {
        var field = FieldBindingTest.class.getField("intValue");
        var binding = new FieldBinding<Integer>(field, this);

        Assertions.assertEquals(int.class, binding.getDefaultType());
        Assertions.assertEquals(-1, binding.getDefault());
        Assertions.assertEquals(-1, binding.get());
        binding.set(value);
        Assertions.assertEquals(value, binding.get());
        binding.set(-1);
    }

    public String stringValue = "default";

    @ParameterizedTest
    @ValueSource(strings = {"", "1", "null", "wow", "hello, world", "$*(_@-"})
    public void testString(String value) throws Exception {
        var field = FieldBindingTest.class.getField("stringValue");
        var binding = new FieldBinding<String>(field, this);

        Assertions.assertEquals(String.class, binding.getDefaultType());
        Assertions.assertEquals("default", binding.getDefault());

        Assertions.assertEquals("default", binding.get());
        binding.set(value);
        Assertions.assertEquals(value, binding.get());
        binding.set("default");
    }
}
