//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package the_gatherer.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.RetainCardPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import the_gatherer.actions.DrawHalfAction;

public class RemoveRetainCardPower extends AbstractPower {
	public static final String POWER_ID = "RemoveRetainCard";
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	public RemoveRetainCardPower(AbstractCreature owner, int amount) {
		this.name = NAME;
		this.ID = POWER_ID;
		this.owner = owner;
		this.updateDescription();
		this.type = PowerType.BUFF;
		this.amount = amount;
		this.isTurnBased = false;
		this.img = new Texture("img/powers/" + ID + ".png");
	}

	public void updateDescription() {
		this.description = DESCRIPTIONS[0];
	}

	@Override
	public void atStartOfTurn() {
		RetainCardPower rcp = (RetainCardPower) AbstractDungeon.player.getPower(RetainCardPower.POWER_ID);
		if (rcp != null) {
			if (rcp.amount > this.amount)
				AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new RetainCardPower(this.owner, -this.amount), -this.amount));
			else
				AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, RetainCardPower.POWER_ID));
		}
		AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
	}
}
