package the_gatherer.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.unique.PoisonLoseHpAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.PoisonPower;
import javassist.CtBehavior;
import the_gatherer.powers.PoisonMasteryPower;

public class PoisonDamagePatch {
	@SpirePatch(clz = PoisonLoseHpAction.class, method = "update")
	public static class LostHPUpdate {
		@SpireInsertPatch(locator = Locator.class, localvars = {"amount"})
		public static void Insert(PoisonLoseHpAction __instance, @ByRef int[] amount) {
			PoisonMasteryPower pmp = (PoisonMasteryPower) AbstractDungeon.player.getPower(PoisonMasteryPower.POWER_ID);
			if (pmp != null) {
				amount[0] *= pmp.amount + 1;
			}
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
			PoisonMasteryPower pmp = (PoisonMasteryPower) AbstractDungeon.player.getPower(PoisonMasteryPower.POWER_ID);
			if (pmp != null) {
				poisonAmt[0] *= pmp.amount + 1;
			}
		}
	}

	@SpirePatch(clz = PoisonPower.class, method = "updateDescription")
	public static class PoisonDescription {
		@SpireInsertPatch(rloc = 5, localvars = {"description", "amount"})
		public static void Insert(PoisonPower __instance, @ByRef String[] description, int amount) {
			PoisonMasteryPower pmp = (PoisonMasteryPower) AbstractDungeon.player.getPower(PoisonMasteryPower.POWER_ID);
			if (pmp != null) {
				description[0] = PoisonPower.DESCRIPTIONS[2] + "[#7fff00]" + (amount * (pmp.amount + 1)) + PoisonPower.DESCRIPTIONS[1];
			}
		}
	}
}
