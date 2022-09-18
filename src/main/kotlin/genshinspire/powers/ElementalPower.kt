package genshinspire.powers

import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.powers.AbstractPower
import genshinspire.egt.Element
import genshinspire.egt.ElementalGauge

abstract class ElementalPower(owner: AbstractCreature, val element: Element, amount: Int) : AbstractPower() {
    init {
        this.owner = owner
        this.amount = amount
    }

    companion object {
        const val SCALING = 100f
    }

    fun asGauge(): ElementalGauge {
        return ElementalGauge(element, amount.toFloat() / SCALING)
    }
}

class PyroPower(owner: AbstractCreature, amount: Int) : ElementalPower(owner, Element.PYRO, amount)
class HydroPower(owner: AbstractCreature, amount: Int) : ElementalPower(owner, Element.HYDRO, amount)
class CryoPower(owner: AbstractCreature, amount: Int) : ElementalPower(owner, Element.CRYO, amount)
class ElectroPower(owner: AbstractCreature, amount: Int) : ElementalPower(owner, Element.ELECTRO, amount)
