package the_gatherer.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import the_gatherer.GathererMod;
import the_gatherer.actions.ChooseLesserPotionAction;
import the_gatherer.patches.CardColorEnum;

public class RecipeChange extends CustomCard {
	private static final String RAW_ID = "RecipeChange";
	public static final String ID = GathererMod.makeID(RAW_ID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = GathererMod.GetCardPath(RAW_ID);
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final AbstractCard.CardType TYPE = CardType.SKILL;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.GATHERER_LIME;
	private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;

	private static final int NEW_COST = 0;

	public RecipeChange() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
		this.exhaust = true;
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new ChooseLesserPotionAction(1, GathererMod.lesserPotionPool.size(), true, upgraded));
	}

	public AbstractCard makeCopy() {
		return new RecipeChange();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			this.upgradeBaseCost(NEW_COST);
		}
	}
}
