package ch.tutteli.atrium.assertions

import ch.tutteli.atrium.api.cc.en_GB.containsExactly
import ch.tutteli.atrium.api.cc.en_GB.toBe
import ch.tutteli.atrium.domain.builders.AssertImpl
import ch.tutteli.atrium.domain.robstoll.lib.assertions.LazyThreadUnsafeAssertionGroup
import ch.tutteli.atrium.verbs.internal.assert
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

object LazyThreadUnsafeAssertionGroupSpec : Spek({

    describe("creating it") {
        var callingCount = 0
        val assertion = AssertImpl.builder.descriptive
            .failing
            .withDescriptionAndRepresentation("b", 3)
            .build()

        val testee = LazyThreadUnsafeAssertionGroup {
            ++callingCount
            AssertImpl.builder.feature
                .withDescriptionAndRepresentation("a", 2)
                .withAssertion(assertion)
                .build()
        }
        test("does not evaluate anything") {
            assert(callingCount).toBe(0)
        }
        test("adding it to a list does not evaluate anything") {
            listOf(testee)
            assert(callingCount).toBe(0)
        }
        on("invoking ${testee::holds.name}") {
            val resultHolds = testee.holds()

            it("evaluates it") {
                assert(callingCount).toBe(1)
            }

            it("returns ${AssertionGroup::holds.name}() of the underlying ${AssertionGroup::class.simpleName}") {
                assert(resultHolds).toBe(false)
            }
        }

        on("invoking ${testee::holds.name} and then ${testee::assertions.name}") {
            val resultHolds = testee.holds()
            val resultAssertions = testee.assertions

            it("evaluates it only once") {
                assert(callingCount).toBe(1)
            }

            it("returns ${AssertionGroup::holds.name}() of the underlying ${AssertionGroup::class.simpleName}") {
                assert(resultHolds).toBe(false)
            }

            it("returns the ${AssertionGroup::assertions.name} of the underlying ${AssertionGroup::class.simpleName}") {
                assert(resultAssertions).containsExactly(assertion)
            }
        }
    }
})
