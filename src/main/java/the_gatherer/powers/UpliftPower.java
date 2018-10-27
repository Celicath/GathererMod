package the_gatherer.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.AbstractPower;
import the_gatherer.GathererMod;
import the_gatherer.cards.Light;
import the_gatherer.interfaces.OnUsePotionEffect;

public class UpliftPower extends AbstractPower implements OnUsePotionEffect {
	private static final String RAW_ID = "Uplift";
	public static final String POWER_ID = GathererMod.makeID(RAW_ID);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
	private AbstractPlayer p;

	public UpliftPower(AbstractCreature owner, int amount) {
		this.name = NAME;
		this.ID = POWER_ID;
		this.owner = owner;
		this.amount = amount;
		this.description = DESCRIPTIONS[0];
		this.type = PowerType.BUFF;
		this.isTurnBased = false;
		this.img = new Texture(GathererMod.GetPowerPath(RAW_ID));
		this.p = AbstractDungeon.player;
	}

	@Override
	public void stackPower(int stackAmount) {
		if (stackAmount > this.amount) {
			this.fontScale = 8.0F;
			this.amount = stackAmount;
			this.updateDescription();
		}
	}

	@Override
	public void updateDescription() {
		if (this.amount == 1) {
			this.description = DESCRIPTIONS[this.amount - 1];
		}
	}

	@Override
	public void onUsePotion(AbstractPotion p) {
		CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

		for (AbstractCard c : this.p.hand.group) {
			if (c.costForTurn > 1) {
				group.addToRandomSpot(c);
			}
		}

		if (group.size() > 0) {
			this.flash();

			AbstractCard c = group.getTopCard();
			c.costForTurn = 1;
			c.isCostModifiedForTurn = true;
			if (this.amount == 2) {
				c.cost = 1;
				c.isCostModified = true;
			}
		}
	}

	@Override
	public void onAfterCardPlayed(AbstractCard usedCard) {
		if (!(usedCard instanceof Light))
			AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
	}
}
