//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

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

public class SecretPlanPower extends AbstractPower {
	private static final String RAW_ID = "SecretPlan";
	public static final String POWER_ID = GathererMod.makeID(RAW_ID);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
	private ArrayList<AbstractCard> cards;

	public static HashSet<AbstractCard> discount1 = new HashSet<>();
	public static HashSet<AbstractCard> discount0 = new HashSet<>();

	public SecretPlanPower(AbstractCard card, boolean upgraded) {
		cards = new ArrayList<>();
		this.name = NAME;
		this.ID = POWER_ID;
		this.type = PowerType.BUFF;
		this.owner = AbstractDungeon.player;
		this.isTurnBased = false;
		this.img = new Texture(GathererMod.GetPowerPath(RAW_ID));

		addCard(card, upgraded);
	}

	public void addCard(AbstractCard card, boolean upgraded) {
		if (upgraded) discount0.add(card);
		else discount1.add(card);
		cards.add(card);
		this.amount = cards.size();
		updateDescription();
	}

	public void updateDescription() {
		if (cards.size() > 1) {
			this.description = DESCRIPTIONS[0] + cards.get(0).name + DESCRIPTIONS[2] + (cards.size() - 1) + DESCRIPTIONS[3] + DESCRIPTIONS[5];
		} else {
			this.description = DESCRIPTIONS[0] + cards.get(0).name + DESCRIPTIONS[1] + DESCRIPTIONS[4];
		}
	}

	@Override
	public void atStartOfTurn() {
		AbstractPlayer p = (AbstractPlayer)owner;

		ArrayList<AbstractCard> cardsToMove = new ArrayList<>();

		for (AbstractCard c : p.drawPile.group) {
			if (discount0.contains(c) || discount1.contains(c)) {
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
			if (discount0.contains(c) || discount1.contains(c)) {
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

		for(AbstractCard c : discount1) {
			c.costForTurn = Math.max(c.costForTurn - 1, 0);
			c.flash();
			c.isCostModifiedForTurn = true;
		}
		for(AbstractCard c : discount0) {
			c.costForTurn = 0;
			c.flash();
			c.isCostModifiedForTurn = true;
		}

		AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
		discount1.clear();
		discount0.clear();
	}
}
