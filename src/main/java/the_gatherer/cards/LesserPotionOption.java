package the_gatherer.cards;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import the_gatherer.GathererMod;
import the_gatherer.potions.SackPotion;

public class LesserPotionOption extends CustomCard {
	private static final String RAW_ID = "LesserPotionOption";
	public static final String ID = GathererMod.makeID(RAW_ID);
	public static final String IMG = GathererMod.GetCardPath("Blank");

	public SackPotion potion;

	public LesserPotionOption(SackPotion p, String description) {
		super(ID, p.name, IMG, -2, description, CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.NONE);
		this.potion = p;
		this.potion.scale = this.drawScale * 1.5f;

		this.rawDescription = this.rawDescription.replace("#b", "[#87ceeb]");
		this.rawDescription = this.rawDescription.replace("#g", "[#7fff00]");
		this.rawDescription = this.rawDescription.replace("#r", "[#ff6563]");
		this.rawDescription = this.rawDescription.replace("#y", "[#efc851]");
		this.initializeDescription();
	}

	@Override
	public void update() {
		super.update();
		this.potion.scale = this.drawScale * 1.5f;
		potion.posX = this.current_x;
		potion.posY = this.current_y + 50 * Settings.scale * this.potion.scale;
	}

	@Override
	public void render(SpriteBatch sb) {
		super.render(sb);
		potion.render(sb);
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
	}

	@Override
	public boolean canUpgrade() {
		return false;
	}

	@Override
	public void upgrade() {
	}

	@Override
	public AbstractCard makeCopy() {
		return new LesserPotionOption(potion, rawDescription);
	}
}