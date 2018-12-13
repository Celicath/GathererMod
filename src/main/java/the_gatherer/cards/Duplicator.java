package the_gatherer.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;
import the_gatherer.GathererMod;
import the_gatherer.actions.UsePotionOnRandomTargetAction;
import the_gatherer.patches.CardColorEnum;

public class Duplicator extends CustomCard {
	private static final String RAW_ID = "Duplicator";
	public static final String ID = GathererMod.makeID(RAW_ID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = GathererMod.GetCardPath(RAW_ID);
	private static final int COST = 2;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardColor COLOR = CardColorEnum.GATHERER_LIME;
	private static final CardRarity RARITY = CardRarity.COMMON;
	private static final CardTarget TARGET = CardTarget.SELF;

	public Duplicator() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
		this.exhaust = true;
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		for (AbstractPotion po : GathererMod.potionSack.potions) {
			if (!(po instanceof PotionSlot)) {
				AbstractDungeon.actionManager.addToBottom(new UsePotionOnRandomTargetAction(po));
			}
		}
	}

	public AbstractCard makeCopy() {
		return new Duplicator();
	}

	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.exhaust = false;
			this.rawDescription = UPGRADE_DESCRIPTION;
			this.initializeDescription();
		}
	}
}
