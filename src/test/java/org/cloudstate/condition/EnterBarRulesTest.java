package org.cloudstate.condition;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_METHOD;
import static org.junit.jupiter.params.provider.Arguments.argumentSet;

import java.util.stream.Stream;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@TestInstance(PER_METHOD)
final class EnterBarRulesTest {

    static Bar openBar = new Bar(true);
    static Bar closedBar = new Bar(false);

    static Customer soberAdult = new Customer(40, true);
    static Customer drunkAdult = new Customer(40, false);

    static Customer teen = new Customer(19, true);
    static Customer kid = new Customer(15, true);

    @ParameterizedTest
    @MethodSource("provideEnterBarConditions")
    void enterBarConditions_evaluate(final EnterBarRules enterBarRules, final boolean expected) {
        assertThat(enterBarRules.evaluate()).isEqualTo(expected);
    }

    private static Stream<Arguments> provideEnterBarConditions() {
        return Stream.of(
                // Bar must be open
                ebr("Closed bar", soberAdult, closedBar, false),

                // No drunk people allowed
                ebr("Drunk adult", drunkAdult, openBar, false),
                ebr("Adult with drunk company", soberAdult, drunkAdult, openBar, false),

                // No kids allowed, not even with adult company
                ebr("Kid alone", kid, openBar, false), ebr("Kid with adult company", kid, soberAdult, openBar, false),
                ebr("Adult with kid company", soberAdult, kid, openBar, false),

                // No teens allowed without adult company
                ebr("Teen alone", teen, openBar, false), ebr("Teen with teen company", teen, teen, openBar, false),

                // Adults allowed when bar is open
                ebr("Adult alone", soberAdult, openBar, true),
                ebr("Adult with adult company", soberAdult, soberAdult, openBar, true),

                // Teens allowed with adult company
                ebr("Teen with adult company", teen, soberAdult, openBar, true),
                ebr("Adult with teen company", soberAdult, teen, openBar, true));
    }

    private static Arguments ebr(final String name, final Customer customer, final Bar bar, final boolean expected) {
        return argumentSet(name, EnterBarRules.of(customer, bar), expected);
    }

    private static Arguments ebr(final String name, final Customer customer, final Customer company, final Bar bar,
                                 final boolean expected) {
        return argumentSet(name, EnterBarRules.of(customer, company, bar), expected);
    }

}
