package the_gatherer.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import the_gatherer.GathererMod;
import the_gatherer.patches.CardColorEnum;

public class FinishingStrike extends CustomCard {
	private static final String RAW_ID = "FinishingStrike";
	public static final String ID = GathererMod.makeID(RAW_ID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = GathererMod.GetCardPath(RAW_ID);
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardColor COLOR = CardColorEnum.GATHERER_LIME;
	private static final CardRarity RARITY = CardRarity.RARE;
	private static final CardTarget TARGET = CardTarget.ENEMY;

	private static final int POWER = 12;
	private static final int UPGRADE_BONUS = 4;
	private static final int HIT = 2;

	public FinishingStrike() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
		this.baseDamage = POWER;
		this.baseMagicNumber = this.magicNumber = HIT;
		this.tags.add(CardTags.STRIKE);
	}

	public void triggerOnGlowCheck() {
		for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
			if (m.currentHealth < AbstractDungeon.player.currentHealth) {
				glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
				return;
			}
		}
		glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		int times = m.currentHealth < p.currentHealth ? magicNumber : 1;
		for (int i = 0; i < times; i++) {
			AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HEAVY));
		}
	}

	public AbstractCard makeCopy() {
		return new FinishingStrike();
	}

	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeDamage(UPGRADE_BONUS);
		}
	}
}
