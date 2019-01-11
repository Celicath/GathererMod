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
			HashSet<String> uniquesHand = new HashSet<>();
			HashSet<String> uniques = new HashSet<>();
			CardGroup moveFromDrawToHand = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
			CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

			for (AbstractCard c : this.p.hand.group) {
				uniquesHand.add(GathererMod.getUniqueID(c));
			}
			for (AbstractCard c : this.p.drawPile.group) {
				tmp.addToRandomSpot(c);
			}
			tmp.shuffle();

			// first: Pick cards not in your hand
			for (int i = 0; uniques.size() < this.amount && i < tmp.size(); ++i) {
				AbstractCard card = tmp.getNCardFromTop(i);
				if (!uniquesHand.contains(GathererMod.getUniqueID(card))) {
					uniquesHand.add(GathererMod.getUniqueID(card));
					uniques.add(GathererMod.getUniqueID(card));
					moveFromDrawToHand.addToBottom(card);
				}
			}
			tmp.group.removeAll(moveFromDrawToHand.group);

			// second: Pick unique cards
			for (int i = 0; uniques.size() < this.amount && i < tmp.size(); ++i) {
				AbstractCard card = tmp.getNCardFromTop(i);
				if (!uniques.contains(GathererMod.getUniqueID(card))) {
					uniques.add(GathererMod.getUniqueID(card));
					moveFromDrawToHand.addToBottom(card);
				}
			}
			tmp.group.removeAll(moveFromDrawToHand.group);

			// final: Pick any card
			for (int i = 0; i < tmp.size(); ++i) {
				AbstractCard card = tmp.getNCardFromTop(i);
				moveFromDrawToHand.addToBottom(card);
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

			this.isDone = true;
		}

		this.tickDuration();
	}
}
