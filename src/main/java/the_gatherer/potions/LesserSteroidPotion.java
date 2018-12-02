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
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import the_gatherer.GathererMod;
import the_gatherer.patches.PotionRarityEnum;

public class LesserSteroidPotion extends SackPotion {
	private static final String RAW_ID = "LesserSteroidPotion";
	public static final String POTION_ID = GathererMod.makeID(RAW_ID);
	private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
	public static final String NAME = potionStrings.NAME;
	public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

	public LesserSteroidPotion() {
		super(NAME, POTION_ID, PotionRarityEnum.LESSER, PotionSize.FAIRY, PotionColor.STEROID);
		this.isThrown = false;

		updateDescription();

		GathererMod.setLesserPotionColors(liquidColor, hybridColor, spotsColor);
	}

	public void use(AbstractCreature target) {
		super.use(target);
		target = AbstractDungeon.player;
		if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, AbstractDungeon.player, new StrengthPower(target, this.potency), this.potency));
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, AbstractDungeon.player, new LoseStrengthPower(target, this.potency), this.potency));
		}
	}

	public AbstractPotion makeCopy() {
		return new the_gatherer.potions.LesserSteroidPotion();
	}

	@Override
	public void updateDescription() {
		super.updateDescription(DESCRIPTIONS);
		this.tips.add(new PowerTip(TipHelper.capitalize(GameDictionary.STRENGTH.NAMES[0]), GameDictionary.keywords.get(GameDictionary.STRENGTH.NAMES[0])));
	}

	@Override
	public int getBasePotency() {
		return 3;
	}
}
