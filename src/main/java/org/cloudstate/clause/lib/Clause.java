package org.cloudstate.clause.lib;

import java.util.function.Supplier;

public final class Clause implements Supplier<Boolean> {

	private final Supplier<Boolean> value;

	private Clause(final Supplier<Boolean> value) {
		this.value = value;
	}

	public static Clause clause(final Supplier<Boolean> value) {
		return new Clause(value);
	}

	public Clause and(final Clause clause) {
		return clause(() -> this.value.get() && clause.value.get());
	}

	public Clause or(final Clause clause) {
		return clause(() -> this.value.get() || clause.value.get());
	}

	public static Clause not(final Clause clause) {
		return clause(() -> !clause.value.get());
	}

	@Override
	public Boolean get() {
		return value.get();
	}

}
