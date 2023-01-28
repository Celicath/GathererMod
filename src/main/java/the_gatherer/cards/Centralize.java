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
import the_gatherer.interfaces.ColoredTextCard;
import the_gatherer.patches.CardColorEnum;

public class Centralize extends AbstractNumberedCard implements ColoredTextCard {
	private static final String RAW_ID = "Centralize";
	public static final String ID = GathererMod.makeID(RAW_ID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = GathererMod.GetCardPath(RAW_ID);
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	private static final AbstractCard.CardType TYPE = AbstractCard.CardType.SKILL;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.GATHERER_LIME;
	private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.BASIC;
	private static final AbstractCard.CardTarget TARGET = AbstractCard.CardTarget.SELF;

	private static final int POWER = 2;

	public Centralize() {
		super(ID, NAME, IMG, COST, DESCRIPTION, EXTENDED_DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
		this.baseMagicNumber = POWER;
		this.magicNumber = this.baseMagicNumber;
		updateDescription();
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
			updateDescription();
		}
	}
}
