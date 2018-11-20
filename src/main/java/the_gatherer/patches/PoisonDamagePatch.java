package the_gatherer.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.unique.PoisonLoseHpAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.PoisonPower;
import javassist.CtBehavior;
import the_gatherer.GathererMod;
import the_gatherer.powers.PoisonMasteryPower;

public class PoisonDamagePatch {
	@SpirePatch(clz = PoisonLoseHpAction.class, method = "update")
	public static class LostHPUpdate {
		@SpireInsertPatch(locator = Locator.class, localvars = {"amount"})
		public static void Insert(PoisonLoseHpAction __instance, @ByRef int[] amount) {
			amount[0] = GathererMod.calcPoisonDamageWithPower(amount[0]);
		}
	}

	private static class Locator extends SpireInsertLocator {
		@Override
		public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
			Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractCreature.class, "damage");
			return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
		}
	}

	@SpirePatch(clz = AbstractCreature.class, method = "renderRedHealthBar")
	public static class HealthBarRender {
		@SpireInsertPatch(rloc = 29, localvars = {"poisonAmt"})
		public static void Insert(AbstractCreature __instance, SpriteBatch sb, float x, float y, @ByRef int[] poisonAmt) {
			poisonAmt[0] = GathererMod.calcPoisonDamageWithPower(poisonAmt[0]);
		}
	}

	@SpirePatch(clz = PoisonPower.class, method = "updateDescription")
	public static class PoisonDescription {
		@SpirePostfixPatch
		public static void PostFix(PoisonPower __instance) {
			PoisonMasteryPower pmp = (PoisonMasteryPower) AbstractDungeon.player.getPower(PoisonMasteryPower.POWER_ID);
			if (pmp != null) {
				int newAmount = GathererMod.calcPoisonDamage(__instance.amount, pmp.amount);
				if (__instance.owner != null && !__instance.owner.isPlayer) {
					__instance.description = PoisonPower.DESCRIPTIONS[2] + newAmount + PoisonPower.DESCRIPTIONS[1];
				} else {
					__instance.description = PoisonPower.DESCRIPTIONS[0] + newAmount + PoisonPower.DESCRIPTIONS[1];
				}
			}
		}
	}
}
