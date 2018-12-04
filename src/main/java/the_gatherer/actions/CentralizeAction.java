package the_gatherer.actions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.UpgradeRandomCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.ArrayList;

public class CentralizeAction extends AbstractGameAction {
	private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("AnyCardFromDeckToHandAction");
	public static final String[] TEXT = uiStrings.TEXT;
	private static final UIStrings uiStrings2 = CardCrawlGame.languagePack.getUIString("Gatherer:CentralizeAction");
	public static final String[] TEXT2 = uiStrings2.TEXT;
	private AbstractPlayer p;

	private boolean upgrade;
	private ArrayList<AbstractCard> fetchedCards;

	public CentralizeAction(int amount, boolean upgrade) {
		this.p = AbstractDungeon.player;
		this.setValues(this.p, AbstractDungeon.player, amount);
		this.actionType = ActionType.CARD_MANIPULATION;
		this.duration = Settings.ACTION_DUR_MED;

		this.upgrade = upgrade;
		this.fetchedCards = new ArrayList<>();
	}

	public void update() {
		if (this.duration == Settings.ACTION_DUR_MED) {
			CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

			for (AbstractCard c : this.p.drawPile.group) {
				tmp.addToRandomSpot(c);
			}

			if (tmp.size() == 0) {
				upgradeCards();
				this.isDone = true;
			} else if (tmp.size() <= this.amount) {
				for (int i = 0; i < tmp.size(); ++i) {
					AbstractCard card = tmp.getNCardFromTop(i);
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
					this.fetchedCards.add(card);
				}
				upgradeCards();
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

				for (AbstractCard card : AbstractDungeon.gridSelectScreen.selectedCards) {
					card.unhover();

					if (this.p.hand.size() == BaseMod.MAX_HAND_SIZE) {
						this.p.drawPile.moveToDiscardPile(card);
						this.p.createHandIsFullDialog();
					} else {
						this.p.drawPile.removeCard(card);
						this.p.hand.addToTop(card);
					}

					this.p.hand.refreshHandLayout();
					this.p.hand.applyPowers();

					this.fetchedCards.add(card);
				}
				upgradeCards();

				AbstractDungeon.gridSelectScreen.selectedCards.clear();
				this.p.hand.refreshHandLayout();
			}

			this.tickDuration();
		}
	}

	void upgradeCards() {
		if (upgrade) {
			int count = this.amount;
			for (AbstractCard card : this.fetchedCards) {
				if (upgrade && (card.type != AbstractCard.CardType.STATUS && card.type != AbstractCard.CardType.CURSE) && card.canUpgrade()) {
					card.upgrade();
					card.superFlash();
					count--;
				}
			}
			for (int i = 0; i < count; i++) {
				AbstractDungeon.actionManager.addToTop(new UpgradeRandomCardAction());
			}
		}
	}
}
