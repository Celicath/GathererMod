package the_gatherer.potions;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.EquilibriumPower;
import the_gatherer.GathererMod;
import the_gatherer.patches.PotionRarityEnum;

public class RetainPotion extends SackPotion {
	private static final String RAW_ID = "RetainPotion";
	public static final String POTION_ID = GathererMod.makeID(RAW_ID);
	private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
	public static final String NAME = potionStrings.NAME;
	public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

	public RetainPotion() {
		super(NAME, POTION_ID, PotionRarityEnum.SPECIAL, PotionSize.H, PotionColor.WHITE);
		this.isThrown = false;

		updateDescription();

		GathererMod.setLesserPotionColors(liquidColor, hybridColor, spotsColor);
	}

	public void use(AbstractCreature target) {
		super.use(target);
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
				AbstractDungeon.player, AbstractDungeon.player, new EquilibriumPower(AbstractDungeon.player, 1), 1));
	}

	public AbstractPotion makeCopy() {
		return new RetainPotion();
	}

	@Override
	public void updateDescription() {
		super.updateDescription(DESCRIPTIONS);
	}

	@Override
	public int getBasePotency() {
		return 1;
	}
}
