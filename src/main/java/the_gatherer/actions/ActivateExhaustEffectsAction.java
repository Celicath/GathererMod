package the_gatherer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class ActivateExhaustEffectsAction extends AbstractGameAction {
	AbstractCard c;

	public ActivateExhaustEffectsAction(AbstractCard c) {
		this.actionType = ActionType.WAIT;
		this.c = c;
	}

	public void update() {
		if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
			this.isDone = true;
			return;
		}
		for (AbstractRelic r : AbstractDungeon.player.relics) {
			r.onExhaust(c);
		}
		for (AbstractPower p : AbstractDungeon.player.powers) {
			p.onExhaust(c);
		}
		c.triggerOnExhaust();
		this.isDone = true;
	}
}
