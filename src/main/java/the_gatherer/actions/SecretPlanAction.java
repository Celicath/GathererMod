package the_gatherer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import the_gatherer.powers.SecretPlanPower;

import java.util.ArrayList;

public class SecretPlanAction extends AbstractGameAction {
	private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("Gatherer:SecretPlanAction");
	public static final String[] TEXT = uiStrings.TEXT;
	private AbstractPlayer p;
	private ArrayList<AbstractCard> alreadyPlanned = new ArrayList<>();

	public SecretPlanAction(int amount) {
		this.actionType = ActionType.CARD_MANIPULATION;
		this.p = AbstractDungeon.player;
		this.duration = Settings.ACTION_DUR_FAST;
		this.amount = amount;
	}

	public void update() {
		if (this.duration == Settings.ACTION_DUR_FAST) {

			for (AbstractCard c : this.p.hand.group) {
				if (SecretPlanPower.cardList.contains(c)) {
					alreadyPlanned.add(c);
				}
			}
			if (this.alreadyPlanned.size() == this.p.hand.group.size()) {
				this.isDone = true;
				return;
			}

			this.p.hand.group.removeAll(this.alreadyPlanned);
			AbstractDungeon.handCardSelectScreen.open(TEXT[0], this.amount, false, true, false, false, true);
			this.tickDuration();

		} else {
			if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
				AbstractPower pow = p.getPower(SecretPlanPower.POWER_ID);
				ArrayList<AbstractCard> cards = new ArrayList<>();

				for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
					cards.add(c);
					if (pow instanceof SecretPlanPower) {
						((SecretPlanPower) pow).addCard(c);
					} else {
						cards.add(c);
					}
					this.p.hand.addToTop(c);
					this.p.hand.refreshHandLayout();
					this.p.hand.applyPowers();
				}
				if (!(pow instanceof SecretPlanPower)) {
					AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(p, p, new SecretPlanPower(cards)));
				}
				this.returnCards();
				AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
				AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
			}

			this.tickDuration();
		}
	}

	private void returnCards() {
		for (AbstractCard c : this.alreadyPlanned) {
			this.p.hand.addToTop(c);
		}

		this.p.hand.refreshHandLayout();
	}
}
