package the_gatherer.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.curses.Normality;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import the_gatherer.GathererMod;
import the_gatherer.cards.Helper.AbstractNumberedCard;
import the_gatherer.interfaces.ColoredTextCard;
import the_gatherer.interfaces.OnObtainEffect;
import the_gatherer.patches.CardColorEnum;

public class CursedBlade extends AbstractNumberedCard implements OnObtainEffect, ColoredTextCard {
	private static final String RAW_ID = "CursedBlade";
	public static final String ID = GathererMod.makeID(RAW_ID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = GathererMod.GetCardPath(RAW_ID);
	private static final int COST = 2;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardColor COLOR = CardColorEnum.GATHERER_LIME;
	private static final CardRarity RARITY = CardRarity.RARE;
	private static final CardTarget TARGET = CardTarget.ENEMY;

	private static final int POWER = 30;
	private static final int UPGRADE_BONUS = 5;
	private static final int MAGIC = 15;

	public CursedBlade() {
		super(ID, NAME, IMG, COST, DESCRIPTION, EXTENDED_DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
		this.baseDamage = POWER;
		this.baseMagicNumber = MAGIC;
		this.magicNumber = this.baseMagicNumber;
		updateDescription();
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
		if (playCount == 0 || upgraded && playCount == 1) {
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new StrengthPower(m, -this.magicNumber), -this.magicNumber));
			if (m != null && !m.hasPower("Artifact")) {
				AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new GainStrengthPower(m, this.magicNumber), this.magicNumber));
			}
		}

		addPlayCount();
	}

	public AbstractCard makeCopy() {
		return new CursedBlade();
	}

	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeDamage(UPGRADE_BONUS);
			updateDescription();
		}
	}

	@Override
	public void onObtain() {
		AbstractDungeon.topLevelEffectsQueue.add(new com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect(new Normality(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, false));
	}
}
