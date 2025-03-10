package ch.tutteli.atrium.domain.robstoll.creating.changers

import ch.tutteli.atrium.checking.AssertionChecker
import ch.tutteli.atrium.core.Option
import ch.tutteli.atrium.core.coreFactory
import ch.tutteli.atrium.core.getOrElse
import ch.tutteli.atrium.core.newReportingPlantNullable
import ch.tutteli.atrium.creating.*
import ch.tutteli.atrium.domain.creating.changers.SubjectChanger
import ch.tutteli.atrium.domain.robstoll.lib.creating.changers._changeSubject
import ch.tutteli.atrium.domain.robstoll.lib.creating.changers._changeSubjectUnreported
import ch.tutteli.atrium.reporting.SHOULD_NOT_BE_SHOWN_TO_THE_USER_BUG_TRANSLATABLE
import ch.tutteli.atrium.reporting.translating.Translatable
import ch.tutteli.atrium.reporting.translating.Untranslatable

class SubjectChangerImpl : SubjectChanger {

    override fun <T, R> unreported(
        originalAssertionContainer: Expect<T>,
        transformation: (T) -> R
    ): Expect<R> = _changeSubjectUnreported(originalAssertionContainer, transformation)

    override fun <T, R> reported(
        originalAssertionContainer: Expect<T>,
        description: Translatable,
        representation: Any,
        canBeTransformed: (T) -> Boolean,
        transformation: (T) -> R,
        failureHandler: SubjectChanger.FailureHandler<T, R>,
        maybeSubAssertions: Option<Expect<R>.() -> Unit>
    ): Expect<R> = _changeSubject(
        originalAssertionContainer,
        description,
        representation,
        canBeTransformed,
        transformation,
        failureHandler,
        maybeSubAssertions
    )

    @Suppress("OverridingDeprecatedMember", "DEPRECATION")
    override fun <T, R : Any> unreported(
        originalPlant: BaseAssertionPlant<T, *>,
        transformation: (T) -> R
    ): Assert<R> {
        val (assertionChecker, assertionVerb) = createDelegatingAssertionCheckerAndVerb(originalPlant)
        return coreFactory.newReportingPlant(
            assertionVerb,
            { transformation(originalPlant.maybeSubject.getOrElse { throw PlantHasNoSubjectException() }) },
            assertionChecker
        )
    }

    @Suppress("OverridingDeprecatedMember", "DEPRECATION")
    override fun <T, R> unreportedNullable(
        originalPlant: BaseAssertionPlant<T, *>,
        transformation: (T) -> R
    ): AssertionPlantNullable<R> {
        val (assertionChecker, assertionVerb) = createDelegatingAssertionCheckerAndVerb(originalPlant)
        return coreFactory.newReportingPlantNullable(
            assertionVerb,
            { transformation(originalPlant.maybeSubject.getOrElse { throw PlantHasNoSubjectException() }) },
            assertionChecker
        )
    }

    private fun createDelegatingAssertionCheckerAndVerb(originalPlant: AssertionHolder): Pair<AssertionChecker, Untranslatable> {
        val assertionChecker = coreFactory.newDelegatingAssertionChecker(originalPlant)
        return assertionChecker to SHOULD_NOT_BE_SHOWN_TO_THE_USER_BUG_TRANSLATABLE
    }
}
