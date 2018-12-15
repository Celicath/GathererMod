package the_gatherer.cards.Helper;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import the_gatherer.GathererMod;

public class GathererCardHelper {
	private static final String RAW_ID = "FlowerSuffix";
	public static final String ID = GathererMod.makeID(RAW_ID);

	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = GathererMod.GetCardPath(RAW_ID);
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;

	public static String FlowerSuffix(int upgrade_count) {
		if (upgrade_count >= 0 && upgrade_count < EXTENDED_DESCRIPTION.length) {
			return EXTENDED_DESCRIPTION[upgrade_count];
		} else {
			return "";
		}
	}
}
