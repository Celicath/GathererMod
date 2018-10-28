package the_gatherer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class FirstAidPotionAction extends AbstractGameAction {

	public FirstAidPotionAction(AbstractCreature target, AbstractCreature source, int amount) {
		this.setValues(target, source, amount);
		this.actionType = ActionType.HEAL;
	}

	public static int CalcAmount(AbstractCreature p, int ratio) {
		return ((p.maxHealth - p.currentHealth) * ratio - 1) / 100 + 1;
	}


	public void update() {
		if (this.duration == 0.5F) {
			this.target.heal(CalcAmount(this.target, this.amount));
		}

		this.tickDuration();
	}
}
