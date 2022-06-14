import bagel.Image;
import bagel.Input;

public class Rock extends Weapon{
    private final int SHOOT_RANGE = 25;
    public Rock(Image bombImage){
        super(bombImage);
    }
    public void update(Input input, Bird bird, int timeScale){
        super.update(input, bird, timeScale);
        super.renderWeapon(SHOOT_RANGE);
    }
}
