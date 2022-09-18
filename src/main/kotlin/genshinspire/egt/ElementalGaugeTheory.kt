package genshinspire.egt

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import genshinspire.powers.ElementalPower
import genshinspire.powers.ElementalPowerFactory

object ElementalGaugeTheory {

    fun getAuras(m: AbstractMonster): List<ElementalPower> {
        return m.powers.filterIsInstance(ElementalPower::class.java)
    }

    fun modifyEgtDamage(m: AbstractMonster, damage: Int, element: Element): Pair<Int, Reaction?> {
        val auras = getAuras(m).map { it.asGauge() }
        return when (element) {
            Element.PYRO -> {
                when {
                    auras.any { it.element == Element.CRYO } -> Pair(damage * 2, Reaction.MELT)
                    auras.any { it.element == Element.HYDRO } -> Pair((damage * 1.5f).toInt(), Reaction.VAPORIZE)
                    else -> Pair(damage, null)
                }
            }
            Element.CRYO -> {
                when {
                    auras.any { it.element == Element.PYRO } -> Pair((damage * 1.5f).toInt(), Reaction.MELT)
                    else -> Pair(damage, null)
                }
            }
            Element.HYDRO -> {
                when {
                    auras.any { it.element == Element.PYRO } -> Pair(damage * 2, Reaction.VAPORIZE)
                    else -> Pair(damage, null)
                }
            }
            else -> {
                Pair(damage, null)
            }
        }
    }

    fun attack(p: AbstractPlayer, m: AbstractMonster, baseDamage: Int, gauge: ElementalGauge, effect: AttackEffect) {
        val actualDamage = modifyEgtDamage(m, baseDamage, gauge.element)
        AbstractDungeon.actionManager.addToBottom(
            DamageAction(
                m,
                DamageInfo(
                    p,
                    actualDamage.first,
                    DamageType.NORMAL
                ),
                effect
            )
        )
        applyAura(p, m, gauge)
    }

    fun applyAura(p: AbstractPlayer, m: AbstractMonster, gauge: ElementalGauge) {
        val auras = getAuras(m)
        when (auras.size) {
            0 -> {
                AbstractDungeon.actionManager.addToBottom(
                    ApplyPowerAction(m, p, ElementalPowerFactory.build(m, gauge.taxed()))
                )
            }
            1 -> {
                // Reaction
            }
            else -> {
                // React with strongest? Check KQM
            }
        }
    }

    fun react(p: AbstractPlayer, m: AbstractMonster, aura: ElementalPower, gauge: ElementalGauge) {
        when (gauge.element) {
            Element.PYRO -> {
                when (aura.element) {
                    Element.PYRO -> {
                        extendGauge(p, m, aura, gauge.units)
                    }
                    Element.HYDRO -> {
                        reduceGauge(p, m, aura, gauge.units * 0.5f)
                    }
                    Element.CRYO -> {
                        reduceGauge(p, m, aura, gauge.units * 2f)
                    }
                    Element.ELECTRO -> {
                        overload()
                        reduceGauge(p, m, aura, gauge.units)
                    }
                    else -> {}
                }
            }
            Element.HYDRO -> {
                when (aura.element) {
                    Element.PYRO -> {
                        reduceGauge(p, m, aura, gauge.units * 2f)
                    }
                    Element.HYDRO -> {
                        extendGauge(p, m, aura, gauge.units)
                    }
                    Element.CRYO -> {
                        // Freeze
                    }
                    Element.ELECTRO -> {
                        electrocharge()
                        reduceGauge(p, m, aura, gauge.units * 0.4f)
                    }
                    else -> {}
                }
            }
            Element.CRYO -> {
                when (aura.element) {
                    Element.PYRO -> {
                        reduceGauge(p, m, aura, gauge.units * 0.5f)
                    }
                    Element.HYDRO -> {
                        // Freeze
                    }
                    Element.CRYO -> {
                        extendGauge(p, m, aura, gauge.units)
                    }
                    Element.ELECTRO -> {
                        // Superconduct
                    }
                    else -> {}
                }
            }
            Element.ELECTRO -> {
                when (aura.element) {
                    Element.PYRO -> {
                        overload()
                        reduceGauge(p, m, aura, gauge.units)
                    }
                    Element.HYDRO -> {
                        electrocharge()
                        reduceGauge(p, m, aura, gauge.units * 0.4f)
                    }
                    Element.CRYO -> {
                        // Superconduct
                    }
                    Element.ELECTRO -> {
                        extendGauge(p, m, aura, gauge.units)
                    }
                    else -> {}
                }
            }
            Element.ANEMO -> {
                swirl(m, p)
            }
            Element.GEO -> {
                // Crystallize
                reduceGauge(p, m, aura, gauge.units * 0.5f)
            }
            else -> {}
        }
    }

    fun reduceGauge(p: AbstractPlayer, m: AbstractMonster, aura: ElementalPower, amount: Float) {
        AbstractDungeon.actionManager.addToBottom(
            ApplyPowerAction(m, p, ElementalPowerFactory.build(m, ElementalGauge(aura.element, -amount)))
        )
    }

    fun extendGauge(p: AbstractPlayer, m: AbstractMonster, aura: ElementalPower, amount: Float) {
        val originalAmount = aura.asGauge().units
        if (amount > originalAmount) {
            val diff = amount - originalAmount
            AbstractDungeon.actionManager.addToBottom(
                ApplyPowerAction(m, p, ElementalPowerFactory.build(m, ElementalGauge(aura.element, diff)))
            )
        }
    }

    fun overload() {
        AbstractDungeon.actionManager.addToBottom(
            DamageAllEnemiesAction(
                null as AbstractCreature?,
                DamageInfo.createDamageMatrix(6, true),
                DamageType.THORNS,
                AttackEffect.FIRE
            )
        )
    }

    fun electrocharge() {
        AbstractDungeon.actionManager.addToBottom(
            DamageAllEnemiesAction(
                null as AbstractCreature?,
                DamageInfo.createDamageMatrix(4, true),
                DamageType.THORNS,
                AttackEffect.LIGHTNING
            )
        )
    }

    fun swirl(m: AbstractMonster, p: AbstractPlayer) {
        AbstractDungeon.actionManager.addToBottom(
            DamageAction(
                m,
                DamageInfo(
                    p, 2, DamageType.THORNS
                )
            )
        )
        val auras = getAuras(m)
        auras.forEach { aura ->
            AbstractDungeon.getCurrRoom().monsters.monsters.forEach { monster ->
                if (monster != m) {
                    applyAura(p, monster, aura.asGauge().taxed())
                }
            }
        }
    }
}
