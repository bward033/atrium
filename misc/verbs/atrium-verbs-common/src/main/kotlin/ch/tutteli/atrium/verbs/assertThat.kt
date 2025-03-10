@file:Suppress("DEPRECATION" /* TODO remove with 1.0.0 */)

package ch.tutteli.atrium.verbs

import ch.tutteli.atrium.assertions.Assertion
import ch.tutteli.atrium.core.CoreFactory
import ch.tutteli.atrium.core.newReportingPlantNullable
import ch.tutteli.atrium.creating.Assert
import ch.tutteli.atrium.creating.AssertionPlant
import ch.tutteli.atrium.creating.AssertionPlantNullable
import ch.tutteli.atrium.domain.builders.AssertImpl
import ch.tutteli.atrium.domain.creating.throwable.thrown.ThrowableThrown
import ch.tutteli.atrium.reporting.Reporter
import ch.tutteli.atrium.reporting.reporter
import ch.tutteli.atrium.verbs.AssertionVerb.ASSERT_THAT
import ch.tutteli.atrium.verbs.AssertionVerb.ASSERT_THAT_THROWN

/**
 * Creates an [AssertionPlant] for the given [subject].
 *
 * @return The newly created plant.
 *
 * @see CoreFactory.newReportingPlant
 */
fun <T : Any> assertThat(subject: T)
    = AssertImpl.coreFactory.newReportingPlant(ASSERT_THAT, { subject }, reporter)

/**
 * Creates an [AssertionPlant] for the given [subject] and [AssertionPlant.addAssertionsCreatedBy] the
 * given [assertionCreator] lambda where the created [Assertion]s are added as a group and usually (depending on
 * the configured [Reporter]) reportBuilder as a whole.
 *
 * @return The newly created plant.
 *
 * @see CoreFactory.newReportingPlantAndAddAssertionsCreatedBy
 */
fun <T : Any> assertThat(subject: T, assertionCreator: Assert<T>.() -> Unit)
    = AssertImpl.coreFactory.newReportingPlantAndAddAssertionsCreatedBy(ASSERT_THAT, { subject }, reporter, assertionCreator)

/**
 * Creates an [AssertionPlantNullable] for the given [subject] which might be `null`.
 *
 * @return The newly created plant.
 *
 * @see CoreFactory.newReportingPlantNullable
 */
fun <T : Any?> assertThat(subject: T)
    = AssertImpl.coreFactory.newReportingPlantNullable(ASSERT_THAT, { subject }, reporter)

/**
 * Creates a [ThrowableThrown.Builder] for the given function [act] which is expected to throw a [Throwable].
 *
 * @return The newly created [ThrowableThrown.Builder].
 */
fun assertThat(act: () -> Unit)
    = AssertImpl.throwable.thrownBuilder(ASSERT_THAT_THROWN, act, reporter)
