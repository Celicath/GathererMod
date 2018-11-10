package the_gatherer.potions;

import basemod.abstracts.CustomPotion;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import the_gatherer.GathererMod;
import the_gatherer.actions.FirstAidPotionAction;
import the_gatherer.patches.PotionRarityEnum;

public class FirstAidPotion extends CustomPotion {
	private static final String RAW_ID = "FirstAidPotion";
	public static final String POTION_ID = GathererMod.makeID(RAW_ID);
	private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
	public static final String NAME = potionStrings.NAME;
	public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

	public FirstAidPotion() {
		super(NAME, POTION_ID, PotionRarityEnum.SPECIAL, PotionSize.H, PotionColor.WHITE);
		this.potency = this.getPotency();
		this.description = DESCRIPTIONS[0] + this.potency + DESCRIPTIONS[1];
		this.isThrown = false;
		this.tips.add(new PowerTip(this.name, this.description));

		GathererMod.setLesserPotionColors(liquidColor, hybridColor, spotsColor);
	}

	public void use(AbstractCreature target) {
		AbstractDungeon.actionManager.addToBottom(new FirstAidPotionAction(AbstractDungeon.player, AbstractDungeon.player, this.potency));
	}

	public AbstractPotion makeCopy() {
		return new FirstAidPotion();
	}

	public int getPotency(int ascensionLevel) {
		return 10;
	}
}
