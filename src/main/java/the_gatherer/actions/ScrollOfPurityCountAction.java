package the_gatherer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import the_gatherer.cards.ScrollOfPurity;

public class ScrollOfPurityCountAction extends AbstractGameAction {
	private AbstractPlayer p;
	private int threshold;
	private int amount;

	public ScrollOfPurityCountAction(int threshold, int amount) {
		this.actionType = ActionType.CARD_MANIPULATION;
		this.p = AbstractDungeon.player;
		this.duration = Settings.ACTION_DUR_FAST;
		this.threshold = threshold;
		this.amount = amount;
	}

	public void update() {
		if (this.p.hand.size() >= threshold) {
			ScrollOfPurity.exhaustCount += amount;
		}
		this.isDone = true;
	}
}
