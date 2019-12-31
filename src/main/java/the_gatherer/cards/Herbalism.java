package the_gatherer.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import the_gatherer.GathererMod;
import the_gatherer.patches.CardColorEnum;

public class Herbalism extends CustomCard {
	private static final String RAW_ID = "Herbalism";
	public static final String ID = GathererMod.makeID(RAW_ID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = GathererMod.GetCardPath(RAW_ID);
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardColor COLOR = CardColorEnum.GATHERER_LIME;
	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.SELF;

	private static final int THRESHOLD = 2;
	private static final int NEW_COST = 0;

	public Herbalism() {
		super(ID, NAME, IMG, COST, UPGRADE_DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
	}

	@Override
	public void applyPowers() {
		this.baseMagicNumber = 0;
		for (AbstractCard c : AbstractDungeon.player.hand.group) {
			if (c.costForTurn >= THRESHOLD || c.costForTurn == -1 && (EnergyPanel.totalCount) >= THRESHOLD) {
				this.baseMagicNumber++;
			}
		}
		this.magicNumber = this.baseMagicNumber;
		super.applyPowers();

		this.rawDescription = UPGRADE_DESCRIPTION + EXTENDED_DESCRIPTION[0] + this.magicNumber + EXTENDED_DESCRIPTION[1];
		this.initializeDescription();
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		if (this.magicNumber > 0) {
			AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(this.magicNumber));
		}

		this.rawDescription = UPGRADE_DESCRIPTION;
		this.initializeDescription();
	}

	public AbstractCard makeCopy() {
		return new Herbalism();
	}

	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeBaseCost(NEW_COST);
			this.initializeDescription();
		}
	}
}
