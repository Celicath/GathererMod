package the_gatherer.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.AbstractPower;
import the_gatherer.GathererMod;
import the_gatherer.interfaces.OnUsePotionEffect;

public class FeelingFinePower extends AbstractPower implements OnUsePotionEffect {
	private static final String RAW_ID = "FeelingFine";
	public static final String POWER_ID = GathererMod.makeID(RAW_ID);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
	private static final int POTION_AMT = 4;

	public FeelingFinePower(AbstractCreature owner) {
		this.name = NAME;
		this.ID = POWER_ID;
		this.owner = owner;
		this.amount = POTION_AMT;
		updateDescription();
		this.type = PowerType.BUFF;
		this.isTurnBased = false;
		this.img = new Texture(GathererMod.GetPowerPath(RAW_ID));
	}

	@Override
	public void onUsePotion(AbstractPotion p) {
		--this.amount;
		if (this.amount == 0) {
			this.flash();
			this.amount = POTION_AMT;
			AbstractDungeon.actionManager.addToBottom(new RemoveDebuffsAction(AbstractDungeon.player));
		}

		this.updateDescription();
	}

	public void stackPower(int stackAmount) {
		// Does not stack
	}

	@Override
	public void updateDescription() {
		if (this.amount == 1) {
			this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
		} else {
			this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[2];
		}
	}

	@Override
	public void atStartOfTurn() {
		this.amount = POTION_AMT;
		this.updateDescription();
	}
}
