package the_gatherer.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.potions.BloodPotion;
import com.megacrit.cardcrawl.potions.BottledMiracle;
import com.megacrit.cardcrawl.potions.GhostInAJar;
import com.megacrit.cardcrawl.potions.PoisonPotion;

import static the_gatherer.patches.AbstractPlayerEnum.THE_GATHERER;

public class PotionPatch {
	@SpirePatch(clz = PotionHelper.class, method = "initialize")
	public static class AddCharacterSpecificPotions {
		@SpirePostfixPatch
		public static void Postfix(AbstractPlayer.PlayerClass chosenClass) {
			if (chosenClass == THE_GATHERER) {
				PotionHelper.potions.add(BloodPotion.POTION_ID);
				PotionHelper.potions.add(GhostInAJar.POTION_ID);
				PotionHelper.potions.add(BottledMiracle.POTION_ID);
				PotionHelper.potions.add(PoisonPotion.POTION_ID);
			}
		}
	}
}
