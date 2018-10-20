package the_gatherer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.utility.QueueCardAction;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import the_gatherer.GathererMod;

import java.util.ArrayList;
import java.util.Iterator;

public class BronzeBladeAction extends AbstractGameAction {
	private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("BronzeBladeAction");
	public static final String[] TEXT = uiStrings.TEXT;
	AbstractPlayer p;
	private ArrayList<AbstractCard> notBasicDefends = new ArrayList<>();

	public BronzeBladeAction() {
		this.actionType = ActionType.WAIT;
		this.p = AbstractDungeon.player;
		this.duration = Settings.ACTION_DUR_FAST;
	}

	public void update() {
		Iterator var1;
		AbstractCard c;
		if (this.duration == Settings.ACTION_DUR_FAST) {
			if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
				this.isDone = true;
				return;
			}

			var1 = this.p.hand.group.iterator();

			while (var1.hasNext()) {
				c = (AbstractCard) var1.next();
				if (!GathererMod.isBasicDefend(c)) {
					this.notBasicDefends.add(c);
				}
			}
			if (this.notBasicDefends.size() == this.p.hand.group.size()) {
				this.isDone = true;
				return;
			}
			else if (this.p.hand.group.size() - this.notBasicDefends.size() == 1) {
				var1 = this.p.hand.group.iterator();

				while(var1.hasNext()) {
					c = (AbstractCard)var1.next();
					if (!notBasicDefends.contains(c)) {
						playCard(c);
						this.isDone = true;
						return;
					}
				}
			}

			this.p.hand.group.removeAll(this.notBasicDefends);
			if (this.p.hand.group.size() > 1) {
				AbstractDungeon.handCardSelectScreen.open(TEXT[0], 1, false, false, false, false);
				this.tickDuration();
				return;
			}
		}
		if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
			var1 = AbstractDungeon.handCardSelectScreen.selectedCards.group.iterator();

			while(var1.hasNext()) {
				c = (AbstractCard)var1.next();
				playCard(c);
			}

			this.returnCards();
			AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
			AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
			this.isDone = true;
		}

		this.tickDuration();
	}

	private void playCard(AbstractCard card) {
		AbstractDungeon.player.hand.group.remove(card);
		card.freeToPlayOnce = true;
		AbstractDungeon.player.limbo.group.add(card);
		card.current_y = -200.0F * Settings.scale;
		card.target_x = (float) Settings.WIDTH / 2.0F + 200.0F * Settings.scale;
		card.target_y = (float) Settings.HEIGHT / 2.0F;
		card.targetAngle = 0.0F;
		card.lighten(false);
		card.drawScale = 0.12F;
		card.targetDrawScale = 0.75F;

		card.applyPowers();
		AbstractDungeon.actionManager.addToTop(new QueueCardAction(card, this.target));
		AbstractDungeon.actionManager.addToTop(new UnlimboAction(card));
		if (!Settings.FAST_MODE) {
			AbstractDungeon.actionManager.addToTop(new WaitAction(Settings.ACTION_DUR_MED));
		} else {
			AbstractDungeon.actionManager.addToTop(new WaitAction(Settings.ACTION_DUR_FASTER));
		}
	}

	private void returnCards() {
		Iterator var1 = this.notBasicDefends.iterator();

		while(var1.hasNext()) {
			AbstractCard c = (AbstractCard)var1.next();
			this.p.hand.addToTop(c);
		}

		this.p.hand.refreshHandLayout();
	}
}
