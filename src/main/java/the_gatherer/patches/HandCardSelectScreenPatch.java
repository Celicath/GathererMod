package the_gatherer.patches;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.screens.select.HandCardSelectScreen;
import javassist.CtBehavior;
import the_gatherer.GathererMod;
import the_gatherer.actions.EnchantAction;
import the_gatherer.actions.TransmuteAction;

public class HandCardSelectScreenPatch {
	@SpirePatch(clz = HandCardSelectScreen.class, method = "update")
	public static class ChangeUpgradeCard {
		@SpirePostfixPatch
		public static void Postfix(HandCardSelectScreen __instance) {
			if (GathererMod.enchantAmount > 0 || GathererMod.transmuteAmount > 0) {
				boolean cardChanged = false;
				AbstractCard c = null;
				if (__instance.selectedCards.size() > 0) {
					c = __instance.selectedCards.getTopCard();
				}
				if (c != GathererMod.cardSelectScreenCard) {
					cardChanged = true;
					GathererMod.cardSelectScreenCard = c;
				}
				if (c != null) {
					if (GathererMod.enchantAmount > 0) {
						__instance.upgradePreviewCard = c.makeStatEquivalentCopy();
						EnchantAction.doEnchant(__instance.upgradePreviewCard, GathererMod.enchantAmount);
					} else {
						GathererMod.transmuteAnimTimer -= Gdx.graphics.getDeltaTime();
						if (GathererMod.transmuteAnimTimer < 0.0F || cardChanged) {
							__instance.upgradePreviewCard = TransmuteAction.getTransformedCard(c, GathererMod.transmuteAmount > 1);
							__instance.upgradePreviewCard.update();
							GathererMod.transmuteAnimTimer = 0.1F;
						}
					}
				}
			}
		}
	}

	@SpirePatch(clz = HandCardSelectScreen.class, method = "render")
	public static class FixRender {
		@SpireInsertPatch(locator = Locator.class)
		public static void Insert(HandCardSelectScreen __instance, SpriteBatch sb) {
			if (GathererMod.enchantAmount > 0) {
				__instance.upgradePreviewCard.isDamageModified = true;
				__instance.upgradePreviewCard.isBlockModified = false;
				__instance.upgradePreviewCard.isMagicNumberModified = true;
				__instance.upgradePreviewCard.isCostModified = true;
			}
		}
	}

	private static class Locator extends SpireInsertLocator {
		@Override
		public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
			Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractCard.class, "render");
			return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
		}
	}
}
