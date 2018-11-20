package the_gatherer.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.helpers.EventHelper;
import the_gatherer.GathererMod;
import the_gatherer.events.BottleCollector;

public class GathererOnlyEventPatch {
	@SpirePatch(clz = EventHelper.class, method = "getEvent")
	public static class GetEvent {
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
				GathererMod.logger.debug("BattleCollector Event Added!");
			}
		}
	}
}
