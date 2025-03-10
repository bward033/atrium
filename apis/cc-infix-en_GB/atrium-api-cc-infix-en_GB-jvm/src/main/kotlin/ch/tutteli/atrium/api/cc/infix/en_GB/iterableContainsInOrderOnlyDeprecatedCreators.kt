@file:JvmMultifileClass
@file:JvmName("IterableContainsInOrderOnlyCreatorsKt")
@file:Suppress("DEPRECATION" /* TODO remove with 1.0.0 */)
package ch.tutteli.atrium.api.cc.infix.en_GB

import ch.tutteli.atrium.creating.Assert
import ch.tutteli.atrium.creating.AssertionPlant
import ch.tutteli.atrium.domain.builders.AssertImpl
import ch.tutteli.atrium.domain.creating.iterable.contains.IterableContains
import ch.tutteli.atrium.domain.creating.iterable.contains.searchbehaviours.InOrderOnlySearchBehaviour

@Deprecated("use `value` instead; will be removed with 1.0.0", ReplaceWith("this value expectedOrNull"))
infix fun <E : Any?, T : Iterable<E>> IterableContains.Builder<E, T, InOrderOnlySearchBehaviour>.nullableValue(expectedOrNull: E): AssertionPlant<T>
    = this value expectedOrNull

@Deprecated("use `entry` instead; will be removed with 1.0.0", ReplaceWith("this entry assertionCreatorOrNull"))
infix fun <E : Any, T : Iterable<E?>> IterableContains.Builder<E?, T, InOrderOnlySearchBehaviour>.nullableEntry(assertionCreatorOrNull: (Assert<E>.() -> Unit)?): AssertionPlant<T>
    = this entry assertionCreatorOrNull

@Deprecated("use `the Values` instead; will be removed with 1.0.0", ReplaceWith("this the Values(nullableValues.expected, *nullableValues.otherExpected)"))
infix fun <E : Any?, T : Iterable<E>> IterableContains.Builder<E, T, InOrderOnlySearchBehaviour>.the(nullableValues: NullableValues<E>): AssertionPlant<T>
    = this the Values(nullableValues.expected, *nullableValues.otherExpected)

@Deprecated("use `the Entries` instead; will be removed with 1.0.0", ReplaceWith("this the Entries(nullableEntries.assertionCreatorOrNull, *nullableEntries.otherAssertionCreatorsOrNulls)"))
infix fun <E : Any, T : Iterable<E?>> IterableContains.Builder<E?, T, InOrderOnlySearchBehaviour>.the(nullableEntries: NullableEntries<E>): AssertionPlant<T>
    = this the Entries(nullableEntries.assertionCreatorOrNull, *nullableEntries.otherAssertionCreatorsOrNulls)
