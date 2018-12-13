package the_gatherer.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CtBehavior;
import the_gatherer.GathererMod;

public class BlockExpirePatch {
	@SpirePatch(clz = GameActionManager.class, method = "getNextAction")
	public static class GetBlockBeforeExpire {
		@SpireInsertPatch(locator = BeforeLocator.class)
		public static void Insert(GameActionManager __instance) {
			if (AbstractDungeon.player != null) {
				GathererMod.remainedBlock = AbstractDungeon.player.currentBlock;
				GathererMod.activateStoneFence();
			}
		}
	}

	private static class BeforeLocator extends SpireInsertLocator {
		@Override
		public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
			Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "hasPower");
			return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
		}
	}
}
