package the_gatherer.cards.Helper;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;

public abstract class AbstractNumberedCard extends CustomCard {
	public int playCount;
	public String DESCRIPTION;
	public String[] EXTENDED_DESCRIPTION;

	public AbstractNumberedCard(String id, String name, String img, int cost, String DESCRIPTION, String[] EXTENDED_DESCRIPTION, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
		super(id, name, img, cost, DESCRIPTION, type, color, rarity, target);
		this.DESCRIPTION = DESCRIPTION;
		this.EXTENDED_DESCRIPTION = EXTENDED_DESCRIPTION;
	}

	@Override
	public void triggerOnGlowCheck() {
		if (playCount == 0 || upgraded && playCount == 1) {
			glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
		} else {
			glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
		}
	}

	public void updateDescription() {
		if (upgraded && playCount == 0) {
			this.rawDescription = DESCRIPTION + EXTENDED_DESCRIPTION[0];
		} else if (playCount == 0 || upgraded && playCount == 1) {
			this.rawDescription = DESCRIPTION + EXTENDED_DESCRIPTION[1];
		} else {
			this.rawDescription = DESCRIPTION + EXTENDED_DESCRIPTION[2];
		}

		this.initializeDescription();
	}

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
		((AbstractNumberedCard) c).playCount = playCount;
		((AbstractNumberedCard) c).updateDescription();
		return c;
	}
}
