package genshinspire.cards

import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.monsters.AbstractMonster
import genshinspire.DefaultMod.makeCardPath
import genshinspire.characters.TheDefault
import genshinspire.utils.makeId

class BennettE : ECard(
    makeId<BennettE>(),
    makeCardPath("Attack.png"),
    1,
    CardType.ATTACK,
    TheDefault.Enums.COLOR_GRAY,
    CardRarity.COMMON,
    CardTarget.ENEMY,
) {

    override fun use(p: AbstractPlayer, m: AbstractMonster) {

    }

    override fun upgrade() {

    }
}
