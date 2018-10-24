package the_gatherer.actions;

import basemod.BaseMod;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.SoulGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;

public class GatherMaterialAction extends AbstractGameAction {
	private boolean shuffleCheck;
	private static final Logger logger = LogManager.getLogger(DrawCardAction.class.getName());
	public static int drawnAttacks = 0;

	public GatherMaterialAction(AbstractCreature source, int draw) {
		this.shuffleCheck = false;
		if (AbstractDungeon.player.hasPower("No Draw")) {
			AbstractDungeon.player.getPower("No Draw").flash();
			this.setValues(AbstractDungeon.player, source, draw);
			this.isDone = true;
			this.duration = 0.0F;
			this.actionType = ActionType.WAIT;
			return;
		}

		this.setValues(AbstractDungeon.player, source, draw);
		this.actionType = ActionType.DRAW;
		if (Settings.FAST_MODE) {
			this.duration = Settings.ACTION_DUR_XFAST;
		} else {
			this.duration = Settings.ACTION_DUR_FASTER;
		}
	}

	public void update() {
		if (this.amount <= 0) {
			this.isDone = true;
		} else {
			int deckSize = AbstractDungeon.player.drawPile.size();
			int discardSize = AbstractDungeon.player.discardPile.size();
			if (!SoulGroup.isActive()) {
				if (deckSize + discardSize == 0) {
					this.isDone = true;
				} else if (AbstractDungeon.player.hand.size() == BaseMod.MAX_HAND_SIZE) {
					AbstractDungeon.player.createHandIsFullDialog();
					this.isDone = true;
				} else {
					if (!this.shuffleCheck) {
						if (this.amount > deckSize) {
							int tmp = this.amount - deckSize;
							AbstractDungeon.actionManager.addToTop(new GatherMaterialAction(AbstractDungeon.player, tmp));
							AbstractDungeon.actionManager.addToTop(new EmptyDeckShuffleAction());
							if (deckSize != 0) {
								AbstractDungeon.actionManager.addToTop(new GatherMaterialAction(AbstractDungeon.player, deckSize));
							}

							this.amount = 0;
							this.isDone = true;
						}

						this.shuffleCheck = true;
					}

					this.duration -= Gdx.graphics.getDeltaTime();
					if (this.amount != 0 && this.duration < 0.0F) {
						if (Settings.FAST_MODE) {
							this.duration = Settings.ACTION_DUR_XFAST;
						} else {
							this.duration = Settings.ACTION_DUR_FASTER;
						}

						--this.amount;
						if (!AbstractDungeon.player.drawPile.isEmpty()) {
							if (AbstractDungeon.player.drawPile.getTopCard().type == AbstractCard.CardType.ATTACK)
								drawnAttacks++;
							AbstractDungeon.player.draw();
							AbstractDungeon.player.hand.refreshHandLayout();
						} else {
							logger.warn("Player attempted to draw from an empty drawpile mid-DrawAction?MASTER DECK: " + AbstractDungeon.player.masterDeck.getCardNames());
							this.isDone = true;
						}

						if (this.amount == 0) {
							this.isDone = true;
						}
					}
				}
			}
		}
	}
}
