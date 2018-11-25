package the_gatherer.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import the_gatherer.GathererMod;
import the_gatherer.actions.PotionUsedAction;
import the_gatherer.actions.RecoveryHerbAction;
import the_gatherer.patches.CardColorEnum;
import the_gatherer.patches.CustomTags;

public class RecoveryHerb extends CustomCard {
	private static final String RAW_ID = "RecoveryHerb";
	public static final String ID = GathererMod.makeID(RAW_ID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = GathererMod.GetCardPath(RAW_ID);
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardColor COLOR = CardColorEnum.LIME;
	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.SELF;

	private static final int POWER = 10;
	private static final int UPGRADE_BONUS = 5;

	public RecoveryHerb() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
		this.exhaust = true;
		this.baseMagicNumber = POWER;
		this.magicNumber = this.baseMagicNumber;

		this.tags.add(CardTags.HEALING);
		this.tags.add(CustomTags.POTION_GEN);
	}

	@Override
	public void applyPowers() {
		super.applyPowers();

		this.rawDescription = DESCRIPTION + EXTENDED_DESCRIPTION[0] + RecoveryHerbAction.CalcAmount(AbstractDungeon.player, this.magicNumber) + EXTENDED_DESCRIPTION[1];
		this.initializeDescription();
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new RecoveryHerbAction(p, p, this.magicNumber));
		AbstractDungeon.actionManager.addToBottom(new PotionUsedAction());

		this.rawDescription = DESCRIPTION;
		this.initializeDescription();
	}

	public AbstractCard makeCopy() {
		return new RecoveryHerb();
	}

	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeMagicNumber(UPGRADE_BONUS);
		}
	}
}
