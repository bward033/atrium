package ch.tutteli.atrium.core.robstoll.lib.checking

import ch.tutteli.atrium.assertions.Assertion
import ch.tutteli.atrium.assertions.AssertionGroup
import ch.tutteli.atrium.assertions.builders.assertionBuilder
import ch.tutteli.atrium.assertions.builders.root
import ch.tutteli.atrium.checking.AssertionChecker
import ch.tutteli.atrium.reporting.AtriumError
import ch.tutteli.atrium.reporting.Reporter
import ch.tutteli.atrium.reporting.translating.Translatable

/**
 * An [AssertionChecker] which throws [AtriumError]s in case an assertion fails
 * and uses the given [reporter] for reporting.
 *
 * @property reporter Will be used for reporting.
 *
 * @constructor An [AssertionChecker] which throws [AtriumError]s in case an assertion fails
 *   and uses the given [reporter] for reporting.
 * @param reporter Will be used for reporting.
 */
class ThrowingAssertionChecker(private val reporter: Reporter) : AssertionChecker {

    /**
     * Creates an [AssertionGroup] -- based on the given [assertionVerb], [representationProvider] and [assertions] --
     * formats it for reporting using the [reporter] and checks whether it holds.
     *
     * Notice, this method will change signature with 1.0.0, representationProvider will change to `representation: Any`
     *
     * @param assertionVerb Is used as [AssertionGroup.description].
     * @param representationProvider Provides the [AssertionGroup.representation].
     * @param assertions Is used as [AssertionGroup.assertions].
     *
     * @throws AssertionError In case the created [AssertionGroup] does not hold.
     */
    override fun check(assertionVerb: Translatable, representationProvider: () -> Any, assertions: List<Assertion>) {
        val assertionGroup = assertionBuilder.root
            .withDescriptionAndRepresentation(assertionVerb, representationProvider)
            .withAssertions(assertions)
            .build()

        val sb = StringBuilder()
        reporter.format(assertionGroup, sb)
        if (!assertionGroup.holds()) {
            throw AtriumError.create(sb.toString(), reporter.atriumErrorAdjuster)
        }
    }
}
