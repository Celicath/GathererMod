package the_gatherer.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import the_gatherer.GathererMod;

public class VenomBarrierPower extends AbstractPower {
	private static final String RAW_ID = "VenomBarrier";
	public static final String POWER_ID = GathererMod.makeID(RAW_ID);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	public VenomBarrierPower(AbstractCreature owner, int amount) {
		this.name = NAME;
		this.ID = POWER_ID;
		this.owner = owner;
		this.amount = amount;
		this.updateDescription();
		this.type = PowerType.BUFF;
		this.isTurnBased = false;
		this.img = new Texture(GathererMod.GetPowerPath(RAW_ID));
	}

	public int onAttacked(DamageInfo info, int damageAmount) {
		if (info.owner != null && info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS && info.owner != this.owner) {
			this.flash();
			AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(info.owner, owner, new PoisonPower(info.owner, owner, this.amount), this.amount));
		}

		return damageAmount;
	}

	public void atStartOfTurn() {
		AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, POWER_ID));
	}

	public void updateDescription() {
		this.description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
	}
}
