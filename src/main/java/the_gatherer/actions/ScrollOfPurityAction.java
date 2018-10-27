package the_gatherer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

public class ScrollOfPurityAction extends AbstractGameAction {
	private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("Gatherer:ScrollOfPurityAction");
	public static final String[] TEXT = uiStrings.TEXT;
	private AbstractPlayer p;
	private int amount;

	public ScrollOfPurityAction(int amount) {
		this.actionType = ActionType.CARD_MANIPULATION;
		this.p = AbstractDungeon.player;
		this.duration = Settings.ACTION_DUR_FAST;
		this.amount = amount;
	}

	public void update() {
		if (this.duration == Settings.ACTION_DUR_FAST) {
			if (this.p.hand.isEmpty()) {
				this.isDone = true;
			} else {
				AbstractDungeon.handCardSelectScreen.open(TEXT[0], 1, false, true, false, false, true);
				this.tickDuration();
			}
		} else {
			if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
				if (AbstractDungeon.handCardSelectScreen.selectedCards.group.size() > 0) {
					for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
						this.p.hand.moveToExhaustPile(c);
					}

					AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
					AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, this.amount));
				}

				AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
			}

			this.tickDuration();
		}
	}
}
