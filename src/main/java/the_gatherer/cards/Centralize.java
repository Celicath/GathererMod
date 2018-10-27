package the_gatherer.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import the_gatherer.GathererMod;
import the_gatherer.actions.CentralizeAction;
import the_gatherer.interfaces.OnceEffect;
import the_gatherer.patches.CardColorEnum;

public class Centralize extends CustomCard implements OnceEffect {
	private static final String RAW_ID = "Centralize";
	public static final String ID = GathererMod.makeID(RAW_ID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = GathererMod.GetCardPath(RAW_ID);
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
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
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
	}

	public AbstractCard makeCopy() {
		return new Centralize();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			this.upgradeMagicNumber(UPGRADE_BONUS);
		}
	}

	public void notFirstTimeEffect() {
		AbstractDungeon.actionManager.addToBottom(new CentralizeAction(magicNumber, false));
	}

	public void firstTimeEffect() {
		AbstractDungeon.actionManager.addToBottom(new CentralizeAction(magicNumber, true));
	}
}