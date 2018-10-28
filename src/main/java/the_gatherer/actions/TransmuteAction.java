package the_gatherer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.AscendersBane;
import com.megacrit.cardcrawl.cards.curses.Necronomicurse;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.ArrayList;

public class TransmuteAction extends AbstractGameAction {
	private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("Gatherer:TransmuteAction");
	public static final String[] TEXT = uiStrings.TEXT;
	private AbstractPlayer p;
	private ArrayList<AbstractCard> notTransformable = new ArrayList<>();

	public TransmuteAction() {
		this.actionType = ActionType.CARD_MANIPULATION;
		this.p = AbstractDungeon.player;
		this.duration = Settings.ACTION_DUR_FAST;
	}

	public void update() {
		if (this.duration == Settings.ACTION_DUR_FAST) {
			if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
				this.isDone = true;
				return;
			}

			for (AbstractCard c : this.p.hand.group) {
				if (c.cardID.equals(Necronomicurse.ID) || c.cardID.equals(AscendersBane.ID)) {
					notTransformable.add(c);
				}
			}
			if (this.notTransformable.size() == this.p.hand.group.size()) {
				this.isDone = true;
				return;
			} else if (this.p.hand.group.size() - this.notTransformable.size() == 1) {
				for (AbstractCard c : this.p.hand.group) {
					if (!notTransformable.contains(c)) {
						doAction(c);
						this.isDone = true;
						return;
					}
				}
			}

			this.p.hand.group.removeAll(this.notTransformable);
			if (this.p.hand.group.size() > 1) {
				AbstractDungeon.handCardSelectScreen.open(TEXT[0], 1, false, false, false, false);
				this.tickDuration();
				return;
			}
		}
		if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {

			for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
				if (!notTransformable.contains(c)) {
					doAction(c);
				}
			}

			this.returnCards();
			AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
			AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
			this.isDone = true;
		}

		this.tickDuration();
	}

	private void doAction(AbstractCard c) {
		AbstractDungeon.player.hand.removeCard(c);
		AbstractDungeon.transformCard(c, false, AbstractDungeon.cardRandomRng);
		AbstractCard card = AbstractDungeon.getTransformedCard();
		card.setCostForTurn(0);
		AbstractDungeon.actionManager.addToTop(new MakeTempCardInHandAction(card));
	}

	private void returnCards() {
		for (AbstractCard c : this.notTransformable) {
			this.p.hand.addToTop(c);
		}

		this.p.hand.refreshHandLayout();
	}
}
