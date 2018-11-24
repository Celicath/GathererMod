package the_gatherer.potions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import the_gatherer.GathererMod;
import the_gatherer.patches.PotionRarityEnum;
import the_gatherer.powers.ExplodingPower;

import java.util.Iterator;

public class LesserExplosivePotion extends SackPotion {
	private static final String RAW_ID = "LesserExplosivePotion";
	public static final String POTION_ID = GathererMod.makeID(RAW_ID);
	private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
	public static final String NAME = potionStrings.NAME;
	public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

	public LesserExplosivePotion() {
		super(NAME, POTION_ID, PotionRarityEnum.LESSER, PotionSize.H, PotionColor.EXPLOSIVE);
		this.isThrown = true;

		updateDescription();

		GathererMod.setLesserPotionColors(liquidColor, hybridColor, spotsColor);
	}

	public void use(AbstractCreature target) { super.use(target);
		Iterator var2 = AbstractDungeon.getMonsters().monsters.iterator();

		while (var2.hasNext()) {
			AbstractMonster m = (AbstractMonster) var2.next();
			if (!m.isDeadOrEscaped()) {
				AbstractDungeon.actionManager.addToBottom(new VFXAction(new ExplosionSmallEffect(m.hb.cX, m.hb.cY), 0.1F));
			}
		}

		AbstractDungeon.actionManager.addToBottom(new WaitAction(0.5F));
		AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(null, DamageInfo.createDamageMatrix(this.potency, true), DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.NONE));
	}

	public AbstractPotion makeCopy() {
		return new LesserExplosivePotion();
	}

	@Override
	public void updateDescription() {
		super.updateDescription(DESCRIPTIONS);
	}

	@Override
	public int getBasePotency() {
		if (AbstractDungeon.player != null && AbstractDungeon.player.hasPower(ExplodingPower.POWER_ID)) {
			return 6 + AbstractDungeon.player.getPower(ExplodingPower.POWER_ID).amount;
		} else {
			return 6;
		}
	}
}
