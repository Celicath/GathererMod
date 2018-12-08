package the_gatherer.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.FastCardObtainEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import the_gatherer.GathererMod;
import the_gatherer.modules.CaseMod;
import the_gatherer.relics.ExplorersPath;

public class PostObtainCardHook {
	@SpirePatch(clz = FastCardObtainEffect.class, method = "update")
	public static class FastCardObtainEffectPatch {
		@SpireInsertPatch(rloc = 9, localvars = {"card"})
		public static void Insert(FastCardObtainEffect __instance, AbstractCard card) {
			CaseMod.publishPostObtainCard(card);

			// ExplorersPath
			if (AbstractDungeon.player.hasRelic(ExplorersPath.ID) && GathererMod.explorersPathBestOption.contains(card)) {
				ExplorersPath ep = (ExplorersPath)AbstractDungeon.player.getRelic(ExplorersPath.ID);
				ep.activate();
			}
		}
	}

	@SpirePatch(clz = ShowCardAndObtainEffect.class, method = "update")
	public static class ShowCardAndObtainEffectPatch {
		@SpireInsertPatch(rloc = 9, localvars = {"card"})
		public static void Insert(ShowCardAndObtainEffect __instance, AbstractCard card) {
			CaseMod.publishPostObtainCard(card);
		}
	}
}
