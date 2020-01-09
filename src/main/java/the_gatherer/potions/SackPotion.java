package the_gatherer.potions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import javafx.util.Pair;
import the_gatherer.GathererMod;
import the_gatherer.actions.ChooseLesserPotionAction;
import the_gatherer.cards.BlackTea;
import the_gatherer.modules.PotionSack;

import java.util.ArrayList;

import static the_gatherer.powers.UpgradeBagPower.POTENCY_MULT;

public abstract class SackPotion extends AbstractPotion {
	private static final String BT_ID = GathererMod.makeID("BlackTeaTag");
	private static final PotionStrings blackTeaStrings = CardCrawlGame.languagePack.getPotionString(BT_ID);
	public static final String BT_NAME = blackTeaStrings.NAME;
	public static final String[] BT_DESCRIPTIONS = blackTeaStrings.DESCRIPTIONS;

	protected String rawName;

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

	public SackPotion(String name, String id, PotionRarity rarity, PotionSize size, PotionEffect effect, Color liquidColor, Color hybridColor, Color spotsColor) {
		super(name, id, rarity, size, effect, liquidColor, hybridColor, spotsColor);
		this.rawName = this.name;
	}

	public SackPotion(String name, String id, PotionRarity rarity, PotionSize size, PotionColor color) {
		super(name, id, rarity, size, color);
		this.rawName = this.name;
	}

	public int upgrade = 0;
	public SackPotionTag tag = SackPotionTag.NORMAL;

	// This code is for old Black Tea, which exhausted when you used <BT> potion.
	// This method is not called if you use Duplicator or Drug Power.
	public void actualUseEffect() {
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
			this.name = this.rawName;
		}
	}

	public abstract void updateDescription();

	protected void updateDescription(String[] DESCRIPTIONS) {
		this.upgrade = PotionSack.potionPotency;
		this.potency = getPotency();

		this.tips.clear();
		this.description = DESCRIPTIONS[0];
		for (int i = 1; i < DESCRIPTIONS.length; i++) {
			this.description += this.potency + DESCRIPTIONS[i];
		}

		this.tips.add(new PowerTip(this.name, this.description));

		if (this.tag.equals(SackPotionTag.BLACKTEA)) {
			this.tips.add(new PowerTip(BT_NAME, BT_DESCRIPTIONS[0]));
		}
	}

	public int getPotency(int ascensionLevel) {
		return getModifiedBasePotency() * (upgrade * POTENCY_MULT + 100) / 100;
	}

	public int getModifiedBasePotency() {
		return getBasePotency();
	}

	public abstract int getBasePotency();

	public void setTag(SackPotionTag tag) {
		this.tag = tag;
		this.name = this.rawName + tag.toString();
		updateDescription();
	}

	public SackPotion getStatEquivalentCopy() {
		SackPotion sp = (SackPotion) this.makeCopy();
		sp.setTag(this.tag);
		return sp;
	}

	public Pair<Integer, String> getMindSearchResult() {
		return new Pair<>(1, ChooseLesserPotionAction.MIND_SEARCH_TEXT[0]);
	}
}
