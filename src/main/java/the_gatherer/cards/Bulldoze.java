package the_gatherer.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import the_gatherer.GathererMod;
import the_gatherer.patches.CardColorEnum;

public class Bulldoze extends CustomCard {
	private static final String RAW_ID = "Bulldoze";
	public static final String ID = GathererMod.makeID(RAW_ID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = GathererMod.GetCardPath(RAW_ID);
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardColor COLOR = CardColorEnum.LIME;
	private static final CardRarity RARITY = CardRarity.RARE;
	private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

	private static final int POWER = 10;
	private static final int MAGIC = 4;
	private static final int MAGIC_BONUS = -1;

	public Bulldoze() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
		this.baseMagicNumber = MAGIC;
		this.magicNumber = this.baseMagicNumber;
		this.baseDamage = POWER;
		this.isMultiDamage = true;
	}

	public int hits() {
		return GathererMod.countUnique(AbstractDungeon.player.discardPile) / magicNumber;
	}

	@Override
	public void applyPowers() {
		super.applyPowers();

		this.rawDescription = DESCRIPTION + EXTENDED_DESCRIPTION[0] + GathererMod.countUnique(AbstractDungeon.player.discardPile) + EXTENDED_DESCRIPTION[1];
		this.initializeDescription();
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		for (int i = 0 ; i < hits(); i++) {
			AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_HEAVY));
		}

		this.rawDescription = DESCRIPTION;
		this.initializeDescription();
	}

	public AbstractCard makeCopy() {
		return new Bulldoze();
	}

	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeMagicNumber(MAGIC_BONUS);
		}
	}
}
