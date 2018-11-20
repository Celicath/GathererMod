package the_gatherer.actions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

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
			if (this.p.drawPile.isEmpty()) {
				this.isDone = true;
				return;
			}

			CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
			HashSet<String> uniques = new HashSet<>();

			for (AbstractCard c : this.p.drawPile.group) {
				if (!uniques.contains(c.cardID)) {
					tmp.addToRandomSpot(c);
					uniques.add(c.cardID);
				}
			}

			tmp.shuffle();
			for (int i = 0; i < this.amount; ++i) {
				if (!tmp.isEmpty()) {
					AbstractCard card = tmp.getBottomCard();
					tmp.removeCard(card);
					if (this.p.hand.size() == BaseMod.MAX_HAND_SIZE) {
						this.p.drawPile.moveToDiscardPile(card);
						this.p.createHandIsFullDialog();
					} else {
						card.unhover();
						card.lighten(true);
						card.setAngle(0.0F);
						card.drawScale = 0.12F;
						card.targetDrawScale = 0.75F;
						card.current_x = CardGroup.DRAW_PILE_X;
						card.current_y = CardGroup.DRAW_PILE_Y;
						this.p.drawPile.removeCard(card);
						AbstractDungeon.player.hand.addToTop(card);
						AbstractDungeon.player.hand.refreshHandLayout();
						AbstractDungeon.player.hand.applyPowers();
					}
				}
			}

			this.isDone = true;
		}

		this.tickDuration();
	}
}
