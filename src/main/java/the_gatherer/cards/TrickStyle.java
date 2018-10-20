package the_gatherer.cards;

import basemod.abstracts.CustomCard;
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
import the_gatherer.patches.AbstractCardEnum;

public class TrickStyle extends CustomCard {
	private static final String CardID = "TrickStyle";
	public static final String ID = GathererMod.makeID(CardID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = "img/cards/" + CardID + ".png";
	private static final int COST = 2;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardColor COLOR = AbstractCardEnum.LIME;
	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.ENEMY;

	private static final int POWER = 17;
	private static final int UPGRADE_BONUS = 5;

	public TrickStyle() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

		this.baseDamage = POWER;
		this.baseBlock = POWER;
		this.retain = true;
	}

	@Override
	public void applyPowers() {
		super.applyPowers();
		this.retain = true;
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
		this.retain = true;
	}

	public AbstractCard makeCopy() {
		return new TrickStyle();
	}

	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeDamage(UPGRADE_BONUS);
			this.upgradeBlock(UPGRADE_BONUS);
		}
	}
}