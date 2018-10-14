package the_gatherer.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;
import the_gatherer.patches.AbstractCardEnum;
import the_gatherer.powers.NextNextTurnBlockPower;

public class TreeGrowth extends CustomCard {
	public static final String ID = "TreeGrowth";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = "img/cards/" + ID + ".png";
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardColor COLOR = AbstractCardEnum.LIME;
	private static final CardRarity RARITY = CardRarity.COMMON;
	private static final CardTarget TARGET = CardTarget.SELF;

	private static final int POWER = 4;
	private static final int UPGRADE_BONUS = 2;

	public TreeGrowth() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
		this.baseBlock = POWER;
		this.block = POWER;

		SetDescription();
	}

	void SetDescription() {
		String color_tag = "";
		if (this.isBlockModified) {
			if (this.block >= this.baseBlock)
				color_tag = "[#7fff00]";
			else
				color_tag = "[#ff6563]";
			this.rawDescription = DESCRIPTION + EXTENDED_DESCRIPTION[0] + color_tag + (this.block + this.baseBlock) + "[]" + EXTENDED_DESCRIPTION[1];
		} else {
			this.rawDescription = DESCRIPTION + EXTENDED_DESCRIPTION[0] + (this.block + this.baseBlock) + EXTENDED_DESCRIPTION[1];
		}
		this.initializeDescription();
	}

	public void applyPowers() {
		super.applyPowers();
		SetDescription();
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new NextTurnBlockPower(p, this.block), this.block));
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new NextNextTurnBlockPower(p, this.block + this.baseBlock), this.block + this.baseBlock));
		SetDescription();
	}

	public AbstractCard makeCopy() {
		return new TreeGrowth();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeBlock(UPGRADE_BONUS);
			this.block = this.baseBlock;
			SetDescription();
		}
	}
}
