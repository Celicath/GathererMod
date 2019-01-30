package the_gatherer.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDrawPileEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
import javassist.CtBehavior;
import the_gatherer.GathererMod;
import the_gatherer.actions.ActivateExhaustEffectsAction;
import the_gatherer.cards.ScrollOfWall;

public class ScrollOfWallPatch {
	public static class ScrollOfWallUtil {
		// -1 = No Exhaust
		// 0~ = Exhaust and gain N plated armor
		public static int needToExhaust(AbstractCard card) {
			boolean hasOne = false;
			int result = 0;
			if (card.type == AbstractCard.CardType.STATUS) {
				for (AbstractCard c : AbstractDungeon.player.hand.group) {
					if (c instanceof ScrollOfWall) {
						hasOne = true;
						result += c.upgraded ? 1 : 0;
					}
				}
			}
			return hasOne ? result : -1;
		}

		public static void setExhaustStatus(AbstractGameEffect age, int num) {
			GathererMod.gainCardEffectToExhaust.add(age);
			for (AbstractCard hc : AbstractDungeon.player.hand.group) {
				if (hc instanceof ScrollOfWall) {
					hc.flash();
				}
			}
			if (num > 0) {
				AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(
						AbstractDungeon.player,
						AbstractDungeon.player,
						new PlatedArmorPower(AbstractDungeon.player, num), num));
			}
			// AbstractDungeon.actionManager.addToTop(new GainPlatedArmorThresholdAction(num, 5));
		}

