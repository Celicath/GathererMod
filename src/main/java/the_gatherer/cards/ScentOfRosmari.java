package the_gatherer.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import the_gatherer.GathererMod;
import the_gatherer.actions.ScentOfRosmariAction;
import the_gatherer.patches.CardColorEnum;

public class ScentOfRosmari extends CustomCard {
	private static final String RAW_ID = "ScentOfRosmari";
	public static final String ID = GathererMod.makeID(RAW_ID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = GathererMod.GetCardPath(RAW_ID);
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardColor COLOR = CardColorEnum.LIME;
	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.SELF;

	private static final int POWER = 7;
	private static final int UPGRADE_BONUS = 2;
	private static final int RETAIN = 2;
	private static final int REATIN_BONUS = 1;

	public ScentOfRosmari() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

		this.baseBlock = POWER;
		this.baseMagicNumber = RETAIN;
		this.magicNumber = this.baseMagicNumber;
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
		AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1f));
		AbstractDungeon.actionManager.addToBottom(new ScentOfRosmariAction(this.magicNumber));
	}

	public AbstractCard makeCopy() {
		return new ScentOfRosmari();
	}

	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeBlock(UPGRADE_BONUS);
			this.upgradeMagicNumber(REATIN_BONUS);
		}
	}
}
