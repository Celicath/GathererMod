package the_gatherer.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import the_gatherer.GathererMod;
import the_gatherer.actions.ObtainLesserPotionAction;
import the_gatherer.patches.AbstractCardEnum;
import the_gatherer.potions.LesserPoisonPotion;
import the_gatherer.powers.PoisonMasteryPower;

public class PoisonMastery extends CustomCard {
	private static final String CardID = "PoisonMastery";
	public static final String ID = GathererMod.makeID(CardID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = "img/cards/" + CardID + ".png";
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	private static final AbstractCard.CardType TYPE = CardType.POWER;
	private static final AbstractCard.CardColor COLOR = AbstractCardEnum.LIME;
	private static final AbstractCard.CardRarity RARITY = CardRarity.RARE;
	private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;

	private static final int POWER = 1;
	private static final int UPGRADE_BONUS = 1;

	public PoisonMastery() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
		this.baseMagicNumber = POWER;
		this.magicNumber = baseMagicNumber;
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		for (int i = 0; i < this.magicNumber; i++)
			AbstractDungeon.actionManager.addToBottom(new ObtainLesserPotionAction(new LesserPoisonPotion()));
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new PoisonMasteryPower(p, 1), 1));
	}

	public AbstractCard makeCopy() {
		return new PoisonMastery();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			this.upgradeMagicNumber(UPGRADE_BONUS);
			this.rawDescription = UPGRADE_DESCRIPTION;
			this.initializeDescription();
		}
	}
}