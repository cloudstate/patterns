package org.cloudstate.condition.lib;

import org.jspecify.annotations.NullMarked;

import java.util.function.Supplier;

@NullMarked
public final class Condition implements Supplier<Boolean> {

    private final Supplier<Boolean> value;

    private Condition(final Supplier<Boolean> value) {
        this.value = value;
    }

    public static Condition condition(final Supplier<Boolean> value) {
        return new Condition(value);
    }

    public static Condition not(final Condition condition) {
        return condition(() -> !condition.value.get());
    }

    public Condition and(final Condition condition) {
        return condition(() -> this.value.get() && condition.value.get());
    }

    public Condition or(final Condition condition) {
        return condition(() -> this.value.get() || condition.value.get());
    }

    @Override
    public Boolean get() {
        return value.get();
    }

}
