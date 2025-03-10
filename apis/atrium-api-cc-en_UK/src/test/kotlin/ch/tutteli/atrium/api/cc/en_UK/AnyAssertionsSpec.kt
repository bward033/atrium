@file:Suppress("DEPRECATION" /* TODO remove with 1.0.0 */)
package ch.tutteli.atrium.api.cc.en_UK

import ch.tutteli.atrium.verbs.internal.AssertionVerbFactory
import ch.tutteli.atrium.creating.Assert
import ch.tutteli.atrium.creating.AssertionPlantNullable
import kotlin.reflect.KFunction2
import kotlin.reflect.KProperty1

class AnyAssertionsSpec : ch.tutteli.atrium.spec.integration.AnyAssertionsSpec(
    AssertionVerbFactory,
    AnyAssertionsSpecFunFactory(),
    AnyAssertionsSpecFunFactory(),
    Assert<Int>::toBe.name,
    Assert<Int>::notToBe.name,
    Assert<Int>::isSame.name,
    Assert<Int>::isNotSame.name,
    AssertionPlantNullable<*>::isNull.name to Companion::toBeNull,
    "toBeNullable not implemented in this api" to Companion::toBeNullable,
    "toBeNullable with Creator not implemented in this api" to Companion::toBeNullIfNullElse,
    getAndImmediatePair(),
    getAndLazyPair()
) {
    class AnyAssertionsSpecFunFactory<T : Any> : ch.tutteli.atrium.spec.integration.AnyAssertionsSpec.AnyAssertionsSpecFunFactory<T> {
        override val toBeFun = Assert<T>::toBe
        override val notToBeFun = Assert<T>::notToBe
        override val isSameFun = Assert<T>::isSame
        override val isNotSameFun = Assert<T>::isNotSame
    }

    companion object {

        private fun toBeNull(plant: AssertionPlantNullable<Int?>){
            plant.isNull()
        }

        private fun toBeNullable(plant: AssertionPlantNullable<Int?>, expected: Int?){
            if (expected == null) plant.isNull()
            else plant.isNotNull { toBe(expected) }
        }

        private fun toBeNullIfNullElse(plant: AssertionPlantNullable<Int?>, assertionCreator: (Assert<Int>.() -> Unit)?) {
            if (assertionCreator == null) plant.isNull()
            else plant.isNotNull(assertionCreator)
        }

        private val andImmediate : KProperty1<Assert<Int>, Assert<Int>> = Assert<Int>::and
        fun getAndImmediatePair(): Pair<String, Assert<Int>.() -> Assert<Int>>
            = andImmediate.name to Assert<Int>::and

        private val andLazyName : KFunction2<Assert<Int>, Assert<Int>.() -> Unit, Assert<Int>> = Assert<Int>::and
        fun getAndLazyPair(): Pair<String, Assert<Int>.(Assert<Int>.() -> Unit) -> Assert<Int>> =
            andLazyName.name to Assert<Int>::and
    }
}
