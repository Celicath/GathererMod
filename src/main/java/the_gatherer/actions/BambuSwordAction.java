package the_gatherer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
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

public class BambuSwordAction extends AbstractGameAction {
	private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("Gatherer:BambuSwordAction");
	public static final String[] TEXT = uiStrings.TEXT;
	AbstractPlayer p;
	AbstractMonster primaryTarget;
	private ArrayList<AbstractCard> notBasics = new ArrayList<>();

	public BambuSwordAction(AbstractMonster m) {
		this.actionType = ActionType.WAIT;
		this.p = AbstractDungeon.player;
		this.duration = Settings.ACTION_DUR_FAST;
		this.primaryTarget = m;
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
				if (!GathererMod.isBasic(c)) {
					this.notBasics.add(c);
				}
			}
			if (this.notBasics.size() == this.p.hand.group.size()) {
				this.isDone = true;
				return;
			} else if (this.p.hand.group.size() - this.notBasics.size() == 1) {
				var1 = this.p.hand.group.iterator();

				while (var1.hasNext()) {
					c = (AbstractCard) var1.next();
					if (!notBasics.contains(c)) {
						playCard(c);
						this.isDone = true;
						return;
					}
				}
			}

			this.p.hand.group.removeAll(this.notBasics);
			if (this.p.hand.group.size() > 1) {
				AbstractDungeon.handCardSelectScreen.open(TEXT[0], 1, false, false, false, false);
				this.tickDuration();
				return;
			}
		}
		if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
			var1 = AbstractDungeon.handCardSelectScreen.selectedCards.group.iterator();

			while (var1.hasNext()) {
				c = (AbstractCard) var1.next();
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
		switch (card.target) {
			case SELF:
				AbstractDungeon.actionManager.addToTop(new QueueCardAction(card, p));
				break;
			case ENEMY:
				AbstractMonster target = primaryTarget;
				if ((primaryTarget.isDying) || (primaryTarget.halfDead)) {
					target = AbstractDungeon.getMonsters().getRandomMonster(true);
				}
				AbstractDungeon.actionManager.addToTop(new QueueCardAction(card, target));
				break;
			default:
				AbstractDungeon.actionManager.addToTop(new QueueCardAction(card, p));
				break;
		}
		AbstractDungeon.actionManager.addToTop(new UnlimboAction(card));
		if (!Settings.FAST_MODE) {
			AbstractDungeon.actionManager.addToTop(new WaitAction(Settings.ACTION_DUR_MED));
		} else {
			AbstractDungeon.actionManager.addToTop(new WaitAction(Settings.ACTION_DUR_FASTER));
		}
	}

	private void returnCards() {
		Iterator var1 = this.notBasics.iterator();

		while (var1.hasNext()) {
			AbstractCard c = (AbstractCard) var1.next();
			this.p.hand.addToTop(c);
		}

		this.p.hand.refreshHandLayout();
	}
}
