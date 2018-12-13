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

public class CollectorsShot extends CustomCard {
	private static final String RAW_ID = "CollectorsShot";
	public static final String ID = GathererMod.makeID(RAW_ID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = GathererMod.GetCardPath(RAW_ID);
	private static final int COST = 2;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardColor COLOR = CardColorEnum.GATHERER_LIME;
	private static final CardRarity RARITY = CardRarity.COMMON;
	private static final CardTarget TARGET = CardTarget.ENEMY;

	public CollectorsShot() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
		updateDamage();
	}

	public void applyPowers() {
		updateDamage();
		super.applyPowers();
	}

	@Override
	public void update() {
		super.update();

		updateDamage();
	}

	private void updateDamage() {
		if (AbstractDungeon.player != null) {
			int temp = this.baseDamage;
			this.baseDamage = GathererMod.countUnique(AbstractDungeon.player.masterDeck) * (upgraded ? 4 : 3) / 2;
			if (this.baseDamage != temp) {
				this.rawDescription = (upgraded ? UPGRADE_DESCRIPTION : DESCRIPTION) + EXTENDED_DESCRIPTION[0];
				this.initializeDescription();
			}
		}
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HEAVY));
	}

	public AbstractCard makeCopy() {
		return new CollectorsShot();
	}

	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.rawDescription = UPGRADE_DESCRIPTION;
			this.initializeDescription();
			updateDamage();
		}
	}
}
