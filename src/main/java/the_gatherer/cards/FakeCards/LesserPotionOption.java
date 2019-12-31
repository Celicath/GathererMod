package the_gatherer.cards.FakeCards;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import the_gatherer.GathererMod;
import the_gatherer.interfaces.ColoredTextCard;
import the_gatherer.potions.SackPotion;

public class LesserPotionOption extends CustomCard implements ColoredTextCard {
	private static final String RAW_ID = "LesserPotionOption";
	public static final String ID = GathererMod.makeID(RAW_ID);
	public static final String IMG = GathererMod.GetCardPath("Blank");

	public SackPotion potion;

	public LesserPotionOption(SackPotion p, String description) {
		super(ID, p.name, IMG, -2, description, CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.NONE);
		this.potion = p;
		this.potion.scale = Settings.scale * this.drawScale * 1.6f;

		StringBuilder newText = new StringBuilder();
		boolean first = true;
		for (String derp : rawDescription.split(" ")) {
			if (first) first = false;
			else newText.append(' ');
			if ((derp.length() > 0) && (derp.charAt(0) == '#')) {
				switch (derp.charAt(1)) {
					case 'r':
						derp = "[#ff6563]" + derp.substring(2) + "[]";
						break;
					case 'g':
						derp = "[#7fff00]" + derp.substring(2) + "[]";
						break;
					case 'b':
						derp = "[#87ceeb]" + derp.substring(2) + "[] ";
						break;
					case 'y':
						derp = "[#efc851]" + derp.substring(2) + "[]";
						break;
				}
				newText.append(derp);
			} else {
				newText.append(derp);
			}
		}
		rawDescription = newText.toString();
		this.initializeDescription();
	}

	@Override
	public void update() {
		super.update();
		this.potion.scale = Settings.scale * this.drawScale * 1.6f;
		potion.posX = this.current_x;
		potion.posY = this.current_y + Settings.scale * this.drawScale * 80;
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