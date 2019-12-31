package the_gatherer.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import javassist.CtBehavior;

public class GridChooseUpToPatch {
	public static boolean patchEnabled = false;

	@SpirePatch(clz = GridCardSelectScreen.class, method = "update")
	public static class ChangeGridSelect {
		@SpireInsertPatch(locator = GridChooseUpToLocator.class)
		public static void Insert(GridCardSelectScreen __instance) {
			if (patchEnabled) {
				__instance.anyNumber = false;
			}
		}

		private static class GridChooseUpToLocator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
				Matcher finalMatcher = new Matcher.FieldAccessMatcher(GridCardSelectScreen.class, "anyNumber");
				return new int[]{LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher)[1]};
			}
		}

		@SpirePostfixPatch
		public static void Postfix(GridCardSelectScreen __instance) {
			if (patchEnabled) {
				__instance.anyNumber = true;
			}
		}
	}
}
