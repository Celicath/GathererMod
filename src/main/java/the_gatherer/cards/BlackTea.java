package the_gatherer.cards;

import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import the_gatherer.GathererMod;
import the_gatherer.actions.ObtainLesserPotionAction;
import the_gatherer.patches.CardColorEnum;
import the_gatherer.patches.CustomTags;
import the_gatherer.potions.SackPotion;

public class BlackTea extends CustomCard implements StartupCard {
	private static final String RAW_ID = "BlackTea";
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

	private static final int POWER = 4;
	private static final int UPGRADE_BONUS = 1;

	public BlackTea() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
		this.baseMagicNumber = POWER;
		this.magicNumber = this.baseMagicNumber;

		this.tags.add(CustomTags.POTION_GEN);
	}

	@Override
	public boolean atBattleStartPreDraw() {
		SackPotion p = GathererMod.returnRandomLesserPotion();
		p.setTag(SackPotion.SackPotionTag.BLACKTEA);
		AbstractDungeon.actionManager.addToBottom(new ObtainLesserPotionAction(p));
		return true;
	}

	@Override
	public void applyPowers() {
		super.applyPowers();
		this.magicNumber = this.baseMagicNumber - GameActionManager.turn;
		if (this.magicNumber < 0)
			this.magicNumber = 0;
		this.rawDescription = DESCRIPTION + EXTENDED_DESCRIPTION[0] + this.magicNumber + EXTENDED_DESCRIPTION[1];
		this.initializeDescription();
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new StrengthPower(p, this.magicNumber), this.magicNumber));

		this.rawDescription = DESCRIPTION;
		this.initializeDescription();
	}

	public AbstractCard makeCopy() {
		return new BlackTea();
	}

	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();

			this.upgradeMagicNumber(UPGRADE_BONUS);
		}
	}
}
