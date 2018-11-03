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
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import the_gatherer.GathererMod;
import the_gatherer.patches.CardColorEnum;

public class RustyPipe extends CustomCard {
	private static final String RAW_ID = "RustyPipe";
	public static final String ID = GathererMod.makeID(RAW_ID);

	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = GathererMod.GetCardPath(RAW_ID);
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardColor COLOR = CardColorEnum.LIME;
	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.ENEMY;

	private static final int MIN_POWER = 5;
	private static final int UPGRADE_BONUS = 3;
	private static final int MIN_MAGIC = 1;
	private static final int MAGIC_BONUS = 1;

	private boolean initialized = false;


	public enum RustyPipeDebuffEnum {
		Weak(0),
		Vulnerable(1),
		Poison(2);

		private int value;

		RustyPipeDebuffEnum(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	public RustyPipe() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

		this.misc = -1;
		this.rawDescription = DESCRIPTION + EXTENDED_DESCRIPTION[4];
		this.initializeDescription();
	}

	private RustyPipeDebuffEnum getDebuff() {
		if (this.misc % 7 < 2)
			return RustyPipeDebuffEnum.Weak;
		else if (this.misc % 7 < 4)
			return RustyPipeDebuffEnum.Vulnerable;
		else
			return RustyPipeDebuffEnum.Poison;
	}

	@Override
	public AbstractCard makeStatEquivalentCopy() {
		RustyPipe c = (RustyPipe) (super.makeStatEquivalentCopy());
		c.updateDescription();
		return c;
	}

	@Override
	public void update() {
		if (this.misc == -1 && AbstractDungeon.cardRng != null /* How do you detect if you are in the compendium? */) {
			this.misc = AbstractDungeon.cardRng.random(48);
		}
		updateDescription();
		super.update();
	}

	public void updateDescription() {
		if (this.misc >= 0 && !initialized) {
			this.baseDamage = misc / 7 + MIN_POWER;
			RustyPipeDebuffEnum debuff = getDebuff();
			this.baseMagicNumber = MIN_MAGIC + (misc % 7 - debuff.getValue() * 2);
			this.magicNumber = this.baseMagicNumber;
			if (upgraded) {
				upgradeEffect();
			}
			this.rawDescription = EXTENDED_DESCRIPTION[0] + EXTENDED_DESCRIPTION[debuff.getValue() + 1] + EXTENDED_DESCRIPTION[4];
			this.initializeDescription();
			initialized = true;
		}
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));

		AbstractPower pow;
		switch (getDebuff()) {
			case Weak:
				pow = new WeakPower(m, this.magicNumber, false);
				break;
			case Vulnerable:
				pow = new VulnerablePower(m, this.magicNumber, false);
				break;
			case Poison:
				pow = new PoisonPower(m, p, this.magicNumber);
				break;
			default:
				pow = new PoisonPower(m, p, this.magicNumber);
		}
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, pow, this.magicNumber));
	}

	public AbstractCard makeCopy() {
		return new RustyPipe();
	}

	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			upgradeEffect();
		}
	}

	private void upgradeEffect() {
		this.upgradeDamage(UPGRADE_BONUS);
		this.upgradeMagicNumber(MAGIC_BONUS);
	}
}
