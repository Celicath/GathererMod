package the_gatherer.powers;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import the_gatherer.GathererMod;

import java.util.ArrayList;
import java.util.HashSet;

public class ScentOfRosmariPower extends AbstractPower {
	private static final String RAW_ID = "ScentOfRosmari";
	public static final String POWER_ID = GathererMod.makeID(RAW_ID);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	public static HashSet<AbstractCard> cardList = new HashSet<>();
	public String firstCard;

	public ScentOfRosmariPower(ArrayList<AbstractCard> cards) {
		this.name = NAME;
		this.ID = POWER_ID;
		this.type = PowerType.BUFF;
		this.owner = AbstractDungeon.player;
		this.isTurnBased = false;
		this.img = new Texture(GathererMod.GetPowerPath(RAW_ID));

		firstCard = cards.get(0).name;

		for (AbstractCard c : cards) {
			addCard(c);
		}
	}

	public void addCard(AbstractCard card) {
		cardList.add(card);
		this.amount = cardList.size();
		updateDescription();
	}

	public void updateDescription() {
		if (cardList.size() > 1) {
			this.description = DESCRIPTIONS[0] + firstCard + DESCRIPTIONS[2] + (amount - 1) + DESCRIPTIONS[3];
		} else {
			this.description = DESCRIPTIONS[0] + firstCard + DESCRIPTIONS[1];
		}
	}

	@Override
	public void atStartOfTurn() {
		AbstractPlayer p = (AbstractPlayer) owner;

		ArrayList<AbstractCard> cardsToMove = new ArrayList<>();

		for (AbstractCard c : p.drawPile.group) {
			if (cardList.contains(c)) {
				if (p.hand.size() == BaseMod.MAX_HAND_SIZE) {
					p.drawPile.moveToDiscardPile(c);
					p.createHandIsFullDialog();
				} else {
					c.unhover();
					c.lighten(true);
					c.setAngle(0.0F);
					c.drawScale = 0.12F;
					c.targetDrawScale = 0.75F;
					c.current_x = CardGroup.DRAW_PILE_X;
					c.current_y = CardGroup.DRAW_PILE_Y;
					cardsToMove.add(c);
				}
			}
		}
		for (AbstractCard c : cardsToMove) {
			p.drawPile.moveToHand(c, p.drawPile);
		}
		cardsToMove.clear();

		for (AbstractCard c : p.discardPile.group) {
			if (cardList.contains(c)) {
				if (p.hand.size() == BaseMod.MAX_HAND_SIZE) {
					p.createHandIsFullDialog();
				} else {
					c.unhover();
					c.lighten(true);
					c.setAngle(0.0F);
					c.drawScale = 0.12F;
					c.targetDrawScale = 0.75F;
					c.current_x = CardGroup.DISCARD_PILE_X;
					c.current_y = CardGroup.DISCARD_PILE_Y;
					cardsToMove.add(c);
				}
			}
		}

		for (AbstractCard c : cardsToMove) {
			p.discardPile.removeCard(c);
			p.hand.addToHand(c);
		}

		cardList.clear();

		AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
	}

	@Override
	public void atEndOfTurn ( boolean isPlayer){
		if (isPlayer) {
			for (AbstractCard c : AbstractDungeon.player.hand.group) {
				if (cardList.contains(c)) {
					c.retain = true;
				}
			}
		}
	}
}
