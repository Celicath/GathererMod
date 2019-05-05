package the_gatherer.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import the_gatherer.GathererMod;

public class PotionSackPatch {
	@SpirePatch(clz = AbstractPlayer.class, method = "combatUpdate")
	public static class PotionSaceUpdatePatch {
		@SpirePostfixPatch
		public static void Postfix(AbstractPlayer __instance) {
			GathererMod.potionSack.update();
		}
	}

	@SpirePatch(clz = EnergyPanel.class, method = "renderOrb")
	public static class PotionSackRenderPatch {
		@SpirePostfixPatch
		public static void Postfix(EnergyPanel __instance, SpriteBatch sb) {
			GathererMod.potionSack.render(sb);
			GathererMod.potionSack.potionUi.render(sb);
		}
	}
}
