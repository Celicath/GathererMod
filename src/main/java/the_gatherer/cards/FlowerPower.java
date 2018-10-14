package the_gatherer.cards;

import basemod.abstracts.CustomCard;
import basemod.helpers.BaseModCardTags;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import the_gatherer.cards.Helper.GathererCardHelper;
import the_gatherer.patches.AbstractCardEnum;
import the_gatherer.patches.CustomTags;

import java.util.Iterator;

public class FlowerPower extends CustomCard {
	public static final String ID = "FlowerPower";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = "img/cards/" + ID + ".png";
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	private static final CardType TYPE = CardType.POWER;
	private static final CardColor COLOR = AbstractCardEnum.LIME;
	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.SELF;

	public FlowerPower() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

		this.rawDescription = GetRawDescription();
		this.initializeDescription();
	}

	private String GetRawDescription()
	{
		String str;
		if (timesUpgraded == 0)
			str = DESCRIPTION;
		else if (timesUpgraded < 3)
			str = "Heal " + (timesUpgraded * 3) + " HP. NL " + DESCRIPTION;
		else str = "Heal " + (timesUpgraded * 3) + " HP. NL " + UPGRADE_DESCRIPTION;
		return str + GathererCardHelper.FlowerSuffix(this.timesUpgraded);
	}

	public void applyPowers() {
		this.baseMagicNumber = 0;

		Iterator it = AbstractDungeon.player.masterDeck.group.iterator();
		while(it.hasNext()) {
			AbstractCard c = (AbstractCard)it.next();
			if (c.hasTag(CustomTags.Flower)) {
				++this.baseMagicNumber;
			}
		}
		this.magicNumber = this.baseMagicNumber;
		super.applyPowers();

		this.rawDescription = GetRawDescription() + EXTENDED_DESCRIPTION[0];
		this.initializeDescription();
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		if (this.timesUpgraded > 0)
			AbstractDungeon.actionManager.addToBottom(new HealAction(p, p, this.timesUpgraded * 3));
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new StrengthPower(p, this.magicNumber), this.magicNumber));
		if (this.timesUpgraded == 3)
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new DexterityPower(p, this.magicNumber), this.magicNumber));

		this.rawDescription = GetRawDescription();

		this.initializeDescription();
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
			this.upgraded = true;
			this.name = NAME + "+" + this.timesUpgraded;
			this.initializeTitle();
			this.rawDescription = GetRawDescription();
			this.initializeDescription();
		}
	}
}
