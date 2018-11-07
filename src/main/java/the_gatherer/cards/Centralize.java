package the_gatherer.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import the_gatherer.GathererMod;
import the_gatherer.actions.CentralizeAction;
import the_gatherer.cards.Helper.AbstractNumberedCard;
import the_gatherer.patches.CardColorEnum;

public class Centralize extends AbstractNumberedCard {
	private static final String RAW_ID = "Centralize";
	public static final String ID = GathererMod.makeID(RAW_ID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = GathererMod.GetCardPath(RAW_ID);
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	private static final AbstractCard.CardType TYPE = AbstractCard.CardType.SKILL;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.LIME;
	private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.BASIC;
	private static final AbstractCard.CardTarget TARGET = AbstractCard.CardTarget.SELF;

	private static final int POWER = 1;
	private static final int UPGRADE_BONUS = 1;

	public Centralize() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
		this.baseMagicNumber = POWER;
		this.magicNumber = this.baseMagicNumber;
		updateDescription();
	}

	public void updateDescription() {
		if (upgraded) {
			if (playCount == 0) {
				this.rawDescription = UPGRADE_DESCRIPTION + EXTENDED_DESCRIPTION[0];
			} else if (playCount == 1) {
				this.rawDescription = UPGRADE_DESCRIPTION + EXTENDED_DESCRIPTION[1];
			} else {
				this.rawDescription = UPGRADE_DESCRIPTION + EXTENDED_DESCRIPTION[3];
			}
		} else {
			if (playCount == 0) {
				this.rawDescription = DESCRIPTION + EXTENDED_DESCRIPTION[2];
			} else {
				this.rawDescription = DESCRIPTION + EXTENDED_DESCRIPTION[3];
			}
		}

		this.initializeDescription();
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new CentralizeAction(magicNumber, playCount == 0 || upgraded && playCount == 1));
		addPlayCount();
	}

	public AbstractCard makeCopy() {
		return new Centralize();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			this.upgradeMagicNumber(UPGRADE_BONUS);
			updateDescription();
		}
	}
}