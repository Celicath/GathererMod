package the_gatherer.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import the_gatherer.GathererMod;
import the_gatherer.cards.Helper.GathererCardHelper;
import the_gatherer.patches.AbstractCardEnum;
import the_gatherer.patches.CustomTags;

public class FlowerPower extends CustomCard {
	private static final String CardID = "FlowerPower";
	public static final String ID = GathererMod.makeID(CardID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = "img/cards/" + CardID + ".png";
	private static final int COST = 2;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	private static final CardType TYPE = CardType.POWER;
	private static final CardColor COLOR = AbstractCardEnum.LIME;
	private static final CardRarity RARITY = CardRarity.RARE;
	private static final CardTarget TARGET = CardTarget.SELF;

	private static final int MAGIC = 2;

	public FlowerPower() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

		this.tags.add(CustomTags.Flower);
		this.baseMagicNumber = MAGIC;
		this.magicNumber = MAGIC;

		this.rawDescription = GetRawDescription();
		this.initializeDescription();
	}

	private String GetRawDescription() {
		String str;
		if (timesUpgraded == 0)
			str = DESCRIPTION;
		else str = EXTENDED_DESCRIPTION[timesUpgraded - 1];
		return str + GathererCardHelper.FlowerSuffix(this.timesUpgraded);
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		if (timesUpgraded <= 2)
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new WeakPower(p, this.magicNumber, false), this.magicNumber));
		if (timesUpgraded <= 1)
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new FrailPower(p, this.magicNumber, false), this.magicNumber));
		if (timesUpgraded <= 0)
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new VulnerablePower(p, this.magicNumber, false), this.magicNumber));

		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new DrawPower(p, this.magicNumber), this.magicNumber));
	}

	public AbstractCard makeCopy() {
		return new FlowerPower();
	}

	@Override
	public boolean canUpgrade() {
		return timesUpgraded < 3;
	}

	public void upgrade() {
		if (timesUpgraded < 3) {
			++this.timesUpgraded;
			if (timesUpgraded == 3)
				this.updateCost(1);
			this.upgraded = true;
			this.name = NAME + "+" + this.timesUpgraded;
			this.initializeTitle();
			this.rawDescription = GetRawDescription();
			this.initializeDescription();
		}
	}
}
