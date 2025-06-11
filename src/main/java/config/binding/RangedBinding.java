package config.binding;

import org.jetbrains.annotations.NotNull;
import asser.Asser;
import config.report.AccessResult;
import config.report.BuiltinResults;

/**
 * @author ZZZank
 */
public class RangedBinding<T extends Comparable<T>> extends DefaultBinding<T> {

    private final T min;
    private final T max;

    public RangedBinding(@NotNull T defaultValue, @NotNull Class<T> defaultType, @NotNull String name, @NotNull T min, @NotNull T max) {
        super(defaultValue, defaultType, name);
        this.min = Asser.tNotNull(min, "min");
        this.max = Asser.tNotNull(max, "max");
    }

    @Override
    public AccessResult<T> validate(T value) {
        var superReport = super.validate(value);
        if (superReport.hasMessage()) {
            return superReport;
        }
        if (value.compareTo(min) < 0 || value.compareTo(max) > 0) {
            return BuiltinResults.outOfRangeError(name, value, min, max);
        }
        return AccessResult.none();
    }
}
