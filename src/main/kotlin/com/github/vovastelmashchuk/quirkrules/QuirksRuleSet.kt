package com.github.vovastelmashchuk.quirkrules

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider
import org.example.detekt.MyRule

class QuirksRuleSet : RuleSetProvider {
    override val ruleSetId: String = "QuirksRuleSet"

    override fun instance(config: Config): RuleSet {
        return RuleSet(
            ruleSetId,
            listOf(
                MyRule(config),
            ),
        )
    }
}
