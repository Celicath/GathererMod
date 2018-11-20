package the_gatherer.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import the_gatherer.GathererMod;
import the_gatherer.actions.DamageRandomEnemyExceptTargetAction;
import the_gatherer.patches.CardColorEnum;

public class GrassHammer extends CustomCard {
	private static final String RAW_ID = "GrassHammer";
	public static final String ID = GathererMod.makeID(RAW_ID);

	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = GathererMod.GetCardPath(RAW_ID);
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardColor COLOR = CardColorEnum.LIME;
	private static final CardRarity RARITY = CardRarity.COMMON;
	private static final CardTarget TARGET = CardTarget.ENEMY;

	private static final int POWER = 8;
	private static final int UPGRADE_BONUS = 3;
	private static final int SECONDARY_POWER = 11;
	private static final int SECONDARY_BONUS = 4;

	public GrassHammer() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
		this.baseDamage = POWER;
		this.damage = POWER;
		this.baseMagicNumber = SECONDARY_POWER;
		this.magicNumber = this.baseMagicNumber;
	}

	@Override
	public void applyPowers() {
		final int current_damage = this.baseDamage;
		this.baseDamage = this.baseMagicNumber;
		super.applyPowers();

		this.magicNumber = this.damage;
		this.isMagicNumberModified = this.magicNumber != this.baseMagicNumber;

		this.baseDamage = current_damage;
		super.applyPowers();
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
		AbstractDungeon.actionManager.addToBottom(
				new DamageRandomEnemyExceptTargetAction(m, new DamageInfo(p, this.magicNumber),
						AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
	}

	public AbstractCard makeCopy() {
		return new GrassHammer();
	}

	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeDamage(UPGRADE_BONUS);
			this.upgradeMagicNumber(SECONDARY_BONUS);
		}
	}
}
