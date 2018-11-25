package the_gatherer.potions;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.WeakPower;
import the_gatherer.GathererMod;
import the_gatherer.patches.PotionRarityEnum;

public class LesserWeakPotion extends SackPotion {
	private static final String RAW_ID = "LesserWeakPotion";
	public static final String POTION_ID = GathererMod.makeID(RAW_ID);
	private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
	public static final String NAME = potionStrings.NAME;
	public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

	public LesserWeakPotion() {
		super(NAME, POTION_ID, PotionRarityEnum.LESSER, PotionSize.H, PotionColor.WEAK);
		this.isThrown = true;
		this.targetRequired = true;

		updateDescription();

		GathererMod.setLesserPotionColors(liquidColor, hybridColor, spotsColor);
	}

	public void use(AbstractCreature target) {
		super.use(target);
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, AbstractDungeon.player, new WeakPower(target, this.potency, false), this.potency));
	}

	public AbstractPotion makeCopy() {
		return new LesserWeakPotion();
	}

	@Override
	public void updateDescription() {
		super.updateDescription(DESCRIPTIONS);
		this.tips.add(new PowerTip(TipHelper.capitalize(GameDictionary.WEAK.NAMES[0]), GameDictionary.keywords.get(GameDictionary.WEAK.NAMES[0])));
	}

	@Override
	public int getBasePotency() {
		return 2;
	}
}
