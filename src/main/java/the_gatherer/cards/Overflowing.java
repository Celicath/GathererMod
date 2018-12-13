package the_gatherer.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ConservePower;
import the_gatherer.GathererMod;
import the_gatherer.patches.CardColorEnum;

public class Overflowing extends CustomCard {
	private static final String RAW_ID = "Overflowing";
	public static final String ID = GathererMod.makeID(RAW_ID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = GathererMod.GetCardPath(RAW_ID);
	private static final int COST = 0;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardColor COLOR = CardColorEnum.GATHERER_LIME;
	private static final CardRarity RARITY = CardRarity.RARE;
	private static final CardTarget TARGET = CardTarget.SELF;

	private static final int POWER = 3;
	private static final int UPGRADE_BONUS = 1;

	public Overflowing() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
		this.baseMagicNumber = POWER;
		this.magicNumber = this.baseMagicNumber;
		this.exhaust = true;
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(this.magicNumber));
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new ConservePower(p, 1), 1));
	}

	public AbstractCard makeCopy() {
		return new Overflowing();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeMagicNumber(UPGRADE_BONUS);
			rawDescription = UPGRADE_DESCRIPTION;
			initializeDescription();
		}
	}
}
