package config.impl;

import config.ConfigCategory;
import config.ConfigEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * @author ZZZank
 */
public class ConfigTreePrinter {
    private final ConfigCategory category;
    private final String indent;
    private final int currentIndentLevel;
    private final Function<ConfigEntry<?>, String> reader;

    private final String formattedIndent;

    public ConfigTreePrinter(ConfigCategory category) {
        this(category, "    ", null);
    }

    public ConfigTreePrinter(ConfigCategory category, String indent, Function<ConfigEntry<?>, String> reader) {
        this(category, indent, 0, reader);
    }

    private ConfigTreePrinter(
        ConfigCategory category,
        String indent,
        int currentIndentLevel,
        Function<ConfigEntry<?>, String> reader
    ) {
        this.category = Objects.requireNonNull(category);
        this.indent = Objects.requireNonNull(indent);
        this.currentIndentLevel = currentIndentLevel;
        this.reader = reader == null ? ConfigEntry::name : reader;
        this.formattedIndent = indent.repeat(currentIndentLevel);
    }

    public List<String> print() {
        var result = new ArrayList<String>();
        for (var entry : category.get().values()) {
            result.add(formattedIndent + reader.apply(entry));
            if (entry.isCategory()) {
                var printer = new ConfigTreePrinter(entry.asCategory(), indent, currentIndentLevel + 1, reader);
                result.addAll(printer.print());
            }
        }
        return result;
    }
}
