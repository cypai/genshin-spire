package genshinspire.actions

import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import genshinspire.cards.QCard
import genshinspire.egt.Element

class GainElementalEnergyAction(val element: Element, val amount: Int) : AbstractGameAction() {

    override fun update() {
        adjustCosts(AbstractDungeon.player.hand)
        adjustCosts(AbstractDungeon.player.drawPile)
        adjustCosts(AbstractDungeon.player.discardPile)
    }

    private fun adjustCosts(cards: CardGroup) {
        cards.group.filterIsInstance<QCard>()
            .forEach { adjustCost(it) }
    }

    private fun adjustCost(card: QCard) {
        when (element) {
            Element.NONE -> {
                card.updateCost(-amount)
            }
            card.element -> {
                card.updateCost(-2 * amount)
            }
            else -> {
                if (amount > 1) {
                    card.updateCost(-amount / 2)
                } else {
                    card.updateCost(-1)
                }
            }
        }
    }

}
