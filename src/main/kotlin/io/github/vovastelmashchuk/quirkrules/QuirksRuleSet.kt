package io.github.vovastelmashchuk.quirkrules

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider

class QuirksRuleSet : RuleSetProvider {
    override val ruleSetId: String = "QuirksRuleSet"

    override fun instance(config: Config): RuleSet {
        return RuleSet(
            ruleSetId,
            listOf(
                UnsafeSealedClassChild(config),
            ),
        )
    }
}
