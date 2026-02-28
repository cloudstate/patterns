package org.cloudstate.clause.lib;

import static java.util.Objects.isNull;

public abstract class AbstractRules<R extends AbstractRules<R>> {

	private Boolean cachedResult = null;

	protected abstract Clause rules();

	public final boolean evaluate() {
		if (isNull(cachedResult)) {
			cachedResult = rules().get();
		}

		return cachedResult;
	}

	@SuppressWarnings("unchecked")
	public final R isTrue(final Runnable action) {
		if (evaluate()) {
			action.run();
		}

		return (R) this;
	}

	public void orElse(final Runnable action) {
		if (!evaluate()) {
			action.run();
		}
	}

}
