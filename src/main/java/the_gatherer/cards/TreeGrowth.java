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
import the_gatherer.GathererMod;
import the_gatherer.patches.CardColorEnum;
import the_gatherer.powers.NextNextTurnBlockPower;

public class TreeGrowth extends CustomCard {
	private static final String RAW_ID = "TreeGrowth";
	public static final String ID = GathererMod.makeID(RAW_ID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = GathererMod.GetCardPath(RAW_ID);
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardColor COLOR = CardColorEnum.GATHERER_LIME;
	private static final CardRarity RARITY = CardRarity.COMMON;
	private static final CardTarget TARGET = CardTarget.SELF;

	private static final int POWER = 5;
	private static final int UPGRADE_BONUS = 2;
	private static final int SECONDARY_POWER = 10;
	private static final int SECONDARY_BONUS = 4;

	public TreeGrowth() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
		this.baseBlock = POWER;
		this.baseMagicNumber = SECONDARY_POWER;
		this.magicNumber = this.baseMagicNumber;
	}

	@Override
	public void applyPowers() {
		final int current_block = this.baseBlock;
		this.baseBlock = this.baseMagicNumber;
		super.applyPowers();

		this.magicNumber = this.block;
		this.isMagicNumberModified = this.magicNumber != this.baseMagicNumber;

		this.baseBlock = current_block;
		super.applyPowers();
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new NextTurnBlockPower(p, this.block), this.block));
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new NextNextTurnBlockPower(p, this.magicNumber), this.magicNumber));
	}

	public AbstractCard makeCopy() {
		return new TreeGrowth();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeBlock(UPGRADE_BONUS);
			upgradeMagicNumber(SECONDARY_BONUS);
		}
	}
}
