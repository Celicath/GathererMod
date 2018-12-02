package the_gatherer.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import the_gatherer.GathererMod;
import the_gatherer.patches.CardColorEnum;

import java.util.HashSet;

public class Polymorphism extends CustomCard {
	private static final String RAW_ID = "Polymorphism";
	public static final String ID = GathererMod.makeID(RAW_ID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = GathererMod.GetCardPath(RAW_ID);
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	private static final AbstractCard.CardType TYPE = CardType.ATTACK;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.LIME;
	private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;

	private static final int POWER = 0;
	private static final int UPGRADE_BONUS = 3;
	private static final int DRAW = 2;

	public Polymorphism() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
		this.baseDamage = POWER;
		this.baseMagicNumber = DRAW;
		this.magicNumber = this.baseMagicNumber;
	}

	private void calculateDamage() {
		int count = 0;
		for (AbstractCard c : AbstractDungeon.player.hand.group) {
			if (c != this)
				count++;
		}
		this.baseDamage = this.baseDamage + count;
	}

	@Override
	public void applyPowers() {
		int temp = this.baseDamage;
		calculateDamage();
		super.applyPowers();
		this.baseDamage = temp;
		this.isDamageModified = true;

		if (isActivated()) {
			this.rawDescription = DESCRIPTION + EXTENDED_DESCRIPTION[1];
		} else {
			this.rawDescription = DESCRIPTION + EXTENDED_DESCRIPTION[0];
		}
		this.initializeDescription();
	}

	@Override
	public void calculateCardDamage(final AbstractMonster mo) {
		int temp = this.baseDamage;
		calculateDamage();
		super.calculateCardDamage(mo);
		this.baseDamage = temp;
	}

	private boolean isActivated() {
		int count = 0;
		HashSet<String> ids = new HashSet<>();
		for (AbstractCard c : AbstractDungeon.player.hand.group) {
			if (c != this) {
				count++;
				ids.add(GathererMod.getUniqueID(c));
			}
		}

		return count == ids.size();
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		if (isActivated()) {
			AbstractDungeon.actionManager.addToTop(new DrawCardAction(AbstractDungeon.player, this.magicNumber));
			AbstractDungeon.actionManager.addToTop(new GainEnergyAction(1));
		}
		AbstractDungeon.actionManager.addToBottom(new DamageAction(m,
				new DamageInfo(p, this.damage, this.damageTypeForTurn),
				AbstractGameAction.AttackEffect.BLUNT_HEAVY));

		this.rawDescription = DESCRIPTION;
		this.initializeDescription();
	}

	public AbstractCard makeCopy() {
		return new Polymorphism();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			this.upgradeDamage(UPGRADE_BONUS);
			this.rawDescription = UPGRADE_DESCRIPTION;
			this.initializeDescription();
		}
	}
}
