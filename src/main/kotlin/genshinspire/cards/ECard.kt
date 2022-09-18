package genshinspire.cards

import basemod.abstracts.CustomCard
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.localization.CardStrings
import genshinspire.tags.CustomTags.CHAR_E

abstract class ECard(
    id: String,
    img: String,
    cost: Int,
    type: CardType,
    color: CardColor,
    rarity: CardRarity,
    target: CardTarget
) : CustomCard(
    id,
    CardCrawlGame.languagePack.getCardStrings(id).NAME,
    img,
    cost,
    CardCrawlGame.languagePack.getCardStrings(id).DESCRIPTION,
    type,
    color,
    rarity,
    target
) {

    init {
        this.tags.add(CHAR_E)
    }

}
