package the_gatherer.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import javassist.CtBehavior;
import the_gatherer.GathererMod;

public class PotionSackPatch {
	@SpirePatch(clz = AbstractPlayer.class, method = "combatUpdate")
	public static class Update
	{
		@SpirePostfixPatch
		public static void Postfix(AbstractPlayer __instance) {
			GathererMod.potionSack.update();
		}
	}

	@SpirePatch(clz = AbstractPlayer.class, method = "render")
	public static class Render
	{
		@SpireInsertPatch(locator=RenderLocator.class)
		public static void Insert(AbstractPlayer __instance, SpriteBatch sb) {
			GathererMod.potionSack.render(sb);
		}
	}

	private static class RenderLocator extends SpireInsertLocator
	{
		@Override
		public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
		{
			Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "renderHealth");
			return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
		}
	}

	@SpirePatch(clz = TopPanel.class, method = "render")
	public static class UiRender
	{
		@SpirePrefixPatch
		public static void Prefix(TopPanel __instance, SpriteBatch sb) {
			GathererMod.potionSack.potionUi.render(sb);
		}
	}
}
