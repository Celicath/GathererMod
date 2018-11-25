package the_gatherer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import the_gatherer.cards.ScrollOfPurity;

public class ScrollOfPurityFollowUpAction extends AbstractGameAction {
	private AbstractPlayer p;

	public ScrollOfPurityFollowUpAction(int amount) {
		this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
		this.p = AbstractDungeon.player;
		this.duration = Settings.ACTION_DUR_FAST;
		this.amount = amount;
	}

	public void update() {
		if (ScrollOfPurity.exhaustCount > 0) {
			AbstractDungeon.actionManager.addToBottom(new ExhaustAction(
					p, p, ScrollOfPurity.exhaustCount, false, true, true));
		}
		this.isDone = true;
	}
}
