package the_gatherer.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ThornsPower;
import the_gatherer.GathererMod;
import the_gatherer.actions.GrassKnotDiscardAction;

public class GrassKnotPower extends AbstractPower {
	private static final String RAW_ID = "GrassKnot";
	public static final String POWER_ID = GathererMod.makeID(RAW_ID);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	public static final int BLOCK = 4;
	public static final int THORNS = 2;

	private boolean alreadyDiscarded;

	public GrassKnotPower(AbstractCreature owner, int amount) {
		this.name = NAME;
		this.ID = POWER_ID;
		this.owner = owner;
		this.amount = amount;
		this.updateDescription();
		this.type = PowerType.BUFF;
		this.isTurnBased = false;
		this.img = new Texture(GathererMod.GetPowerPath(RAW_ID));
		alreadyDiscarded = false;
	}

	public void discardForBenefit() {
		if (!alreadyDiscarded) {
			alreadyDiscarded = true;

			AbstractDungeon.actionManager.addToBottom(new GrassKnotDiscardAction(this.amount));
		}
	}
	public static void gainBlock(int num) {
		AbstractDungeon.actionManager.addToTop(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, num * BLOCK));
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ThornsPower(AbstractDungeon.player, num * THORNS), num * THORNS));
	}

	@Override
	public void updateDescription() {
		this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1] + BLOCK + DESCRIPTIONS[2] + THORNS + DESCRIPTIONS[3];
	}
}
