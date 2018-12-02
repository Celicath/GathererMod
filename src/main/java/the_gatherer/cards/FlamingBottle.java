package the_gatherer.cards;

import basemod.abstracts.CustomCard;
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
import the_gatherer.patches.CardColorEnum;
import the_gatherer.patches.CustomTags;
import the_gatherer.potions.LesserFirePotion;

public class FlamingBottle extends CustomCard {
	private static final String RAW_ID = "FlamingBottle";
	public static final String ID = GathererMod.makeID(RAW_ID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = GathererMod.GetCardPath(RAW_ID);
	private static final int COST = 2;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final AbstractCard.CardType TYPE = CardType.ATTACK;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.LIME;
	private static final AbstractCard.CardRarity RARITY = CardRarity.RARE;
	private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;

	private static final int POWER = 12;
	private static final int UPGRADE_BONUS = 6;

	public FlamingBottle() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
		this.baseDamage = POWER;

		this.tags.add(CustomTags.POTION_GEN);
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.DamageAction(m,
				new DamageInfo(p, this.damage, this.damageTypeForTurn),
				AbstractGameAction.AttackEffect.FIRE));
		AbstractDungeon.actionManager.addToBottom(new ObtainLesserPotionAction(new LesserFirePotion(), true));
	}

	public AbstractCard makeCopy() {
		return new FlamingBottle();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			this.upgradeDamage(UPGRADE_BONUS);
		}
	}

	@Override
	public void initializeDescription() {
		super.initializeDescription();
		//keywords.add("Lesser Fire Potion");
	}
}