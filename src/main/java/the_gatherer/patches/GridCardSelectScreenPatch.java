package the_gatherer.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import javassist.CtBehavior;
import the_gatherer.GathererMod;
import the_gatherer.cards.SpareBottle;

public class GridCardSelectScreenPatch {
	@SpirePatch(clz = GridCardSelectScreen.class, method = "render")
	public static class ChangeUpgradeCard {
		@SpireInsertPatch(locator = Locator.class, localvars = {"hoveredCard"})
		public static void Insert(GridCardSelectScreen __instance, SpriteBatch sb, AbstractCard hoveredCard) {
			if (GathererMod.bottleCollector) {

				boolean cardChanged = false;
				if (hoveredCard != GathererMod.cardSelectScreenCard) {
					cardChanged = true;
					GathererMod.cardSelectScreenCard = hoveredCard;
				}
				if (hoveredCard != null && (cardChanged || !(__instance.upgradePreviewCard instanceof SpareBottle))) {
					__instance.upgradePreviewCard = SpareBottle.makeTransformedCopy(hoveredCard);
					__instance.upgradePreviewCard.drawScale = 0.875F;
				}
			}
		}
	}

	private static class Locator extends SpireInsertLocator {
		@Override
		public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
			Matcher finalMatcher = new Matcher.FieldAccessMatcher(GridCardSelectScreen.class, "upgradePreviewCard");
			return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
		}
	}
}
