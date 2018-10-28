package the_gatherer.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import the_gatherer.GathererMod;

public class BalancedGrowthPower extends AbstractPower {
	private static final String RAW_ID = "BalancedGrowth";
	public static final String POWER_ID = GathererMod.makeID(RAW_ID);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	public BalancedGrowthPower(AbstractCreature owner, int amount) {
		this.name = NAME;
		this.ID = POWER_ID;
		this.owner = owner;
		this.amount = amount;
		this.updateDescription();
		this.type = PowerType.BUFF;
		this.isTurnBased = false;
		this.img = new Texture(GathererMod.GetPowerPath(RAW_ID));
	}

	public void updateDescription() {
		this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
	}

	public void atStartOfTurnPostDraw() {
		StrengthPower strPower = (StrengthPower) AbstractDungeon.player.getPower(StrengthPower.POWER_ID);
		int str = strPower == null ? 0 : strPower.amount;
		DexterityPower dexPower = (DexterityPower) AbstractDungeon.player.getPower(DexterityPower.POWER_ID);
		int dex = dexPower == null ? 0 : dexPower.amount;
		if (str != dex) {
			this.flash();
			if (str < dex) {
				int gainAmount = Math.min(dex - str, this.amount);
				AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner, new StrengthPower(owner, gainAmount), gainAmount));
			} else {
				int gainAmount = Math.min(str - dex, this.amount);
				AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner, new DexterityPower(owner, gainAmount), gainAmount));
			}
		}
	}
}
