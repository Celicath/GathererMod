package the_gatherer.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.green.Alchemize;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.helpers.EventHelper;
import com.megacrit.cardcrawl.random.Random;
import javassist.CtBehavior;
import the_gatherer.events.BottleCollector;

import java.util.ArrayList;

import static the_gatherer.GathererMod.logger;

public class GathererOnlyEventPatch {
	@SpirePatch(clz = AbstractDungeon.class, method = "getShrine")
	public static class GenEvent {
		@SpireInsertPatch(locator = GetEventLocator.class, localvars = {"tmp"})
		public static void Insert(Random rng, ArrayList<String> tmp) {
			if (tmp.contains(BottleCollector.ID)) {
				boolean possible = false;
				for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
					if (c.hasTag(CustomTags.POTION_GEN) || c.cardID.equals(Alchemize.ID)) {
						possible = true;
						break;
					}
				}
				if (!possible) {
					tmp.remove(BottleCollector.ID);
				}
			}
		}
	}

	private static class GetEventLocator extends SpireInsertLocator {
		@Override
		public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
			Matcher finalMatcher = new Matcher.MethodCallMatcher(ArrayList.class, "get");
			return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
		}
	}

	@SpirePatch(clz = EventHelper.class, method = "getEvent")
	public static class GetEvent {
		@SpirePostfixPatch
		public static AbstractEvent Postfix(AbstractEvent __result, String key) {
			if (key.equals(BottleCollector.ID)) {
				return new BottleCollector();
			} else {
				return __result;
			}
		}
	}

	@SpirePatch(clz = AbstractDungeon.class, method = "initializeSpecialOneTimeEventList")
	public static class AddToSpecialOneTimeEventList {
		public static void Postfix(AbstractDungeon dungeon) {
			if (AbstractDungeon.player.chosenClass == AbstractPlayerEnum.THE_GATHERER) {
				AbstractDungeon.specialOneTimeEventList.add(BottleCollector.ID);
				logger.debug("BottleCollector Event Added!");
			}
		}

	}
}
