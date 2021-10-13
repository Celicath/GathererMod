package the_gatherer.potions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import basemod.Pair;
import the_gatherer.GathererMod;
import the_gatherer.actions.ChooseLesserPotionAction;
import the_gatherer.patches.PotionRarityEnum;
import the_gatherer.powers.BomberFormPower;
import the_gatherer.powers.ExplodingPower;

public class LesserExplosivePotion extends SackPotion {
	private static final String RAW_ID = "LesserExplosivePotion";
	public static final String POTION_ID = GathererMod.makeID(RAW_ID);
	private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
	public static final String NAME = potionStrings.NAME;
	public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

	public LesserExplosivePotion() {
		super(NAME, POTION_ID, PotionRarityEnum.LESSER, PotionSize.H, PotionColor.EXPLOSIVE);
		this.isThrown = true;

		this.potency = getPotency();

		updateDescription();

		GathererMod.setLesserPotionColors(liquidColor, hybridColor, spotsColor);
	}

	public void use(AbstractCreature target) {
		for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
			if (!m.isDeadOrEscaped()) {
				AbstractDungeon.actionManager.addToBottom(new VFXAction(new ExplosionSmallEffect(m.hb.cX, m.hb.cY), 0.08F));
			}
		}

		AbstractDungeon.actionManager.addToBottom(new WaitAction(0.4F));
		AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(null, DamageInfo.createDamageMatrix(this.potency, true), DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.NONE));

		BomberFormPower bfp = (BomberFormPower) AbstractDungeon.player.getPower(BomberFormPower.POWER_ID);
		if (bfp != null) {
			AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ExplodingPower(AbstractDungeon.player, bfp.amount), bfp.amount));
		}
	}

	public AbstractPotion makeCopy() {
		return new LesserExplosivePotion();
	}

	@Override
	public void updateDescription() {
		super.updateDescription(DESCRIPTIONS);
	}

	@Override
	public int getModifiedBasePotency() {
		if (AbstractDungeon.player != null) {
			AbstractPower ep = AbstractDungeon.player.getPower(ExplodingPower.POWER_ID);
			if (ep != null) {
				return getBasePotency() + ep.amount;
			}
		}
		return getBasePotency();
	}

	@Override
	public int getBasePotency() {
		return 6;
	}

	@Override
	public Pair<Integer, String> getMindSearchResult() {
		int weight = 1;
		String thought = ChooseLesserPotionAction.MIND_SEARCH_TEXT[0];

		if (ChooseLesserPotionAction.enemyCount > 1) {
			weight += ChooseLesserPotionAction.enemyCount * 2;
			thought = ChooseLesserPotionAction.MIND_SEARCH_TEXT[7];
		}
		if (ChooseLesserPotionAction.lowestEnemyHP <= getPotency()) {
			weight += 12;
			thought = ChooseLesserPotionAction.MIND_SEARCH_TEXT[8];
		}

		return new Pair<>(weight, thought);
	}
}
