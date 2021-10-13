package the_gatherer.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import the_gatherer.cards.Helper.AbstractTaggedCard;

public class LoadCardPatch {
	@SpirePatch2(clz = CardLibrary.class, method = "getCopy", paramtypez = {String.class, int.class, int.class})
	public static class GetCopy {
		@SpirePostfixPatch
		public static AbstractCard Postfix(AbstractCard __result) {
			if (__result instanceof AbstractTaggedCard) {
				((AbstractTaggedCard) __result).setTag(__result.misc);
			}
			return __result;
		}
	}
}
