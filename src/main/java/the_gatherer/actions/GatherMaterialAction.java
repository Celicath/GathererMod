package the_gatherer.actions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import the_gatherer.GathererMod;

import java.util.HashSet;

public class GatherMaterialAction extends AbstractGameAction {
	private AbstractPlayer p;

	public GatherMaterialAction(int amount) {
		this.p = AbstractDungeon.player;
		this.setValues(this.p, this.p, amount);
		this.actionType = ActionType.CARD_MANIPULATION;
		this.duration = Settings.ACTION_DUR_MED;
	}

	public void update() {
		if (this.duration == Settings.ACTION_DUR_MED) {
			HashSet<String> uniques = new HashSet<>();
			CardGroup moveFromDrawToHand = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
			CardGroup moveFromDiscardToHand = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
			CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

			for (AbstractCard c : this.p.drawPile.group) {
				tmp.addToRandomSpot(c);
			}
			tmp.shuffle();
			for (int i = 0; uniques.size() < this.amount && i < tmp.size(); ++i) {
				AbstractCard card = tmp.getNCardFromTop(i);
				if (!uniques.contains(GathererMod.getUniqueID(card))) {
					uniques.add(GathererMod.getUniqueID(card));
					moveFromDrawToHand.addToBottom(card);
				}
			}

			if (this.amount > uniques.size()) {
				tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
				for (AbstractCard c : this.p.discardPile.group) {
					tmp.addToRandomSpot(c);
				}
				tmp.shuffle();
				for (int i = 0; uniques.size() < this.amount && i < tmp.size(); ++i) {
					AbstractCard card = tmp.getNCardFromTop(i);
					if (!uniques.contains(GathererMod.getUniqueID(card))) {
						uniques.add(GathererMod.getUniqueID(card));
						moveFromDiscardToHand.addToBottom(card);
					}
				}
			}

			for (int i = 0; i < moveFromDrawToHand.size(); i++) {
				AbstractCard card = moveFromDrawToHand.getNCardFromTop(i);
				if (this.p.hand.size() == BaseMod.MAX_HAND_SIZE) {
					this.p.drawPile.moveToDiscardPile(card);
					this.p.createHandIsFullDialog();
				} else {
					this.p.drawPile.moveToHand(card, this.p.drawPile);
				}
			}


			for (int i = 0; i < moveFromDiscardToHand.size(); i++) {
				AbstractCard card = moveFromDiscardToHand.getNCardFromTop(i);
				if (this.p.hand.size() == BaseMod.MAX_HAND_SIZE) {
					this.p.createHandIsFullDialog();
				} else {
					card.unhover();
					card.lighten(true);
					card.setAngle(0.0F);
					card.drawScale = 0.12F;
					card.targetDrawScale = 0.75F;
					card.current_x = CardGroup.DISCARD_PILE_X;
					card.current_y = CardGroup.DISCARD_PILE_Y;
					this.p.discardPile.removeCard(card);
					AbstractDungeon.player.hand.addToTop(card);
					AbstractDungeon.player.hand.refreshHandLayout();
					AbstractDungeon.player.hand.applyPowers();
				}
			}

			this.isDone = true;
		}

		this.tickDuration();
	}
}
