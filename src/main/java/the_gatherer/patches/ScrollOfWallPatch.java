package the_gatherer.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDrawPileEffect;
import javassist.CtBehavior;
import the_gatherer.powers.ScrollOfWallPower;

public class ScrollOfWallPatch {
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
			if(srcCard.type == AbstractCard.CardType.STATUS && AbstractDungeon.player.hasPower(ScrollOfWallPower.POWER_ID)) {
				AbstractDungeon.player.discardPile.removeCard(srcCard);
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
		public static void Postfix(ShowCardAndAddToDiscardEffect __instance, AbstractCard srcCard) {
			if(srcCard.type == AbstractCard.CardType.STATUS && AbstractDungeon.player.hasPower(ScrollOfWallPower.POWER_ID)) {
				AbstractDungeon.player.discardPile.removeCard(srcCard);
			}
		}
	}

	@SpirePatch(clz = ShowCardAndAddToDiscardEffect.class, method = "update")
	public static class AddToDiscardUpdate {
		@SpireInsertPatch(locator = AddToDiscardLocator.class, localvars = {"card"})
		public static SpireReturn Insert(ShowCardAndAddToDiscardEffect __instance, AbstractCard card) {
			if(card.type == AbstractCard.CardType.STATUS && AbstractDungeon.player.hasPower(ScrollOfWallPower.POWER_ID)) {
				CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
				group.addToTop(card);
				group.moveToExhaustPile(card);
				((ScrollOfWallPower)AbstractDungeon.player.getPower(ScrollOfWallPower.POWER_ID)).doAction();
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
			if(srcCard.type == AbstractCard.CardType.STATUS && AbstractDungeon.player.hasPower(ScrollOfWallPower.POWER_ID)) {
				AbstractDungeon.player.drawPile.removeCard(srcCard);
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
			if(srcCard.type == AbstractCard.CardType.STATUS && AbstractDungeon.player.hasPower(ScrollOfWallPower.POWER_ID)) {
				AbstractDungeon.player.drawPile.removeCard(srcCard);
			}
		}
	}

	@SpirePatch(clz = ShowCardAndAddToDrawPileEffect.class, method = "update")
	public static class AddToDrawPileUpdate {
		@SpireInsertPatch(locator = AddToDrawPileLocator.class, localvars = {"card"})
		public static SpireReturn Insert(ShowCardAndAddToDrawPileEffect __instance, AbstractCard card) {
			if (card.type == AbstractCard.CardType.STATUS && AbstractDungeon.player.hasPower(ScrollOfWallPower.POWER_ID)) {
				CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
				group.addToTop(card);
				group.moveToExhaustPile(card);
				((ScrollOfWallPower)AbstractDungeon.player.getPower(ScrollOfWallPower.POWER_ID)).doAction();
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
}
