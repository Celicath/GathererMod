package the_gatherer.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.unique.SwordBoomerangAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.AbstractPower;
import the_gatherer.GathererMod;
import the_gatherer.interfaces.OnUsePotionEffect;

public class SpareBottlePower extends AbstractPower implements OnUsePotionEffect {
	private static final String RAW_ID = "SpareBottle";
	public static final String POWER_ID = GathererMod.makeID(RAW_ID);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	public SpareBottlePower(AbstractCreature owner, int amount) {
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
		this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
	}

	public void doAction() {
		this.flash();
		AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, this.amount));
		AbstractDungeon.actionManager.addToBottom(
				new SwordBoomerangAction(AbstractDungeon.getMonsters().getRandomMonster(true),
						new DamageInfo(AbstractDungeon.player, this.amount, DamageInfo.DamageType.THORNS), 1));
	}

	public void onUsePotion(AbstractPotion p) {
		doAction();
	}
}
