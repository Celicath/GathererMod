package the_gatherer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import the_gatherer.GathererMod;


public class ObtainLesserPotionAction extends AbstractGameAction {
	private AbstractPotion potion;

	public ObtainLesserPotionAction(AbstractPotion potion) {
		this.actionType = ActionType.SPECIAL;
		this.duration = Settings.ACTION_DUR_XFAST;
		this.potion = potion;
	}

	public void update() {
		if (this.duration == Settings.ACTION_DUR_XFAST) {
			if (AbstractDungeon.player.hasRelic("Sozu")) {
				AbstractDungeon.player.getRelic("Sozu").flash();
			} else {
				GathererMod.potionSack.addPotion(this.potion);
			}
		}

		this.tickDuration();
	}
}
