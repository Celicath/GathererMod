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
	private boolean upgraded;

	public StoneFencePower(boolean upgraded) {
		this.name = NAME;
		this.ID = POWER_ID;
		this.upgraded = upgraded;
		this.updateDescription();
		this.type = PowerType.BUFF;
		this.owner = AbstractDungeon.player;
		this.isTurnBased = false;
		this.img = new Texture(GathererMod.GetPowerPath(RAW_ID));
	}

	public void upgrade() {
		this.upgraded = true;
		updateDescription();
	}

	public void updateDescription() {
		if (this.upgraded) {
			this.description = DESCRIPTIONS[1];
		} else {
			this.description = DESCRIPTIONS[0];
		}
	}

	public void activate() {
		if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
			this.flash();

			Thrower c = new Thrower();
			c.setDamage(GathererMod.remainedBlock);
			if (upgraded) {
				c.upgrade();
			}
			AbstractDungeon.actionManager.addToTop(new MakeTempCardInHandAction(c, 1, false));
		}
	}
}
