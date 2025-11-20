package config;

import config.test_obj.ManuallyBuiltConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import type_token.TypeToken;

import java.util.List;

/**
 * @author ZZZank
 */
public class FillEntryTypeTest {

    @Test
    public void test() {
        Assertions.assertEquals(int[].class, ManuallyBuiltConfig.INT_ARRAY.defaultType());
        Assertions.assertEquals(Integer.class, ManuallyBuiltConfig.INTEGER.defaultType());
        Assertions.assertEquals(Double.class, ManuallyBuiltConfig.MAX_DBL.defaultType());
        Assertions.assertEquals(new TypeToken<List<String>>() {}.unwrap(), ManuallyBuiltConfig.LIST_STR.defaultType());

        Assertions.assertEquals(new TypeToken<List<Integer>>() {}.unwrap(), ManuallyBuiltConfig.SubCategory.INT_LIST.defaultType());
    }
}
