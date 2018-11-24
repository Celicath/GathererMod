package the_gatherer.potions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import the_gatherer.GathererMod;
import the_gatherer.actions.RecoveryHerbAction;
import the_gatherer.patches.PotionRarityEnum;

public class RecoveryPotionPlus extends SackPotion {
	private static final String RAW_ID = "RecoveryPotionPlus";
	public static final String POTION_ID = GathererMod.makeID(RAW_ID);
	private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
	public static final String NAME = potionStrings.NAME;
	public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

	public RecoveryPotionPlus() {
		super(NAME, POTION_ID, PotionRarityEnum.SPECIAL, PotionSize.H, PotionColor.NONE);
		this.isThrown = false;

		updateDescription();

		this.liquidColor = new Color(1.0f, 1.0f, 1.0f, 0.8f);
		this.hybridColor = new Color(0.85f, 0.85f, 0.9f, 0.8f);
		this.spotsColor = null;
	}

	public void use(AbstractCreature target) { super.use(target);
		AbstractDungeon.actionManager.addToBottom(new RecoveryHerbAction(AbstractDungeon.player, AbstractDungeon.player, this.potency));
	}

	public AbstractPotion makeCopy() {
		return new RecoveryPotionPlus();
	}

	@Override
	public void updateDescription() {
		super.updateDescription(DESCRIPTIONS);
	}

	@Override
	public int getBasePotency() {
		return 15;
	}
}
