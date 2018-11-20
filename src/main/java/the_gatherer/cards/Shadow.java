package the_gatherer.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import the_gatherer.GathererMod;
import the_gatherer.powers.GlowPower;

public class Shadow extends CustomCard {
	private static final String RAW_ID = "Shadow";
	public static final String ID = GathererMod.makeID(RAW_ID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = GathererMod.GetCardPath(RAW_ID);
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardColor COLOR = CardColor.COLORLESS;
	private static final CardRarity RARITY = CardRarity.SPECIAL;
	private static final CardTarget TARGET = CardTarget.ENEMY;

	private static final int POWER = 8;
	private static final int UPGRADE_BONUS = 3;

	public Shadow() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

		this.baseDamage = POWER;
	}

	private void applyGlowPower() {
		if (AbstractDungeon.player.hasPower(GlowPower.POWER_ID)) {
			this.damage = this.damage * (1 + AbstractDungeon.player.getPower(GlowPower.POWER_ID).amount);
			this.isDamageModified = true;
		}
	}

	@Override
	public void applyPowers() {
		super.applyPowers();
		applyGlowPower();
	}

	@Override
	public void calculateCardDamage(final AbstractMonster mo) {
		super.calculateCardDamage(mo);
		applyGlowPower();
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new GlowPower(p, 1), 1));
	}

	public AbstractCard makeCopy() {
		return new Shadow();
	}

	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeDamage(UPGRADE_BONUS);
		}
	}
}
