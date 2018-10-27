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
import the_gatherer.patches.CardColorEnum;
import the_gatherer.powers.SpareBottlePower;

public class SpareBottle extends CustomCard {
	private static final String RAW_ID = "SpareBottle";
	public static final String ID = GathererMod.makeID(RAW_ID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = GathererMod.GetCardPath(RAW_ID);
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.POWER;
	private static final CardColor COLOR = CardColorEnum.LIME;
	private static final CardRarity RARITY = CardRarity.BASIC;
	private static final CardTarget TARGET = CardTarget.SELF;

	private static final int POWER = 3;
	private static final int UPGRADE_BONUS = 2;

	public SpareBottle() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
		this.baseMagicNumber = POWER;
		this.magicNumber = this.baseMagicNumber;
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpareBottlePower(p, this.magicNumber), this.magicNumber));
	}

	public AbstractCard makeCopy() {
		return new SpareBottle();
	}

	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeMagicNumber(UPGRADE_BONUS);
		}
	}
}
