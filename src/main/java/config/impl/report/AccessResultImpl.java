package config.impl.report;

import config.AccessResult;
import utils.Asser;

import java.util.function.Supplier;

/**
 * @author ZZZank
 */
public record AccessResultImpl<T>(
    T value,
    ResultType type,
    Supplier<String> messageProvider
) implements AccessResult<T> {

    public AccessResultImpl(T value, ResultType type, Supplier<String> messageProvider) {
        this.value = value;
        this.type = Asser.tNotNull(type, "result type");
        this.messageProvider = Asser.tNotNull(messageProvider, "message provider");
    }

    @Override
    public String message() {
        return this.messageProvider.get();
    }
}
