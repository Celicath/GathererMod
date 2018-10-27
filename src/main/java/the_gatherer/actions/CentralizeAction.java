package the_gatherer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.Iterator;

public class CentralizeAction extends AbstractGameAction {
	private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("AnyCardFromDeckToHandAction");
	public static final String[] TEXT = uiStrings.TEXT;
	private static final UIStrings uiStrings2 = CardCrawlGame.languagePack.getUIString("Gatherer:CentralizeAction");
	public static final String[] TEXT2 = uiStrings2.TEXT;
	private AbstractPlayer p;

	private boolean upgrade;

	public CentralizeAction(int amount, boolean upgrade) {
		this.p = AbstractDungeon.player;
		this.setValues(this.p, AbstractDungeon.player, amount);
		this.actionType = ActionType.CARD_MANIPULATION;
		this.duration = Settings.ACTION_DUR_MED;

		this.upgrade = upgrade;
	}

	public void update() {
		AbstractCard card;
		if (this.duration == Settings.ACTION_DUR_MED) {
			CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
			Iterator it = this.p.drawPile.group.iterator();

			while (it.hasNext()) {
				card = (AbstractCard) it.next();
				tmp.addToRandomSpot(card);
			}

			if (tmp.size() == 0) {
				this.isDone = true;
			} else if (tmp.size() == 1) {
				card = tmp.getTopCard();
				if (this.p.hand.size() == 10) {
					this.p.drawPile.moveToDiscardPile(card);
					this.p.createHandIsFullDialog();
				} else {
					card.unhover();
					card.lighten(true);
					card.setAngle(0.0F);
					card.drawScale = 0.12F;
					card.targetDrawScale = 0.75F;
					card.current_x = CardGroup.DRAW_PILE_X;
					card.current_y = CardGroup.DRAW_PILE_Y;
					this.p.drawPile.removeCard(card);
					AbstractDungeon.player.hand.addToTop(card);
					AbstractDungeon.player.hand.refreshHandLayout();
					AbstractDungeon.player.hand.applyPowers();
				}

				this.isDone = true;
			} else if (tmp.size() <= this.amount) {
				for (int i = 0; i < tmp.size(); ++i) {
					card = tmp.getNCardFromTop(i);
					if (this.p.hand.size() == 10) {
						this.p.drawPile.moveToDiscardPile(card);
						this.p.createHandIsFullDialog();
					} else {
						card.unhover();
						card.lighten(true);
						card.setAngle(0.0F);
						card.drawScale = 0.12F;
						card.targetDrawScale = 0.75F;
						card.current_x = CardGroup.DRAW_PILE_X;
						card.current_y = CardGroup.DRAW_PILE_Y;
						this.p.drawPile.removeCard(card);
						AbstractDungeon.player.hand.addToTop(card);
						AbstractDungeon.player.hand.refreshHandLayout();
						AbstractDungeon.player.hand.applyPowers();
					}
				}

				this.isDone = true;
			} else {
				if (upgrade) {
					if (this.amount == 1) {
						AbstractDungeon.gridSelectScreen.open(tmp, this.amount, TEXT2[0], false);
					} else {
						AbstractDungeon.gridSelectScreen.open(tmp, this.amount, TEXT2[1], false);
					}
				} else {
					if (this.amount == 1) {
						AbstractDungeon.gridSelectScreen.open(tmp, this.amount, TEXT[0], false);
					} else {
						AbstractDungeon.gridSelectScreen.open(tmp, this.amount, TEXT[1], false);
					}
				}
				this.tickDuration();
			}
		} else {
			if (AbstractDungeon.gridSelectScreen.selectedCards.size() != 0) {
				Iterator it = AbstractDungeon.gridSelectScreen.selectedCards.iterator();

				while (it.hasNext()) {
					card = (AbstractCard) it.next();
					card.unhover();

					if (this.p.hand.size() == 10) {
						this.p.drawPile.moveToDiscardPile(card);
						this.p.createHandIsFullDialog();
					} else {
						this.p.drawPile.removeCard(card);
						this.p.hand.addToTop(card);
					}

					this.p.hand.refreshHandLayout();
					this.p.hand.applyPowers();

					if (upgrade) {
						card.upgrade();
						card.superFlash();
					}
				}

				AbstractDungeon.gridSelectScreen.selectedCards.clear();
				this.p.hand.refreshHandLayout();
			}

			this.tickDuration();
		}
	}
}
