package the_gatherer.potions;

import basemod.abstracts.CustomPotion;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import the_gatherer.GathererMod;
import the_gatherer.cards.BlackTea;
import the_gatherer.modules.PotionSack;

import java.util.ArrayList;

public abstract class SackPotion extends CustomPotion {
	private static final String BT_ID = GathererMod.makeID("BlackTeaTag");
	private static final PotionStrings blackTeaStrings = CardCrawlGame.languagePack.getPotionString(BT_ID);
	public static final String BT_NAME = blackTeaStrings.NAME;
	public static final String[] BT_DESCRIPTIONS = blackTeaStrings.DESCRIPTIONS;

	public enum SackPotionTag {
		NORMAL, BLACKTEA;

		public String toString() {
			switch (this) {
				case NORMAL:
					return "";
				case BLACKTEA:
					return " <BT>";
			}
			return "";
		}
	}

	public SackPotion(String name, String id, PotionRarity rarity, PotionSize size, PotionColor color) {
		super(name, id, rarity, size, color);
	}

	public int upgrade = 0;
	public SackPotionTag tag = SackPotionTag.NORMAL;

	@Override
	public void use(AbstractCreature target) {
		if (tag == SackPotionTag.BLACKTEA) {
			ArrayList<AbstractCard> handCopy = new ArrayList<>();
			for (AbstractCard c : AbstractDungeon.player.hand.group) {
				if (c instanceof BlackTea) {
					handCopy.add(c);
				}
			}
			ArrayList<AbstractCard> deckCopy = new ArrayList<>();
			for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
				if (c instanceof BlackTea) {
					deckCopy.add(c);
				}
			}
			ArrayList<AbstractCard> discardCopy = new ArrayList<>();
			for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
				if (c instanceof BlackTea) {
					discardCopy.add(c);
				}
			}
			for (AbstractCard c : handCopy) {
				AbstractDungeon.actionManager.addToTop(new ExhaustSpecificCardAction(c, AbstractDungeon.player.hand));
			}
			for (AbstractCard c : deckCopy) {
				AbstractDungeon.actionManager.addToTop(new ExhaustSpecificCardAction(c, AbstractDungeon.player.drawPile));
			}
			for (AbstractCard c : discardCopy) {
				AbstractDungeon.actionManager.addToTop(new ExhaustSpecificCardAction(c, AbstractDungeon.player.discardPile));
			}
		}
	}

	public abstract void updateDescription();

	protected void updateDescription(String[] DESCRIPTIONS) {
		this.upgrade = PotionSack.potionPotency;
		this.potency = getPotency();

		this.tips.clear();
		if (DESCRIPTIONS.length > 1) {
			this.description = DESCRIPTIONS[0] + this.potency + DESCRIPTIONS[1];
		} else {
			this.description = DESCRIPTIONS[0];
		}

		if (tag == SackPotionTag.BLACKTEA) {
			this.description += BT_DESCRIPTIONS[0];
		}

		this.tips.add(new PowerTip(this.name, this.description));
	}

	public int getPotency(int ascensionLevel) {
		return getBasePotency() * (upgrade + 2) / 2;
	}

	public abstract int getBasePotency();

	public void setTag(SackPotionTag tag) {
		this.tag = tag;
		this.name += tag.toString();
		updateDescription();
	}
}
