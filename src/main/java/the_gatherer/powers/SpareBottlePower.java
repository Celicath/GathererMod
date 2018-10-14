package the_gatherer.powers;

import basemod.helpers.CardTags;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.unique.SwordBoomerangAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.green.AfterImage;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import the_gatherer.interfaces.OnUsePotionEffect;
import the_gatherer.patches.CustomTags;

public class SpareBottlePower extends AbstractPower implements OnUsePotionEffect {
	public static final String POWER_ID = "SpareBottle";
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
		this.img = new Texture("img/powers/" + ID + ".png");
	}

	public void updateDescription() {
		this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
	}

	@Override
	public void onUseCard(AbstractCard card, UseCardAction action) {
		if (CardTags.hasTag(card, CustomTags.PotionCard))
			doAction();
	}

	public void doAction() {
		this.flash();
		AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, this.amount));
		AbstractDungeon.actionManager.addToBottom(
			new SwordBoomerangAction(AbstractDungeon.getMonsters().getRandomMonster(true),
			new DamageInfo(AbstractDungeon.player, this.amount, DamageInfo.DamageType.THORNS), 1));
	}

	public void onUsePotion() {
		doAction();
	}
}
