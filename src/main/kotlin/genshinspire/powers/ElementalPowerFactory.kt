package genshinspire.powers

import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.powers.AbstractPower
import genshinspire.egt.Element
import genshinspire.egt.ElementalGauge

object ElementalPowerFactory {
    fun build(owner: AbstractCreature, gauge: ElementalGauge): AbstractPower {
        val amount = (gauge.units * ElementalPower.SCALING).toInt()
        return when (gauge.element) {
            Element.PYRO -> PyroPower(owner, amount)
            Element.HYDRO -> HydroPower(owner, amount)
            Element.CRYO -> CryoPower(owner, amount)
            Element.ELECTRO -> ElectroPower(owner, amount)
            else -> throw IllegalArgumentException("${gauge.element} not appropriate for ElementalPower")
        }
    }
}
