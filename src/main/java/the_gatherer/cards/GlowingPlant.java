package the_gatherer.cards;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import the_gatherer.GathererMod;
import the_gatherer.interfaces.OnObtainEffect;
import the_gatherer.patches.CardColorEnum;
import the_gatherer.powers.GlowPower;

import java.util.ArrayList;

public class GlowingPlant extends CustomCard implements OnObtainEffect {
	private static final String RAW_ID = "GlowingPlant";
	public static final String ID = GathererMod.makeID(RAW_ID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = GathererMod.GetCardPath(RAW_ID);
	private static final int COST = 0;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardColor COLOR = CardColorEnum.LIME;
	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.SELF;

	private ArrayList<AbstractCard> tooltips;

	public GlowingPlant() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

		tooltips = new ArrayList<>();
		tooltips.add(new Light());
		tooltips.add(new Shadow());
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new GlowPower(p, 1), 1));
		int pow = 0;
		if (p.hasPower(GlowPower.POWER_ID)) {
			pow = p.getPower(GlowPower.POWER_ID).amount;
		}
		AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, upgraded ? pow + 1 : pow));
	}

	public AbstractCard makeCopy() {
		return new GlowingPlant();
	}

	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.rawDescription = UPGRADE_DESCRIPTION;
			this.initializeDescription();
		}
	}

	@Override
	public void onObtain() {
		AbstractDungeon.topLevelEffectsQueue.add(new com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect(new Shadow(), Settings.WIDTH / 3.0F, Settings.HEIGHT / 2.0F, false));
		AbstractDungeon.topLevelEffectsQueue.add(new com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect(new Light(), Settings.WIDTH * 2 / 3.0F, Settings.HEIGHT / 2.0F, false));
	}

	@Override
	public void renderCardTip(SpriteBatch sb) {
		super.renderCardTip(sb);
		boolean renderTip = (boolean) ReflectionHacks.getPrivate(this, AbstractCard.class, "renderTip");

		int count = 0;
		if (!Settings.hideCards && renderTip) {
			if (AbstractDungeon.player != null && AbstractDungeon.player.isDraggingCard) {
				return;
			}
			for (AbstractCard c : tooltips) {
				float dx = (AbstractCard.IMG_WIDTH * 0.9f - 5f) * drawScale;
				float dy = (AbstractCard.IMG_HEIGHT * 0.4f - 5f) * drawScale;
				if (current_x > Settings.WIDTH * 0.75f) {
					c.current_x = current_x + dx;
				} else {
					c.current_x = current_x - dx;
				}
				if (count == 0) {
					c.current_y = current_y + dy;
				} else {
					c.current_y = current_y - dy;
				}
				c.drawScale = drawScale * 0.8f;
				c.render(sb);
				count++;
			}
		}
	}
}
