package the_gatherer.cards;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;

public class LesserPotionOption extends AbstractCard
{
	public static final String ID = "hubris:DisguiseKitOption";

	public AbstractPotion potion;

	public LesserPotionOption(AbstractPotion p, String description)
	{
		super(ID, p.name, null, null, -2, description, CardType.SKILL, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.NONE);
	}

	@Override
	public void update() {
		potion.posX = this.current_x;
		potion.posY = this.current_y;
	}

	@Override
	public void render(SpriteBatch sb) {
		potion.render(sb);
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m)
	{
	}

	@Override
	public boolean canUpgrade()
	{
		return false;
	}

	@Override
	public void upgrade()
	{
	}

	@Override
	public AbstractCard makeCopy()
	{
		return new LesserPotionOption(potion, rawDescription);
	}
}