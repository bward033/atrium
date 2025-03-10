@file:JvmMultifileClass
@file:JvmName("AnyAssertionsKt")

package ch.tutteli.atrium.api.fluent.en_GB

import ch.tutteli.atrium.checking.AssertionChecker
import ch.tutteli.atrium.creating.Expect
import ch.tutteli.atrium.domain.builders.ExpectImpl
import ch.tutteli.atrium.reporting.Reporter
import kotlin.js.JsName
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName

/**
 * Expects that the subject of the assertion is (equal to) [expected].
 *
 * @return This assertion container to support a fluent API.
 * @throws AssertionError Might throw an [AssertionError] if the assertion made is not correct.
 */
fun <T : Any> Expect<T>.toBe(expected: T) = addAssertion(ExpectImpl.any.toBe(this, expected))

/**
 * Expects that the subject of the assertion is (equal to) [expected].
 *
 * @return This assertion container to support a fluent API.
 * @throws AssertionError Might throw an [AssertionError] if the assertion made is not correct.
 */
@JvmName("toBeNullable")
@JsName("toBeNullable")
inline fun <reified T : Any> Expect<T?>.toBe(expected: T?): Expect<T?> =
    addAssertion(ExpectImpl.any.toBeNullable(this, T::class, expected))

/**
 * Expects that the subject of the assertion is not (equal to) [expected].
 *
 * @return This assertion container to support a fluent API.
 * @throws AssertionError Might throw an [AssertionError] if the assertion made is not correct.
 */
fun <T> Expect<T>.notToBe(expected: T) = addAssertion(ExpectImpl.any.notToBe(this, expected))

/**
 * Expects that the subject of the assertion is the same instance as [expected].
 *
 * @return This assertion container to support a fluent API.
 * @throws AssertionError Might throw an [AssertionError] if the assertion made is not correct.
 */
fun <T> Expect<T>.isSameAs(expected: T) = addAssertion(ExpectImpl.any.isSame(this, expected))

/**
 * Expects that the subject of the assertion is not the same instance as [expected].
 *
 * @return This assertion container to support a fluent API.
 * @throws AssertionError Might throw an [AssertionError] if the assertion made is not correct.
 */
fun <T> Expect<T>.isNotSameAs(expected: T) = addAssertion(ExpectImpl.any.isNotSame(this, expected))

/**
 * Expects that the subject of the assertion is either `null` in case [assertionCreatorOrNull]
 * is `null` or is not `null` and holds all assertions [assertionCreatorOrNull] might create.
 *
 * Depending on the implementation, it is not much more than a shortcut for
 * ```kotlin
 * if (assertionCreatorOrNull == null) toBe(null)
 * else notToBeNull(assertionCreatorOrNull)
 * ```
 *
 * @return This assertion container to support a fluent API.
 * @throws AssertionError Might throw an [AssertionError] if the assertion made is not correct.
 */
inline fun <reified T : Any> Expect<T?>.toBeNullIfNullGivenElse(noinline assertionCreatorOrNull: (Expect<T>.() -> Unit)?) =
    addAssertion(ExpectImpl.any.toBeNullIfNullGivenElse(this, T::class, assertionCreatorOrNull))

/**
 * Expects that the subject of the assertion is not null and changes the subject to the non-nullable version.
 *
 * It delegates to [isA] with [T] as type.
 *
 * @return An assertion container with the non-nullable type [T] (was `T?` before).
 * @throws AssertionError Might throw an [AssertionError] if the assertion made is not correct.
 */
@Suppress(/* less magic */ "RemoveExplicitTypeArguments")
inline fun <reified T : Any> Expect<T?>.notToBeNull(): Expect<T> = isA<T>()

/**
 * Expects that the subject of the assertion is not null and
 * that it holds all assertions the given [assertionCreator] creates.
 *
 * It delegates to [isA] with [T] as type.
 *
 * @return An assertion container with the non-nullable type [T] (was `T?` before)
 * @throws AssertionError Might throw an [AssertionError] if the assertion made is not correct.
 */
@Suppress(/* less magic */ "RemoveExplicitTypeArguments")
inline fun <reified T : Any> Expect<T?>.notToBeNull(noinline assertionCreator: Expect<T>.() -> Unit): Expect<T> =
    isA<T>(assertionCreator)

