package the_gatherer.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import the_gatherer.events.BottleCollector;

public class SpareBottleEventPatch {

	@SpirePatch(clz = AbstractPotion.class, method = "canDiscard")
	public static class Discard {
		@SpirePostfixPatch
		public static boolean Postfix(boolean __return, AbstractPotion __instance) {
			if (AbstractDungeon.getCurrRoom().event instanceof BottleCollector) {
				return false;
			} else {
				return __return;
			}
		}
	}

	@SpirePatch(clz = AbstractPotion.class, method = "canUse")
	public static class Use {
		@SpirePostfixPatch
		public static boolean Postfix(boolean __return, AbstractPotion __instance) {
			if (AbstractDungeon.getCurrRoom().event instanceof BottleCollector) {
				return false;
			} else {
				return __return;
			}
		}
	}
}
