package the_gatherer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import the_gatherer.GathererMod;
import the_gatherer.potions.PlaceHolderPotion;

public class PotionUsedAction extends AbstractGameAction {
	private AbstractPlayer p;

	public PotionUsedAction() {
		this.actionType = ActionType.SPECIAL;
		this.p = AbstractDungeon.player;
		this.duration = Settings.ACTION_DUR_FAST;
	}

	public void update() {
		if (this.duration == Settings.ACTION_DUR_FAST) {
			GathererMod.ActivatePotionUseEffects(new PlaceHolderPotion());
		}

		this.tickDuration();
	}
}
