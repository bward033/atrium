@file:Suppress("OVERRIDE_BY_INLINE", "NOTHING_TO_INLINE")

package ch.tutteli.atrium.domain.builders.creating

import ch.tutteli.atrium.assertions.Assertion
import ch.tutteli.atrium.core.polyfills.loadSingleService
import ch.tutteli.atrium.creating.*
import ch.tutteli.atrium.domain.creating.MapAssertions
import ch.tutteli.atrium.domain.creating.MapEntryAssertions
import ch.tutteli.atrium.domain.creating.changers.ExtractedFeaturePostStep
import ch.tutteli.atrium.domain.creating.mapAssertions
import kotlin.reflect.KClass

/**
 * Delegates inter alia to the implementation of [MapAssertions].
 * In detail, it implements [MapAssertions] by delegating to [mapAssertions]
 * which in turn delegates to the implementation via [loadSingleService].
 */
object MapAssertionsBuilder : MapAssertions {

    /**
     * Returns [MapEntryAssertionsBuilder]
     * which inter alia delegates to the implementation of [MapEntryAssertions].
     */
    inline val entry get() : MapEntryAssertionsBuilder = MapEntryAssertionsBuilder

    override inline fun <K, V : Any, T : Map<out K, V?>> contains(
        assertionContainer: Expect<T>,
        valueType: KClass<V>,
        keyValuePairs: List<Pair<K, V?>>
    ) = mapAssertions.contains(assertionContainer, valueType, keyValuePairs)

    override inline fun <K, V : Any, T : Map<out K, V?>> containsKeyWithValueAssertions(
        assertionContainer: Expect<T>,
        valueType: KClass<V>,
        keyValues: List<Pair<K, (Expect<V>.() -> Unit)?>>
    ) = mapAssertions.containsKeyWithValueAssertions(assertionContainer, valueType, keyValues)


    override inline fun <K> containsKey(subjectProvider: SubjectProvider<Map<out K, *>>, key: K) =
        mapAssertions.containsKey(subjectProvider, key)

    override inline fun <K> containsNotKey(subjectProvider: SubjectProvider<Map<out K, *>>, key: K) =
        mapAssertions.containsNotKey(subjectProvider, key)


    override inline fun isEmpty(subjectProvider: SubjectProvider<Map<*, *>>) =
        mapAssertions.isEmpty(subjectProvider)

    override inline fun <K, V, T : Map<out K, V>> getExisting(
        assertionContainer: Expect<T>,
        key: K
    ): ExtractedFeaturePostStep<T, V> = mapAssertions.getExisting(assertionContainer, key)

    override inline fun isNotEmpty(subjectProvider: SubjectProvider<Map<*, *>>) =
        mapAssertions.isNotEmpty(subjectProvider)

    override inline fun <T : Map<*, *>> size(assertionContainer: Expect<T>) = mapAssertions.size(assertionContainer)

    // everything below is deprecated functionality and will be removed with 1.0.0

    @Suppress("DEPRECATION", "OverridingDeprecatedMember")
    override inline fun <K, V> contains(
        plant: AssertionPlant<Map<out K, V>>,
        keyValuePairs: List<Pair<K, V>>
    ) = mapAssertions.contains(plant, keyValuePairs)

    @Suppress("DEPRECATION", "OverridingDeprecatedMember")
    override inline fun <K, V : Any> containsKeyWithValueAssertions(
        plant: AssertionPlant<Map<out K, V?>>,
        keyValues: List<Pair<K, (Assert<V>.() -> Unit)?>>
    ) = mapAssertions.containsKeyWithValueAssertions(plant, keyValues)

    @Suppress("DEPRECATION", "OverridingDeprecatedMember")
    override inline fun <K, V : Any> getExisting(plant: AssertionPlant<Map<out K, V>>, key: K) =
        mapAssertions.getExisting(plant, key)

    @Suppress("DEPRECATION", "OverridingDeprecatedMember")
    override inline fun <K, V : Any> getExisting(
        plant: AssertionPlant<Map<out K, V>>,
        key: K,
        noinline assertionCreator: AssertionPlant<V>.() -> Unit
    ) = mapAssertions.getExisting(plant, key, assertionCreator)

    @Suppress("DEPRECATION", "OverridingDeprecatedMember")
    override inline fun <K, V> getExistingNullable(plant: AssertionPlant<Map<out K, V>>, key: K) =
        mapAssertions.getExistingNullable(plant, key)

    @Suppress("DEPRECATION", "OverridingDeprecatedMember")
    override inline fun <K, V> getExistingNullable(
        plant: AssertionPlant<Map<out K, V>>,
        key: K,
        noinline assertionCreator: AssertionPlantNullable<V>.() -> Unit
    ) = mapAssertions.getExistingNullable(plant, key, assertionCreator)

    @Suppress("DEPRECATION", "OverridingDeprecatedMember")
    override inline fun hasSize(plant: AssertionPlant<Map<*, *>>, size: Int) = mapAssertions.hasSize(plant, size)

    @Suppress("DEPRECATION", "OverridingDeprecatedMember")
    override inline fun <K> keys(
        plant: AssertionPlant<Map<out K, *>>,
        noinline assertionCreator: AssertionPlant<Set<K>>.() -> Unit
    ): Assertion = mapAssertions.keys(plant, assertionCreator)

    @Suppress("DEPRECATION", "OverridingDeprecatedMember")
    override inline fun <V> values(
        plant: AssertionPlant<Map<*, V>>,
        noinline assertionCreator: AssertionPlant<Collection<V>>.() -> Unit
    ): Assertion = mapAssertions.values(plant, assertionCreator)
}
