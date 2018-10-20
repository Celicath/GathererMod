package the_gatherer.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import the_gatherer.cards.Light;

public class ShadowPower extends AbstractPower {
	public static final String POWER_ID = "ShadowPower";
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	public ShadowPower(AbstractCreature owner) {
		this.name = NAME;
		this.ID = POWER_ID;
		this.owner = owner;
		this.amount = -1;
		this.description = DESCRIPTIONS[0];
		this.type = PowerType.BUFF;
		this.isTurnBased = false;
		this.img = new Texture("img/powers/" + ID + ".png");
	}

	@Override
	public void onAfterCardPlayed(AbstractCard usedCard) {
		if (usedCard instanceof Light)
			AbstractDungeon.actionManager.addToTop(new DrawCardAction(this.owner, 2));
		AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
	}
}
