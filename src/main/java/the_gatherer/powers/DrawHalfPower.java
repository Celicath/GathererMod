//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package the_gatherer.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import the_gatherer.GathererMod;
import the_gatherer.actions.DrawHalfAction;

public class DrawHalfPower extends AbstractPower {
	private static final String RAW_ID = "DrawHalf";
	public static final String POWER_ID = GathererMod.makeID(RAW_ID);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	public DrawHalfPower(AbstractCreature owner) {
		this.name = NAME;
		this.ID = POWER_ID;
		this.owner = owner;
		this.updateDescription();
		this.type = PowerType.BUFF;
		this.amount = 1;
		this.isTurnBased = false;
		this.img = new Texture(GathererMod.GetPowerPath(RAW_ID));
	}

	public void updateDescription() {
		this.description = DESCRIPTIONS[0];
	}

	@Override
	public void atStartOfTurn() {
		if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
			AbstractDungeon.actionManager.addToBottom(new DrawHalfAction(AbstractDungeon.player));
		}
	}
}
