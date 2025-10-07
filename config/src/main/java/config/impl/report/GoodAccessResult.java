package config.impl.report;

import config.AccessResult;

import java.util.Optional;

/**
 * @author ZZZank
 */
public record GoodAccessResult<T>(
    T value
) implements AccessResult<T> {

    @Override
    public ResultType type() {
        return ResultType.GOOD;
    }

    @Override
    public String message() {
        return null;
    }

    @Override
    public Optional<String> messageOptional() {
        return Optional.empty();
    }
}
