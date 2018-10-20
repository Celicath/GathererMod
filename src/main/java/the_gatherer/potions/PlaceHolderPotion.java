package the_gatherer.potions;

import basemod.abstracts.CustomPotion;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import the_gatherer.patches.PotionRarityEnum;

public class PlaceHolderPotion extends CustomPotion {
	public static final String POTION_ID = "PlaceHolderPotion";
	private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
	public static final String NAME = potionStrings.NAME;
	public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

	public PlaceHolderPotion() {
		super(NAME, POTION_ID, PotionRarityEnum.SPECIAL, PotionSize.H, PotionColor.WHITE);
		this.potency = this.getPotency();
		this.description = DESCRIPTIONS[0];
		this.isThrown = false;
		this.tips.add(new PowerTip(this.name, this.description));
	}

	public void use(AbstractCreature target) {
	}

	public AbstractPotion makeCopy() {
		return new PlaceHolderPotion();
	}

	public int getPotency(int ascensionLevel) {
		return 1;
	}
}