package the_gatherer.cards;

import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AlwaysRetainField;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import the_gatherer.GathererMod;

public class Glitched extends CustomCard {
	private static final String RAW_ID = "Glitched";
	public static final String ID = GathererMod.makeID(RAW_ID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = GathererMod.GetCardPath(RAW_ID);
	private static final int COST = 3;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.STATUS;
	private static final CardColor COLOR = CardColor.COLORLESS;
	private static final CardRarity RARITY = CardRarity.COMMON;
	private static final CardTarget TARGET = CardTarget.SELF;

	private static final int NEW_COST = 2;

	public Glitched() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

		this.rawDescription = DESCRIPTION;
		this.initializeDescription();
		AlwaysRetainField.alwaysRetain.set(this, true);
		this.exhaust = true;
	}

	@Override
	public boolean canUse(AbstractPlayer p, AbstractMonster m) {
		return this.hasEnoughEnergy();
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
	}

	public AbstractCard makeCopy() {
		return new Glitched();
	}

	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeBaseCost(NEW_COST);
		}
	}
}
