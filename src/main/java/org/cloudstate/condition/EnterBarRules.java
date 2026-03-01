package org.cloudstate.condition;

import static java.util.Objects.*;
import static org.cloudstate.condition.lib.Condition.condition;

import org.cloudstate.condition.lib.AbstractRules;
import org.cloudstate.condition.lib.Condition;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class EnterBarRules extends AbstractRules<EnterBarRules> {

    private final Customer customer;
    private final @Nullable Customer company;
    private final Bar bar;

    private EnterBarRules(final Customer customer, final @Nullable Customer company, final Bar bar) {
        this.customer = requireNonNull(customer, "Customer must be provided");
        this.company = company;
        this.bar = requireNonNull(bar, "Bar must be provided");
    }

    public static EnterBarRules of(final Customer customer, final Bar bar) {
        return of(customer, null, bar);
    }

    public static EnterBarRules of(final Customer customer, final @Nullable Customer company, final Bar bar) {
        return new EnterBarRules(customer, company, bar);
    }

    @Override
    protected Condition conditions() {
        return barIsOpen()
                .and(allAreSober())
                .and(ofLegalAge().or(lateTeenWithAdultCompany()));
    }

    Condition barIsOpen() {
        return condition(bar::open);
    }

    Condition allAreSober() {
        return condition(() -> isSober(customer) && isSober(company));
    }

    Condition ofLegalAge() {
        return condition(() -> isLegalAge(customer) && isLegalAge(company));
    }

    Condition lateTeenWithAdultCompany() {
        return condition(() -> isLateTeen(customer) && isAdult(company) || isAdult(customer) && isLateTeen(company));
    }

    private static boolean isSober(final @Nullable Customer customer) {
        return isNull(customer) || customer.sober();
    }

    private static boolean isLegalAge(final @Nullable Customer customer) {
        return isNull(customer) || customer.age() >= 20;
    }

    private static boolean isLateTeen(final @Nullable Customer customer) {
        return nonNull(customer) && customer.age() >= 18;
    }

    private static boolean isAdult(final @Nullable Customer customer) {
        return nonNull(customer) && customer.age() >= 35;
    }

}
