package the_gatherer.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.FruitJuice;
import com.megacrit.cardcrawl.vfx.SpeechBubble;
import the_gatherer.GathererMod;
import the_gatherer.actions.UsePotionOnRandomTargetAction;
import the_gatherer.patches.CardColorEnum;

public class DrugPower extends CustomCard {
	private static final String RAW_ID = "DrugPower";
	public static final String ID = GathererMod.makeID(RAW_ID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = GathererMod.GetCardPath(RAW_ID);
	private static final int COST = 2;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardColor COLOR = CardColorEnum.LIME;
	private static final CardRarity RARITY = CardRarity.COMMON;
	private static final CardTarget TARGET = CardTarget.ENEMY;

	private static final int POWER = 12;
	private static final int UPGRADE_BONUS = 4;

	public DrugPower() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

		this.baseDamage = POWER;
	}

	public void applyPowers() {
		if (GathererMod.lastPotionUsedThisTurn != null) {
			this.rawDescription = DESCRIPTION + EXTENDED_DESCRIPTION[1] + GathererMod.lastPotionUsedThisTurn.name + EXTENDED_DESCRIPTION[2];
		} else {
			this.rawDescription = DESCRIPTION + EXTENDED_DESCRIPTION[0];
		}
		this.initializeDescription();
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new DamageAction(m,
				new DamageInfo(p, this.damage, this.damageTypeForTurn),
				AbstractGameAction.AttackEffect.BLUNT_HEAVY));

		if (GathererMod.lastPotionUsedThisTurn != null) {
			boolean notUsing = false;
			if (GathererMod.lastPotionUsedThisTurn instanceof FruitJuice) {
				if (GathererMod.drugPowerHPGain >= 100) {
					AbstractDungeon.effectList.add(new SpeechBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 2.0f,
							EXTENDED_DESCRIPTION[3], true));
					notUsing = true;
				} else {
					GathererMod.drugPowerHPGain += GathererMod.lastPotionUsedThisTurn.getPotency();
				}
			}
			if (!notUsing) {
				AbstractDungeon.actionManager.addToBottom(new UsePotionOnRandomTargetAction(GathererMod.lastPotionUsedThisTurn));
			}
		}

		this.rawDescription = DESCRIPTION;
		this.initializeDescription();
	}

	public AbstractCard makeCopy() {
		return new DrugPower();
	}

	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeDamage(UPGRADE_BONUS);
		}
	}
}
