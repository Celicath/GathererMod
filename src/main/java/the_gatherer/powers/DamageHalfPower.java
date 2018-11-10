package the_gatherer.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import the_gatherer.GathererMod;

public class DamageHalfPower extends AbstractPower {
	private static final String RAW_ID = "DamageHalf";
	public static final String POWER_ID = GathererMod.makeID(RAW_ID);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	private boolean justApplied;

	public DamageHalfPower(AbstractCreature owner, int amount, boolean isSourceMonster) {
		this.name = NAME;
		this.ID = POWER_ID;
		this.owner = owner;
		this.amount = amount;
		this.updateDescription();
		this.type = PowerType.DEBUFF;
		this.isTurnBased = false;
		this.img = new Texture(GathererMod.GetPowerPath(RAW_ID));
		this.justApplied = isSourceMonster;
	}

	@Override
	public float atDamageGive(float damage, DamageInfo.DamageType type) {
		if (type == DamageInfo.DamageType.NORMAL) {
			for (int i = 0; i < amount; i++) {
				damage /= 2;
			}
			return damage;
		} else {
			return damage;
		}
	}

	public void atEndOfRound() {
		if (this.justApplied) {
			this.justApplied = false;
		} else {
			AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
		}
	}

	public void updateDescription() {
		if (this.amount < 24) {
			this.description = DESCRIPTIONS[0] + (1 << this.amount) + DESCRIPTIONS[1];
		} else {
			this.description = DESCRIPTIONS[2];
		}
	}
}
