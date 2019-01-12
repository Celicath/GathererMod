package the_gatherer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import the_gatherer.GathererMod;
import the_gatherer.cards.MiningStrike;
import the_gatherer.cards.WitheringStrike;

import java.util.ArrayList;

public class EnchantAction extends AbstractGameAction {
	private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("Gatherer:EnchantAction");
	public static final String[] TEXT = uiStrings.TEXT;
	private AbstractPlayer p;
	private ArrayList<AbstractCard> notStrike = new ArrayList<>();
	private int amount;

	public EnchantAction(int amount) {
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
				if (!c.hasTag(AbstractCard.CardTags.STRIKE)) {
					notStrike.add(c);
				}
			}
			if (this.notStrike.size() == this.p.hand.group.size()) {
				this.isDone = true;
				return;
			} else if (this.p.hand.group.size() - this.notStrike.size() == 1) {
				for (AbstractCard c : this.p.hand.group) {
					if (!notStrike.contains(c)) {
						doEnchant(c, amount);
						this.isDone = true;
						return;
					}
				}
			}

			this.p.hand.group.removeAll(this.notStrike);
			GathererMod.cardSelectScreenCard = null;
			GathererMod.enchantAmount = amount;
			AbstractDungeon.handCardSelectScreen.open(TEXT[0], 1, false, false, false, true);
			this.tickDuration();
			return;
		}
		if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {

			for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
				if (!notStrike.contains(c)) {
					doEnchant(c, amount);
					this.p.hand.addToTop(c);
				}
			}

			this.returnCards();
			AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
			AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
			GathererMod.enchantAmount = 0;
			this.isDone = true;
		}

		this.tickDuration();
	}

	public static void doEnchant(AbstractCard c, int amount) {
		c.updateCost(1);
		c.baseDamage += amount;
		if (c.baseMagicNumber != -1) {
			int delta = 1;
			if (c instanceof MiningStrike) {
				delta = -1;
				if (c.baseMagicNumber + delta <= 0)
					delta = 0;
			}
			c.baseMagicNumber += delta;
			c.magicNumber = c.baseMagicNumber;
			if (c instanceof WitheringStrike && WitheringStrike.EXTENDED_DESCRIPTION != null) {
				c.rawDescription = WitheringStrike.EXTENDED_DESCRIPTION[0];
				c.initializeDescription();
			}
		}
		c.superFlash();
	}

	private void returnCards() {
		for (AbstractCard c : this.notStrike) {
			this.p.hand.addToTop(c);
		}

		this.p.hand.refreshHandLayout();
	}
}
