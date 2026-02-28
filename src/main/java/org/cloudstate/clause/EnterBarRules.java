package org.cloudstate.clause;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;
import static org.cloudstate.clause.lib.Clause.clause;

import org.cloudstate.clause.lib.AbstractRules;
import org.cloudstate.clause.lib.Clause;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class EnterBarRules extends AbstractRules<EnterBarRules> {

	private final Person customer;
	private final Person company;
	private final Bar bar;

	private EnterBarRules(final Person customer, final @Nullable Person company, final Bar bar) {
		this.customer = requireNonNull(customer, "Customer must be provided");
		this.company = company;
		this.bar = requireNonNull(bar, "Bar must be provided");
	}

	public static EnterBarRules of(final Person customer, final @Nullable Person company, final Bar bar) {
		return new EnterBarRules(customer, company, bar);
	}

	@Override
	protected Clause rules() {
		return barIsOpen().and(allAreSober()).and(ofLegalAge().or(lateTeenWithAdultCompany()));
	}

	protected Clause barIsOpen() {
		return clause(() -> bar.open());
	}

	protected Clause allAreSober() {
		return clause(() -> isSober(customer) && isSober(company));
	}

	protected Clause ofLegalAge() {
		return clause(() -> isLegalAge(customer) && isLegalAge(company));
	}

	protected Clause lateTeenWithAdultCompany() {
		return clause(() -> isLateTeen(customer) && isAdult(company) || isAdult(customer) && isLateTeen(company));
	}

	private static boolean isSober(final @Nullable Person person) {
		return nonNull(person) ? person.sober() : true;
	}

	private static boolean isLegalAge(final @Nullable Person person) {
		return nonNull(person) ? person.age() >= 20 : true;
	}

	private static boolean isLateTeen(final Person person) {
		return person.age() >= 18;
	}

	private static boolean isAdult(final @Nullable Person person) {
		return nonNull(person) ? person.age() >= 35 : false;
	}

}
