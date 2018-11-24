package the_gatherer.cards;

import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AlwaysRetainField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import the_gatherer.GathererMod;
import the_gatherer.patches.CardColorEnum;

public class Duality extends CustomCard {
	private static final String RAW_ID = "Duality";
	public static final String ID = GathererMod.makeID(RAW_ID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = GathererMod.GetCardPath(RAW_ID);
	private static final int COST = 2;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardColor COLOR = CardColorEnum.LIME;
	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.ENEMY;

	private static final int POWER = 16;
	private static final int UPGRADE_BONUS = 5;

	public Duality() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

		this.baseDamage = POWER;
		this.baseBlock = POWER;

		AlwaysRetainField.alwaysRetain.set(this, true);
	}

	@Override
	public void initializeDescription() {
		super.initializeDescription();
		keywords.remove("switch");
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		if (this.type == CardType.ATTACK)
			AbstractDungeon.actionManager.addToBottom(new DamageAction(m,
					new DamageInfo(p, this.damage, this.damageTypeForTurn),
					AbstractGameAction.AttackEffect.SLASH_HEAVY));
		else
			AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
	}

	@Override
	public void triggerOnEndOfTurnForPlayingCard() {
		transform();
	}

	public void transform() {
		if (this.type == CardType.ATTACK) {
			this.type = CardType.SKILL;
			this.target = CardTarget.SELF;
			this.rawDescription = EXTENDED_DESCRIPTION[0];
			this.initializeDescription();
		} else {
			this.type = CardType.ATTACK;
			this.target = CardTarget.ENEMY;
			this.rawDescription = DESCRIPTION;
			this.initializeDescription();
		}
	}

	public AbstractCard makeCopy() {
		return new Duality();
	}

	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeDamage(UPGRADE_BONUS);
			this.upgradeBlock(UPGRADE_BONUS);
		}
	}
}