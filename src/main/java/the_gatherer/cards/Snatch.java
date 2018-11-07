package the_gatherer.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import the_gatherer.GathererMod;
import the_gatherer.actions.ObtainLesserPotionAction;
import the_gatherer.cards.Helper.AbstractNumberedCard;
import the_gatherer.patches.CardColorEnum;

public class Snatch extends AbstractNumberedCard {
	private static final String RAW_ID = "Snatch";
	public static final String ID = GathererMod.makeID(RAW_ID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = GathererMod.GetCardPath(RAW_ID);
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	private static final AbstractCard.CardType TYPE = CardType.ATTACK;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.LIME;
	private static final AbstractCard.CardRarity RARITY = CardRarity.COMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;

	private static final int POWER = 8;
	private static final int UPGRADE_BONUS = 2;

	public Snatch() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
		this.baseDamage = POWER;
		updateDescription();
	}

	public void updateDescription() {
		if (upgraded && playCount == 0) {
			this.rawDescription = DESCRIPTION + EXTENDED_DESCRIPTION[0];
		} else if(upgraded && playCount == 1 || playCount == 0) {
			this.rawDescription = DESCRIPTION + EXTENDED_DESCRIPTION[1];
		} else {
			this.rawDescription = DESCRIPTION + EXTENDED_DESCRIPTION[2];
		}

		this.initializeDescription();
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.DamageAction(m,
				new DamageInfo(p, this.damage, this.damageTypeForTurn),
				AbstractGameAction.AttackEffect.SLASH_VERTICAL));
		if (playCount == 0 || upgraded && playCount == 1) {
			AbstractDungeon.actionManager.addToBottom(new ObtainLesserPotionAction(GathererMod.returnRandomLesserPotion()));
		}
		addPlayCount();
	}

	public AbstractCard makeCopy() {
		return new Snatch();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			this.upgradeDamage(UPGRADE_BONUS);
			upgraded = true;
			updateDescription();
		}
	}
}