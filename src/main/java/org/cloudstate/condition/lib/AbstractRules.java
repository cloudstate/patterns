package org.cloudstate.condition.lib;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import static java.util.Objects.isNull;

@NullMarked
public abstract class AbstractRules<R extends AbstractRules<R>> {

	private @Nullable Boolean cachedResult = null;

	protected abstract Condition conditions();

	public final boolean evaluate() {
		if (isNull(cachedResult)) {
			cachedResult = conditions().get();
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
