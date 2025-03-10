package ch.tutteli.atrium.specs.integration

import ch.tutteli.atrium.api.fluent.en_GB.endsWith
import ch.tutteli.atrium.api.fluent.en_GB.message
import ch.tutteli.atrium.api.fluent.en_GB.messageContains
import ch.tutteli.atrium.api.fluent.en_GB.toThrow
import ch.tutteli.atrium.specs.*
import ch.tutteli.atrium.specs.verbs.AssertionVerbFactory
import ch.tutteli.atrium.translations.DescriptionBasic
import ch.tutteli.atrium.translations.DescriptionCharSequenceAssertion.*
import org.spekframework.spek2.style.specification.Suite

abstract class CharSequenceAssertionsSpec(
    verbs: AssertionVerbFactory,
    isEmpty: Fun0<CharSequence>,
    isNotEmpty: Fun0<CharSequence>,
    isNotBlank: Fun0<CharSequence>,
    startsWith: Fun1<CharSequence, CharSequence>,
    startsWithChar: Fun1<CharSequence, Char>,
    startsNotWith: Fun1<CharSequence, CharSequence>,
    startsNotWithChar: Fun1<CharSequence, Char>,
    endsWith: Fun1<CharSequence, CharSequence>,
    endsWithChar: Fun1<CharSequence, Char>,
    endsNotWith: Fun1<CharSequence, CharSequence>,
    endsNotWithChar: Fun1<CharSequence, Char>,
    describePrefix: String = "[Atrium] "
) : CharSequenceContainsSpecBase({

    include(object : SubjectLessSpec<CharSequence>(
        describePrefix,
        isEmpty.forSubjectLess(),
        isNotEmpty.forSubjectLess(),
        isNotBlank.forSubjectLess(),
        startsWith.forSubjectLess(""),
        startsWithChar.forSubjectLess('\u0000'),
        startsNotWith.forSubjectLess(""),
        startsNotWithChar.forSubjectLess('\u0000'),
        endsWith.forSubjectLess(""),
        endsWithChar.forSubjectLess('\u0000'),
        endsNotWith.forSubjectLess(""),
        endsNotWithChar.forSubjectLess('\u0000')
    ) {})

    fun describeFun(vararg funName: String, body: Suite.() -> Unit) =
        describeFunTemplate(describePrefix, funName, body = body)

    val expect = verbs::checkException
    val fluent = verbs.check(text as CharSequence)

    val isNot = DescriptionBasic.IS_NOT.getDefault()
    val itIs = DescriptionBasic.IS.getDefault()
    val emptyString: CharSequence = ""
    val blankString: CharSequence = "   "
    val notBlankString: CharSequence = "not blank string"

    val emptyStringBuilder: CharSequence = StringBuilder()
    val blankStringBuilder: CharSequence = StringBuilder(blankString)
    val notBlankStringBuilder: CharSequence = StringBuilder("not blank string")


    describeFun(isEmpty.name, isNotEmpty.name) {
        val isEmptyFun = isEmpty.lambda
        val isNotEmptyFun = isNotEmpty.lambda

        context("string is empty") {
            it("${isEmpty.name} does not throw") {
                verbs.check(emptyString).isEmptyFun()
                verbs.check(emptyStringBuilder).isEmptyFun()
            }
            it("${isNotEmpty.name} throws an AssertionError") {
                expect {
                    verbs.check(emptyString).isNotEmptyFun()
                }.toThrow<AssertionError> { message { endsWith("$isNot: empty") } }
                expect {
                    verbs.check(emptyStringBuilder).isNotEmptyFun()
                }.toThrow<AssertionError> { message { endsWith("$isNot: empty") } }
            }
        }
        context("string is not empty") {

            it("${isEmpty.name} throws an AssertionError") {
                expect {
                    verbs.check(blankString).isEmptyFun()
                }.toThrow<AssertionError> { message { endsWith("$itIs: empty") } }
                expect {
                    verbs.check(blankStringBuilder).isEmptyFun()
                }.toThrow<AssertionError> { message { endsWith("$itIs: empty") } }
            }
            it("${isNotEmpty.name} does not throw") {
                verbs.check(blankString).isNotEmptyFun()
                verbs.check(blankStringBuilder).isNotEmptyFun()
            }
        }
    }

    describeFun(isNotBlank.name) {
        val isNotBlankFun = isNotBlank.lambda

        context("string is blank") {
            it("throws an AssertionError") {

                expect {
                    verbs.check(blankString).isNotBlankFun()
                }.toThrow<AssertionError> { message { endsWith("$isNot: blank") } }
                expect {
                    verbs.check(blankStringBuilder).isNotBlankFun()
                }.toThrow<AssertionError> { message { endsWith("$isNot: blank") } }
            }
        }
        context("string is not blank") {
            it("does not throw") {
                verbs.check(notBlankString).isNotBlankFun()
                verbs.check(notBlankStringBuilder).isNotBlankFun()
            }
        }
    }

    describeFun(startsWith.name, startsNotWith.name) {
        val startsWithFun = startsWith.lambda
        val startsNotWithFun = startsNotWith.lambda

        context("text '$text'") {
            it("${startsWith.name} 'Hello' does not throw") {
                fluent.startsWithFun("Hello")
            }
            it("${startsNotWith.name} 'Hello' throws an AssertionError") {
                expect {
                    fluent.startsNotWithFun("Hello")
                }.toThrow<AssertionError> { messageContains(STARTS_NOT_WITH.getDefault()) }
            }

            it("${startsWith.name} 'Robert' throws an AssertionError") {
                expect {
                    fluent.startsWithFun("goodbye")
                }.toThrow<AssertionError> { messageContains(STARTS_WITH.getDefault()) }
            }
            it("${startsNotWith.name} 'Robert' does not throw") {
                fluent.startsNotWithFun("goodbye")
            }
        }
    }

    describeFun(startsWithChar.name, startsNotWithChar.name) {
        val startsWithCharFun = startsWithChar.lambda
        val startsNotWithCharFun = startsNotWithChar.lambda

        context("text '$text'") {
            it("${startsWithChar.name} 'H' does not throw") {
                fluent.startsWithCharFun('H')
            }
            it("${startsNotWithChar.name} 'H' throws an AssertionError") {
                expect {
                    fluent.startsNotWithCharFun('H')
                }.toThrow<AssertionError> { messageContains(STARTS_NOT_WITH.getDefault()) }
            }

            it("${startsWithChar.name} 't' throws an AssertionError") {
                expect {
                    fluent.startsWithCharFun('t')
                }.toThrow<AssertionError> { messageContains(STARTS_WITH.getDefault()) }
            }
            it("${startsNotWithChar.name} 't' does not throw") {
                fluent.startsNotWithCharFun('t')
            }
        }
    }

    describeFun(endsWith.name, endsNotWith.name) {
        val endsWithFun = endsWith.lambda
        val endsNotWithFun = endsNotWith.lambda

        context("text '$text'") {
            it("${endsWith.name} 'Hello' throws an AssertionError") {
                expect {
                    fluent.endsWithFun("Hello")
                }.toThrow<AssertionError> { messageContains(ENDS_WITH.getDefault()) }
            }
            it("${endsNotWith.name} 'Hello' does not throw") {
                fluent.endsNotWithFun("Hello")
            }

            it("${endsWith.name} 'Robert' does not throw") {
                fluent.endsWithFun("Robert")
            }
            it("${endsNotWith.name} 'Robert' throws an AssertionError") {
                expect {
                    fluent.endsNotWithFun("Robert")
                }.toThrow<AssertionError> { messageContains(ENDS_NOT_WITH.getDefault()) }
            }
        }
    }

    describeFun(endsWithChar.name, endsNotWithChar.name) {
        val endsWithCharFun = endsWithChar.lambda
        val endsNotWithCharFun = endsNotWithChar.lambda

        context("text '$text'") {
            it("${endsWithChar.name} 'H' throws an AssertionError") {
                expect {
                    fluent.endsWithCharFun('H')
                }.toThrow<AssertionError> { messageContains(ENDS_WITH.getDefault()) }
            }
            it("${endsNotWithChar.name} 'H' does not throw") {
                fluent.endsNotWithCharFun('H')
            }

            it("${endsWithChar.name} 't' does not throw") {
                fluent.endsWithCharFun('t')
            }
            it("${endsNotWithChar.name} 't' throws an AssertionError") {
                expect {
                    fluent.endsNotWithCharFun('t')
                }.toThrow<AssertionError> { messageContains(ENDS_NOT_WITH.getDefault()) }
            }
        }
    }
})
