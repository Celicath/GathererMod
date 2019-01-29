package the_gatherer.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.SoulGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import the_gatherer.GathererMod;
import the_gatherer.modules.CaseMod;
import the_gatherer.relics.ExplorersPath;

public class PostObtainCardHook {
	@SpirePatch(clz = SoulGroup.class, method = "obtain")
	public static class SoulGroupObtainPatch {
		public static void Postfix(SoulGroup __instance, AbstractCard card, boolean obtainCard) {
			CaseMod.publishPostObtainCard(card);

			// ExplorersPath
			if (AbstractDungeon.player.hasRelic(ExplorersPath.ID) && GathererMod.explorersPathBestOption.contains(card)) {
				ExplorersPath ep = (ExplorersPath) AbstractDungeon.player.getRelic(ExplorersPath.ID);
				ep.chooseCorrectly();
			}
		}
	}
}
