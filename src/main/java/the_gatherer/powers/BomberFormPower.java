package the_gatherer.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import the_gatherer.actions.ObtainLesserPotionAction;
import the_gatherer.potions.LesserExplosivePotion;
import the_gatherer.potions.LesserPoisonPotion;

public class BomberFormPower extends AbstractPower {
	public static final String POWER_ID = "BomberForm";
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	public BomberFormPower(AbstractCreature owner, int amount, String newName) {
		this.name = newName;
		this.ID = POWER_ID;
		this.owner = owner;
		this.amount = amount;
		this.updateDescription();
		this.type = PowerType.BUFF;
		this.isTurnBased = false;
		this.img = new Texture("img/powers/" + ID + ".png");
	}

	public BomberFormPower(AbstractCreature owner, int amount) {
		this(owner, amount, NAME);
	}

	public void atStartOfTurn() {
		this.flash();
		AbstractDungeon.actionManager.addToBottom(new ObtainLesserPotionAction(new LesserExplosivePotion()));
	}

	public void updateDescription() {
		this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
	}
}
