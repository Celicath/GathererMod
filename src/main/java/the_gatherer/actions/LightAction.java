package the_gatherer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import the_gatherer.cards.Shadow;

public class LightAction extends AbstractGameAction {

	public LightAction(AbstractCreature source, int amount) {
		setValues(AbstractDungeon.player, source, amount);
		actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
		duration = Settings.ACTION_DUR_FAST;
	}

	public void update() {
		if (this.duration == Settings.ACTION_DUR_FAST) {
			for (AbstractCard c : AbstractDungeon.player.hand.group) {
				if (c instanceof Shadow) {
					c.baseDamage += amount;
					c.applyPowers();
					c.flash();
				}
			}
		}
		tickDuration();
	}
}
