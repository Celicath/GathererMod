package the_gatherer.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;
import the_gatherer.GathererMod;
import the_gatherer.cards.Helper.GathererCardHelper;
import the_gatherer.patches.AbstractCardEnum;
import the_gatherer.patches.CustomTags;

public class FlowerBeam extends CustomCard {
	private static final String CardID = "FlowerBeam";
	public static final String ID = GathererMod.makeID(CardID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = "img/cards/" + CardID + ".png";
	private static final int COST = 2;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardColor COLOR = AbstractCardEnum.LIME;
	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

	private static final int POWER = 12;
	private static final int UPGRADE_BONUS = 2;

	public FlowerBeam() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
		this.baseDamage = POWER;
		this.isMultiDamage = true;
		this.tags.add(CustomTags.Flower);

		this.rawDescription = GetRawDescription();
		this.initializeDescription();
	}

	private String GetRawDescription() {
		return DESCRIPTION + GathererCardHelper.FlowerSuffix(timesUpgraded);
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_DEFECT_BEAM"));
		AbstractDungeon.actionManager.addToBottom(new VFXAction(p, new CleaveEffect(), 0.1F));
		AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.NONE));
	}

	public AbstractCard makeCopy() {
		return new FlowerBeam();
	}

	@Override
	public boolean canUpgrade() {
		return timesUpgraded < 3;
	}

	public void upgrade() {
		if (timesUpgraded < 3) {
			upgradeDamage(UPGRADE_BONUS);
			++this.timesUpgraded;
			if (timesUpgraded == 3)
				upgradeBaseCost(1);
			this.upgraded = true;
			this.name = NAME + "+" + this.timesUpgraded;
			this.initializeTitle();
			this.rawDescription = GetRawDescription();
			this.initializeDescription();
		}
	}
}
