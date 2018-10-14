package the_gatherer.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import java.util.Iterator;

public class PollutePower extends AbstractPower {
	public static final String POWER_ID = "Pollute";
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	public PollutePower(AbstractCreature owner, int amount, String newName) {
		this.name = newName;
		this.ID = POWER_ID;
		this.owner = owner;
		this.amount = amount;
		this.updateDescription();
		this.type = PowerType.BUFF;
		this.isTurnBased = false;
		this.img = new Texture("img/powers/" + ID + ".png");
	}

	public PollutePower(AbstractCreature owner, int amount) {
		this(owner, amount, NAME);
	}

	public void updateDescription() {
		this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
	}

	public void atStartOfTurn() {
		this.flash();
		AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
		if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
			this.flash();
			Iterator var3 = AbstractDungeon.getMonsters().monsters.iterator();

			while(var3.hasNext()) {
				AbstractMonster monster = (AbstractMonster)var3.next();
				if (!monster.isDead && !monster.isDying) {
					AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(monster, this.owner, new PoisonPower(monster, this.owner, this.amount), this.amount));
					AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(monster, this.owner, new WeakPower(monster, this.amount, false), this.amount));
					AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(monster, this.owner, new VulnerablePower(monster, this.amount, false), this.amount));
				}
			}
		}
	}
}
