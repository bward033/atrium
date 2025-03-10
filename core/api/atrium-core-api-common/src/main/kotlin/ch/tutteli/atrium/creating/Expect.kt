package ch.tutteli.atrium.creating

import ch.tutteli.atrium.assertions.Assertion
import ch.tutteli.atrium.assertions.DescriptiveAssertion
import ch.tutteli.atrium.assertions.builders.assertionBuilder
import ch.tutteli.atrium.core.None
import ch.tutteli.atrium.core.Option
import ch.tutteli.atrium.core.Some
import ch.tutteli.atrium.reporting.translating.Translatable

/**
 * DSL Marker for [Expect].
 */
@DslMarker
annotation class ExpectMarker


/**
 * Represents the extension point of [Assertion] functions and sophisticated builders for subjects of type [T].
 *
 * It is also the base type of all assertion container types (like [ReportingAssertionContainer],
 * [CollectingAssertionContainer] etc.)
 *
 * @param T The type of the [subject] of the assertion.
 */
@ExpectMarker
interface Expect<T> : SubjectProvider<T>, AssertionHolder {

    /**
     * Either [Some] wrapping the subject of the current assertion or
     * [None] in case a previous subject change was not successful.
     */
    override val maybeSubject: Option<T>

    /**
     * Adds the assertions created by the [assertionCreator] lambda to this container and
     * adds a failing assertion to this container in case the [assertionCreator] did not create a single assertion.
     *
     * A failing assertion is added to the container since we assume that the user or assertion function writer
     * did a mistake and forgot to add an assertion. This can happen if one only creates assertions without adding
     * them to the container or if one simply let the lambda empty.
     *
     * @param assertionCreator The receiver function which should create and add assertions to this container.
     *
     * @return This assertion container to support a fluent API.
     * @throws AssertionError Might throw an [AssertionError] depending on the concrete implementation.
     */
    fun addAssertionsCreatedBy(assertionCreator: Expect<T>.() -> Unit): Expect<T>

    /**
     * Adds the given [assertion] to this container.
     *
     * @param assertion The assertion which will be added to this container.
     *
     * @return This assertion container to support a fluent API.
     *
     * @throws AssertionError Might throw an [AssertionError] in case [Assertion]s are immediately
     *   evaluated (see [ReportingAssertionContainer]).
     */
    override fun addAssertion(assertion: Assertion): Expect<T>

    /**
     * Creates a [DescriptiveAssertion] based on the given [description], [expected] and [test]
     * and [adds][addAssertion] it to the container.
     *
     * @param description The description of the assertion, e.g., `is less than`.
     * @param expected The expected value, e.g., `5`
     * @param test Indicates whether the assertion holds or fails.
     *
     * @return This assertion container to support a fluent API.
     * @throws AssertionError Might throw an [AssertionError] in case [Assertion]s are immediately
     *   evaluated (see [ReportingAssertionContainer]).
     */
    fun createAndAddAssertion(description: Translatable, expected: Any?, test: (T) -> Boolean): Expect<T> =
        addAssertion(assertionBuilder.createDescriptive(this, description, expected, test))
}
