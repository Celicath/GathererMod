package the_gatherer.cards.Helper;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Iterator;

public abstract class AbstractNumberedCard extends CustomCard {
	public int playCount;

	public AbstractNumberedCard(String id, String name, String img, int cost, String rawDescription, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
		super(id, name, img, cost, rawDescription, type, color, rarity, target);
	}

	public abstract void updateDescription();

	public void initializeNumberedCard() {
		playCount = 0;
		updateDescription();
	}

	public void addPlayCount() {
		for (AbstractCard c : GetAllInBattleInstances.get(this.uuid)) {
			AbstractNumberedCard nc = (AbstractNumberedCard) c;
			nc.playCount++;
			nc.updateDescription();
		}
	}

	@Override
	public AbstractCard makeStatEquivalentCopy() {
		AbstractCard c = super.makeStatEquivalentCopy();
		((AbstractNumberedCard)c).playCount = playCount;
		((AbstractNumberedCard)c).updateDescription();
		return c;
	}
}
