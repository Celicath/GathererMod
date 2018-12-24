package the_gatherer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import the_gatherer.powers.GrassKnotPower;

import java.util.ArrayList;

public class GrassKnotDiscardAction extends AbstractGameAction {
	private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("DiscardAction");
	public static final String[] TEXT = uiStrings.TEXT;
	private AbstractPlayer p;
	private ArrayList<AbstractCard> notRetaining = new ArrayList<>();

	public GrassKnotDiscardAction(int amount) {
		this.actionType = ActionType.CARD_MANIPULATION;
		this.p = AbstractDungeon.player;
		this.duration = Settings.ACTION_DUR_FAST;
		this.amount = amount;
	}

	public void update() {
		if (this.duration == Settings.ACTION_DUR_FAST) {
			if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
				this.isDone = true;
				return;
			}

			for (AbstractCard c : this.p.hand.group) {
				if (!c.retain) {
					notRetaining.add(c);
				}
			}
			if (this.notRetaining.size() == this.p.hand.group.size()) {
				this.isDone = true;
				return;
			}

			this.p.hand.group.removeAll(this.notRetaining);
			AbstractDungeon.handCardSelectScreen.open(TEXT[0], this.amount, false, true, false, false, true);
			this.tickDuration();
		} else {
			if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
				int num = AbstractDungeon.handCardSelectScreen.selectedCards.group.size();
				for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
					c.retain = false;
					this.p.hand.moveToDiscardPile(c);
					c.triggerOnManualDiscard();
					GameActionManager.incrementDiscard(false);
				}

				GrassKnotPower.gainBlock(num);
				this.returnCards();
				AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
				AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
			}

			this.tickDuration();
		}
	}

	private void returnCards() {
		for (AbstractCard c : this.notRetaining) {
			this.p.hand.addToTop(c);
		}

		this.p.hand.refreshHandLayout();
	}
}
