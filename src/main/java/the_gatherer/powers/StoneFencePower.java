package the_gatherer.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import the_gatherer.GathererMod;
import the_gatherer.cards.Thrower;

public class StoneFencePower extends AbstractPower {
	private static final String RAW_ID = "StoneFence";
	public static final String POWER_ID = GathererMod.makeID(RAW_ID);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
	private int upgradeAmount = 0;

	/// amount : 2 = upgraded, 1 = unupgraded
	public StoneFencePower(int amount) {
		this.name = NAME;
		this.ID = POWER_ID;
		this.amount = 1;
		this.upgradeAmount = amount - 1;
		this.updateDescription();
		this.type = PowerType.BUFF;
		this.owner = AbstractDungeon.player;
		this.isTurnBased = false;
		this.img = new Texture(GathererMod.GetPowerPath(RAW_ID));
	}

	@Override
	public void stackPower(int amount) {
		this.amount++;
		if (amount == 2)
			this.upgradeAmount++;
		updateDescription();
	}

	public void updateDescription() {
		this.description = DESCRIPTIONS[0];
		if (amount > upgradeAmount) {
			if (upgradeAmount > 0) {
				this.description += upgradeAmount + DESCRIPTIONS[1] + DESCRIPTIONS[2] + (amount - upgradeAmount) + DESCRIPTIONS[3];
			} else {
				this.description += (amount - upgradeAmount) + DESCRIPTIONS[3];
			}
		} else {
			this.description += upgradeAmount + DESCRIPTIONS[1];
		}
		this.description += DESCRIPTIONS[4];
	}

	public void activate() {
		if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
			this.flash();

			Thrower c = new Thrower();
			c.setDamage(GathererMod.remainedBlock);

			Thrower cUp = new Thrower();
			cUp.setDamage(GathererMod.remainedBlock);
			cUp.upgrade();
			if (upgradeAmount > 0)
				AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(cUp, upgradeAmount, false));
			if (amount > upgradeAmount)
				AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(c, amount - upgradeAmount, false));
		}
	}
}
