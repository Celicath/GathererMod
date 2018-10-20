package the_gatherer.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import the_gatherer.GathererMod;
import the_gatherer.actions.BronzeBladeAction;
import the_gatherer.actions.DamageRandomEnemyExceptTargetAction;
import the_gatherer.patches.AbstractCardEnum;

public class GlassHammer extends CustomCard {
	private static final String CardID = "GlassHammer";
	public static final String ID = GathererMod.makeID(CardID);

	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = "img/cards/" + CardID + ".png";
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardColor COLOR = AbstractCardEnum.LIME;
	private static final CardRarity RARITY = CardRarity.COMMON;
	private static final CardTarget TARGET = CardTarget.ENEMY;

	private static final int POWER = 8;
	private static final int UPGRADE_BONUS = 3;

	public GlassHammer() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
		this.baseDamage = POWER;
		this.damage = POWER;

		ResetDescription();
	}

	int GetSecondaryDamage() {
		return Math.max(this.damage - (upgraded ? 3 : 2), 0);
	}

	int GetNormalSecondaryDamage() {
		return Math.max(this.baseDamage - (upgraded ? 3 : 2), 0);
	}

	void SetDescription() {
		String color_tag = "";
		if (this.isDamageModified) {
			if (this.damage >= this.baseDamage)
				color_tag = "[#7fff00]";
			else
				color_tag = "[#ff6563]";
			this.rawDescription = DESCRIPTION + color_tag + GetSecondaryDamage() + "[]" + EXTENDED_DESCRIPTION[0];
		} else {
			this.rawDescription = DESCRIPTION + GetSecondaryDamage() + EXTENDED_DESCRIPTION[0];
		}
		this.initializeDescription();
	}

	void ResetDescription() {
		this.rawDescription = DESCRIPTION + GetNormalSecondaryDamage() + EXTENDED_DESCRIPTION[0];
		this.initializeDescription();
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
		AbstractDungeon.actionManager.addToBottom(
				new DamageRandomEnemyExceptTargetAction(m, new DamageInfo(p, GetSecondaryDamage(), this.damageTypeForTurn),
						AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
	}

	public AbstractCard makeCopy() {
		return new GlassHammer();
	}

	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeDamage(UPGRADE_BONUS);
			this.damage = this.baseDamage;
			this.upgraded = true;
			SetDescription();
		}
	}
}
