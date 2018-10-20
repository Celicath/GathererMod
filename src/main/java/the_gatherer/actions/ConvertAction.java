package the_gatherer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

public class ConvertAction extends AbstractGameAction {
	private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("RecycleAction");
	public static final String[] TEXT = uiStrings.TEXT;
	private AbstractPlayer p;
	private int amount;

	public ConvertAction(int amount) {
		this.actionType = ActionType.CARD_MANIPULATION;
		this.p = AbstractDungeon.player;
		this.duration = Settings.ACTION_DUR_FAST;
		this.amount = amount;
	}

	public void update() {
		if (this.duration == Settings.ACTION_DUR_FAST) {
			if (this.p.hand.isEmpty()) {
				this.isDone = true;
			} else if (this.p.hand.size() == 1) {
				doAction(this.p.hand.getBottomCard());
				this.tickDuration();
			} else {
				AbstractDungeon.handCardSelectScreen.open(TEXT[0], 1, false);
				this.tickDuration();
			}
		} else {
			if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
				for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
					doAction(c);
				}

				AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
				AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
			}

			this.tickDuration();
		}
	}

	void doAction(AbstractCard c) {
		if (c.rarity == CardRarity.UNCOMMON || c.rarity == CardRarity.RARE) {
			AbstractDungeon.actionManager.addToTop(new GainBlockAction(p, p, this.amount));
		}
		this.p.hand.moveToExhaustPile(c);
	}
}
