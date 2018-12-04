package the_gatherer.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EquilibriumPower;
import the_gatherer.GathererMod;
import the_gatherer.patches.CardColorEnum;
import the_gatherer.patches.CustomTags;
import the_gatherer.powers.LuckyCloverPower;

public class LuckyClover extends CustomCard {
	private static final String RAW_ID = "LuckyClover";
	public static final String ID = GathererMod.makeID(RAW_ID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = GathererMod.GetCardPath(RAW_ID);
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final AbstractCard.CardType TYPE = CardType.POWER;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.LIME;
	private static final AbstractCard.CardRarity RARITY = CardRarity.RARE;
	private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;

	private static final int NEW_COST = 0;

	public LuckyClover() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new EquilibriumPower(p, 1), 1));
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new LuckyCloverPower(p)));
	}

	public AbstractCard makeCopy() {
		return new LuckyClover();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			this.upgradeBaseCost(NEW_COST);
		}
	}
}
