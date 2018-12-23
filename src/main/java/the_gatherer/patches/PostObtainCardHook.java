package the_gatherer.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.FastCardObtainEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import javassist.CtBehavior;
import the_gatherer.GathererMod;
import the_gatherer.modules.CaseMod;
import the_gatherer.relics.ExplorersPath;

public class PostObtainCardHook {
	@SpirePatch(clz = FastCardObtainEffect.class, method = "update")
	public static class FastCardObtainEffectPatch {
		@SpireInsertPatch(locator = FastCardLocator.class, localvars = {"card"})
		public static void Insert(FastCardObtainEffect __instance, AbstractCard card) {
			CaseMod.publishPostObtainCard(card);

			// ExplorersPath
			if (AbstractDungeon.player.hasRelic(ExplorersPath.ID) && GathererMod.explorersPathBestOption.contains(card)) {
				ExplorersPath ep = (ExplorersPath) AbstractDungeon.player.getRelic(ExplorersPath.ID);
				ep.chooseCorrectly();
			}
		}
	}

	private static class FastCardLocator extends SpireInsertLocator {
		@Override
		public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
			Matcher finalMatcher = new Matcher.FieldAccessMatcher(FastCardObtainEffect.class, "isDone");
			return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
		}
	}

	@SpirePatch(clz = ShowCardAndObtainEffect.class, method = "update")
	public static class ShowCardAndObtainEffectPatch {
		@SpireInsertPatch(locator = ShowCardLocator.class, localvars = {"card"})
		public static void Insert(ShowCardAndObtainEffect __instance, AbstractCard card) {
			CaseMod.publishPostObtainCard(card);
		}
	}

	private static class ShowCardLocator extends SpireInsertLocator {
		@Override
		public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
			Matcher finalMatcher = new Matcher.FieldAccessMatcher(ShowCardAndObtainEffect.class, "isDone");
			return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
		}
	}
}