		public static void exhaustIncomingCard(AbstractGameEffect age, AbstractCard c) {
			AbstractDungeon.actionManager.addToTop(new ActivateExhaustEffectsAction(c));
			c.unhover();
			c.untip();
			c.stopGlowing();

			AbstractDungeon.effectsQueue.add(new ExhaustCardEffect(c));
			AbstractDungeon.player.exhaustPile.addToTop(c);
			GathererMod.gainCardEffectToExhaust.remove(age);
		}
	}

	@SpirePatch(
			clz = ShowCardAndAddToDiscardEffect.class,
			method = SpirePatch.CONSTRUCTOR,
			paramtypez = {
					AbstractCard.class,
					float.class,
					float.class
			}
	)
	public static class AddToDiscardConstruct1 {
		@SpirePostfixPatch
		public static void Postfix(ShowCardAndAddToDiscardEffect __instance, AbstractCard srcCard, float x, float y) {
			int num = ScrollOfWallUtil.needToExhaust(srcCard);
			if (num >= 0) {
				AbstractDungeon.player.discardPile.removeCard(srcCard);
				__instance.duration *= 0.5f;
				ScrollOfWallUtil.setExhaustStatus(__instance, num);
			}
		}
	}

	@SpirePatch(
			clz = ShowCardAndAddToDiscardEffect.class,
			method = SpirePatch.CONSTRUCTOR,
			paramtypez = {
					AbstractCard.class
			}
	)
	public static class AddToDiscardConstruct2 {
		@SpirePostfixPatch
		public static void Postfix(ShowCardAndAddToDiscardEffect __instance, AbstractCard card) {
			int num = ScrollOfWallUtil.needToExhaust(card);
			if (num >= 0) {
				AbstractDungeon.player.discardPile.removeCard(card);
				__instance.duration *= 0.5f;
				ScrollOfWallUtil.setExhaustStatus(__instance, num);
			}
		}
	}

	@SpirePatch(clz = ShowCardAndAddToDiscardEffect.class, method = "update")
	public static class AddToDiscardUpdate {
		@SpireInsertPatch(locator = AddToDiscardLocator.class, localvars = {"card"})
		public static SpireReturn Insert(ShowCardAndAddToDiscardEffect __instance, AbstractCard card) {
			if (GathererMod.gainCardEffectToExhaust.contains(__instance)) {
				ScrollOfWallUtil.exhaustIncomingCard(__instance, card);
				return SpireReturn.Return(null);
			}
			return SpireReturn.Continue();
		}
	}

	private static class AddToDiscardLocator extends SpireInsertLocator {
		@Override
		public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
			Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractCard.class, "shrink");
			return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
		}
	}

	@SpirePatch(
			clz = ShowCardAndAddToDrawPileEffect.class,
			method = SpirePatch.CONSTRUCTOR,
			paramtypez = {
					AbstractCard.class,
					float.class,
					float.class,
					boolean.class,
					boolean.class,
					boolean.class
			}
	)
	public static class AddToDrawPileConstruct1 {
		@SpirePostfixPatch
		public static void Postfix(ShowCardAndAddToDrawPileEffect __instance, AbstractCard srcCard, float x, float y, boolean randomSpot, boolean cardOffset, boolean toBottom) {
			int num = ScrollOfWallUtil.needToExhaust(srcCard);
			if (num >= 0) {
				AbstractDungeon.player.drawPile.removeCard(srcCard);
				__instance.duration *= 0.5f;
				ScrollOfWallUtil.setExhaustStatus(__instance, num);
			}
		}
	}

	@SpirePatch(
			clz = ShowCardAndAddToDrawPileEffect.class,
			method = SpirePatch.CONSTRUCTOR,
			paramtypez = {
					AbstractCard.class,
					boolean.class,
					boolean.class,
			}
	)
	public static class AddToDrawPileConstruct2 {
		@SpirePostfixPatch
		public static void Postfix(ShowCardAndAddToDrawPileEffect __instance, AbstractCard srcCard, boolean randomSpot, boolean toBottom) {
			int num = ScrollOfWallUtil.needToExhaust(srcCard);
			if (num >= 0) {
				AbstractDungeon.player.drawPile.removeCard(srcCard);
				__instance.duration *= 0.5f;
				ScrollOfWallUtil.setExhaustStatus(__instance, num);
			}
		}
	}

	@SpirePatch(clz = ShowCardAndAddToDrawPileEffect.class, method = "update")
	public static class AddToDrawPileUpdate {
		@SpireInsertPatch(locator = AddToDrawPileLocator.class, localvars = {"card"})
		public static SpireReturn Insert(ShowCardAndAddToDrawPileEffect __instance, AbstractCard card) {
			if (GathererMod.gainCardEffectToExhaust.contains(__instance)) {
				ScrollOfWallUtil.exhaustIncomingCard(__instance, card);
				return SpireReturn.Return(null);
			}
			return SpireReturn.Continue();
		}
	}

	private static class AddToDrawPileLocator extends SpireInsertLocator {
		@Override
		public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
			Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractCard.class, "shrink");
			return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
		}
	}

	@SpirePatch(
			clz = ShowCardAndAddToHandEffect.class,
			method = SpirePatch.CONSTRUCTOR,
			paramtypez = {
					AbstractCard.class,
					float.class,
					float.class
			}
	)
	public static class AddToHandConstructor1 {
		@SpirePostfixPatch()
		public static void Postfix(ShowCardAndAddToHandEffect __instance, AbstractCard card, float offsetX, float offsetY) {
			int num = ScrollOfWallUtil.needToExhaust(card);
			if (num >= 0) {
				ScrollOfWallUtil.setExhaustStatus(__instance, num);
			}
		}
	}

	@SpirePatch(
			clz = ShowCardAndAddToHandEffect.class,
			method = SpirePatch.CONSTRUCTOR,
			paramtypez = {
					AbstractCard.class
			}
	)
	public static class AddToHandConstructor2 {
		@SpirePostfixPatch()
		public static void Postfix(ShowCardAndAddToHandEffect __instance, AbstractCard card) {
			int num = ScrollOfWallUtil.needToExhaust(card);
			if (num >= 0) {
				ScrollOfWallUtil.setExhaustStatus(__instance, num);
			}
		}
	}

	@SpirePatch(clz = ShowCardAndAddToHandEffect.class, method = "update")
	public static class AddToHandUpdate {
		@SpireInsertPatch(locator = AddToHandLocator.class, localvars = {"card"})
		public static void Insert(ShowCardAndAddToHandEffect __instance, AbstractCard card) {
			if (GathererMod.gainCardEffectToExhaust.contains(__instance)) {
				AbstractDungeon.player.hand.removeCard(card);
				ScrollOfWallUtil.exhaustIncomingCard(__instance, card);
			}
		}
	}

	private static class AddToHandLocator extends SpireInsertLocator {
		@Override
		public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
			Matcher finalMatcher = new Matcher.FieldAccessMatcher(ShowCardAndAddToHandEffect.class, "isDone");
			return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
		}
	}
}
