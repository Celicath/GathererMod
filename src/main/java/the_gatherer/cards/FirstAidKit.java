package the_gatherer.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import the_gatherer.GathererMod;
import the_gatherer.actions.FirstAidPotionAction;
import the_gatherer.actions.ObtainLesserPotionAction;
import the_gatherer.patches.AbstractCardEnum;
import the_gatherer.patches.CustomTags;
import the_gatherer.potions.FirstAidPotion;
import the_gatherer.potions.FirstAidPotionPlus;

public class FirstAidKit extends CustomCard {
	private static final String CardID = "FirstAidKit";
	public static final String ID = GathererMod.makeID(CardID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = "img/cards/" + CardID + ".png";
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardColor COLOR = AbstractCardEnum.LIME;
	private static final CardRarity RARITY = CardRarity.COMMON;
	private static final CardTarget TARGET = CardTarget.SELF;

	private static final int POWER = 7;
	private static final int UPGRADE_BONUS = 3;

	public FirstAidKit() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
		this.tags.add(CardTags.HEALING);
		this.exhaust = true;
		this.baseMagicNumber = POWER;
		this.magicNumber = this.baseMagicNumber;
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		if (this.upgraded)
			AbstractDungeon.actionManager.addToBottom(new ObtainLesserPotionAction(new FirstAidPotionPlus()));
		else
			AbstractDungeon.actionManager.addToBottom(new ObtainLesserPotionAction(new FirstAidPotion()));
	}

	public AbstractCard makeCopy() {
		return new FirstAidKit();
	}

	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeMagicNumber(UPGRADE_BONUS);
			this.rawDescription = UPGRADE_DESCRIPTION;
			this.initializeDescription();
		}
	}
}
