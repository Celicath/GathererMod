package the_gatherer.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import the_gatherer.cards.Glitched;

public class StatusEnergyPatch {
	@SpirePatch(clz = AbstractCard.class, method = "renderEnergy")
	public static class RenderEnergyPrefix {
		@SpirePrefixPatch
		public static void Prefix(AbstractCard __instance, SpriteBatch sb) {
			if (__instance instanceof Glitched) {
				__instance.type = AbstractCard.CardType.SKILL;
			}
		}
	}

	@SpirePatch(clz = AbstractCard.class, method = "renderEnergy")
	public static class RenderEnergyPostfix {
		@SpirePostfixPatch
		public static void Postfix(AbstractCard __instance, SpriteBatch sb) {
			if (__instance instanceof Glitched) {
				__instance.type = AbstractCard.CardType.STATUS;
			}
		}
	}
}
