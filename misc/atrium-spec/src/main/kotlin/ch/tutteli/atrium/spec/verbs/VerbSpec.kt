@file:Suppress("DEPRECATION" /* TODO remove with 1.0.0 */)

package ch.tutteli.atrium.spec.verbs

import ch.tutteli.atrium.api.cc.en_GB.*
import ch.tutteli.atrium.core.coreFactory
import ch.tutteli.atrium.creating.Assert
import ch.tutteli.atrium.creating.AssertionPlant
import ch.tutteli.atrium.creating.AssertionPlantNullable
import ch.tutteli.atrium.domain.builders.AssertImpl
import ch.tutteli.atrium.domain.builders.ExpectImpl
import ch.tutteli.atrium.domain.creating.throwable.thrown.ThrowableThrown
import ch.tutteli.atrium.spec.AssertionVerb.ASSERT
import ch.tutteli.atrium.spec.AssertionVerb.EXPECT_THROWN
import ch.tutteli.atrium.spec.inCaseOf
import ch.tutteli.atrium.spec.prefixedDescribe
import ch.tutteli.atrium.translations.DescriptionComparableAssertion.*
import ch.tutteli.atrium.translations.DescriptionThrowableAssertion
import ch.tutteli.atrium.translations.DescriptionTypeTransformationAssertion
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.SpecBody
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.it

// does not make sense to test the verbs with the verbs themselves. Thus we create our own assertion verbs here
@Suppress("DEPRECATION")
private fun <T : Any> assert(subject: T): AssertionPlant<T>
    = coreFactory.newReportingPlant(ASSERT, { subject }, AtriumReporterSupplier.REPORTER)

@Suppress("DEPRECATION")
private fun <T : Any> assert(subject: T, assertionCreator: Assert<T>.() -> Unit)
    = coreFactory.newReportingPlantAndAddAssertionsCreatedBy(ASSERT, { subject }, AtriumReporterSupplier.REPORTER, assertionCreator)

@Suppress("DEPRECATION")
private fun <T : Any?> assert(subject: T)
    = coreFactory.newReportingPlantNullable(ASSERT, { subject }, AtriumReporterSupplier.REPORTER)

private fun expect(act: () -> Unit)
    = AssertImpl.throwable.thrownBuilder(EXPECT_THROWN, act, AtriumReporterSupplier.REPORTER)

private object AtriumReporterSupplier {
    val REPORTER by lazy {
        ExpectImpl.reporterBuilder
            .withoutTranslationsUseDefaultLocale()
            .withDetailedObjectFormatter()
            .withDefaultAssertionFormatterController()
            .withDefaultAssertionFormatterFacade()
            .withTextSameLineAssertionPairFormatter()
            .withTextCapabilities()
            .withDefaultAtriumErrorAdjusters()
            .withOnlyFailureReporter()
            .build()
    }
}

abstract class VerbSpec(
    plantCheckImmediately: Pair<String, (subject: Int) -> AssertionPlant<Int>>,
    plantCheckLazily: Pair<String, (subject: Int, assertionCreator: AssertionPlant<Int>.() -> Unit) -> AssertionPlant<Int>>,
    plantNullable: Pair<String, (subject: Int?) -> AssertionPlantNullable<Int?>>,
    plantExpect: Pair<String, (act: () -> Unit) -> ThrowableThrown.Builder>,
    describePrefix: String = "[Atrium] "
) : Spek({

    fun prefixedDescribe(description: String, body: SpecBody.() -> Unit) {
        prefixedDescribe(describePrefix, description, body)
    }

    prefixedDescribe("assertion verb '${plantCheckImmediately.first}' which immediately evaluates assertions") {
        val (_, assertionVerb) = plantCheckImmediately

        it("does not throw an exception in case the assertion holds") {
            assertionVerb(1).toBe(1)
        }
        it("throws an AssertionError as soon as one assertion fails") {
            expect {
                assertionVerb(1).isLessOrEquals(10).and.isLessOrEquals(0).and.isGreaterOrEquals(2)
            }.toThrow<AssertionError> {
                message {
                    contains(": 1")
                    contains("${IS_LESS_OR_EQUALS.getDefault()}: 0")
                    containsNot("${IS_GREATER_OR_EQUALS.getDefault()}: 2")
                }
            }
        }
    }

    prefixedDescribe("assertion verb '${plantCheckImmediately.first}' which lazily evaluates assertions") {
        val (_, assertionVerb) = plantCheckLazily

        context("the assertions hold") {
            it("does not throw an exception") {
                assertionVerb(1) { toBe(1) }
            }
            context("a subsequent assertion holds") {
                it("does not throw an exception") {
                    assertionVerb(1) { toBe(1) }.isLessThan(2)
                }
            }
            context("a subsequent group of assertions hold") {
                it("does not throw an exception") {
                    assertionVerb(1) { toBe(1) }.and { isLessThan(2) }
                }
            }
            context("a subsequent assertion fails") {
                it("throws an AssertionError") {
                    expect {
                        assertionVerb(1) { toBe(1) }.isLessThan(1)
                    }.toThrow<AssertionError> {
                        messageContains("${IS_LESS_THAN.getDefault()}: 1")
                    }
                }
            }

            context("multiple assertions of a subsequent group of assertion fails") {
                it("evaluates all assertions and then throws an AssertionError") {
                    expect {
                        assertionVerb(1) { toBe(1) }.and { isLessThan(0); isGreaterThan(2) }
                    }.toThrow<AssertionError> {
                        messageContains(
                            ": 1",
                            "${IS_LESS_THAN.getDefault()}: 0",
                            "${IS_GREATER_THAN.getDefault()}: 2"
                        )
                    }
                }
            }
        }

        context("one assertion fails") {
            it("evaluates all assertions and then throws an AssertionError") {
                expect {
                    assertionVerb(1) {
                        isLessThan(0)
                        isGreaterThan(2)
                    }
                }.toThrow<AssertionError> {
                    message {
                        contains(": 1")
                        contains("${IS_LESS_THAN.getDefault()}: 0")
                        contains("${IS_GREATER_THAN.getDefault()}: 2")
                    }
                }
            }
        }
    }

    prefixedDescribe("assertion verb '${plantNullable.first}' which supports nullable subjects") {
        inCaseOf("a nullable subject") {
            val (_, assertionVerb) = plantNullable
            it("does not throw an exception when calling toBe(`null`)") {
                assertionVerb(null).toBe(null)
            }
            it("throws an AssertionError when calling notToBeNull") {
                expect {
                    assertionVerb(null).notToBeNull {}
                }.toThrow<AssertionError> {
                    @Suppress("DEPRECATION")
                    messageContains(
                        DescriptionTypeTransformationAssertion.IS_A.getDefault(),
                        Integer::class.java.name
                    )
                }
            }
        }
    }

    prefixedDescribe("assertion verb '${plantExpect.first}' which deals with exceptions") {
        inCaseOf("an IllegalArgumentException occurs") {
            val (_, assertionVerb) = plantExpect
            it("does not throw an exception expecting an IllegalArgumentException") {
                assertionVerb {
                    throw IllegalArgumentException()
                }.toThrow<IllegalArgumentException>{}
            }
            it("throws an AssertionError when expecting an UnsupportedOperationException") {
                expect {
                    assertionVerb {
                        throw IllegalArgumentException()
                    }.toThrow<UnsupportedOperationException>{}
                }.toThrow<AssertionError> {
                    messageContains(
                        DescriptionThrowableAssertion.IS_A.getDefault(),
                        IllegalArgumentException::class.java.name,
                        UnsupportedOperationException::class.java.name
                    )
                }
            }
        }
    }

})
