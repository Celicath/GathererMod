package the_gatherer.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.events.shrines.NoteForYourself;
import the_gatherer.GathererMod;

public class DeckChangeHook {
	@SpirePatch(clz = CardGroup.class, method = "removeCard", paramtypez = AbstractCard.class)
	public static class RemoveCardPatch {
		public static void Postfix(CardGroup __instance, AbstractCard c) {
			if (__instance.type == CardGroup.CardGroupType.MASTER_DECK) {
				GathererMod.updateTypeCount();
			}
		}
	}

	@SpirePatch(clz = NoteForYourself.class, method = "buttonEffect")
	public static class NoteForYourselfPatch {
		public static void Postfix(NoteForYourself __instance, int buttonPressed) {
			if (buttonPressed == 0) {
				GathererMod.updateTypeCount();
			}
		}
	}

	@SpirePatch(clz = CardCrawlGame.class, method = "loadPlayerSave")
	public static class LoadPatch {
		public static void Postfix(CardCrawlGame CardCrawlGame, AbstractPlayer p) {
			GathererMod.updateTypeCount();
		}
	}
}
