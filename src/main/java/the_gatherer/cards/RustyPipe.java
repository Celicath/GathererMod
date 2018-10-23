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
import com.megacrit.cardcrawl.powers.*;
import the_gatherer.GathererMod;
import the_gatherer.patches.AbstractCardEnum;

public class RustyPipe extends CustomCard {
	private static final String CardID = "RustyPipe";
	public static final String ID = GathererMod.makeID(CardID);

	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = "img/cards/" + CardID + ".png";
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardColor COLOR = AbstractCardEnum.LIME;
	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.ENEMY;

	private static final int MIN_POWER = 4;
	private static final int UPGRADE_BONUS = 3;
	private static final int MIN_MAGIC = 1;
	private static final int MAGIC_BONUS = 1;

	private boolean initialized = false;

	public RustyPipe() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

		this.misc = -1;
	}

	private int getDebuff() {
		if (this.misc % 7 < 2)
			return 0;
		else if (this.misc % 7 < 4)
			return 1;
		else
			return 2;
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
			int debuff = getDebuff();
			this.baseMagicNumber = MIN_MAGIC + (misc % 7 - debuff * 2);
			this.rawDescription = EXTENDED_DESCRIPTION[0] + EXTENDED_DESCRIPTION[debuff + 1] + EXTENDED_DESCRIPTION[4];
			this.initializeDescription();
			initialized = true;
		}
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));

		AbstractPower pow;
		switch (getDebuff()) {
			case 0:
				pow = new PoisonPower(m, p, this.magicNumber);
				break;
			case 1:
				pow = new WeakPower(m, this.magicNumber, false);
				break;
			case 2:
				pow = new VulnerablePower(m, this.magicNumber, false);
				break;
		}
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, pow, this.magicNumber));
	}

	public AbstractCard makeCopy() {
		return new RustyPipe();
	}

	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeDamage(UPGRADE_BONUS);
			this.upgradeMagicNumber(MAGIC_BONUS);
		}
	}
}
