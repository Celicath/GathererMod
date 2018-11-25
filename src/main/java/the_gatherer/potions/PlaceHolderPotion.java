package the_gatherer.potions;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import the_gatherer.GathererMod;
import the_gatherer.patches.PotionRarityEnum;

public class PlaceHolderPotion extends SackPotion {
	private static final String RAW_ID = "PlaceHolderPotion";
	public static final String POTION_ID = GathererMod.makeID(RAW_ID);
	private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
	public static final String NAME = potionStrings.NAME;
	public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

	public PlaceHolderPotion() {
		super(NAME, POTION_ID, PotionRarityEnum.SPECIAL, PotionSize.H, PotionColor.WHITE);
		this.isThrown = false;

		updateDescription();
	}

	public void use(AbstractCreature target) {
		super.use(target);
	}

	public AbstractPotion makeCopy() {
		return new PlaceHolderPotion();
	}

	@Override
	public void updateDescription() {
		super.updateDescription(DESCRIPTIONS);
	}

	@Override
	public int getBasePotency() {
		return 1;
	}
}
