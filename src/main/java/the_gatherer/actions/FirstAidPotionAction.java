package the_gatherer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import the_gatherer.cards.FirstAidPotion;

public class FirstAidPotionAction extends AbstractGameAction {

	public FirstAidPotionAction(AbstractCreature target, AbstractCreature source, int amount) {
		this.setValues(target, source, amount);
		this.actionType = ActionType.HEAL;
	}

	public void update() {
		if (this.duration == 0.5F) {
			this.target.heal(FirstAidPotion.CalcAmount(this.target, this.amount));
		}

		this.tickDuration();
	}
}
