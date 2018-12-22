package the_gatherer.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ScreenStatePatch {
	@SpireEnum
	public static AbstractDungeon.CurrentScreen EXCESS_POTION_HANDLE;
}
