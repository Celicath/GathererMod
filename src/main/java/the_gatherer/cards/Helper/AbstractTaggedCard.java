package the_gatherer.cards.Helper;

import basemod.abstracts.CustomCard;
import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import the_gatherer.GathererMod;

public abstract class AbstractTaggedCard extends CustomCard {
	protected String rawName;

	public AbstractTaggedCard(String id, String name, String img, int cost, String rawDescription, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
		super(id, name, img, cost, rawDescription, type, color, rarity, target);
		this.rawName = this.name;
	}

	/* Interfaces */
	public abstract String getTagName(int tag);

	public void setTag(int tag) {
		this.misc = tag;
		updateTagDescription();
	}

	public void updateTagDescription() {
		this.name = this.rawName + " " + getTagName(this.misc);
		this.initializeTitle();
		this.initializeDescription();
		//this.update();
	}

	public abstract void upgradeEffect();

	/* Overrides */
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.name = this.rawName;
			this.upgradeName();
			this.upgradeEffect();
			this.rawName = this.name;
			this.updateTagDescription();
		}
	}

	@Override
	public AbstractCard makeStatEquivalentCopy() {
		AbstractCard c = super.makeStatEquivalentCopy();
		((AbstractTaggedCard) c).setTag(misc);
		return c;
	}
}