/**
 * Expects that the subject of the assertion *is a* [TSub] (the same type or a sub-type)
 * and changes the subject to this type.
 *
 * Notice, that asserting a function type is [flawed](https://youtrack.jetbrains.com/issue/KT-27846).
 * The actual types are ignored as function types erase to Function0,
 * Function1 etc. on byte code level, which means the assertion holds as long as the subject is a
 * function and has the same amount of arguments regardless if the types differ. For instance
 * `assert({x: Int -> "hello"}).isA<String -> Unit>{}` holds, even though `(Int) -> String` is clearly not
 * a `(String) -> Unit`.
 *
 * More generally speaking, the [flaw](https://youtrack.jetbrains.com/issue/KT-27826) applies to all generic types.
 * For instance `isA<List<String>>` would only check if the subject is a `List` without checking if
 * the element type is actually `String`. Or in other words
 * `assert(listOf(1, 2)).isA<List<String>>{}` holds, even though `List<Int>` is clearly not a `List<String>`.
 *
 * @return An assertion container with the new type [TSub].
 * @throws AssertionError Might throw an [AssertionError] if the assertion made is not correct.
 */
inline fun <reified TSub : Any> Expect<*>.isA(): Expect<TSub> =
    ExpectImpl.any.isA(this, TSub::class).getExpectOfFeature()

/**
 * Expects that the subject of the assertion *is a* [TSub] (the same type or a sub-type) and
 * that it holds all assertions the given [assertionCreator] creates.
 *
 * Notice, in contrast to other assertion functions which expect an [assertionCreator], this function returns not
 * [Expect] of the initial type, which was some type `T `, but an [Expect] of the specified type [TSub].
 * This has the side effect that a subsequent call has only assertion functions available which are suited for [TSub].
 * Since [Expect] is invariant it especially means that an assertion function which was not written in a generic way
 * will not be available. Fixing such a function is easy (in most cases),
 * you need to transform it into a generic from. Following an example:
 *
 * ```
 * interface Person
 * class Student: Person
 * fun Expect<Person>.foo()        = "dummy"  // limited only to Person, not recommended
 * fun <T: Person> Expect<T>.bar() = "dummy"  // available to Person and all subtypes, the way to go
 * fun Expect<Student>.baz()       = "dummy"  // specific only for Student, ok since closed class
 *
 * val p: Person = Student()
 * expect(p)               // subject of type Person
 *   .isA<Student> { ... } // subject now refined to Student
 *   .baz()                // available via Student
 *   .foo()                // not available to Student, only to Person, results in compilation error
 *   .bar()                // available via T : Person
 * ```
 *
 * Notice, that asserting a function type is [flawed](https://youtrack.jetbrains.com/issue/KT-27846).
 * The actual types are ignored as function types erase to Function0,
 * Function1 etc. on byte code level, which means the assertion holds as long as the subject is a
 * function and has the same amount of arguments regardless if the types differ. For instance
 * `assert({x: Int -> "hello"}).isA<String -> Unit>{}` holds, even though `(Int) -> String` is clearly not
 * a `(String) -> Unit`.
 *
 * More generally speaking, the [flaw](https://youtrack.jetbrains.com/issue/KT-27826) applies to all generic types.
 * For instance `isA<List<String>>` would only check if the subject is a `List` without checking if
 * the element type is actually `String`. Or in other words
 * `assert(listOf(1, 2)).isA<List<String>>{}` holds, even though `List<Int>` is clearly not a `List<String>`.
 *
 * @return An assertion container with the new type [TSub].
 * @throws AssertionError Might throw an [AssertionError] if the assertion made is not correct.
 */
inline fun <reified TSub : Any> Expect<*>.isA(noinline assertionCreator: Expect<TSub>.() -> Unit): Expect<TSub> =
    ExpectImpl.any.isA(this, TSub::class).addToFeature(assertionCreator)

/**
 * Can be used to separate single assertions.
 *
 * For instance `expect(1).isLessThan(2).and.isGreaterThan(0)` creates
 * two assertions (not one assertion with two sub-assertions) - the first asserts that 1 is less than 2 and the second
 * asserts that 1 is greater than 0. If the first assertion fails, then usually (depending on the configured
 * [AssertionChecker]) the second assertion is not evaluated.
 *
 * @return This assertion container to support a fluent API.
 */
inline val <T> Expect<T>.and: Expect<T> get() = this

/**
 * Can be used to create a group of sub assertions when using the fluent API.
 *
 * For instance `assert(1).isLessThan(3).and { isEven(); isGreaterThan(1) }` creates
 * two assertions where the second one consists of two sub-assertions. In case the first assertion holds, then the
 * second one is evaluated as a whole. Meaning, even though 1 is not even, it still evaluates that 1 is greater than 1.
 * Hence the reporting might (depending on the configured [Reporter]) contain both failing sub-assertions.
 *
 * @return This assertion container to support a fluent API.
 */
infix fun <T> Expect<T>.and(assertionCreator: Expect<T>.() -> Unit) = addAssertionsCreatedBy(assertionCreator)
