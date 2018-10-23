package the_gatherer.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.green.Defend_Green;
import com.megacrit.cardcrawl.cards.red.Strike_Red;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import the_gatherer.GathererMod;
import the_gatherer.cards.RustyPipe;
import the_gatherer.powers.PoisonMasteryPower;

public class LoadCardPatch {
	@SpirePatch(clz = CardLibrary.class, method = "getCopy", paramtypez = { String.class, int.class, int.class })
	public static class PoisonDescription {
		@SpireInsertPatch(rloc = 14, localvars = {"retVal"})
		public static void Insert(String key, int upgradeTime, int misc, AbstractCard retVal) {
			if (retVal.cardID.equals(Strike_Red.ID)) {
				GathererMod.logger.info("Strike_RED");
				retVal.baseDamage += retVal.misc;
			}
			if (retVal.cardID.equals(Defend_Green.ID)) {
				GathererMod.logger.info("Defend_Green");
				retVal.baseBlock += retVal.misc;
			}
		}
	}
}
