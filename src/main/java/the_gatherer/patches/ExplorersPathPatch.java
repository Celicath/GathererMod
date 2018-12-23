package the_gatherer.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.relics.QuestionCard;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import the_gatherer.GathererMod;
import the_gatherer.relics.ExplorersPath;

import java.util.ArrayList;

public class ExplorersPathPatch {
	@SpirePatch(
			clz = RewardItem.class,
			method = "claimReward"
	)
	public static class ClaimReward {
		@SpirePostfixPatch
		public static void Postfix(RewardItem __instance) {
			if (__instance.type == RewardItem.RewardType.CARD && AbstractDungeon.player.hasRelic(ExplorersPath.ID)) {
				AbstractDungeon.player.getRelic(ExplorersPath.ID).flash();
			}
		}
	}

	@SpirePatch(
			clz = CardRewardScreen.class,
			method = "render"
	)
	public static class ShowRatio {
		@SpirePostfixPatch
		public static void Postfix(CardRewardScreen __instance, SpriteBatch sb) {
			if (AbstractDungeon.player.hasRelic(ExplorersPath.ID) && AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) {
				for (AbstractCard c : __instance.rewardGroup) {
					double ratio = ExplorersPath.getRatio(c.cardID);
					String ratioText = "??%";
					if (ratio > 0.01f && ratio < 99.99f)
						ratioText = String.format("%.2f%%", ratio);
					boolean contains = GathererMod.explorersPathBestOption.contains(c);

					FontHelper.renderFontCentered(sb, FontHelper.panelNameFont,
							contains ? ">" + ratioText + "<" : ratioText,
							c.target_x, c.target_y + 200.0F * Settings.scale,
							contains ? Color.YELLOW.cpy() : Color.WHITE.cpy());
				}
			}
		}
	}

	@SpirePatch(
			clz = CardRewardScreen.class,
			method = "open"
	)
	public static class PickLowest {
		public static void Postfix(CardRewardScreen __instance, ArrayList<AbstractCard> cards, RewardItem rItem, String header) {
			if (AbstractDungeon.player.hasRelic(ExplorersPath.ID) && AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) {
				double min = 100.1;
				double min2 = 100.1;
				AbstractCard cmin = null;
				AbstractCard cmin2 = null;
				for (AbstractCard c : __instance.rewardGroup) {
					double r = ExplorersPath.getRatio(c.cardID);
					if (r < min) {
						min2 = min;
						cmin2 = cmin;
						min = r;
						cmin = c;
					} else if (r < min2) {
						min2 = r;
						cmin2 = c;
					}
				}
				if (cmin != null) {
					GathererMod.explorersPathBestOption.add(cmin);
				}
				if (AbstractDungeon.player.hasRelic(QuestionCard.ID) && cmin2 != null) {
					GathererMod.explorersPathBestOption.add(cmin2);
				}
			}
		}
	}
}
