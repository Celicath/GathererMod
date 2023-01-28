package the_gatherer.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import the_gatherer.GathererMod;

public class PotionSackPatch {
	@SpirePatch(clz = AbstractPlayer.class, method = "combatUpdate")
	public static class PotionSackUpdatePatch {
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

	@SpirePatch2(clz = AbstractPlayer.class, method = "updateFullKeyboardCardSelection")
	public static class SkipCardSelectPatch {
		@SpirePrefixPatch
		public static SpireReturn<Boolean> Prefix() {
			if (GathererMod.potionSack != null && GathererMod.potionSack.potionUi.targetMode) {
				return SpireReturn.Return(false);
			} else {
				return SpireReturn.Continue();
			}
		}
	}
}
