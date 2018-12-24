package the_gatherer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import the_gatherer.GathererMod;
import the_gatherer.powers.GrassKnotPower;

import java.util.ArrayList;

public class GrassKnotDiscardAction extends AbstractGameAction {
	private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("DiscardAction");
	public static final String[] TEXT = uiStrings.TEXT;
	private AbstractPlayer p;
	private ArrayList<AbstractCard> alreadyPlanned = new ArrayList<>();

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

			if (this.p.hand.size() <= 0) {
				this.isDone = true;
				return;
			}

			AbstractDungeon.handCardSelectScreen.open(TEXT[0], this.amount, false, true, false, false, true);
			AbstractDungeon.player.hand.applyPowers();
			this.tickDuration();
		}

		if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
			int num = AbstractDungeon.handCardSelectScreen.selectedCards.size();
			for (AbstractCard card : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
				this.p.hand.moveToDiscardPile(card);
				card.triggerOnManualDiscard();
				GameActionManager.incrementDiscard(false);
			}
			GathererMod.logger.debug("NUM=" + num);
			GrassKnotPower.gainBlock(num);

			AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
		}

		this.tickDuration();
	}
}
