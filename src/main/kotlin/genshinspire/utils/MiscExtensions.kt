package genshinspire.utils

import com.megacrit.cardcrawl.cards.AbstractCard
import genshinspire.DefaultMod

inline fun <reified T : AbstractCard> makeId(): String = DefaultMod.makeID(T::class.java.simpleName)
