package the_gatherer.potions;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import the_gatherer.patches.PotionRarityEnum;

public class LesserSwiftPotion extends AbstractPotion {
	public static final String POTION_ID = "LesserSwiftPotion";
	private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
	public static final String NAME = potionStrings.NAME;
	public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

	public LesserSwiftPotion() {
		super(NAME, POTION_ID, PotionRarityEnum.LESSER, PotionSize.H, PotionColor.SWIFT);
		this.potency = this.getPotency();
		this.description = DESCRIPTIONS[1] + this.potency + DESCRIPTIONS[2];
		this.isThrown = false;
		this.tips.add(new PowerTip(this.name, this.description));
	}

	public void use(AbstractCreature target) {
		AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, this.potency));
	}

	public AbstractPotion makeCopy() {
		return new LesserSwiftPotion();
	}

	public int getPotency(int ascensionLevel) {
		return 2;
	}
	}
