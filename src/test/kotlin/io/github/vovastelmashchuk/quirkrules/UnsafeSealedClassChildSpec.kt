package io.github.vovastelmashchuk.quirkrules

import io.gitlab.arturbosch.detekt.rules.KotlinCoreEnvironmentTest
import io.gitlab.arturbosch.detekt.test.compileAndLintWithContext
import io.kotest.matchers.collections.shouldHaveSize
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.junit.jupiter.api.Test

@KotlinCoreEnvironmentTest
internal class UnsafeSealedClassChildSpec(private val env: KotlinCoreEnvironment) {

    @Test
    fun `report sealed class has normal class as child inside sealed body`() {
        val code = """
            sealed class Foo {
                data class Bar1(val foo: Int): Foo()
                //The wrong class
                class Bar2 : Foo()
                data object Bar3 : Foo()
            }
        """.trimIndent()

        val findings = UnsafeSealedClassChild().compileAndLintWithContext(env, code)
        findings shouldHaveSize 1
    }

    @Test
    fun `report sealed class has normal class as child outside sealed body`() {
        val code = """
            sealed class Foo {
                data class Bar1(val foo: Int): Foo()
                data object Bar3 : Foo()
            }
            //The wrong class
            class Bar2 : Foo()
        """.trimIndent()

        UnsafeSealedClassChild().compileAndLintWithContext(env, code) shouldHaveSize 1
    }

    @Test
    fun `does not report sealed class has object as child outside sealed body`() {
        val code = """
            sealed class Foo {
                data class Bar1(val foo: Int): Foo()
                data object Bar3 : Foo()
            }
            object Bar2 : Foo()
        """.trimIndent()

        UnsafeSealedClassChild().compileAndLintWithContext(env, code) shouldHaveSize 0
    }

    @Test
    fun `does not report sealed class all child ok outside sealed body`() {
        val code = """
            sealed class Foo
            data class Bar1(val foo: Int): Foo()
            data object Bar2 : Foo()
            data object Bar3 : Foo()
            data object Bar5 : Foo()
        """.trimIndent()

        UnsafeSealedClassChild().compileAndLintWithContext(env, code) shouldHaveSize 0
    }

    @Test
    fun `does not report sealed class with other sealed class as child, all child ok outside sealed body`() {
        val code = """
            sealed class Foo
            sealed class ChildFoo: Foo()
            data class Bar1(val foo: Int): Foo()
            data object Bar2 : ChildFoo()
            data object Bar3 : Foo()
            data object Bar5 : Foo()
        """.trimIndent()

        UnsafeSealedClassChild().compileAndLintWithContext(env, code) shouldHaveSize 0
    }

    @Test
    fun `report sealed class has open class as child`() {
        val code = """
            sealed class Foo
            open class ChildFoo(open val i : Int): Foo()
            
            data class Bar1(val foo: Int): Foo()
            data object Bar2 : ChildFoo(0)
            data class Bar3(override val i : Int) : ChildFoo(i)
            data object Bar5 : Foo()
        """.trimIndent()

        UnsafeSealedClassChild().compileAndLintWithContext(env, code) shouldHaveSize 1
    }

    @Test
    fun `does not report sealed interface`() {
        val code = """
            sealed interface Error

            sealed class IOError(): Error
            open class CustomError(): Error
        """.trimIndent()

        UnsafeSealedClassChild().compileAndLintWithContext(env, code) shouldHaveSize 0
    }
}
