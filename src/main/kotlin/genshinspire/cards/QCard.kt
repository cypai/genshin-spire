package genshinspire.cards

import basemod.abstracts.CustomCard
import com.megacrit.cardcrawl.core.CardCrawlGame
import genshinspire.egt.Element
import genshinspire.tags.CustomTags.CHAR_Q

abstract class QCard(
    id: String,
    img: String,
    val baseCost: Int,
    type: CardType,
    color: CardColor,
    rarity: CardRarity,
    target: CardTarget
) : CustomCard(
    id,
    CardCrawlGame.languagePack.getCardStrings(id).NAME,
    img,
    baseCost,
    CardCrawlGame.languagePack.getCardStrings(id).DESCRIPTION,
    type,
    color,
    rarity,
    target
) {

    init {
        this.tags.add(CHAR_Q)
    }

    abstract val element: Element

}
