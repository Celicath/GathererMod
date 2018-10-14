package the_gatherer.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.vfx.FastCardObtainEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import the_gatherer.modules.CaseMod;

public class PostObtainCardHook {
	@SpirePatch(clz = FastCardObtainEffect.class, method = "update")
	public static class FastCardObtainEffectPatch {
		@SpireInsertPatch(rloc = 9, localvars = {"card"})
		public static void Insert(FastCardObtainEffect __instance, AbstractCard card) {
			CaseMod.publishPostObtainCard(card);
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
