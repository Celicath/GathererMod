package the_gatherer.actions;
//

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.Iterator;

public class OptionalTopdeckAction extends AbstractGameAction {
	private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("PutOnDeckAction");
	public static final String[] TEXT = uiStrings.TEXT;
	private AbstractPlayer p;
	public static int numPlaced;

	public OptionalTopdeckAction(AbstractCreature target, AbstractCreature source, int amount) {
		this.target = target;
		this.p = (AbstractPlayer) target;
		this.setValues(target, source, amount);
		this.actionType = ActionType.CARD_MANIPULATION;
	}

	public void update() {
		if (this.duration == 0.5F) {
			if (this.p.hand.size() < this.amount) {
				this.amount = this.p.hand.size();
			}

			int i;
			if (this.p.hand.group.size() > this.amount) {
				numPlaced = this.amount;
				AbstractDungeon.handCardSelectScreen.open(TEXT[0], this.amount, false, true, false, false, true);
				this.tickDuration();
				return;
			}

			for (i = 0; i < this.p.hand.size(); ++i) {
				this.p.hand.moveToDeck(this.p.hand.getRandomCard(false), false);
			}
		}

		if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
			Iterator var3 = AbstractDungeon.handCardSelectScreen.selectedCards.group.iterator();

			while (var3.hasNext()) {
				AbstractCard c = (AbstractCard) var3.next();
				this.p.hand.moveToDeck(c, false);
			}

			AbstractDungeon.player.hand.refreshHandLayout();
			AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
		}

		this.tickDuration();
	}
}
