package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import type_token.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * @author ZZZank
 */
public class TypeTokenTest {

    @Test
    public void testClass() {
        var token = new TypeToken<String>(){}.unwrap();
        Assertions.assertInstanceOf(Class.class, token);
        Assertions.assertEquals(String.class, token);

        token = new TypeToken<List>(){}.unwrap();
        Assertions.assertInstanceOf(Class.class, token);
        Assertions.assertEquals(List.class, token);
    }

    @Test
    public void testArrayClass() {
        TypeToken<?> token = new TypeToken<String[]>(){};
        Assertions.assertInstanceOf(Class.class, token.unwrap());
        Assertions.assertEquals(String[].class, token.unwrap());
        Assertions.assertEquals(String.class, ((Class<?>) token.unwrap()).componentType());

        token = new TypeToken<int[]>(){};
        Assertions.assertInstanceOf(Class.class, token.unwrap());
        Assertions.assertEquals(int[].class, token.unwrap());
        Assertions.assertEquals(int.class, ((Class<?>) token.unwrap()).componentType());

        token = new TypeToken<List[]>(){};
        Assertions.assertInstanceOf(Class.class, token.unwrap());
        Assertions.assertEquals(List[].class, token.unwrap());
        Assertions.assertEquals(List.class, ((Class<?>) token.unwrap()).componentType());
    }

    @Test
    public void testParam() {
        var type = new TypeToken<List<String>>() {}.unwrap();
        var parameterized = (ParameterizedType) type;
        Assertions.assertEquals(List.class, parameterized.getRawType());
        Assertions.assertArrayEquals(new Object[]{String.class}, parameterized.getActualTypeArguments());
    }
}
