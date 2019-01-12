package the_gatherer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

public class SaveValuablesAction extends AbstractGameAction {
	private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("Gatherer:SaveValuablesAction");
	public static final String[] TEXT = uiStrings.TEXT;
	private AbstractPlayer p;

	public SaveValuablesAction(AbstractCreature source, int amount) {
		this.p = AbstractDungeon.player;
		this.setValues(null, source, amount);
		this.actionType = ActionType.CARD_MANIPULATION;
		this.duration = Settings.ACTION_DUR_FASTER;
	}

	public void update() {
		if (AbstractDungeon.getCurrRoom().isBattleEnding()) {
			this.isDone = true;
		} else {
			if (this.duration == Settings.ACTION_DUR_FASTER) {
				if (this.p.discardPile.isEmpty()) {
					this.isDone = true;
					return;
				}

				AbstractDungeon.gridSelectScreen.open(this.p.discardPile, this.amount, true, TEXT[0] + this.amount + TEXT[1]);
			} else if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
				for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
					this.p.discardPile.removeCard(c);
					this.p.hand.moveToDeck(c, true);
				}
				AbstractDungeon.gridSelectScreen.selectedCards.clear();
			}

			this.tickDuration();
		}
	}
}
