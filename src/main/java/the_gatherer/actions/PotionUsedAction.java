package the_gatherer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import the_gatherer.GathererMod;

public class PotionUsedAction extends AbstractGameAction {
	private AbstractPlayer p;
	private AbstractPotion potion;
	private boolean disableTopyOrnithopter;

	public PotionUsedAction(AbstractPotion potion, boolean disableTopyOrnithopter) {
		this.actionType = ActionType.SPECIAL;
		this.p = AbstractDungeon.player;
		this.potion = potion;
		this.duration = Settings.ACTION_DUR_FAST;
		this.disableTopyOrnithopter = disableTopyOrnithopter;
	}

	public void update() {
		if (this.duration == Settings.ACTION_DUR_FAST) {
			GathererMod.ActivatePotionUseEffects(potion, disableTopyOrnithopter);
		}

		this.tickDuration();
	}
}
